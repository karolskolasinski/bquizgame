package pl.karolskolasinski.bquizgame.repository;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizSetupRepository extends JpaRepository<UserQuiz, Long> {

    /*LAST WEEK STATISTICS*/
    @Query(value = "SELECT count(*) FROM bquizgame.user_quiz uq WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    int gamesPlayedLastWeek();

    @Query(value = "SELECT count(*) FROM bquizgame.user_answer ua JOIN bquizgame.answer a ON ua.answer_id = a.id JOIN bquizgame.user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    double correctAnswersLastWeek();

    @Query(value = "SELECT count(*) FROM bquizgame.user_answer ua JOIN bquizgame.user_quiz uq ON ua.user_quiz_id = uq.id WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    double allAnswersLastWeek();

    @Query(value = "SELECT max(difficulty) FROM (SELECT sum(difficulty) AS difficulty FROM bquizgame.user_answer ua JOIN bquizgame.question q ON ua.question_id = q.id JOIN bquizgame.answer a ON ua.answer_id = a.id JOIN bquizgame.user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY) GROUP BY ua.user_quiz_id) AS max_week", nativeQuery = true)
    Optional<Integer> bestScoreLastWeek();

    @Query(value = "SELECT u FROM (SELECT max(difficulty), u FROM (SELECT sum(difficulty) AS difficulty, username AS u FROM bquizgame.user_answer ua JOIN bquizgame.question q ON ua.question_id = q.id JOIN bquizgame.answer a ON ua.answer_id = a.id JOIN bquizgame.user_quiz uq ON ua.user_quiz_id = uq.id JOIN bquizgame.account ac on uq.account_id = ac.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY) GROUP BY ua.user_quiz_id ORDER BY difficulty DESC) AS max_week) as d_u", nativeQuery = true)
    String bestUserLastWeek();


    /*LAST MONTH STATISTICS*/
    @Query(value = "SELECT count(*) FROM bquizgame.user_quiz uq WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    int gamesPlayedLastMonth();

    @Query(value = "SELECT count(*) FROM bquizgame.user_answer ua JOIN bquizgame.answer a ON ua.answer_id = a.id JOIN bquizgame.user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    double correctAnswersLastMonth();

    @Query(value = "SELECT count(*) FROM bquizgame.user_answer ua JOIN bquizgame.user_quiz uq ON ua.user_quiz_id = uq.id WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    double allAnswersLastMonth();

    @Query(value = "SELECT max(difficulty) FROM (SELECT sum(difficulty) AS difficulty FROM bquizgame.user_answer ua JOIN bquizgame.question q ON ua.question_id = q.id JOIN bquizgame.answer a ON ua.answer_id = a.id JOIN bquizgame.user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH) GROUP BY ua.user_quiz_id) AS max_month", nativeQuery = true)
    Optional<Integer> bestScoreLastMonth();

    @Query(value = "SELECT u FROM (SELECT max(difficulty), u FROM (SELECT sum(difficulty) AS difficulty, username AS u FROM bquizgame.user_answer ua JOIN bquizgame.question q ON ua.question_id = q.id JOIN bquizgame.answer a ON ua.answer_id = a.id JOIN bquizgame.user_quiz uq ON ua.user_quiz_id = uq.id JOIN bquizgame.account ac on uq.account_id = ac.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH) GROUP BY ua.user_quiz_id ORDER BY difficulty DESC) AS max_month) as d_u", nativeQuery = true)
    String bestUserLastMonth();


    /*ALL STATISTICS*/
    @Query(value = "SELECT count(*) FROM bquizgame.user_quiz", nativeQuery = true)
    int gamesPlayedAll();

    @Query(value = "SELECT count(*) FROM bquizgame.user_answer ua JOIN bquizgame.answer a ON ua.answer_id = a.id WHERE a.correct = 1", nativeQuery = true)
    double correctAnswersAll();

    @Query(value = "SELECT count(*) FROM bquizgame.user_answer", nativeQuery = true)
    double allAnswersAll();

    @Query(value = "SELECT max(difficulty) FROM (SELECT sum(difficulty) AS difficulty FROM bquizgame.user_answer ua JOIN bquizgame.question q ON ua.question_id = q.id JOIN bquizgame.answer a ON ua.answer_id = a.id WHERE a.correct = 1 GROUP BY ua.user_quiz_id) AS max_all", nativeQuery = true)
    int bestScoreAll();

    /*CLEAR DATA*/
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM bquizgame.user_quiz WHERE quiz_id IS NULL", nativeQuery = true)
    void clearUnplayedQuizzes();

    /*USER STATISTICS*/
    @Query(value = "SELECT count(*) FROM bquizgame.user_quiz WHERE account_id = :accountId", nativeQuery = true)
    int playedQuizesByPlayerAccountId(Long accountId);
}

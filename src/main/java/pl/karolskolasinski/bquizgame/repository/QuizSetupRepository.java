package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface QuizSetupRepository extends JpaRepository<UserQuiz, Long> {

    /*LAST WEEK STATISTICS*/
    @Query(value = "SELECT count(*), CONVERT(USING utf8) FROM heroku_cb7f22ae822d1c1.user_quiz uq WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    int gamesPlayedLastWeek();

    @Query(value = "SELECT count(*), CONVERT(USING utf8) FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.answer a ON ua.answer_id = a.id JOIN heroku_cb7f22ae822d1c1.user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    double correctAnswersLastWeek();

    @Query(value = "SELECT count(*), CONVERT(USING utf8) FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.user_quiz uq ON ua.user_quiz_id = uq.id WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    double allAnswersLastWeek();

    @Query(value = "SELECT max(difficulty), CONVERT(USING utf8) FROM (SELECT sum(difficulty) AS difficulty FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.question q ON ua.question_id = q.id JOIN heroku_cb7f22ae822d1c1.answer a ON ua.answer_id = a.id JOIN heroku_cb7f22ae822d1c1.user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY) GROUP BY ua.user_quiz_id) AS max_week", nativeQuery = true)
    Optional<Integer> bestScoreLastWeek();

    @Query(value = "SELECT u, CONVERT(USING utf8) FROM (SELECT max(difficulty), u FROM (SELECT sum(difficulty) AS difficulty, username AS u FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.question q ON ua.question_id = q.id JOIN heroku_cb7f22ae822d1c1.answer a ON ua.answer_id = a.id JOIN heroku_cb7f22ae822d1c1.user_quiz uq ON ua.user_quiz_id = uq.id JOIN heroku_cb7f22ae822d1c1.account ac on uq.account_id = ac.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY) GROUP BY ua.user_quiz_id ORDER BY difficulty DESC) AS max_week) as d_u", nativeQuery = true)
    String bestUserLastWeek();


    /*LAST MONTH STATISTICS*/
    @Query(value = "SELECT count(*) FROM heroku_cb7f22ae822d1c1.user_quiz uq WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    int gamesPlayedLastMonth();

    @Query(value = "SELECT count(*) FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.answer a ON ua.answer_id = a.id JOIN heroku_cb7f22ae822d1c1.user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    double correctAnswersLastMonth();

    @Query(value = "SELECT count(*) FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.user_quiz uq ON ua.user_quiz_id = uq.id WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    double allAnswersLastMonth();

    @Query(value = "SELECT max(difficulty) FROM (SELECT sum(difficulty) AS difficulty FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.question q ON ua.question_id = q.id JOIN heroku_cb7f22ae822d1c1.answer a ON ua.answer_id = a.id JOIN heroku_cb7f22ae822d1c1.user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH) GROUP BY ua.user_quiz_id) AS max_month", nativeQuery = true)
    Optional<Integer> bestScoreLastMonth();

    @Query(value = "SELECT u FROM (SELECT max(difficulty), u FROM (SELECT sum(difficulty) AS difficulty, username AS u FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.question q ON ua.question_id = q.id JOIN heroku_cb7f22ae822d1c1.answer a ON ua.answer_id = a.id JOIN heroku_cb7f22ae822d1c1.user_quiz uq ON ua.user_quiz_id = uq.id JOIN heroku_cb7f22ae822d1c1.account ac on uq.account_id = ac.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH) GROUP BY ua.user_quiz_id ORDER BY difficulty DESC) AS max_month) as d_u", nativeQuery = true)
    String bestUserLastMonth();


    /*ALL STATISTICS*/
    @Query(value = "SELECT count(*) FROM heroku_cb7f22ae822d1c1.user_quiz", nativeQuery = true)
    int gamesPlayedAll();

    @Query(value = "SELECT count(*) FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.answer a ON ua.answer_id = a.id WHERE a.correct = 1", nativeQuery = true)
    double correctAnswersAll();

    @Query(value = "SELECT count(*) FROM heroku_cb7f22ae822d1c1.user_answer", nativeQuery = true)
    double allAnswersAll();

    @Query(value = "SELECT max(difficulty) FROM (SELECT sum(difficulty) AS difficulty FROM heroku_cb7f22ae822d1c1.user_answer ua JOIN heroku_cb7f22ae822d1c1.question q ON ua.question_id = q.id JOIN heroku_cb7f22ae822d1c1.answer a ON ua.answer_id = a.id WHERE a.correct = 1 GROUP BY ua.user_quiz_id) AS max_all", nativeQuery = true)
    Optional<Integer> bestScoreAll();

    /*CLEAR DATA*/
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM heroku_cb7f22ae822d1c1.user_quiz WHERE quiz_id IS NULL", nativeQuery = true)
    void clearUnplayedQuizzes();

    /*USER STATISTICS*/
    @Query(value = "SELECT count(*) FROM heroku_cb7f22ae822d1c1.user_quiz WHERE account_id = :accountId", nativeQuery = true)
    int playedQuizesByAccountId(Long accountId);

    @Query(value = "SELECT max(heroku_cb7f22ae822d1c1.user_quiz.quiz_start_date_time) FROM heroku_cb7f22ae822d1c1.user_quiz WHERE account_id = :accountId", nativeQuery = true)
    LocalDateTime lastQuizDateTimeByAccountId(Long accountId);

    @Query(value = "SELECT max(heroku_cb7f22ae822d1c1.user_quiz.player1score) FROM heroku_cb7f22ae822d1c1.user_quiz WHERE player1name = :username", nativeQuery = true)
    Set<Integer> maxScoreByUsername(String username);
}

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

    // last week statistics
    @Query(value = "SELECT count(*) FROM user_quiz uq WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    int gamesPlayedLastWeek();


    @Query(value = "SELECT count(*) FROM user_answer ua JOIN answer a ON ua.answer_id = a.id JOIN user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    double correctAnswersLastWeek();


    @Query(value = "SELECT count(*) FROM user_answer ua JOIN user_quiz uq ON ua.user_quiz_id = uq.id WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY)", nativeQuery = true)
    double allAnswersLastWeek();


    @Query(value = "SELECT max(difficulty) FROM (SELECT sum(difficulty) AS difficulty FROM user_answer ua JOIN question q ON ua.question_id = q.id JOIN answer a ON ua.answer_id = a.id JOIN user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY) GROUP BY ua.user_quiz_id) AS max_week", nativeQuery = true)
    Optional<Integer> bestScoreLastWeek();


    @Query(value = "SELECT u FROM (SELECT max(difficulty), u FROM (SELECT sum(difficulty) AS difficulty, max(username) AS u FROM user_answer ua JOIN question q ON ua.question_id = q.id JOIN answer a ON ua.answer_id = a.id JOIN user_quiz uq ON ua.user_quiz_id = uq.id JOIN account ac ON uq.account_id = ac.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 7 DAY) GROUP BY ua.user_quiz_id ORDER BY difficulty DESC) AS max_week GROUP BY u) as d_u", nativeQuery = true)
    String bestUserLastWeek();


    // last month statistics
    @Query(value = "SELECT count(*) FROM user_quiz uq WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    int gamesPlayedLastMonth();


    @Query(value = "SELECT count(*) FROM user_answer ua JOIN answer a ON ua.answer_id = a.id JOIN user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    double correctAnswersLastMonth();


    @Query(value = "SELECT count(*) FROM user_answer ua JOIN user_quiz uq ON ua.user_quiz_id = uq.id WHERE uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH)", nativeQuery = true)
    double allAnswersLastMonth();


    @Query(value = "SELECT max(difficulty) FROM (SELECT sum(difficulty) AS difficulty FROM user_answer ua JOIN question q ON ua.question_id = q.id JOIN answer a ON ua.answer_id = a.id JOIN user_quiz uq ON ua.user_quiz_id = uq.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH) GROUP BY ua.user_quiz_id) AS max_month", nativeQuery = true)
    Optional<Integer> bestScoreLastMonth();


    @Query(value = "SELECT u FROM (SELECT max(difficulty), u FROM (SELECT sum(difficulty) AS difficulty, max(username) AS u FROM user_answer ua JOIN question q ON ua.question_id = q.id JOIN answer a ON ua.answer_id = a.id JOIN user_quiz uq ON ua.user_quiz_id = uq.id JOIN account ac ON uq.account_id = ac.id WHERE a.correct = 1 AND uq.quiz_start_date_time >= (CURDATE() - INTERVAL 1 MONTH) GROUP BY ua.user_quiz_id ORDER BY difficulty DESC) AS max_month GROUP BY u) as d_u", nativeQuery = true)
    String bestUserLastMonth();


    // all time statistics
    @Query(value = "SELECT count(*) FROM user_quiz", nativeQuery = true)
    int gamesPlayedAll();


    @Query(value = "SELECT count(*) FROM user_answer ua JOIN answer a ON ua.answer_id = a.id WHERE a.correct = 1", nativeQuery = true)
    double correctAnswersAll();


    @Query(value = "SELECT count(*) FROM user_answer", nativeQuery = true)
    double allAnswersAll();


    @Query(value = "SELECT max(difficulty) FROM (SELECT sum(difficulty) AS difficulty FROM user_answer ua JOIN question q ON ua.question_id = q.id JOIN answer a ON ua.answer_id = a.id WHERE a.correct = 1 GROUP BY ua.user_quiz_id) AS max_all", nativeQuery = true)
    Optional<Integer> bestScoreAll();


    // clear data
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_quiz WHERE quiz_id IS NULL", nativeQuery = true)
    void clearUnplayedQuizzes();


    // user statistics
    @Query(value = "SELECT count(*) FROM user_quiz WHERE account_id = :accountId", nativeQuery = true)
    int playedQuizesByAccountId(Long accountId);


    @Query(value = "SELECT max(user_quiz.quiz_start_date_time) FROM user_quiz WHERE account_id = :accountId", nativeQuery = true)
    LocalDateTime lastQuizDateTimeByAccountId(Long accountId);


    @Query(value = "SELECT max(user_quiz.player1score) FROM user_quiz WHERE player1name = :username", nativeQuery = true)
    Set<Integer> maxScoreByUsername(String username);

}

package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;

@Repository
public interface QuizSetupRepository extends JpaRepository<UserQuiz, Long> {
}

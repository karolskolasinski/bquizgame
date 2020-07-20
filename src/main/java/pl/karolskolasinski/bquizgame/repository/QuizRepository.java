package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}

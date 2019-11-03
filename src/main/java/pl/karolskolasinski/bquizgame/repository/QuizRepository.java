package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}

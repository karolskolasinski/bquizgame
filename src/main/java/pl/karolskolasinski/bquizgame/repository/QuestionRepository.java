package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.karolskolasinski.bquizgame.model.schema.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}

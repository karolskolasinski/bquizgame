package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.karolskolasinski.bquizgame.model.schema.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}

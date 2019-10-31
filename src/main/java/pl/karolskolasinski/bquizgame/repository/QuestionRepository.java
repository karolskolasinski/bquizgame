package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.karolskolasinski.bquizgame.model.schema.Question;

import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT category FROM bquizgame.question", nativeQuery = true)
    Set<String> findAllCategories();

    boolean existsByContent(String content);
}

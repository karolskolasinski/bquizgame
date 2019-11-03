package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.karolskolasinski.bquizgame.model.schema.Question;

import java.util.List;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT category FROM bquizgame.question", nativeQuery = true)
    Set<String> findAllCategories();

    @Query(value = "SELECT * FROM bquizgame.question WHERE difficulty = 1 AND category = :choosedCategory", nativeQuery = true)
    List<Question> findAllDifficulty1ByChoosedCategory(String choosedCategory);

    @Query(value = "SELECT * FROM bquizgame.question WHERE difficulty = 2 AND category = :choosedCategory", nativeQuery = true)
    List<Question> findAllDifficulty2ByChoosedCategory(String choosedCategory);

    @Query(value = "SELECT * FROM bquizgame.question WHERE difficulty = 3 AND category = :choosedCategory", nativeQuery = true)
    List<Question> findAllDifficulty3ByChoosedCategory(String choosedCategory);

    @Query(value = "SELECT * FROM bquizgame.question WHERE difficulty = 4 AND category = :choosedCategory", nativeQuery = true)
    List<Question> findAllDifficulty4ByChoosedCategory(String choosedCategory);

    boolean existsByContent(String content);

}

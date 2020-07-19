package pl.karolskolasinski.bquizgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.karolskolasinski.bquizgame.model.dto.ICategoryStatsDto;
import pl.karolskolasinski.bquizgame.model.schema.Question;

import java.util.List;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT category FROM question", nativeQuery = true)
    Set<String> findAllCategories();

    @Query(value = "SELECT * FROM question WHERE category = :chosenCategory", nativeQuery = true)
    List<Question> findAllByChosenCategory(String chosenCategory);

    @Query(value = "SELECT * FROM question WHERE difficulty = 1 AND category = :chosenCategory", nativeQuery = true)
    List<Question> findAllDifficulty1ByChosenCategory(String chosenCategory);

    @Query(value = "SELECT * FROM question WHERE difficulty = 2 AND category = :chosenCategory", nativeQuery = true)
    List<Question> findAllDifficulty2ByChosenCategory(String chosenCategory);

    @Query(value = "SELECT * FROM question WHERE difficulty = 3 AND category = :chosenCategory", nativeQuery = true)
    List<Question> findAllDifficulty3ByChosenCategory(String chosenCategory);

    @Query(value = "SELECT * FROM question WHERE difficulty = 4 AND category = :chosenCategory", nativeQuery = true)
    List<Question> findAllDifficulty4ByChosenCategory(String chosenCategory);

    boolean existsByContent(String content);

    @Query(value = "SELECT question.category AS category, question.difficulty AS difficulty, count(*) AS count FROM question GROUP BY question.difficulty, question.category ORDER BY question.category, question.difficulty", nativeQuery = true)
    List<ICategoryStatsDto> countCategoriesByDifficulty();
}

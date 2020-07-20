package pl.karolskolasinski.bquizgame.component;

import org.springframework.context.annotation.Configuration;
import pl.karolskolasinski.bquizgame.model.schema.Question;

import java.util.Set;

@Configuration
public class WithdrawQuestionUtility {

    public static boolean disableQuestion(Set<Question> withrdawnQuestions, Long id) {
        if (withrdawnQuestions == null) {
            return false;
        }

        return withrdawnQuestions.stream().anyMatch(question -> question.getId().equals(id));
    }

}

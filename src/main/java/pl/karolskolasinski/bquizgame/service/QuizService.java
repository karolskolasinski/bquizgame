package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuizRepository;
import pl.karolskolasinski.bquizgame.repository.QuizSetupRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizSetupRepository quizSetupRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, QuizSetupRepository quizSetupRepository) {
        this.quizRepository = quizRepository;
        this.quizSetupRepository = quizSetupRepository;
    }

    /*Create Quiz and save in database*/
    public Quiz createQuiz() {
        return quizRepository.save(new Quiz());
    }
}

package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuizSetupRepository;

@Service
public class QuizSetupService {

    private final QuizSetupRepository quizSetupRepository;

    @Autowired
    public QuizSetupService(QuizSetupRepository quizSetupRepository) {
        this.quizSetupRepository = quizSetupRepository;
    }

    /*Create new quiz*/
    public void createQuiz(byte numberOfPlayers, UserQuiz userQuiz) {
        userQuiz.setNumberOfPlayers(numberOfPlayers);
        quizSetupRepository.save(userQuiz);
    }
}

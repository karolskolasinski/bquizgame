package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuizSetupRepository;

import java.util.Optional;

@Service
public class QuizSetupService {

    private final QuizSetupRepository quizSetupRepository;

    @Autowired
    public QuizSetupService(QuizSetupRepository quizSetupRepository) {
        this.quizSetupRepository = quizSetupRepository;
    }

    /*Return Optional<UserQuiz> by id*/
    private Optional<UserQuiz> returnUserQuizById(Long id) {
        return quizSetupRepository.findById(id);
    }

    /*Create new quiz*/
    public void createQuiz(byte numberOfPlayers, UserQuiz userQuiz) {
        userQuiz.setNumberOfPlayers(numberOfPlayers);
        quizSetupRepository.save(userQuiz);
    }

    public UserQuiz setUsernamesToUserQuizById(Long newUserQuizId, String usernamePlayer1, String usernamePlayer2, String usernamePlayer3, String usernamePlayer4) {
        if (returnUserQuizById(newUserQuizId).isPresent()) {
            UserQuiz userQuiz = returnUserQuizById(newUserQuizId).get();
            userQuiz.setPlayer1Name(usernamePlayer1);
            userQuiz.setPlayer2Name(usernamePlayer2);
            userQuiz.setPlayer3Name(usernamePlayer3);
            userQuiz.setPlayer4Name(usernamePlayer4);
            quizSetupRepository.save(userQuiz);
            return userQuiz;
        }
        return new UserQuiz();
    }
}

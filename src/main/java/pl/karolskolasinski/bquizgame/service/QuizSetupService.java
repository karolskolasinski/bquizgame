package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuizSetupRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;

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

    /*Create new UserQuiz*/
    public void createQuiz(byte numberOfPlayers, UserQuiz userQuiz) {
        userQuiz.setNumberOfPlayers(numberOfPlayers);
        quizSetupRepository.save(userQuiz);
    }

    /*Set names to UserQuiz*/
    public UserQuiz setUsernamesToUserQuizByQuizId(Long newUserQuizId, String usernamePlayer1, String usernamePlayer2, String usernamePlayer3, String usernamePlayer4) {
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

    public UserQuiz setCategoriesToUserQuizByQuizId(Long newUserQuizId, Set<String> allCategories) {
        if (returnUserQuizById(newUserQuizId).isPresent()) {
            UserQuiz userQuiz = returnUserQuizById(newUserQuizId).get();
            userQuiz.setCategories(String.join(",", allCategories));
            return userQuiz;
        }
        return new UserQuiz();
    }
}

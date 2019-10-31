package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuizSetupRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    /*Create new UserQuiz with given number of players*/
    public void createUserQuizWithGivenNumberOfPlayers(byte numberOfPlayers, UserQuiz userQuiz) {
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

    /*Set all categories to newUserQuiz without saving to database!*/
    public UserQuiz setCategoriesToUserQuizByQuizId(Long newUserQuizId, Set<String> allCategories) {
        if (returnUserQuizById(newUserQuizId).isPresent()) {
            UserQuiz userQuiz = returnUserQuizById(newUserQuizId).get();
            userQuiz.setCategories(String.join(",", allCategories));
            return userQuiz;
        }
        return new UserQuiz();
    }

    /*Add choosed category to newUserQuiz with saving to database!*/
    public UserQuiz addChoosedCategoriesToNewUserQuiz(Long newUserQuizId, HttpServletRequest request) {
        Optional<UserQuiz> userQuizOptional = quizSetupRepository.findById(newUserQuizId);
        if (userQuizOptional.isPresent()) {

            UserQuiz userQuiz = userQuizOptional.get();
            Set<String> choosedCategories = new HashSet<>();
            Map<String, String[]> choosedCategoriesParameters = request.getParameterMap();

            for (String choosedCategory : choosedCategoriesParameters.keySet()) {
                if (choosedCategoriesParameters.get(choosedCategory)[0].equals("on")) {
                    choosedCategories.add(choosedCategory);   //todo -----> findByName(choosedCategory).ifPresent(ChoosedCategories::add);
                }
            }

            userQuiz.setCategories(String.join(",", choosedCategories));
            quizSetupRepository.save(userQuiz);
            return userQuiz;
        }
        return new UserQuiz();
    }

    public boolean bindQuizWithUserQuiz(Long newUserQuizId, Quiz quiz) {
        Optional<UserQuiz> userQuizOptional = quizSetupRepository.findById(newUserQuizId);
        if (userQuizOptional.isPresent()) {
            UserQuiz newUserQuiz = userQuizOptional.get();
            newUserQuiz.setQuiz(quiz);
            quizSetupRepository.save(newUserQuiz);
            return true;
        }
        return false;
    }
}

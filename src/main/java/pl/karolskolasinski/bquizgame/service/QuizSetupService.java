package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuestionRepository;
import pl.karolskolasinski.bquizgame.repository.QuizRepository;
import pl.karolskolasinski.bquizgame.repository.QuizSetupRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class QuizSetupService {

    private final QuizSetupRepository quizSetupRepository;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    @Autowired
    public QuizSetupService(QuizSetupRepository quizSetupRepository, QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.quizSetupRepository = quizSetupRepository;
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    /*Return Optional<UserQuiz> by id*/
    public UserQuiz returnUserQuizById(Long id) {
        Optional<UserQuiz> userQuizOptional = quizSetupRepository.findById(id);
        return userQuizOptional.orElseGet(UserQuiz::new);
    }

    /*Create new UserQuiz with given number of players*/
    public void createUserQuizWithGivenNumberOfPlayers(byte numberOfPlayers, UserQuiz userQuiz) {
        userQuiz.setNumberOfPlayers(numberOfPlayers);
        quizSetupRepository.save(userQuiz);
    }

    /*Set names to UserQuiz*/
    public UserQuiz setUsernamesToUserQuizByQuizId(Long newUserQuizId, String usernamePlayer1, String usernamePlayer2, String usernamePlayer3, String usernamePlayer4) {
        UserQuiz userQuiz = returnUserQuizById(newUserQuizId);
        userQuiz.setPlayer1Name(usernamePlayer1);
        userQuiz.setPlayer2Name(usernamePlayer2);
        userQuiz.setPlayer3Name(usernamePlayer3);
        userQuiz.setPlayer4Name(usernamePlayer4);
        userQuiz.setCurrentPlayer(usernamePlayer1);
        quizSetupRepository.save(userQuiz);
        return userQuiz;
    }

    /*Set all categories to newUserQuiz without saving to database!*/
    public UserQuiz setCategoriesToUserQuizByQuizId(Long newUserQuizId, Set<String> allCategories) {
        UserQuiz userQuiz = returnUserQuizById(newUserQuizId);
        userQuiz.setCategories(String.join(",", allCategories));
        return userQuiz;
    }

    /*Add choosed category to newUserQuiz with saving to database!*/
    public UserQuiz addChoosedCategoriesToNewUserQuiz(Long newUserQuizId, HttpServletRequest request) {
        Optional<UserQuiz> userQuizOptional = quizSetupRepository.findById(newUserQuizId);
        if (userQuizOptional.isPresent()) {
            UserQuiz newUserQuiz = userQuizOptional.get();

            /*Initialize variables (collections)*/
            Set<String> choosedCategories = new HashSet<>();
            List<Question> questionsByChoosedCategories = new ArrayList<>();
            Map<String, String[]> choosedCategoriesParameters = request.getParameterMap();

            /*Filling collections*/
            for (String choosedCategory : choosedCategoriesParameters.keySet()) {
                if (choosedCategoriesParameters.get(choosedCategory)[0].equals("on")) {
                    choosedCategories.add(choosedCategory);   //todo -----> findByName(choosedCategory).ifPresent(ChoosedCategories::add);
                    shufflingThanSublistingAndAddingQuestionsToList(questionRepository.findAllDifficulty1ByChoosedCategory(choosedCategory), questionsByChoosedCategories);
                    shufflingThanSublistingAndAddingQuestionsToList(questionRepository.findAllDifficulty2ByChoosedCategory(choosedCategory), questionsByChoosedCategories);
                    shufflingThanSublistingAndAddingQuestionsToList(questionRepository.findAllDifficulty3ByChoosedCategory(choosedCategory), questionsByChoosedCategories);
                    shufflingThanSublistingAndAddingQuestionsToList(questionRepository.findAllDifficulty4ByChoosedCategory(choosedCategory), questionsByChoosedCategories);
                }
            }

            /*Binding and saving Quiz+UserQuiz*/
            newUserQuiz.setCategories(String.join(",", choosedCategories));
            Set<Question> fourQuestionsFromEachCategoryToAddToQuiz = new HashSet<>(questionsByChoosedCategories);
            Quiz quiz = new Quiz();
            quizRepository.save(quiz);
            quiz.setQuestionSet(fourQuestionsFromEachCategoryToAddToQuiz);
            quizRepository.save(quiz);
            newUserQuiz.setQuiz(quiz);
            quizSetupRepository.save(newUserQuiz);
            return newUserQuiz;
        }
        return new UserQuiz();
    }

    /*Shuffling questions, sublist to 4 questions on the list, add them to Quiz questions list*/
    private void shufflingThanSublistingAndAddingQuestionsToList(List<Question> allDifficultyByChoosedCategory, List<Question> questionsByChoosedCategories) {
        Collections.shuffle(allDifficultyByChoosedCategory);
        allDifficultyByChoosedCategory = allDifficultyByChoosedCategory.subList(0, 4);
        questionsByChoosedCategories.addAll(allDifficultyByChoosedCategory);
    }
}

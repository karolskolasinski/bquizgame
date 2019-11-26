package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.dto.ICategoryStatsDto;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuestionRepository;
import pl.karolskolasinski.bquizgame.repository.QuizRepository;
import pl.karolskolasinski.bquizgame.repository.QuizSetupRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /*Set all categories to newUserQuiz without saving to database*/
    public UserQuiz setCategoriesToUserQuizByQuizId(Long newUserQuizId, Set<String> allCategories) {
        UserQuiz userQuiz = returnUserQuizById(newUserQuizId);
        userQuiz.setCategories(String.join(",", allCategories));
        return userQuiz;
    }

    /*Add choosed category to newUserQuiz with saving to database*/
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
        allDifficultyByChoosedCategory = allDifficultyByChoosedCategory.subList(0, 1);
        questionsByChoosedCategories.addAll(allDifficultyByChoosedCategory);
    }

    public boolean duplicates(String usernamePlayer1, String usernamePlayer2, String usernamePlayer3, String usernamePlayer4) {
        if (usernamePlayer2 == null) {
            return false;
        }
        if (usernamePlayer3 == null) {
            return duplicates(new String[]{usernamePlayer1, usernamePlayer2});
        }
        if (usernamePlayer4 == null) {
            return duplicates(new String[]{usernamePlayer1, usernamePlayer2, usernamePlayer3});
        }
        return duplicates(new String[]{usernamePlayer1, usernamePlayer2, usernamePlayer3, usernamePlayer4});
    }

    private boolean duplicates(final String[] names) {
        Set<String> temporary = new HashSet<>();
        for (String name : names) {
            if (temporary.contains(name)) {
                return true;
            }
            temporary.add(name);
        }
        return false;
    }

    /*LAST WEEK STATISTICS*/
    public int gamesPlayedLastWeek() {
        return quizSetupRepository.gamesPlayedLastWeek();
    }

    public double correctAnswersLastWeek() {
        return quizSetupRepository.correctAnswersLastWeek();
    }

    public double allAnswersLastWeek() {
        return quizSetupRepository.allAnswersLastWeek();
    }

    public int bestScoreLastWeek() {
        Optional<Integer> bestScoreLastWeek = quizSetupRepository.bestScoreLastWeek();
        return bestScoreLastWeek.orElse(0);
    }

    public String bestUserLastWeek() {
        return quizSetupRepository.bestUserLastWeek();
    }


    /*LAST MONTH STATISTICS*/
    public int gamesPlayedLastMonth() {
        return quizSetupRepository.gamesPlayedLastMonth();
    }

    public double correctAnswersLastMonth() {
        return quizSetupRepository.correctAnswersLastMonth();
    }

    public double allAnswersLastMonth() {
        return quizSetupRepository.allAnswersLastMonth();
    }

    public int bestScoreLastMonth() {
        Optional<Integer> bestScoreLastMonth = quizSetupRepository.bestScoreLastMonth();
        return bestScoreLastMonth.orElse(0);
    }

    public String bestUserLastMonth() {
        return quizSetupRepository.bestUserLastMonth();
    }


    /*ALL STATISTICS*/
    public int gamesPlayedAll() {
        return quizSetupRepository.gamesPlayedAll();
    }

    public double correctAnswersAll() {
        return quizSetupRepository.correctAnswersAll();
    }

    public double allAnswersAll() {
        return quizSetupRepository.allAnswersAll();
    }

    public int bestScoreAll() {
        Optional<Integer> bestScoreAll = quizSetupRepository.bestScoreAll();
        return bestScoreAll.orElse(0);
    }

    public void clearUnplayedQuizzes() {
        System.out.println("clearUnplayedQuizzes");
        quizSetupRepository.clearUnplayedQuizzes();
    }


    /*USER STATISTICS*/
    public int playedQuizesByAccountId(Long accountId) {
        return quizSetupRepository.playedQuizesByAccountId(accountId);
    }

    public String lastQuizDateTimeByAccountId(Long accountId) {
        LocalDateTime localDateTime = quizSetupRepository.lastQuizDateTimeByAccountId(accountId);
        return localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public Integer maxScoreByUsername(String username) {
        Set<Integer> integers = quizSetupRepository.maxScoreByUsername(username);
        return integers.iterator().next();
    }

    /*CATEGORY STATISTICS*/
    public List<ICategoryStatsDto> countCategoriesByDifficulty() {
        return questionRepository.countCategoriesByDifficulty();
    }

}

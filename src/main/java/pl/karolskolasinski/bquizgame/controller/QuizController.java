package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.karolskolasinski.bquizgame.model.schema.Answer;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.service.QuizService;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/quiz/")
public class QuizController {

    private QuizService quizService;
    private QuizSetupService quizSetupService;

    @Autowired
    public QuizController(QuizService quizService, QuizSetupService quizSetupService) {
        this.quizService = quizService;
        this.quizSetupService = quizSetupService;
    }

    @PostMapping("/board")
    public String board(Model model, Long newUserQuizId, HttpServletRequest request) {
        UserQuiz userQuiz = quizSetupService.addChoosedCategoriesToNewUserQuiz(newUserQuizId, request);

        Set<Question> questionSet = userQuiz.getQuiz().getQuestionSet();
        List<String> categories = questionSet.stream().map(Question::getCategory).distinct().collect(Collectors.toList());

        model.addAttribute("categories", categories);
        model.addAttribute("newUserQuiz", quizSetupService.addChoosedCategoriesToNewUserQuiz(newUserQuizId, request));

        model.addAttribute("currentPlayerPlace", quizService.playerPlace(newUserQuizId, userQuiz.getCurrentPlayer()));

        return "quiz/quiz-board";
    }

    @GetMapping("/pickQuestion/{newUserQuizId}/{difficulty}/{category}")
    public String pickQuestion(Model model, @PathVariable(name = "newUserQuizId") Long newUserQuizId, @PathVariable(name = "difficulty") int difficulty, @PathVariable(name = "category") String category) {
        Question currentQuestion = quizService.pickQuestion(newUserQuizId, difficulty, category);
        List<Answer> questionAnswerList = quizService.questionAnswersSetToList(currentQuestion);
        model.addAttribute("currentQuestion", currentQuestion);
        model.addAttribute("questionAnswerList", questionAnswerList);
        model.addAttribute("questionAnswerOrder", quizService.questionAnswersOrder(questionAnswerList));
        model.addAttribute("newUserQuiz", quizSetupService.returnUserQuizById(newUserQuizId));
        model.addAttribute("currentPlayerPlace", quizService.playerPlace(newUserQuizId, quizSetupService.returnUserQuizById(newUserQuizId).getCurrentPlayer()));
        return "quiz/quiz-currentquestion";
    }

    @GetMapping("/pickQuestionResult/{newUserQuizId}/{answerId}/{currentQuestionId}/{questionAnswerOrder}")
    public String pickQuestionResult(Model model, @PathVariable(name = "newUserQuizId") Long newUserQuizId, @PathVariable(name = "answerId") Long answerId, @PathVariable(name = "currentQuestionId") Long currentQuestionId, @PathVariable(name = "questionAnswerOrder") String questionAnswerOrder) {
        model.addAttribute("userAnswer", quizService.setPlayerScoreAndReturnUserAnswer(newUserQuizId, answerId, currentQuestionId));
        model.addAttribute("orderedQuestionAnswers", quizService.getOrderedAnswers(questionAnswerOrder));
        model.addAttribute("correct_id", quizService.getCorrectId(quizService.getOrderedAnswers(questionAnswerOrder)));
        model.addAttribute("selected_id", answerId);
        model.addAttribute("currentPlayerPlace", quizService.playerPlace(newUserQuizId, quizSetupService.returnUserQuizById(newUserQuizId).getCurrentPlayer()));
        return "quiz/quiz-currentquestionresult";
    }

    @GetMapping("/board/{newUserQuizId}")
    public String scoreCounter(Model model, @PathVariable(name = "newUserQuizId") Long newUserQuizId) {
        model.addAttribute("newUserQuiz", quizService.setNextPlayerAndReturnNewUserQuiz(newUserQuizId));

        List<Question> answered = quizService.withdrawQuestion(newUserQuizId);

        model.addAttribute("withdrawnQuestions", answered);


        Set<Question> questionSet = quizSetupService.returnUserQuizById(newUserQuizId).getQuiz().getQuestionSet();
        List<String> categories = questionSet.stream().map(Question::getCategory).distinct().collect(Collectors.toList());
        model.addAttribute("categories", categories);
        model.addAttribute("currentPlayerPlace", quizService.playerPlace(newUserQuizId, quizSetupService.returnUserQuizById(newUserQuizId).getCurrentPlayer()));
        return "quiz/quiz-board";
    }

    @GetMapping("/summary/{newUserQuizId}")
    public String summary(Model model, @PathVariable(name = "newUserQuizId") Long newUserQuizId) {
        model.addAttribute("results", quizService.results(newUserQuizId));
        return "quiz/quiz-summary";
    }
}



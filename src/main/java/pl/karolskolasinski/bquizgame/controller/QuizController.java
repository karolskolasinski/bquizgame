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
import pl.karolskolasinski.bquizgame.model.schema.Quiz;
import pl.karolskolasinski.bquizgame.model.userplays.UserAnswer;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuizRepository;
import pl.karolskolasinski.bquizgame.service.QuizService;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        model.addAttribute("newUserQuiz", quizSetupService.addChoosedCategoriesToNewUserQuiz(newUserQuizId, request));
        return "quiz/quiz-board";
    }

    @GetMapping("/pickQuestion/{newUserQuizId}/{difficulty}/{category}")
    public String pickQuestion(Model model, @PathVariable(name = "newUserQuizId") Long newUserQuizId, @PathVariable(name = "difficulty") int difficulty, @PathVariable(name = "category") String category) {
        Question currentQuestion = quizService.pickQuestion(newUserQuizId, difficulty, category);
        List<Answer> questionList = quizService.questionAnswersSetToList(currentQuestion);
        StringBuilder questionAnswerOrder = new StringBuilder();
        questionList.forEach(answer -> questionAnswerOrder.append(answer.getId()).append(","));

        model.addAttribute("currentQuestion", currentQuestion);
        model.addAttribute("questionAnswerOptions", questionList);
        model.addAttribute("questionAnswerOrder", questionAnswerOrder);
        model.addAttribute("newUserQuiz", quizSetupService.returnUserQuizById(newUserQuizId));
        return "quiz/quiz-currentquestion";
    }

    @GetMapping("/pickQuestionResult/{newUserQuizId}/{answerId}/{currentQuestionId}/{answersorder}")
    public String pickQuestionResult(Model model, @PathVariable(name = "newUserQuizId") Long newUserQuizId, @PathVariable(name = "answerId") Long answerId, @PathVariable(name = "currentQuestionId") Long currentQuestionId, @PathVariable(name = "answersorder") String answersOrder) {
        model.addAttribute("userAnswer", quizService.checkIsAnswerCorrect(newUserQuizId, answerId, currentQuestionId));
        return "quiz/quiz-currentquestionresult";
    }


}



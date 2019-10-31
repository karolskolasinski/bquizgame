package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.QuizRepository;
import pl.karolskolasinski.bquizgame.service.QuizService;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

import javax.servlet.http.HttpServletRequest;

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
}



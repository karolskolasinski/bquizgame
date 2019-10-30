package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

@Controller
@RequestMapping(path = "/quizSetup/")
public class QuizSetupController {

    private QuizSetupService quizSetupService;

    @Autowired
    public QuizSetupController(QuizSetupService quizSetupService) {
        this.quizSetupService = quizSetupService;
    }

    @GetMapping("/numberOfPlayers/{numberOfPlayers}")
    public String getNumberOfPlayers(Model model, @PathVariable(name = "numberOfPlayers") byte numberOfPlayers, UserQuiz userQuiz) {
        quizSetupService.createQuiz(numberOfPlayers, userQuiz);
        model.addAttribute("newUserQuiz", userQuiz);
        return "quiz/quiz-setusernames";
    }

//    @GetMapping("/setUsernames/")
//    public String getNumberOfPlayers(Model model, @PathVariable(name = "numberOfPlayers") byte numberOfPlayers, UserQuiz userQuiz) {
//        quizSetupService.createQuiz(numberOfPlayers, userQuiz);
//        model.addAttribute("newUserQuiz", userQuiz);
//        return "redirect:/quiz/quiz-setusernames";
//    }
}



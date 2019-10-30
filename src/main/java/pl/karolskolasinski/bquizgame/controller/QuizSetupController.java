package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    /*Get number of players GET*/
    @GetMapping("/numberOfPlayers/{numberOfPlayers}")
    public String getNumberOfPlayers(Model model, @PathVariable(name = "numberOfPlayers") byte numberOfPlayers, UserQuiz userQuiz) {
        quizSetupService.createQuiz(numberOfPlayers, userQuiz);
        model.addAttribute("newUserQuiz", userQuiz);
        return "quiz/quiz-setusernames";
    }

    /*Set players usernames POST*/
    @PostMapping("/setUsernames")
    public String setUsernames(Model model, Long newUserQuizId, String usernamePlayer1, String usernamePlayer2, String usernamePlayer3, String usernamePlayer4) {
        model.addAttribute("newUserQuiz", quizSetupService.setUsernamesToUserQuizById(newUserQuizId, usernamePlayer1, usernamePlayer2, usernamePlayer3, usernamePlayer4));
        return "quiz/quiz-setcategories";
    }
}



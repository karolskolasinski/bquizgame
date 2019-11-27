package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.service.AccountService;
import pl.karolskolasinski.bquizgame.service.QuestionService;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

import java.security.Principal;

@Controller
@RequestMapping(path = "/quizSetup/")
public class QuizSetupController {

    private QuizSetupService quizSetupService;
    private QuestionService questionService;
    private AccountService accountService;

    @Autowired
    public QuizSetupController(QuizSetupService quizSetupService, QuestionService questionService, AccountService accountService) {
        this.quizSetupService = quizSetupService;
        this.questionService = questionService;
        this.accountService = accountService;
    }

    /*Get number of players GET*/
    @GetMapping("/setUsernames/{numberOfPlayers}")
    public String getNumberOfPlayers(Model model, @PathVariable(name = "numberOfPlayers") byte numberOfPlayers, UserQuiz userQuiz) {
        /*Clear unplayed userQuizzes*/
        quizSetupService.clearUnplayedQuizzes();
        quizSetupService.createUserQuizWithGivenNumberOfPlayers(numberOfPlayers, userQuiz);
        model.addAttribute("newUserQuiz", userQuiz);
        return "quizsetup/quizsetup-usernames";
    }

    /*Set players usernames and give allCategories to newUserQuiz for displaying in quizsetup-categories POST*/
    @PostMapping("/setCategories")
    public String setUsernames(Model model, Long newUserQuizId, String usernamePlayer1, String usernamePlayer2, String usernamePlayer3, String usernamePlayer4) {
        if (quizSetupService.duplicates(usernamePlayer1, usernamePlayer2, usernamePlayer3, usernamePlayer4)) {
            return usernamesDuplicateError(model, newUserQuizId);
        } else {
            quizSetupService.setUsernamesToUserQuizByQuizId(newUserQuizId, usernamePlayer1, usernamePlayer2, usernamePlayer3, usernamePlayer4);
            model.addAttribute("newUserQuiz", quizSetupService.setCategoriesToUserQuizByQuizId(newUserQuizId, questionService.returnAllCategories()));
            return "quizsetup/quizsetup-categories";
        }
    }

    /*Duplicate usernames error*/
    private String usernamesDuplicateError(Model model, Long newUserQuizId) {
        model.addAttribute("newUserQuiz", quizSetupService.returnUserQuizById(newUserQuizId));
        model.addAttribute("errorMessage", "Nie możesz podać dwóch takich samych nazw.");
        return "quizsetup/quizsetup-usernames";
    }

    /*Authenticated quiz GET*/
    @GetMapping("/authQuiz")
    public String authenticatedQuiz(Model model, Principal principal, UserQuiz newUserQuiz) {
        quizSetupService.createUserQuizWithGivenNumberOfPlayers((byte) 1, newUserQuiz);
        newUserQuiz.setAccount(accountService.findByUsername(principal.getName()));
        quizSetupService.setCategoriesToUserQuizByQuizId(newUserQuiz.getId(), questionService.returnAllCategories());
        quizSetupService.setUsernamesToUserQuizByQuizId(newUserQuiz.getId(), principal.getName(), null, null, null);
        model.addAttribute("newUserQuiz", quizSetupService.setCategoriesToUserQuizByQuizId(newUserQuiz.getId(), questionService.returnAllCategories()));
        return "quizsetup/quizsetup-categories";
    }

}

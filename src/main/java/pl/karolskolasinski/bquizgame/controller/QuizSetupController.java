package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.karolskolasinski.bquizgame.model.account.Account;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.service.AccountService;
import pl.karolskolasinski.bquizgame.service.QuestionService;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

import java.security.Principal;
import java.util.Optional;

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
        quizSetupService.createUserQuizWithGivenNumberOfPlayers(numberOfPlayers, userQuiz);
        model.addAttribute("newUserQuiz", userQuiz);
        return "quizsetup/quizsetup-usernames";
    }

    /*Set players usernames and give allCategories to newUserQuiz for displaying in quizsetup-categories POST*/
    @PostMapping("/setCategories")
    public String setUsernames(Model model, Long newUserQuizId, String usernamePlayer1, String usernamePlayer2, String usernamePlayer3, String usernamePlayer4) {
        if (quizSetupService.duplicates(usernamePlayer1, usernamePlayer2, usernamePlayer3, usernamePlayer4)) {
            return usernamesError(model, newUserQuizId);
        }
        model.addAttribute("newUserQuiz", quizSetupService.setUsernamesToUserQuizByQuizId(newUserQuizId, usernamePlayer1, usernamePlayer2, usernamePlayer3, usernamePlayer4)); //todo czy potrzebny ten model?
        model.addAttribute("newUserQuiz", quizSetupService.setCategoriesToUserQuizByQuizId(newUserQuizId, questionService.returnAllCategories()));
        return "quizsetup/quizsetup-categories";
    }

    private String usernamesError(Model model, Long newUserQuizId) {
        model.addAttribute("newUserQuiz", quizSetupService.returnUserQuizById(newUserQuizId));
        model.addAttribute("errorMessage", "Nie możesz podać dwóch takich samych nazw.");
        return "quizsetup/quizsetup-usernames";
    }

    @GetMapping("/authQuiz")
    public String authenticatedQuiz(Model model, Principal principal, UserQuiz newUserQuiz) {
        quizSetupService.createUserQuizWithGivenNumberOfPlayers((byte) 1, newUserQuiz);
        Optional<Account> accountByUsername = accountService.findByUsername(principal.getName());
        accountByUsername.ifPresent(newUserQuiz::setAccount);
        model.addAttribute("newUserQuiz", quizSetupService.setCategoriesToUserQuizByQuizId(newUserQuiz.getId(), questionService.returnAllCategories()));
        model.addAttribute("newUserQuiz", quizSetupService.setUsernamesToUserQuizByQuizId(newUserQuiz.getId(), principal.getName(), null, null, null)); //todo czy potrzebny ten model?
        model.addAttribute("newUserQuiz", quizSetupService.setCategoriesToUserQuizByQuizId(newUserQuiz.getId(), questionService.returnAllCategories()));
        return "quizsetup/quizsetup-categories";
    }


}



package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.service.QuestionService;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

@Controller
@RequestMapping(path = "/quizSetup/")
public class QuizSetupController {

    private QuizSetupService quizSetupService;
    private QuestionService questionService;

    @Autowired
    public QuizSetupController(QuizSetupService quizSetupService, QuestionService questionService) {
        this.quizSetupService = quizSetupService;
        this.questionService = questionService;
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
        model.addAttribute("newUserQuiz", quizSetupService.setUsernamesToUserQuizByQuizId(newUserQuizId, usernamePlayer1, usernamePlayer2, usernamePlayer3, usernamePlayer4)); //todo czy potrzebny ten model?
        model.addAttribute("newUserQuiz", quizSetupService.setCategoriesToUserQuizByQuizId(newUserQuizId, questionService.returnAllCategories()));
        return "quizsetup/quizsetup-categories";
    }
}



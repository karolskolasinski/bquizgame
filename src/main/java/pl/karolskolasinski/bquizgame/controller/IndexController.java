package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index/index";
    }

    @GetMapping("/login")
    public String login() {
        return "account/login-form";
    }

    @GetMapping("/play")
    public String play(){
        return "quiz/quiz-numberofplayers";
    }
}

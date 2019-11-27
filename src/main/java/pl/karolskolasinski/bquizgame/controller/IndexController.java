package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.karolskolasinski.bquizgame.service.QuizSetupService;

import java.text.DecimalFormat;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    private final QuizSetupService quizSetupService;
    private DecimalFormat decimalFormat = new DecimalFormat("##.##");

    @Autowired
    public IndexController(QuizSetupService quizSetupService) {
        this.quizSetupService = quizSetupService;
    }

    /*Dispaly index with statistisc GET*/
    @GetMapping("/")
    public String index(Model model) {
        /*Last week statistics*/
        model.addAttribute("gamesPlayedLastWeek", quizSetupService.gamesPlayedLastWeek());
        model.addAttribute("percentageOfCorrectAnswersLastWeek", decimalFormat.format(quizSetupService.correctAnswersLastWeek() *100 / quizSetupService.allAnswersLastWeek()));
        model.addAttribute("bestScoreLastWeek", quizSetupService.bestScoreLastWeek());
        model.addAttribute("bestUserLastWeek", quizSetupService.bestUserLastWeek());

        /*Last month statistics*/
        model.addAttribute("gamesPlayedLastMonth", quizSetupService.gamesPlayedLastMonth());
        model.addAttribute("percentageOfCorrectAnswersLastMonth", decimalFormat.format(quizSetupService.correctAnswersLastMonth() *100 / quizSetupService.allAnswersLastMonth()));
        model.addAttribute("bestScoreLastMonth", quizSetupService.bestScoreLastMonth());
        model.addAttribute("bestUserLastMonth", quizSetupService.bestUserLastMonth());

        /*Last month statistics*/
        model.addAttribute("gamesPlayedAll", quizSetupService.gamesPlayedAll());
        model.addAttribute("percentageOfCorrectAnswersAll", decimalFormat.format(quizSetupService.correctAnswersAll() *100 / quizSetupService.allAnswersAll()));
        model.addAttribute("bestScoreAll", quizSetupService.bestScoreAll());

        return "index/index";
    }

    /*Login GET*/
    @GetMapping("/login")
    public String login() {
        return "account/login-form";
    }

    /*Start new quiz GET*/
    @GetMapping("quizSetup/numberofplayers")
    public String play() {
        return "quizsetup/quizsetup-numberofplayers";
    }
}

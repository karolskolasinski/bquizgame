package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.karolskolasinski.bquizgame.model.dto.AnswersContentDto;
import pl.karolskolasinski.bquizgame.model.dto.SearchQuestionDto;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(path = "/editor/")
public class EditorController {

    private final QuestionService questionService;

    @Autowired
    public EditorController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /*Add new question GET*/
    @GetMapping("/add")
    public String addNewQuestion(Model model, Question question, AnswersContentDto answersContent) {
        model.addAttribute("newQuestion", question);
        model.addAttribute("answersContent", answersContent);
        model.addAttribute("categories", questionService.returnAllCategories());
        return "editor/editor-add";
    }

    /*Add new question POST*/
    @PostMapping("/add")
    public String addNewQuestion(Question question, AnswersContentDto answersContent, HttpServletRequest request) {
        questionService.bindAnswersWithQuestion(question, answersContent);
        questionService.setDifficultyAndSave(question, request);
        return "redirect:" + request.getHeader("referer");
    }

    /*Choose category and difficulty to edit GET*/
    @GetMapping("/choose")
    public String chooseCategoryAndDifficulty(Model model, SearchQuestionDto searchQuestion) {
        searchQuestion.setCategories(questionService.returnAllCategories());
        model.addAttribute("searchQuestion", searchQuestion);
        return "editor/editor-choose";
    }

    /*Choose category and difficulty to edit POST*/
    @PostMapping("/choose")
    public String questionsToDisplay(Model model, SearchQuestionDto searchQuestion) {
        String catgory = searchQuestion.getCategories().iterator().next();
        if (searchQuestion.getDifficulty() == 0) {
            model.addAttribute("questionsList", questionService.getAllByCategory(catgory));
        } else {
            model.addAttribute("questionsList", questionService.getAllByCategoryAndDifficulty(catgory, searchQuestion.getDifficulty()));
        }
        return "editor/editor-editlist";
    }

    /*Update question GET*/
    @GetMapping("/edit/{questionId}")
    public String update(Model model, @PathVariable(name = "questionId") Long questionId) {
        Question questionById = questionService.getOneById(questionId);
        model.addAttribute("answersContent", questionService.extractAnswersContent(questionById));
        model.addAttribute("newQuestion", questionById);
        model.addAttribute("categories", questionService.returnAllCategories());
        return "editor/editor-add";
    }

    /*Category statistics GET*/
    @GetMapping("/categoryStats")
    public String getCategoryStats(Model model) {
        model.addAttribute("categoryByDifficulty", questionService.countCategoriesByDifficulty());
        return "account/account-categorystats";
    }
}

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

    @GetMapping("/add")
    public String addQuestion(Model model, Question question, AnswersContentDto answersContent) {
        Set<String> categories = questionService.returnAllCategories();
        model.addAttribute("newQuestion", question);
        model.addAttribute("answersContent", answersContent);
        model.addAttribute("categories", categories);
        return "editor/editor-add";
    }

    @PostMapping("/add")
    public String createPostMethod(Question question, AnswersContentDto answersContent, HttpServletRequest request) {
        questionService.bindAnswersWithQuestion(question, answersContent);
        questionService.setDifficultyAndSave(question, request);
        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }

    @GetMapping("/choose")
    public String chooseCategoryAndDifficulty(Model model, SearchQuestionDto searchQuestion) {
        searchQuestion.setCategories(questionService.returnAllCategories());
        model.addAttribute("searchQuestion", searchQuestion);
        return "editor/editor-choose";
    }

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

    @GetMapping("/edit/{questionId}")
    public String update(Model model, @PathVariable(name = "questionId") Long questionId) {
        Question questionById = questionService.getOneById(questionId);
        Set<String> categories = questionService.returnAllCategories();
        model.addAttribute("answersContent", questionService.extractAnswersContent(questionById));
        model.addAttribute("newQuestion", questionById);
        model.addAttribute("categories", categories);
        return "editor/editor-add";
    }


}

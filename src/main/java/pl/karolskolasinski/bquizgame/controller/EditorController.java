package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.karolskolasinski.bquizgame.model.dto.AnswersContentDto;
import pl.karolskolasinski.bquizgame.model.schema.Answer;
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
        return "redirect:/editor/editor-edit";
    }

}

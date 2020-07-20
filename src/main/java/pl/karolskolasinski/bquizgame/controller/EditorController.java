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

@Controller
@RequestMapping(path = "/editor/")
public class EditorController {

    private final QuestionService questionService;

    @Autowired
    public EditorController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @GetMapping("/add")
    public String addNewQuestion(Model model, Question question, AnswersContentDto answersContentDto) {
        model.addAttribute("newQuestion", question);
        model.addAttribute("answersContentDto", answersContentDto);
        model.addAttribute("categories", questionService.returnAllCategories());

        return "editor/editor-add";
    }


    @PostMapping("/add")
    public String addNewQuestion(Model model, SearchQuestionDto searchQuestion, Question question, HttpServletRequest request) {
        questionService.bindAnswersWithQuestion(question, request);
        questionService.setDifficultyAndSave(question, request);

        return chooseCategoryAndDifficulty(model, searchQuestion);
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
        model.addAttribute("answersContentDto", questionService.extractAnswersContent(questionById));
        model.addAttribute("newQuestion", questionById);
        model.addAttribute("categories", questionService.returnAllCategories());

        return "editor/editor-edit";
    }


    @PostMapping("/edit")
    public String update(Question question, HttpServletRequest request) {
        questionService.update(question, request);

        return "redirect:" + request.getHeader("referer");
    }


    @GetMapping("/delete/{questionId}")
    public String delete(HttpServletRequest request, @PathVariable(name = "questionId") Long questionId) {
        questionService.deleteQuestion(questionId);

        return "redirect:" + request.getHeader("referer");
    }


    @GetMapping("/categoryStats")
    public String getCategoryStats(Model model) {
        model.addAttribute("categoryByDifficulty", questionService.countCategoriesByDifficulty());

        return "account/account-categorystats";
    }

}

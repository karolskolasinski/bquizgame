package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.dto.AnswersContentDto;
import pl.karolskolasinski.bquizgame.model.dto.CategoryStatsDto;
import pl.karolskolasinski.bquizgame.model.dto.ICategoryStatsDto;
import pl.karolskolasinski.bquizgame.model.schema.Answer;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.repository.AnswerRepository;
import pl.karolskolasinski.bquizgame.repository.QuestionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public Set<String> returnAllCategories() {
        return questionRepository.findAllCategories();
    }

    public void bindAnswersWithQuestion(Question question, AnswersContentDto answersContent) {
        Set<Answer> answers = new HashSet<>();
        Answer answer1 = returnAnswerWithContent(answersContent.getAnswer1Content(), true);
        Answer answer2 = returnAnswerWithContent(answersContent.getAnswer2Content(), false);
        Answer answer3 = returnAnswerWithContent(answersContent.getAnswer3Content(), false);
        Answer answer4 = returnAnswerWithContent(answersContent.getAnswer4Content(), false);
        answers.add(answer1);
        answers.add(answer2);
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        if (!answer3.getAnswerContent().isEmpty()) {
            answers.add(answer3);
            answerRepository.save(answer3);
        }
        if (!answer4.getAnswerContent().isEmpty()) {
            answers.add(answer4);
            answerRepository.save(answer4);
        }
        question.setAnswers(answers);
        answer1.setQuestion(question);
        answer2.setQuestion(question);
        answer3.setQuestion(question);
        answer4.setQuestion(question);
    }

    private Answer returnAnswerWithContent(String content, boolean correct) {
        Answer answer = new Answer();
        answer.setAnswerContent(content);
        answer.setCorrect(correct);
        return answer;
    }

    public void setDifficultyAndSave(Question question, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            if (stringEntry.getKey().equals("0") ||
                    stringEntry.getKey().equals("1") ||
                    stringEntry.getKey().equals("2") ||
                    stringEntry.getKey().equals("3") ||
                    stringEntry.getKey().equals("4")) {
                String difficulty = Arrays.toString(stringEntry.getValue());
                question.setDifficulty(Integer.parseInt(difficulty.substring(1, difficulty.length() - 1)));
            }
        }
        questionRepository.save(question);
    }

    public List<Question> getAllByCategory(String catgory) {
        return questionRepository.findAllByChoosedCategory(catgory);
    }

    public List<Question> getAllByCategoryAndDifficulty(String catgory, int difficulty) {
        if (difficulty == 1) {
            return questionRepository.findAllDifficulty1ByChoosedCategory(catgory);
        } else if (difficulty == 2) {
            return questionRepository.findAllDifficulty2ByChoosedCategory(catgory);
        } else if (difficulty == 3) {
            return questionRepository.findAllDifficulty3ByChoosedCategory(catgory);
        } else {
            return questionRepository.findAllDifficulty4ByChoosedCategory(catgory);
        }
    }

    public Question getOneById(Long questionId) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        return questionOptional.orElseGet(Question::new);
    }

    public AnswersContentDto extractAnswersContent(Question questionById) {
        Set<Answer> answers = questionById.getAnswers();
        List<String> answersContents = new ArrayList<>();
        for (Answer answer : answers) {
            answersContents.add(answer.getAnswerContent());
        }
        AnswersContentDto answersContentDto = new AnswersContentDto();
        answersContentDto.setAnswer1Content(answersContents.get(0));
        answersContentDto.setAnswer2Content(answersContents.get(1));
        answersContentDto.setAnswer3Content(answersContents.get(2));
        answersContentDto.setAnswer4Content(answersContents.get(3));
        return answersContentDto;
    }
}

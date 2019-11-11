package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.dto.AnswersContentDto;
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

        if (!answersContent.getAnswer3Content().isEmpty()) {
            Answer answer3 = returnAnswerWithContent(answersContent.getAnswer3Content(), false);
            answers.add(answer3);
            answerRepository.save(answer3);
            Objects.requireNonNull(answer3).setQuestion(question);
        }
        if (!answersContent.getAnswer4Content().isEmpty()) {
            Answer answer4 = returnAnswerWithContent(answersContent.getAnswer4Content(), false);
            answers.add(answer4);
            answerRepository.save(answer4);
            Objects.requireNonNull(answer4).setQuestion(question);
        }
        answers.add(answer1);
        answers.add(answer2);
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        question.setAnswers(answers);
        answer1.setQuestion(question);
        answer2.setQuestion(question);
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
            if (stringEntry.getKey().equals("0")) {
                String difficulty = Arrays.toString(stringEntry.getValue());
                question.setDifficulty(Integer.parseInt(difficulty.substring(1, difficulty.length() - 1)));
            }
        }
        questionRepository.save(question);
    }
}

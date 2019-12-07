package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.dto.AnswersContentDto;
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

    public void bindAnswersWithQuestion(Question question, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] answersContents = parameterMap.get("answersContent");
        Set<Answer> answers = new HashSet<>();
        Answer answer1 = returnAnswerWithContent(answersContents[0], true);
        Answer answer2 = returnAnswerWithContent(answersContents[1], false);
        Answer answer3 = returnAnswerWithContent(answersContents[2], false);
        Answer answer4 = returnAnswerWithContent(answersContents[3], false);

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
        setQuestionToAnswer(question, answer1);
        setQuestionToAnswer(question, answer2);
        setQuestionToAnswer(question, answer3);
        setQuestionToAnswer(question, answer4);
    }

    private Answer returnAnswerWithContent(String content, boolean correct) {
        Answer answer = new Answer();
        setAnswerWithContent(content, answer);
        answer.setCorrect(correct);
        return answer;
    }

    public void setDifficultyAndSave(Question question, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        setDifficulty(question,parameterMap);
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
        List<Answer> answerList = getSortedAnswersList(questionById);
        AnswersContentDto answersContentDto = new AnswersContentDto();
        answersContentDto.setAnswer1Content(getAnswer(answerList, 0).getAnswerContent());
        answersContentDto.setAnswer2Content(getAnswer(answerList, 1).getAnswerContent());
        if (answerList.size() == 3) {
            answersContentDto.setAnswer3Content(getAnswer(answerList, 2).getAnswerContent());
        }
        if (answerList.size() == 4) {
            answersContentDto.setAnswer3Content(getAnswer(answerList, 2).getAnswerContent());
            answersContentDto.setAnswer4Content(getAnswer(answerList, 3).getAnswerContent());
        }
        return answersContentDto;
    }

    private List<Answer> getSortedAnswersList(Question questionById) {
        List<Answer> answerList = new ArrayList<>(questionById.getAnswers());
        answerList.sort(Comparator.comparing(Answer::getId));
        return answerList;
    }

    public List<ICategoryStatsDto> countCategoriesByDifficulty() {
        return questionRepository.countCategoriesByDifficulty();
    }

    public void update(Question question, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        updateAnswers(question, parameterMap);
        updateQuestion(question, parameterMap);
    }

    private void updateAnswers(Question question, Map<String, String[]> parameterMap) {
        List<Answer> answerList = getSortedAnswersList(questionRepository.getOne(question.getId()));
        checkNullableAnswer(answerList, parameterMap.get("answer1Content")[0], question, 1);
        checkNullableAnswer(answerList, parameterMap.get("answer2Content")[0], question, 2);
        checkNullableAnswer(answerList, parameterMap.get("answer3Content")[0], question, 3);
        checkNullableAnswer(answerList, parameterMap.get("answer4Content")[0], question, 4);
    }

    private void updateQuestion(Question question, Map<String, String[]> parameterMap) {
        setDifficulty(question, parameterMap);
        question.setContent(parameterMap.get("content")[0]);
        questionRepository.save(question);
    }

    private void setDifficulty(Question question, Map<String, String[]> parameterMap) {
        for (int i = 0; i <= 4; i++) {
            if (parameterMap.containsKey("" + i)) {
                question.setDifficulty(Integer.parseInt(parameterMap.get("" + i)[0]));
            }
        }
    }

    private void checkNullableAnswer(List<Answer> answerList, String answerContent, Question question, int i) {
        if (!answerContent.trim().isEmpty()) {
            if (answerList.size() >= i) {
                Answer answer = getAnswer(answerList, i - 1);
                setAnswerWithContent(answerContent, answer);
                answerRepository.save(answer);
            } else {
                Answer newAnswer = new Answer();
                setQuestionToAnswer(question, newAnswer);
                setAnswerWithContent(answerContent, newAnswer);
                answerRepository.save(newAnswer);
            }
        } else {
            if (answerList.size() >= i) {
                answerRepository.delete(getAnswer(answerList, i - 1));
            }
        }
    }

    private void setQuestionToAnswer(Question question, Answer newAnswer) {
        newAnswer.setQuestion(question);
    }

    private void setAnswerWithContent(String answerContent, Answer answer) {
        answer.setAnswerContent(answerContent);
    }

    private Answer getAnswer(List<Answer> answerList, int i) {
        return answerList.get(i);
    }

}

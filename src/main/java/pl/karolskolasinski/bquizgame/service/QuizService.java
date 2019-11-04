package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.schema.Answer;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;
import pl.karolskolasinski.bquizgame.model.userplays.UserAnswer;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.AnswerRepository;
import pl.karolskolasinski.bquizgame.repository.QuestionRepository;
import pl.karolskolasinski.bquizgame.repository.QuizRepository;
import pl.karolskolasinski.bquizgame.repository.QuizSetupRepository;

import java.util.*;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizSetupRepository quizSetupRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, QuizSetupRepository quizSetupRepository, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.quizSetupRepository = quizSetupRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public Question pickQuestion(Long newUserQuizId, int difficulty, String category) {
        Optional<UserQuiz> userQuizOptional = quizSetupRepository.findById(newUserQuizId);
        if (userQuizOptional.isPresent()) {
            Long id = userQuizOptional.get().getQuiz().getId();
            Optional<Quiz> quizOptional = quizRepository.findById(id);
            if (quizOptional.isPresent()) {
                Quiz quiz = quizOptional.get();
                Set<Question> questionSet = quiz.getQuestionSet();
                for (Question question : questionSet) {
                    if (question.getDifficulty() == difficulty && question.getCategory().equals(category)) {
                        return question;
                    }
                }
            }
            return new Question();
        }
        return new Question();
    }

    public List<Answer> questionAnswersSetToList(Question question) {
        List<Answer> answers = new ArrayList<>(question.getAnswers());
        Collections.shuffle(answers);
        return answers;
    }

    public UserAnswer checkIsAnswerCorrect(Long newUserQuizId, Long answerId, Long currentQuestionId) {
        UserAnswer userAnswer = new UserAnswer();
        UserQuiz userQuiz = quizSetupRepository.getOne(newUserQuizId);
        userAnswer.setUserQuiz(userQuiz);
        userAnswer.setAnswer(answerRepository.getOne(answerId));
        userAnswer.setQuestion(questionRepository.getOne(currentQuestionId));
        return userAnswer;
    }
}

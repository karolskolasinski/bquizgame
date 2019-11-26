package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.dto.ICategoryStatsDto;
import pl.karolskolasinski.bquizgame.model.dto.ResultsDto;
import pl.karolskolasinski.bquizgame.model.schema.Answer;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;
import pl.karolskolasinski.bquizgame.model.userplays.UserAnswer;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;
import pl.karolskolasinski.bquizgame.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizSetupRepository quizSetupRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserAnswerRepository userAnswerRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, QuizSetupRepository quizSetupRepository, AnswerRepository answerRepository, QuestionRepository questionRepository, UserAnswerRepository userAnswerRepository) {
        this.quizRepository = quizRepository;
        this.quizSetupRepository = quizSetupRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    public List<String> categories(Long newUserQuizId) {
        Set<Question> questionSet = quizSetupRepository.getOne(newUserQuizId).getQuiz().getQuestionSet();
        return questionSet.stream().map(Question::getCategory).distinct().sorted().collect(Collectors.toList());
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

    public UserAnswer setPlayerScoreAndReturnUserAnswer(Long newUserQuizId, Long answerId, Long currentQuestionId) {
        UserAnswer userAnswer = new UserAnswer();
        UserQuiz newUserQuiz = quizSetupRepository.getOne(newUserQuizId);
        userAnswer.setUserQuiz(newUserQuiz);
        userAnswer.setAnswer(answerRepository.getOne(answerId));
        userAnswer.setQuestion(questionRepository.getOne(currentQuestionId));
        userAnswerRepository.save(userAnswer);
        playerScore(newUserQuiz, userAnswer);
        return userAnswer;
    }

    public StringBuilder questionAnswersOrder(List<Answer> questionAnswerList) {
        StringBuilder questionAnswerOrder = new StringBuilder();
        questionAnswerList.forEach(answer -> questionAnswerOrder.append(answer.getId()).append(","));
        return questionAnswerOrder;
    }

    public List<Answer> getOrderedAnswers(String questionAnswerOrder) {
        List<Answer> orderedAnswers = new ArrayList<>();
        orderedAnswers.add(answerRepository.getOne(Long.valueOf(questionAnswerOrder.split(",")[0])));
        orderedAnswers.add(answerRepository.getOne(Long.valueOf(questionAnswerOrder.split(",")[1])));
        orderedAnswers.add(answerRepository.getOne(Long.valueOf(questionAnswerOrder.split(",")[2])));
        orderedAnswers.add(answerRepository.getOne(Long.valueOf(questionAnswerOrder.split(",")[3])));
        return orderedAnswers;
    }

    public Long getCorrectId(List<Answer> orderedAnswers) {
        return orderedAnswers.stream().filter(Answer::isCorrect).findFirst().get().getId();
    }

    /*Count current player score*/
    public UserQuiz setNextPlayerAndReturnNewUserQuiz(Long newUserQuizId) {
        UserQuiz newUserQuiz = quizSetupRepository.getOne(newUserQuizId);
        playerQueue(newUserQuiz);
        return newUserQuiz;
    }

    private void playerScore(UserQuiz newUserQuiz, UserAnswer userAnswer) {

        if (newUserQuiz.getNumberOfPlayers() == 1 && userAnswer.getAnswer().isCorrect()) {
            newUserQuiz.setPlayer1Score(newUserQuiz.getPlayer1Score() + userAnswer.getQuestion().getDifficulty());
            quizSetupRepository.save(newUserQuiz);
        } else if (newUserQuiz.getNumberOfPlayers() == 2 && userAnswer.getAnswer().isCorrect()) {
            if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer1Name())) {
                newUserQuiz.setPlayer1Score(newUserQuiz.getPlayer1Score() + userAnswer.getQuestion().getDifficulty());
            } else {
                newUserQuiz.setPlayer2Score(newUserQuiz.getPlayer2Score() + userAnswer.getQuestion().getDifficulty());
            }
            quizSetupRepository.save(newUserQuiz);
        } else if (newUserQuiz.getNumberOfPlayers() == 3 && userAnswer.getAnswer().isCorrect()) {
            if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer1Name())) {
                newUserQuiz.setPlayer1Score(newUserQuiz.getPlayer1Score() + userAnswer.getQuestion().getDifficulty());
            } else if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer2Name())) {
                newUserQuiz.setPlayer2Score(newUserQuiz.getPlayer2Score() + userAnswer.getQuestion().getDifficulty());
            } else {
                newUserQuiz.setPlayer3Score(newUserQuiz.getPlayer3Score() + userAnswer.getQuestion().getDifficulty());
            }
            quizSetupRepository.save(newUserQuiz);
        } else if (newUserQuiz.getNumberOfPlayers() == 4 && userAnswer.getAnswer().isCorrect()) {
            if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer1Name())) {
                newUserQuiz.setPlayer1Score(newUserQuiz.getPlayer1Score() + userAnswer.getQuestion().getDifficulty());
            } else if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer2Name())) {
                newUserQuiz.setPlayer2Score(newUserQuiz.getPlayer2Score() + userAnswer.getQuestion().getDifficulty());
            } else if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer3Name())) {
                newUserQuiz.setPlayer3Score(newUserQuiz.getPlayer3Score() + userAnswer.getQuestion().getDifficulty());
            } else {
                newUserQuiz.setPlayer4Score(newUserQuiz.getPlayer4Score() + userAnswer.getQuestion().getDifficulty());
            }
            quizSetupRepository.save(newUserQuiz);
        }
    }

    private void playerQueue(UserQuiz newUserQuiz) {
        switch (newUserQuiz.getNumberOfPlayers()) {
            case 1:
                break;
            case 2:
                if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer1Name())) {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer2Name());
                } else {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer1Name());
                }
                quizSetupRepository.save(newUserQuiz);
                break;
            case 3:
                if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer1Name())) {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer2Name());
                } else if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer2Name())) {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer3Name());
                } else {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer1Name());
                }
                quizSetupRepository.save(newUserQuiz);
                break;
            case 4:
                if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer1Name())) {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer2Name());
                } else if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer2Name())) {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer3Name());
                } else if (newUserQuiz.getCurrentPlayer().equals(newUserQuiz.getPlayer3Name())) {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer4Name());
                } else {
                    newUserQuiz.setCurrentPlayer(newUserQuiz.getPlayer1Name());
                }
                quizSetupRepository.save(newUserQuiz);
                break;
            default:
                break;
        }
    }

    public List<Question> withdrawQuestion(Long newUserQuizId) {
        Optional<UserQuiz> quizOptional = quizSetupRepository.findById(newUserQuizId);
        if (quizOptional.isPresent()) {
            UserQuiz quiz = quizOptional.get();
            return quiz.getUserAnswers().stream().map(UserAnswer::getQuestion).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public int playerPlace(Long newUserQuizId, String currentPlayer) {
        UserQuiz newUserQuiz = quizSetupRepository.getOne(newUserQuizId);
        Integer[] scores = new Integer[]{newUserQuiz.getPlayer1Score(), newUserQuiz.getPlayer2Score(), newUserQuiz.getPlayer3Score(), newUserQuiz.getPlayer4Score()};
        Arrays.sort(scores, Collections.reverseOrder());

        switch (newUserQuiz.getNumberOfPlayers()) {
            case 1:
                return 1;
            case 2:
                if (currentPlayer.equals(newUserQuiz.getPlayer1Name())) {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer1Score()) + 1;
                } else {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer2Score()) + 1;
                }
            case 3:
                if (currentPlayer.equals(newUserQuiz.getPlayer1Name())) {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer1Score()) + 1;
                } else if (currentPlayer.equals(newUserQuiz.getPlayer2Name())) {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer2Score()) + 1;
                } else {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer3Score()) + 1;
                }
            case 4:
                if (currentPlayer.equals(newUserQuiz.getPlayer1Name())) {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer1Score()) + 1;
                } else if (currentPlayer.equals(newUserQuiz.getPlayer2Name())) {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer2Score()) + 1;
                } else if (currentPlayer.equals(newUserQuiz.getPlayer3Name())) {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer3Score()) + 1;
                } else {
                    return Arrays.asList(scores).indexOf(newUserQuiz.getPlayer4Score()) + 1;
                }
            default:
                break;
        }
        return 1;
    }

    public List<ResultsDto> results(Long newUserQuizId) {
        UserQuiz newUserQuiz = quizSetupRepository.getOne(newUserQuizId);
        List<ResultsDto> results = new ArrayList<>();

        ResultsDto player1resuts = new ResultsDto();
        ResultsDto player2resuts = new ResultsDto();
        ResultsDto player3resuts = new ResultsDto();
        ResultsDto player4resuts = new ResultsDto();

        switch (newUserQuiz.getNumberOfPlayers()) {
            case 1:
                player1resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer1Name()));
                player1resuts.setName(newUserQuiz.getPlayer1Name());
                player1resuts.setScore(newUserQuiz.getPlayer1Score());
                results.add(player1resuts);
                break;
            case 2:
                player1resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer1Name()));
                player1resuts.setName(newUserQuiz.getPlayer1Name());
                player1resuts.setScore(newUserQuiz.getPlayer1Score());
                results.add(player1resuts);

                player2resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer2Name()));
                player2resuts.setName(newUserQuiz.getPlayer2Name());
                player2resuts.setScore(newUserQuiz.getPlayer2Score());
                results.add(player2resuts);
                break;
            case 3:
                player1resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer1Name()));
                player1resuts.setName(newUserQuiz.getPlayer1Name());
                player1resuts.setScore(newUserQuiz.getPlayer1Score());
                results.add(player1resuts);

                player2resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer2Name()));
                player2resuts.setName(newUserQuiz.getPlayer2Name());
                player2resuts.setScore(newUserQuiz.getPlayer2Score());
                results.add(player2resuts);

                player3resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer3Name()));
                player3resuts.setName(newUserQuiz.getPlayer3Name());
                player3resuts.setScore(newUserQuiz.getPlayer3Score());
                results.add(player3resuts);
                break;
            case 4:
                player1resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer1Name()));
                player1resuts.setName(newUserQuiz.getPlayer1Name());
                player1resuts.setScore(newUserQuiz.getPlayer1Score());
                results.add(player1resuts);

                player2resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer2Name()));
                player2resuts.setName(newUserQuiz.getPlayer2Name());
                player2resuts.setScore(newUserQuiz.getPlayer2Score());
                results.add(player2resuts);

                player3resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer3Name()));
                player3resuts.setName(newUserQuiz.getPlayer3Name());
                player3resuts.setScore(newUserQuiz.getPlayer3Score());
                results.add(player3resuts);

                player4resuts.setPlace(playerPlace(newUserQuizId, newUserQuiz.getPlayer4Name()));
                player4resuts.setName(newUserQuiz.getPlayer4Name());
                player4resuts.setScore(newUserQuiz.getPlayer4Score());
                results.add(player4resuts);
                break;
            default:
                break;
        }
        results.sort(Comparator.comparing(ResultsDto::getPlace));
        return results;
    }
}

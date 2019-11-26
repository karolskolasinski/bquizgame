package pl.karolskolasinski.bquizgame.component;

import org.hibernate.TransientPropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.karolskolasinski.bquizgame.model.account.Account;
import pl.karolskolasinski.bquizgame.model.account.AccountRole;
import pl.karolskolasinski.bquizgame.model.schema.Answer;
import pl.karolskolasinski.bquizgame.model.schema.Question;
import pl.karolskolasinski.bquizgame.repository.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private AccountRepository accountRepository;
    private AccountRoleRepository accountRoleRepository;
    private PasswordEncoder passwordEncoder;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    @Autowired
    public DataInitializer(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository, PasswordEncoder passwordEncoder, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        addDefaultRole("USER");
        addDefaultRole("ADMIN");
        addDefaultRole("MODERATOR");
        addDefaultUser("admin", "admin", "admin@admin.com", "ADMIN", "USER");
        addDefaultUser("user", "user", "user@user.com", "USER");
        addDefaultQuestions();
    }

    private void addDefaultUser(String username, String password, String email, String... roles) {
        if (!accountRepository.existsByUsername(username)) {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(passwordEncoder.encode(password));
            account.setEmail(email);
            account.setAccountRoles(findRoles(roles));
            accountRepository.save(account);
        }
    }

    private Set<AccountRole> findRoles(String[] roles) {
        Set<AccountRole> accountRoles = new HashSet<>();
        for (String role : roles) {
            accountRoleRepository.findByName(role).ifPresent(accountRoles::add);
        }
        return accountRoles;
    }

    private void addDefaultRole(String role) {
        if (!accountRoleRepository.existsByName(role)) {
            AccountRole newRole = new AccountRole();
            newRole.setName(role);
            accountRoleRepository.save(newRole);
        }
    }

    private void addDefaultQuestions() {
        File file = new File(Objects.requireNonNull(classLoader.getResource("questions/questions_answers.html")).getFile());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            tryReadFromFileAndSaveToDatabase(file, reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryReadFromFileAndSaveToDatabase(File file, BufferedReader reader) throws IOException {
        if (file.exists()) {
            while (reader.ready()) {

                /*Creating default questions*/
                Question question = new Question();
                question.setCategory(reader.readLine());
                question.setDifficulty(Integer.parseInt(reader.readLine()));
                question.setContent(reader.readLine());
                question.setReference(reader.readLine());

                if (!questionRepository.existsByContent(question.getContent())) {
                    questionRepository.save(question);
                    /*Creating answers to each question*/
                    Answer answer1 = new Answer();
                    answer1.setCorrect(true);
                    bindAnswerWithQuestion(reader, question, answer1);
                    Answer answer2 = new Answer();
                    bindAnswerWithQuestion(reader, question, answer2);
                    Answer answer3 = new Answer();
                    bindAnswerWithQuestion(reader, question, answer3);
                    Answer answer4 = new Answer();
                    bindAnswerWithQuestion(reader, question, answer4);

                    /*Read separator*/
                    reader.readLine();
                } else {
                    for (int i = 0; i <= 4; i++) {
                        reader.readLine();
                    }
                }
            }
        } else {
            System.err.println("file " + file.getName() + " does not exist.");
        }
    }

    private void bindAnswerWithQuestion(BufferedReader reader, Question question, Answer answer) throws IOException {
        answer.setAnswerContent(reader.readLine());
        answer.setQuestion(question);
        try {
            answerRepository.save(answer);
        } catch (TransientPropertyValueException | InvalidDataAccessApiUsageException e) {
            System.err.println("object (Answer) references an unsaved transient instance (Question)");
        }
    }
}

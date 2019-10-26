package pl.karolskolasinski.bquizgame.model.userplays;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.karolskolasinski.bquizgame.model.account.Account;
import pl.karolskolasinski.bquizgame.model.schema.Quiz;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime quizStartDateTime;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Quiz quiz;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "userQuiz", fetch = FetchType.EAGER)   // <-------- ??
    private Set<UserAnswer> userAnswers;
}

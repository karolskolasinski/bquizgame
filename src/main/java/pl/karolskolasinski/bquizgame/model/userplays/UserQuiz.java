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


    @Column(nullable = false, columnDefinition = "tinyint default 1")
    private byte numberOfPlayers;


    private String player1Name;
    private String player2Name;
    private String player3Name;
    private String player4Name;
    private String currentPlayer;
    private String categories;


    @Column(columnDefinition = "tinyint default 0")
    private int player1Score;


    @Column(columnDefinition = "tinyint default 0")
    private int player2Score;


    @Column(columnDefinition = "tinyint default 0")
    private int player3Score;


    @Column(columnDefinition = "tinyint default 0")
    private int player4Score;


    @ManyToOne
    private Quiz quiz;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "userQuiz", fetch = FetchType.EAGER)
    private Set<UserAnswer> userAnswers;

}

package pl.karolskolasinski.bquizgame.model.schema;

import lombok.*;
import pl.karolskolasinski.bquizgame.model.userplays.UserAnswer;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String answerContent;

    @NotNull
    @Column(columnDefinition = "tinyint default 0")
    private boolean correct;

    @ManyToOne
    private Question question;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "answer", fetch = FetchType.EAGER)
    private Set<UserAnswer> userAnswers;
}

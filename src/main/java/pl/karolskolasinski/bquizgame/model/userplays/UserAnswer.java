package pl.karolskolasinski.bquizgame.model.userplays;

import lombok.*;
import pl.karolskolasinski.bquizgame.model.schema.Answer;
import pl.karolskolasinski.bquizgame.model.schema.Question;

import javax.persistence.*;
import java.util.Set;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "userAnswer", fetch = FetchType.EAGER)   // <-------- ??
    private Set<Question> questionList;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "userAnswer", fetch = FetchType.EAGER)   // <-------- ??
    private Set<Answer> answerList;
}

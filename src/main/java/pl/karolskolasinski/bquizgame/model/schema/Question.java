package pl.karolskolasinski.bquizgame.model.schema;

import lombok.*;
import pl.karolskolasinski.bquizgame.model.userplays.UserAnswer;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String category;

    @NotEmpty
    @Size(min = 1, max = 4)
    int difficulty;

    @NotEmpty
    private String content;

    @ManyToOne
    private Quiz quiz;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)   // <-------- ??
    private Set<Answer> answerList;

    @ManyToOne
    private UserAnswer userAnswer;
}

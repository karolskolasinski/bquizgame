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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String category;

    @NotNull
    int difficulty;

    @NotEmpty
    private String content;

    @Column(columnDefinition = "text")
    private String reference;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "questionSet")
    private Set<Quiz> quizez;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answers;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<UserAnswer> userAnswers;
}

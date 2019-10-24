package pl.karolskolasinski.bquizgame.model.schema;

import lombok.*;
import pl.karolskolasinski.bquizgame.model.userplays.UserAnswer;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String answerContent;

    @NotEmpty
    private boolean correct;

    private String reference;

    @ManyToOne
    private Question question;

    @ManyToOne
    private UserAnswer userAnswer;
}

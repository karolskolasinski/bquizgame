package pl.karolskolasinski.bquizgame.model.userplays;

import lombok.*;
import pl.karolskolasinski.bquizgame.model.schema.Answer;
import pl.karolskolasinski.bquizgame.model.schema.Question;

import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private Question question;


    @ManyToOne
    private Answer answer;


    @ManyToOne
    private UserQuiz userQuiz;

}

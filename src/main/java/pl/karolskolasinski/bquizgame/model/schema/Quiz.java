package pl.karolskolasinski.bquizgame.model.schema;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime quizStartDateTime;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER)   // <-------- ??
    private Set<Question> questionList;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER)   // <-------- ??
    private Set<UserQuiz> userQuizzes;
}

package pl.karolskolasinski.bquizgame.model.account;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import pl.karolskolasinski.bquizgame.model.userplays.UserQuiz;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 3)
    private String username;

    @NotEmpty
    @Size(min = 4)
    private String password;

    @NotEmpty
    @Size(min = 4)
    private String email;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime registrationDateTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(value = org.hibernate.annotations.CascadeType.DETACH)
    private Set<AccountRole> accountRoles;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<UserQuiz> userQuizzes;

    public boolean isAdmin() {
        return accountRoles.stream()
                .anyMatch(accountRole -> accountRole.getName().equals("ADMIN"));
    }
}


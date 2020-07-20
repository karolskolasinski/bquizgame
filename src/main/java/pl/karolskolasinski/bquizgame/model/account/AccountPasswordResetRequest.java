package pl.karolskolasinski.bquizgame.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPasswordResetRequest {

    private Long accountId;


    @NotEmpty
    @Size(min = 4, max = 72)
    private String resetPassword;

}

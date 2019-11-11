package pl.karolskolasinski.bquizgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswersContentDto {

    private String answer1Content;
    private String answer2Content;
    private String answer3Content;
    private String answer4Content;
}

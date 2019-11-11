package pl.karolskolasinski.bquizgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultsDto {

    private Integer place;
    private String name;
    private Integer score;
}

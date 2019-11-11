package pl.karolskolasinski.bquizgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchQuestionDto {

    private Set<String> categories;
    private int difficulty;
}

package pl.karolskolasinski.bquizgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryStatsDto {

    private String category;
    private Integer difficulty;
    private Integer count;
}

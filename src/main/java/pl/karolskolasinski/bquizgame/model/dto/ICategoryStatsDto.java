package pl.karolskolasinski.bquizgame.model.dto;

public interface ICategoryStatsDto {

    Long getId();

    String getCategory();

    Integer getDifficulty();

    Integer getCount();
}

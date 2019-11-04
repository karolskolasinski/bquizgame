package pl.karolskolasinski.bquizgame.component;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CharacterUtility {

    public static String intToCharLetter(int i) {
        return "" + (char) (i + 65);
    }
}

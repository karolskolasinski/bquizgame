package pl.karolskolasinski.bquizgame.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterUtilityTest {

    @Test
    void shouldReturnALetterWhenNumberIs0() {
        //given
        final int number0 = 0;

        //when
        String intToCharLetter = CharacterUtility.intToCharLetter(number0);

        //then
        assertEquals("A", intToCharLetter);
    }

    @Test
    void shouldReturnBLetterWhenNumberIs1() {
        //given
        final int number1 = 1;

        //when
        String intToCharLetter = CharacterUtility.intToCharLetter(number1);

        //then
        assertEquals("B", intToCharLetter);
    }

    @Test
    void shouldReturnCLetterWhenNumberIs2() {
        //given
        final int number2 = 2;

        //when
        String intToCharLetter = CharacterUtility.intToCharLetter(number2);

        //then
        assertEquals("C", intToCharLetter);
    }

    @Test
    void shouldReturnDLetterWhenNumberIs3() {
        //given
        final int number3 = 3;

        //when
        String intToCharLetter = CharacterUtility.intToCharLetter(number3);

        //then
        assertEquals("D", intToCharLetter);
    }
}

package pl.karolskolasinski.bquizgame.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterUtilityTest {

    @Test
    void shouldReturnALetterWhenNumberIs0() {
        //given
        CharacterUtility characterUtility = new CharacterUtility();
        final int number0 = 0;

        //then
        assertEquals("A", characterUtility.intToCharLetter(number0));
    }

    @Test
    void shouldReturnBLetterWhenNumberIs1() {
        //given
        CharacterUtility characterUtility = new CharacterUtility();
        final int number1 = 1;

        //then
        assertEquals("B", characterUtility.intToCharLetter(number1));
    }
    @Test
    void shouldReturnCLetterWhenNumberIs2() {
        //given
        CharacterUtility characterUtility = new CharacterUtility();
        final int number2 = 2;

        //then
        assertEquals("C", characterUtility.intToCharLetter(number2));
    }

    @Test
    void shouldReturnDLetterWhenNumberIs3() {
        //given
        CharacterUtility characterUtility = new CharacterUtility();
        final int number3 = 3;

        //then
        assertEquals("D", characterUtility.intToCharLetter(number3));
    }
}

package com.guliash.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelperFunctionsTester extends BaseTester {

    @Test
    public void checkWordCharacterUnderscoreIsOk() {
        assertEquals(ArithmeticParser.isWordOnlyCharacter('_'), true);
    }

    @Test
    public void checkWordCharacterDollarIsOk() {
        assertEquals(ArithmeticParser.isWordOnlyCharacter('$'), true);
    }

    @Test
    public void checkWordCharacterLetterIsOk1() {
        assertEquals(ArithmeticParser.isWordOnlyCharacter('b'), true);
    }

    @Test
    public void checkWordCharacterLetterIsOk2() {
        assertEquals(ArithmeticParser.isWordOnlyCharacter('ш'), true);
    }

    @Test
    public void checkWordCharacterSpaceIsBad() {
        assertEquals(ArithmeticParser.isWordOnlyCharacter(' '), false);
    }

    @Test
    public void checkWordCharacterDigitIsBad() {
        assertEquals(ArithmeticParser.isWordOnlyCharacter('9'), false);
    }

}

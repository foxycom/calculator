package com.guliash.parser.evaluator.java;

import com.guliash.parser.ArithmeticParser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelperFunctionsTester extends BaseTester {

    @Test
    public void checkWordCharacterUnderscoreIsOk() {
        Assert.assertEquals(ArithmeticParser.isWordOnlyCharacter('_'), true);
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
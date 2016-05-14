package com.guliash.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelperFunctionsTester extends BaseParserTester {

    @Test
    public void checkWordCharacterUnderscoreIsOk() {
        Assert.assertEquals(Verify.isWordOnlyCharacter('_'), true);
    }

    @Test
    public void checkWordCharacterDollarIsOk() {
        assertEquals(Verify.isWordOnlyCharacter('$'), true);
    }

    @Test
    public void checkWordCharacterLetterIsOk1() {
        assertEquals(Verify.isWordOnlyCharacter('b'), true);
    }

    @Test
    public void checkWordCharacterLetterIsOk2() {
        assertEquals(Verify.isWordOnlyCharacter('ш'), true);
    }

    @Test
    public void checkWordCharacterSpaceIsBad() {
        assertEquals(Verify.isWordOnlyCharacter(' '), false);
    }

    @Test
    public void checkWordCharacterDigitIsBad() {
        assertEquals(Verify.isWordOnlyCharacter('9'), false);
    }

}

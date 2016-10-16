package com.guliash.parser;

import com.guliash.parser.stemmer.Stemmer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StemmerTester {

    @Test
    public void checkWordCharacterUnderscoreIsOk() {
        assertEquals(Stemmer.isWordOnlyCharacter('_'), true);
    }

    @Test
    public void checkWordCharacterDollarIsOk() {
        assertEquals(Stemmer.isWordOnlyCharacter('$'), true);
    }

    @Test
    public void checkWordCharacterLetterIsOk1() {
        assertEquals(Stemmer.isWordOnlyCharacter('b'), true);
    }

    @Test
    public void checkWordCharacterLetterIsOk2() {
        assertEquals(Stemmer.isWordOnlyCharacter('ш'), true);
    }

    @Test
    public void checkWordCharacterSpaceIsBad() {
        assertEquals(Stemmer.isWordOnlyCharacter(' '), false);
    }

    @Test
    public void checkWordCharacterDigitIsBad() {
        assertEquals(Stemmer.isWordOnlyCharacter('9'), false);
    }
}

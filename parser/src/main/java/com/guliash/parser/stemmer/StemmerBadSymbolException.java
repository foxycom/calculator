package com.guliash.parser.stemmer;

import java.util.Locale;

public class StemmerBadSymbolException extends RuntimeException {

    /**
     * 0-based index of bad symbol
     */
    private int charPosition;
    /**
     * bad symbol itself
     */
    private char symbol;

    public StemmerBadSymbolException(int charPosition, char symbol) {
        this.charPosition = charPosition;
        this.symbol = symbol;
    }

    @Override
    public String getMessage() {
        return String.format(Locale.US, "Bad symbol %c found at position %d", symbol, charPosition);
    }

    public int getCharPosition() {
        return charPosition;
    }

    public char getSymbol() {
        return symbol;
    }
}

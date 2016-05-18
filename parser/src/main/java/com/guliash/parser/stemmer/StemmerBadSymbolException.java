package com.guliash.parser.stemmer;

import java.util.Locale;

public class StemmerBadSymbolException extends RuntimeException {
    
    int charPosition;
    char symbol;

    public StemmerBadSymbolException(int charPosition, char symbol) {
        this.charPosition = charPosition;
        this.symbol = symbol;
    }

    @Override
    public String getMessage() {
        return String.format(Locale.US, "Bad symbol %c found at position %d", symbol, charPosition);
    }
}

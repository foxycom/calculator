package com.guliash.parser.stemmer;

import com.guliash.parser.Verify;

public class Stemmer {

    private String s;
    private int pos;
    private char ch;
    private Lexeme lexeme;
    private String word;
    private double number;

    public enum Lexeme {
        NUMBER,
        DIVISION,
        MULTIPLICATION,
        ADDITION,
        SUBTRACTION,
        WORD,
        END_OF_LINE,
        OPEN_BRACKET,
        CLOSE_BRACKET,
        COMMA
    }

    public Stemmer(String s) {
        this.s = s;
    }

    public void start() {
        readChar();
        readLexeme();
    }

    private void readChar() {
        if (pos < s.length()) {
            ch = s.charAt(pos++);
        } else {
            ch = '\n';
        }
    }

    public void readLexeme() {
        while (ch == ' ') {
            readChar();
        }
        if (ch == '*') {
            lexeme = Lexeme.MULTIPLICATION;
            readChar();
        } else if (ch == '/') {
            lexeme = Lexeme.DIVISION;
            readChar();
        } else if (ch == '+') {
            lexeme = Lexeme.ADDITION;
            readChar();
        } else if (ch == '-') {
            lexeme = Lexeme.SUBTRACTION;
            readChar();
        } else if (Character.isDigit(ch)) {
            lexeme = Lexeme.NUMBER;
            number();
        } else if (ch == '(') {
            lexeme = Lexeme.OPEN_BRACKET;
            readChar();
        } else if (ch == ')') {
            lexeme = Lexeme.CLOSE_BRACKET;
            readChar();
        } else if (ch == ',') {
            lexeme = Lexeme.COMMA;
            readChar();
        } else if (isWordOnlyCharacter(ch)) {
            lexeme = Lexeme.WORD;
            word = "";
            while (isWordOnlyCharacter(ch) || Character.isDigit(ch)) {
                word += ch;
                readChar();
            }
        } else if (ch == '\n') {
            lexeme = Lexeme.END_OF_LINE;
        } else {
            throw new StemmerBadSymbolException(pos - 1, ch);
        }
    }

    private void number() {
        if (!Character.isDigit(ch)) {
            throw new InvalidNumberException(pos - 1);
        }
        double res = 0;
        while (Character.isDigit(ch)) {
            res *= 10;
            res += Character.getNumericValue(ch);
            readChar();
        }
        if (ch == '.') {
            readChar();
            double pow = 0.1;
            while (Character.isDigit(ch)) {
                res += Character.getNumericValue(ch) * pow;
                pow *= 0.1;
                readChar();
            }
            if (ch == 'e' || ch == 'E') {
                res *= exponent();
            }
        } else if (ch == 'e' || ch == 'E') {
            res *= exponent();
        }
        number = res;
    }

    private double exponent() {
        if (ch != 'e' && ch != 'E') {
            throw new InvalidNumberException(pos - 1);
        }
        readChar();
        double m = 10;
        int pow2 = 0;
        double res = 1;
        if (ch == '-') {
            m = 0.1;
            readChar();
        } else if (ch == '+') {
            m = 10;
            readChar();
        } else if (!Character.isDigit(ch)) {
            throw new InvalidNumberException(pos - 1);
        }
        if (!Character.isDigit(ch)) {
            throw new InvalidNumberException(pos - 1);
        }
        while (Character.isDigit(ch)) {
            pow2 *= 10;
            pow2 += Character.getNumericValue(ch);
            readChar();
        }
        res *= Math.pow(m, pow2);
        return res;
    }

    public void verifyAndRead(Lexeme lexeme) {
        if (this.lexeme == lexeme) {
            readLexeme();
        } else {
            throw new VerifyAssertionException(lexeme, this.lexeme);
        }
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    public String getWord() {
        return word;
    }

    public double getNumber() {
        return number;
    }

    public static boolean isWordOnlyCharacter(char ch) {
        return Character.isLetter(ch) || ch == '$' || ch == '_';
    }

}

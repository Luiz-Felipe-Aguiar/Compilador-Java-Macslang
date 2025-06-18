package AnalisadorLexico;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class AnalisadorLexico {
    // Palavras-chave da linguagem
    private static final Set<String> KEYWORDS = Set.of("var", "func", "return");
    // Tipos primitivos
    private static final Set<String> TYPES = Set.of("int", "float", "string", "char");
    // Operadores e delimitadores
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "=");
    private static final Set<String> DELIMITERS = Set.of(";", "(", ")", "{", "}", ",", ":");

    //Validadores dos identifiers
    private static final String IDENTIFIER_REGEX = "[a-zA-Z_]\\w*";
    private static final String NUMBER_REGEX = "\\d+";
    private static final String FLOAT_REGEX = "\\d+(\\.\\d+)?";
    private static final String STRING_REGEX = "\"[^\"]*\"";
    private static final String CHAR_REGEX = "'(.)'";

    //Metodo para a separação dos tokens
    public static List<Token> tokenize(String input) {
        //Array list de retorno
        List<Token> tokens = new ArrayList<>();

        //Splita uma experssão regular
        String[] words = input.split("\\s+|(?=[;,+\\-*/(){}=:])|(?<=;|,|\\+|\\-|\\*|/|\\(|\\)|\\{|\\}|=|:)");

        //Loop que identifica os valores do array words
        for (String word : words) {
            if (word.isEmpty()) continue;

            TokenType type = null;

            if (KEYWORDS.contains(word)) {
                type = TokenType.KEYWORD;
            } else if (TYPES.contains(word)) {
                type = TokenType.TYPE;
            } else if (OPERATORS.contains(word)) {
                type = TokenType.OPERATOR;
            } else if (DELIMITERS.contains(word)) {
                type = TokenType.DELIMITER;
            } else if (word.matches(NUMBER_REGEX)) {
                type = TokenType.NUMBER;
            } else if (word.matches(FLOAT_REGEX)) {
                type = TokenType.FLOAT;
            } else if (word.matches(STRING_REGEX)) {
                type = TokenType.STRING;
            } else if (word.matches(CHAR_REGEX)) {
                type = TokenType.CHAR;
            } else if (word.matches(IDENTIFIER_REGEX)) {
                type = TokenType.IDENTIFIER;
            } else {
                type = TokenType.UNKNOWN;
            }

            tokens.add(new Token(type, word));
        }

        return tokens;
    }
}

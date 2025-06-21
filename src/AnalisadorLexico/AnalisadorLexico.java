package AnalisadorLexico;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalisadorLexico {
    // Set de palavras-chaves da linguagem
    private static final Set<String> KEYWORDS = Set.of("var", "func", "return");
    // Set de tipos primitivos
    private static final Set<String> TYPES = Set.of("int", "float", "string", "char");
    // Set de operadores e delimitadores
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "=");
    private static final Set<String> DELIMITERS = Set.of(";", "(", ")", "{", "}", ",", ":");

    // Regex validadores dos tipos
    private static final String IDENTIFIER_REGEX = "[a-zA-Z_]\\w*";
    private static final String NUMBER_REGEX = "\\d+(?!\\.)";
    private static final String FLOAT_REGEX = "\\d+\\.\\d+";
    private static final String STRING_REGEX = "\"[^\"]*\"";
    private static final String CHAR_REGEX = "'(.)'";

    // Metodo que faz a separação dos tokens
    public static List<Token> tokenize(String input) {
        // Inicialização do array list que retornamos
        List<Token> tokens = new ArrayList<>();

        // Analisador de padroes baseados no regex informadp para dividir em tokens
        Pattern pattern = Pattern.compile("\"[^\"]*\"|'[^']'|\\d+\\.\\d+|\\d+|[a-zA-Z_]\\w*|[;,+\\-*/(){}=:]");
        Matcher matcher = pattern.matcher(input);

        // Loop que identifica o tipo de cada token
        while (matcher.find()) {
            String word = matcher.group();
            if (word.isEmpty()) continue;

            if (KEYWORDS.contains(word)) {
                tokens.add(new Token(TokenType.KEYWORD, word));
            } else if (TYPES.contains(word)) {
                tokens.add(new Token(TokenType.TYPE, word));
            } else if (OPERATORS.contains(word)) {
                tokens.add(new Token(TokenType.OPERATOR, word));
            } else if (DELIMITERS.contains(word)) {
                tokens.add(new Token(TokenType.DELIMITER, word));
            } else if (word.matches(FLOAT_REGEX)) {
                tokens.add(new Token(TokenType.FLOAT, word));
            } else if (word.matches(NUMBER_REGEX)) {
                tokens.add(new Token(TokenType.NUMBER, word));
            } else if (word.matches(STRING_REGEX)) {
                tokens.add(new Token(TokenType.STRING, word));
            } else if (word.matches(CHAR_REGEX)) {
                tokens.add(new Token(TokenType.CHAR, word));
            } else if (word.matches(IDENTIFIER_REGEX)) {
                tokens.add(new Token(TokenType.IDENTIFIER, word));
            } else {
                tokens.add(new Token(TokenType.UNKNOWN, word));
            }
        }

        return tokens;
    }
}
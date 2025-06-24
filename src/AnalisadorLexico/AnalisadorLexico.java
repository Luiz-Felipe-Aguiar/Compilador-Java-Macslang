package AnalisadorLexico;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String NUMBER_REGEX = "\\d+(?!\\.)";
    private static final String FLOAT_REGEX = "\\d+\\.\\d+";
    private static final String STRING_REGEX = "\"[^\"]*\"";
    private static final String CHAR_REGEX = "'(.)'";

    //Metodo para a separação dos tokens
    public static List<Token> tokenize(String input) {
        //Array list de retorno
        List<Token> tokens = new ArrayList<>();
        // Regex que captura todos os tipos de tokens

        //Apenas um modelo de dividão por regex
        Pattern pattern = Pattern.compile("\"[^\"]*\"|'[^']'|\\d+\\.\\d+|\\d+|[a-zA-Z_]\\w*|[;,+\\-*/(){}=:]");
        // Classe que acha os valores com base no modelo passado acima
        Matcher matcher = pattern.matcher(input);

        //Loop que identifica os  do array words
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

package AnalisadorLexico;

import java.util.*; //import para criar um arraylist que recebe o codigop

public class AnalisadorLexico {
    // aqui usamos o static final para que possamos ter um conjunto imutavel de valores para um tipo de token
    private static final Set<String> KEYWORDS = Set.of("var", "func", "return");
    private static final Set<String> TYPES = Set.of("int", "float", "string", "char");
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "=");
    private static final Set<String> DELIMITERS = Set.of(";", "(", ")", "{", "}", ",", ":");

    //regex que validam se um validam os identifiers estão ok
    private static final String IDENTIFIER_REGEX = "[a-zA-Z_]\\w*";
    private static final String NUMBER_REGEX = "\\d+";
    private static final String STRING_REGEX = "\".*\"";
    private static final String CHAR_REGEX = "'.'";

    //metodo para a separação dos tokenx, tokenizaçao
    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();// aqui sera recebido o codigo a ser analisado em forma de um array list de tokens

        String[] words = input.split("\\s+|(?=[;,+\\-*/(){}=:])|(?<=;|,|\\+|\\-|\\*|/|\\(|\\)|\\{|\\}|=|:)"); // metodo split recebe uma experssao regular como parametro
        // o metodo split recebe uma regex como parametro.

        // \\s+ -> Divide palavras separadas por espaços (\\s+).
        // (?=[;,+\\-*/(){}=:])  -> Separa operadores antes de ((?=[...])), garantindo que não grudem em palavras
        // (?<=;|,|\\+|\\-|\\*|/|\\(|\\)|\\{|\\}|=|:) -> Separa operadores depois ((?<=...)), garantindo também que fiquem isolados corretamente.

        //aqui abaixo só temos um loop uqe primeiro pega o array list já dividio e passa a palavra separada nas condições atribundo ela como valor em um token e seu tipo correto
        for (String word : words) {
            if (word.isEmpty()) continue; // para ignorar os tokens vazios que estavam dando erro no sintatico
            if (KEYWORDS.contains(word)) {
                tokens.add(new Token(TokenType.KEYWORD, word));
            } else if (TYPES.contains(word)) {
                tokens.add(new Token(TokenType.TYPE, word));
            } else if (OPERATORS.contains(word)) {
                tokens.add(new Token(TokenType.OPERATOR, word));
            } else if (DELIMITERS.contains(word)) {
                tokens.add(new Token(TokenType.DELIMITER, word));
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

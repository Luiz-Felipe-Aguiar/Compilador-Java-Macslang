import AnalisadorLexico.Token;
import AnalisadorLexico.TokenType;
import AnalisadorSemantico.AnalisadorSemantico;
import AnalisadorSintatico.ErroSintatico;
import java.util.Arrays;
import java.util.List;

public class TesteSave {
    public static void main(String[] args) {
        testarCodigo("func teste(x: int): int {\n" +
                "    var resultado: int = x * 2;\n" +
                "    return resultado;\n" +
                "}", "Teste válido (int)");

        testarCodigo("func teste(x: int): string {\n" +
                "    var resultado: int = x * 2;\n" +
                "    return resultado;\n" +
                "}", "Teste com erro semântico (string)");
    }

    private static void testarCodigo(String code, String descricao) {
        System.out.println("⚡ Executando: " + descricao);

        List<Token> tokens = tokenizeMock(code);
        //tokens.forEach(token -> System.out.println(token.getType() + "('" + token.getValue() + "')"));

        AnalisadorSemantico semantico = new AnalisadorSemantico(tokens);

        try {
            boolean codigoValido = semantico.analisar();
            if (codigoValido) {
                System.out.println("✅ Código semântico válido!");
            }
        } catch (ErroSintatico e) {
            System.out.println("❌ Erro semântico detectado: " + e.getMessage());
        }

        System.out.println("------------------------------------------------\n");
    }

    private static List<Token> tokenizeMock(String code) {
        return Arrays.asList(
                new Token(TokenType.KEYWORD, "func"),
                new Token(TokenType.IDENTIFIER, "calculaDobro"),
                new Token(TokenType.DELIMITER, "("),
                new Token(TokenType.IDENTIFIER, "x"),
                new Token(TokenType.DELIMITER, ":"),
                new Token(TokenType.TYPE, "int"),
                new Token(TokenType.DELIMITER, ")"),
                new Token(TokenType.DELIMITER, ":"),
                new Token(TokenType.TYPE, code.contains(": string") ? "string" : "int"),
                new Token(TokenType.DELIMITER, "{"),
                new Token(TokenType.KEYWORD, "var"),
                new Token(TokenType.IDENTIFIER, "resultado"),
                new Token(TokenType.DELIMITER, ":"),
                new Token(TokenType.TYPE, "int"),
                new Token(TokenType.OPERATOR, "="),
                new Token(TokenType.IDENTIFIER, "x"),
                new Token(TokenType.OPERATOR, "*"),
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.DELIMITER, ";"),
                new Token(TokenType.KEYWORD, "return"),
                new Token(TokenType.IDENTIFIER, "resultado"),
                new Token(TokenType.DELIMITER, ";"),
                new Token(TokenType.DELIMITER, "}")
        );
    }
}
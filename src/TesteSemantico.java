import AnalisadorLexico.AnalisadorLexico;
import AnalisadorLexico.Token;
import AnalisadorSemantico.AnalisadorSemantico;
import AnalisadorSemantico.ErroSemantico;

import java.util.List;

public class TesteSemantico {
    public static void main(String[] args) {
        // Teste com código válido
        String codeCerto = "func calculaDobro(x: int): int {\n" +
                "    var resultado: int = x * 2;\n" +
                "    return resultado;\n" +
                "}";

        // Teste com erro semântico (retorno incorreto)
        String codeErrado = "func calculaDobro(x: int): string {\n" +
                "    var resultado: int = x * 2;\n" +
                "    return resultado;\n" +
                "}";

        testarCodigo(codeCerto, "Teste válido");
        testarCodigo(codeErrado, "Teste com erro semântico");
    }

    private static void testarCodigo(String code, String descricao) {
        System.out.println("Executando: " + descricao);

        List<Token> tokens = AnalisadorLexico.tokenize(code);
        tokens.forEach(System.out::println);

        AnalisadorSemantico semantico = new AnalisadorSemantico(tokens);
        boolean codigoValido = semantico.analisar(); // Agora usamos o retorno de analisar()

        if (codigoValido) {
            System.out.println("Código semântico válido! ✅");
        }

        System.out.println("------------------------------------------------\n");
    }
}
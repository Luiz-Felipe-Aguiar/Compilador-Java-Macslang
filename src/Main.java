import AnalisadorLexico.*;
import AnalisadorSintatico.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String declaration_correto = "var x: int = 10;"; //da certo
        //String declaration_errado = "func fatorial(n: int): int { var resultado: int = 1*n; return resultado;}";

        String function_correto = """
                func calculaDobro(x: int): int {
                    var resultado: int = x * 2;
                    return resultado;
                }""";

        String function_errado = """
                func calculaDobro(x: int): string {
                    var resultado: int = x * 2;
                    return resultado;
                }""";

        testarCodigo(function_correto);

        testarCodigo(function_errado);
    }

    private static void testarCodigo(String codigo) {
        try {
            List<Token> tokens = AnalisadorLexico.tokenize(codigo);
            //Para imprimir cada token
            // tokens.forEach(System.out::println);

            AnalisadorSintatico analisador = new AnalisadorSintatico(tokens);
            analisador.analisar();

            System.out.println("✅ Código válido!");
        } catch (ErroSintatico e) {
            System.err.println("❌ Erro sintático: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        }
    }
}
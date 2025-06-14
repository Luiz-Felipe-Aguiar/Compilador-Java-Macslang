import AnalisadorLexico.*;
import AnalisadorSintatico.*;

import AnalisadorLexico.AnalisadorLexico;
import AnalisadorLexico.Token;
import AnalisadorSintatico.AnalisadorSintatico;
import AnalisadorSintatico.ErroSintatico;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        //String code = "var x: int = 10;"; //da certo
        //String code = "func fatorial(n: int): int { var resultado: int = 1*n; return resultado;}";
        /*
        String code = "func soma(a: int, b: int): int {\n" +
                "    return a + b;\n" +
                "}";
         */

        String codeCerto = "func calculaDobro(x: int): int {\n" +
                "    var resultado: int = x * 2;\n" +
                "    return resultado;\n" +
                "}";

        String codeErrado = "func calculaDobro(x: int): string {\n" +
                "    var resultado: int = x * 2;\n" +
                "    return resultado;\n" +
                "}";
        List<Token> tokens = AnalisadorLexico.tokenize(codeCerto);

        List<Token> tokensErrados = AnalisadorLexico.tokenize(codeErrado);

        tokens.forEach(System.out::println);
        try {
            AnalisadorSintatico parser = new AnalisadorSintatico(tokens);
            parser.analisar();
            System.out.println("Código válido!");
        } catch (ErroSintatico e) {
            System.err.println(e.getMessage());
        }

        tokensErrados.forEach(System.out::println);
        try {
            AnalisadorSintatico parser = new AnalisadorSintatico(tokensErrados);
            parser.analisar();
            System.out.println("Código válido!");
        } catch (ErroSintatico e) {
            System.err.println(e.getMessage());
        }
    }
}
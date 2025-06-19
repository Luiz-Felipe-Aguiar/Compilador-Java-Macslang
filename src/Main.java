import AnalisadorLexico.*;
import AnalisadorSintatico.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        //Função correta com operação e retorno:
        System.out.println("Função correta com operação e retorno:");
        String funcao_correta = """
                func calculaDobro(x: int): int {
                    var resultado: int = x * 2;
                    return resultado;
                }
            """;
        testarCodigo(funcao_correta);

        //Declaração de variável com tipos diferentes:
        System.out.println("Declaração de variável com tipos diferentes:");
        String variaveis_corretas = """
                var idade: int = 30;
                var preco: float = 99.9;
                var nome: string = "Henrique";
                var letra: char = 'A';
            """;
        testarCodigo(variaveis_corretas);

        //Função com múltiplos parâmetros:
        System.out.println("Função com múltiplos parâmetros:");
        String funcao_multiplos_parametros = """
                func soma(a: int, b: int): int {
                    var resultado: int = a + b;
                    return resultado;
                }
            """;
        testarCodigo(funcao_multiplos_parametros);

        //Função com declaração interna de variável string:
        System.out.println("Função com declaração interna de variável string:");
        String funcao_com_string = """
                func saudacao(nome: string): string {
                    var texto: string = "Olá, " + nome;
                    return texto;
                }
            """;
        testarCodigo(funcao_com_string);

        //Teste de variável com float e múltiplas operações
        System.out.println("Teste de variável com float e múltiplas operações:");
        String operacoes = """
                var valor: float = 2.5 * 4.0 + 3.5;
            """;
        testarCodigo(operacoes);

        //Erro: variável sem : tipo
        System.out.println("Erro: variável sem : tipo: ");
        String erro_sem_tipo = """
                var idade = 30;
            """;
        testarCodigo(erro_sem_tipo);

        //Erro: função sem parênteses
        System.out.println("Erro: função sem parênteses:");
        String erro_funcao_sem_parenteses = """
                func soma: int {
                    return 0;
                }
            """;
        testarCodigo(erro_funcao_sem_parenteses);

        //Erro: função sem retorno obrigatório
        System.out.println("Erro: função sem retorno obrigatório:");
        String erro_funcao_sem_return = """
                func soma(a: int, b: int): int {
                    var resultado: int = a + b;
                }
            """;
        testarCodigo(erro_funcao_sem_return);

        //Erro: expressão sem ponto e vírgula
        System.out.println("Erro: expressão sem ponto e vírgula:");
        String erro_faltando_ponto_virgula = """
                func exemplo(x: int): int {
                    var resultado: int = x + 10
                    return resultado;
                }
            """;
        testarCodigo(erro_faltando_ponto_virgula);

        //Erro: tipo inexistente
        System.out.println("Erro: tipo inexistente:");
        String erro_tipo_invalido = """
                var x: inteiro = 10;
            """;
        testarCodigo(erro_tipo_invalido);

    }

    private static void testarCodigo(String codigo) {
        try {
            List<Token> tokens = AnalisadorLexico.tokenize(codigo);
            //Para imprimir cada token
            tokens.forEach(System.out::println);

            AnalisadorSintatico analisador = new AnalisadorSintatico(tokens);
            analisador.analisar();

            System.out.println("✅ Código válido!");
        } catch (ErroSintatico e) {
            System.out.println("❌" + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
        System.out.println("-----------------");
    }
}
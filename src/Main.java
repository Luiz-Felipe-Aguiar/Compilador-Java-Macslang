import AnalisadorLexico.*;
import AnalisadorSintatico.*;
import AnalisadorSemantico.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        //Função correta com operação e retorno:
        String funcao_correta = """
                func calculaDobro(x: int): int {
                    var resultado: int = x * 2;
                    return resultado;
                }
            """;
        testarCodigo("Função correta com operação e retorno", funcao_correta);

        //Declaração de variável com tipos diferentes:
        String variaveis_corretas = """
                var idade: int = 30;
                var preco: float = 99.9;
                var nome: string = "Henrique";
                var letra: char = 'A';
            """;
        testarCodigo("Declaração de variável com tipos diferentes", variaveis_corretas);

        //Função com múltiplos parâmetros:
        String funcao_multiplos_parametros = """
                func soma(a: int, b: int): int {
                    var resultado: int = a + b;
                    return resultado;
                }
            """;
        testarCodigo("Função com múltiplos parâmetros", funcao_multiplos_parametros);

        //Função com declaração interna de variável string:
        String funcao_com_string = """
                func saudacao(nome: string): string {
                    var texto: string = "Olá, " + nome;
                    return texto;
                }
            """;
        testarCodigo("Função com declaração interna de variável string", funcao_com_string);

        //Teste de variável com float e múltiplas operações
        String operacoes = """
                var valor: float = 2.5 * 4.0 + 3.5;
            """;
        testarCodigo("Teste de variável com float e múltiplas operações", operacoes);

        //Erro: variável sem : tipo
        String erro_sem_tipo = """
                var idade = 30;
            """;
        testarCodigo("Erro: variável sem : tipo", erro_sem_tipo);

        //Erro: função sem parênteses
        String erro_funcao_sem_parenteses = """
                func soma: int {
                    return 0;
                }
            """;
        testarCodigo("Erro: função sem parênteses", erro_funcao_sem_parenteses);

        //Erro: função sem retorno obrigatório
        String erro_funcao_sem_return = """
                func soma(a: int, b: int): int {
                    var resultado: int = a + b;
                }
            """;
        testarCodigo("Erro: função sem retorno obrigatório", erro_funcao_sem_return);

        //Erro: expressão sem ponto e vírgula
        String erro_faltando_ponto_virgula = """
                func exemplo(x: int): int {
                    var resultado: int = x + 10
                    return resultado;
                }
            """;
        testarCodigo("Erro: expressão sem ponto e vírgula", erro_faltando_ponto_virgula);

        //Erro: tipo inexistente
        String erro_tipo_invalido = """
                var x: inteiro = 10;
            """;
        testarCodigo("Erro: tipo inexistente", erro_tipo_invalido);

    }
    //Metodo para que chamamos para testar as sentenças de codigo das variaveis acima
    private static void testarCodigo(String nomeTeste, String codigo) {
        System.out.println("🔍 Testando: " + nomeTeste);
        System.out.println("Código:\n" + codigo);
        System.out.println("--------------");

        try {
            // Analise lexica
            List<Token> tokens = AnalisadorLexico.tokenize(codigo);
            System.out.println("Análise léxica concluída.");

            // Descomente se quiser ver os tokens que são analisados
            // tokens.forEach(System.out::println);

            // Analise sintatica
            AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tokens);
            analisadorSintatico.analisar();
            System.out.println("Análise sintática concluída.");

            // Analise semantica
            AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico(tokens);
            boolean valido = analisadorSemantico.analisar();

            if (valido) {
                System.out.println("Análise semântica concluída.");
                System.out.println("Código válido!");
            } else {
                System.out.println("Código contém erro semântico.");
            }

        } catch (ErroSintatico e) {
            System.out.println("Erro sintático: " + e.getMessage());
        } catch (ErroSemantico e) {
            System.out.println("Erro semântico: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        System.out.println("==============================\n");
    }

}
import AnalisadorLexico.AnalisadorLexico;
import AnalisadorLexico.Token;
import AnalisadorSemantico.AnalisadorSemantico;
import AnalisadorSintatico.ErroSintatico;
import java.util.List;

public class TesteSemantico {
    public static void main(String[] args) {
        // Caso 1: Código válido – função soma que retorna int corretamente.
        String codigo1 = "func soma(x: int): int { " +
                "    resultado: int = x + 1; " +
                "    return resultado; " +
                "}";
        executarTeste(codigo1, "Teste válido: soma (retorno int)");

        // Caso 2: Código válido – função toStr que retorna string corretamente.
        String codigo2 = "func toStr(x: int): string { " +
                "    var texto: string = \"Valor:\"; " +
                "    return texto; " +
                "}";
        executarTeste(codigo2, "Teste válido: toStr (retorno string)");

        // Caso 3: Erro semântico – função com tipo de retorno incompatível
        String codigo3 = "func erroTipo(x: int): string { " +
                "    var resultado: int = x * 2; " +
                "    return resultado; " +
                "}";
        executarTeste(codigo3, "Erro semântico: Tipo de retorno incompatível");

        // Caso 4: Erro semântico – função tentando retornar variável não declarada
        String codigo4 = "func erroVar(x: int): int { " +
                "    return inexistente; " +
                "}";
        executarTeste(codigo4, "Erro semântico: Variável não declarada");
    }

    private static void executarTeste(String codigo, String descricao) {
        System.out.println("⚡ Executando: " + descricao);
        try {
            // Utiliza o analisador léxico (classe AnalisadorLexico) para tokenizar o código
            List<Token> tokens = AnalisadorLexico.tokenize(codigo);
            // Cria o analisador semântico passando a lista de tokens
            AnalisadorSemantico semantico = new AnalisadorSemantico(tokens);
            // Executa a análise semântica
            semantico.analisar();
            System.out.println(" Código semântico válido!");
        } catch (ErroSintatico e) {
            System.out.println("Erro semântico detectado: " + e.getMessage());
        }
        System.out.println("------------------------------------------------\n");
    }
}
package AnalisadorSemantico;

import AnalisadorLexico.Token;
import AnalisadorLexico.TokenType;
import AnalisadorSintatico.ErroSintatico;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AnalisadorSemantico {
    // Tabelas de símbolos para variáveis e funções.
    private final Map<String, String> variaveis = new HashMap<>();
    private final Map<String, String> funcoes = new HashMap<>();
    private final List<Token> tokens;
    private int posicaoAtual = 0;
    private String funcaoAtual = null;

    public AnalisadorSemantico(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token tokenAtual() {
        return posicaoAtual < tokens.size() ? tokens.get(posicaoAtual) : null;
    }

    private void consumirToken() {
        posicaoAtual++;
    }

    // Metodo principal que percorre os tokens.
    public boolean analisar() throws ErroSintatico {
        // Processa tokens de nível superior (declarações de funções, por exemplo)
        while (posicaoAtual < tokens.size()) {
            Token token = tokenAtual();
            if (token.getType() == TokenType.KEYWORD) {
                if (token.getValue().equals("func")) {
                    analisarDeclaracaoFuncao();
                }
                // Outras declarações globais podem ser processadas aqui.
            } else {
                consumirToken();
            }
        }
        System.out.println("Análise semântica concluída sem erros!");
        return true;
    }

    // Processa declarações de variáveis. Essas variáveis serão registradas na tabela de símbolos.
    private void analisarDeclaracaoVariavel() {
        consumirToken(); // Consome "var"
        Token nome = tokenAtual(); // Nome da variável
        consumirToken();
        consumirToken(); // Consome ":"
        Token tipo = tokenAtual(); // Tipo da variável
        consumirToken();
        consumirToken(); // Consome "="
        // (Poderíamos analisar o valor de atribuição, mas aqui apenas registramos o tipo.)
        variaveis.put(nome.getValue(), tipo.getValue());
        System.out.println(" Registrando variável: " + nome.getValue() + " com tipo " + tipo.getValue());
        System.out.println(" Tabela de Variáveis Atualizada: " + variaveis);
    }

    // Processa declarações de função e, em seguida, processa o bloco da função.
    private void analisarDeclaracaoFuncao() throws ErroSintatico {
        consumirToken(); // Consome "func"
        Token nome = tokenAtual(); // Nome da função
        consumirToken(); // Avança após o nome
        consumirToken(); // Consome "("

        // (Processa os parâmetros. Aqui, para simplificação, ignoramos a lista de parâmetros.)
        // Avança tokens até encontrar a ")"
        while(tokenAtual() != null && !tokenAtual().getValue().equals(")")){
            consumirToken();
        }
        consumirToken(); // Consome ")"
        consumirToken(); // Consome ":" que antecede o tipo de retorno

        Token tipoRetorno = tokenAtual(); // Tipo de retorno da função
        consumirToken(); // Consome o tipo
        funcoes.put(nome.getValue(), tipoRetorno.getValue());
        funcaoAtual = nome.getValue();
        System.out.println(" Registrando função: " + nome.getValue() + " com retorno " + tipoRetorno.getValue());
        System.out.println(" Tabela de Funções Atual: " + funcoes);

        consumirToken(); // Consome "{" que inicia o bloco da função

        // Processa o corpo da função até encontrar "}"
        while(tokenAtual() != null && !tokenAtual().getValue().equals("}")){
            Token t = tokenAtual();
            if(t.getType() == TokenType.KEYWORD) {
                if(t.getValue().equals("var")){
                    analisarDeclaracaoVariavel();
                    continue; // já consome os tokens necessários
                } else if(t.getValue().equals("return")){
                    analisarRetornoFuncao();
                    continue;
                }
            }
            consumirToken();
        }
        consumirToken(); // Consome "}" de fechamento do bloco da função
    }

    // Processa a instrução de retorno dentro da função.
    private void analisarRetornoFuncao() throws ErroSintatico {
        consumirToken(); // Consome "return"
        Token retorno = tokenAtual();

        // Obtém o tipo esperado da função
        String tipoRetornoEsperado = funcoes.get(funcaoAtual);
        if (tipoRetornoEsperado == null) {
            throw new ErroSintatico("Erro semântico: Função " + funcaoAtual + " não foi declarada corretamente.");
        }

        // Exibe a tabela de variáveis antes da comparação
        System.out.println(" Tabela de Variáveis Antes da Comparação: " + variaveis);

        // Obtém o tipo real da variável retornada (supondo que o token seja um IDENTIFIER)
        String tipoRetornoReal = variaveis.get(retorno.getValue());

        // Exibe os valores exatos da comparação
        System.out.println(" Comparação de tipos:");
        System.out.println(" Função '" + funcaoAtual + "' espera retorno do tipo: " + tipoRetornoEsperado);
        System.out.println(" Retornando variável: " + retorno.getValue() + " que tem o tipo: " + tipoRetornoReal);

        if (tipoRetornoReal == null) {
            throw new ErroSintatico("Erro semântico: A variável '" + retorno.getValue() + "' não foi decarada.");
        }

        if (!tipoRetornoEsperado.equals(tipoRetornoReal)) {
            throw new ErroSintatico("Erro semântico: Tipo de retorno inválido na função " + funcaoAtual +
                    ". Esperado: " + tipoRetornoEsperado + ", Obtido: " + tipoRetornoReal);
        }
    }
}
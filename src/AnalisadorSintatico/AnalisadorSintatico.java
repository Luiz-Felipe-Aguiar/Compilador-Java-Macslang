package AnalisadorSintatico;

import AnalisadorLexico.Token;
import AnalisadorLexico.TokenType;
import java.util.List;

public class AnalisadorSintatico {
    private List<Token> tokens;
    private int posicaoAtual = 0;

    // classe recebe uma List<Tokens> que vem to analisador lexico
    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Retorna o token que esta sendo analisado no arrayllist, retorna null se a posição for maior que o array
    private Token tokenAtual() {
        return posicaoAtual < tokens.size() ? tokens.get(posicaoAtual) : null;
    }

    private void consumirToken() {
        //System.out.println("Consumindo token: " + tokenAtual() + " na posição " + posicaoAtual);
        posicaoAtual++;
    }

    // Ponto inicial da validação sintatica, percorre por todos os tokens
    public void analisar() throws ErroSintatico {
        while (posicaoAtual < tokens.size()) {
            analisarDeclaracao();
        }
    }

    // Analisa a declaração e a valida baseado no tipo da sentença, declaração de variavel ou função
    private void analisarDeclaracao() throws ErroSintatico {
        Token token = tokenAtual();
        if (token != null && token.getType() == TokenType.KEYWORD) {
            if (token.getValue().equals("var")) {
                consumirToken();
                analisarDeclaracaoVariavel();
            } else if (token.getValue().equals("func")) {
                consumirToken();
                analisarDeclaracaoFuncao();
            } else {
                throw new ErroSintatico("Esperado 'var' ou 'func', encontrado: " + token.getValue());
            }
        } else {
            throw new ErroSintatico("Esperado uma declaração (var ou func), encontrado: " + (token != null ? token.getValue() : "EOF"));
        }
    }

    // analisador de variavel. Se ela segue essa ordem.
    private void analisarDeclaracaoVariavel() throws ErroSintatico {
        verificarToken(TokenType.IDENTIFIER, null);
        verificarToken(TokenType.DELIMITER, ":");
        verificarToken(TokenType.TYPE, null);
        verificarToken(TokenType.OPERATOR, "=");
        analisarExpressao();
        verificarToken(TokenType.DELIMITER, ";");
    }

    // analisador de função. Se ela segue a ordem abaixo.
    private void analisarDeclaracaoFuncao() throws ErroSintatico {
        verificarToken(TokenType.IDENTIFIER, null);
        verificarToken(TokenType.DELIMITER, "(");
        analisarParametros();
        verificarToken(TokenType.DELIMITER, ")");
        verificarToken(TokenType.DELIMITER, ":");
        verificarToken(TokenType.TYPE, null);
        verificarToken(TokenType.DELIMITER, "{");

        while (tokenAtual() != null && !tokenAtual().getValue().equals("}")) {
            analisarComando();
        }

        verificarToken(TokenType.DELIMITER, "}");
    }

    // analisa os parametros que a função tem
    private void analisarParametros() throws ErroSintatico {
        while (tokenAtual() != null && tokenAtual().getType() == TokenType.IDENTIFIER) {
            consumirToken();
            verificarToken(TokenType.DELIMITER, ":");
            verificarToken(TokenType.TYPE, null);
            if (tokenAtual() != null && tokenAtual().getValue().equals(",")) {
                consumirToken();
            } else {
                break;
            }
        }
    }

    // analisador do bloco de codigo dentro da função
    private void analisarComando() throws ErroSintatico {
        Token token = tokenAtual();

        if (token != null && token.getType() == TokenType.IDENTIFIER) {
            consumirToken();
            verificarToken(TokenType.OPERATOR, "=");
            analisarExpressao();
            verificarToken(TokenType.DELIMITER, ";");
        } else if (token != null && token.getValue().equals("var")) {
            consumirToken();
            analisarDeclaracaoVariavel();
        } else if (token != null && token.getValue().equals("return")) {
            consumirToken();
            analisarExpressao();
            verificarToken(TokenType.DELIMITER, ";");
        } else {
            throw new ErroSintatico("Comando inválido: " + token.getValue());
        }
    }

    // metodo que verifica o tipo do token informado com algum valor(optional).
    private void verificarToken(TokenType esperado, String valorEsperado) throws ErroSintatico {
        Token token = tokenAtual();

        boolean tipoCorreto = token != null && token.getType() == esperado;
        boolean valorCorreto = valorEsperado == null || (token != null && token.getValue().equals(valorEsperado));

        if (!tipoCorreto || !valorCorreto) {
            String esperadoStr = valorEsperado != null ? "'" + valorEsperado + "'" : esperado.toString();
            throw new ErroSintatico("Esperado " + esperadoStr + ", encontrado: " + (token != null ? token.getValue() : "EOF"));
        }

        consumirToken();
    }

    //  verifica se o termo é um dos tipos do analisador lexico
    private void verificarTermo() throws ErroSintatico {
        Token token = tokenAtual();
        if (token == null ||
                (token.getType() != TokenType.NUMBER &&
                token.getType() != TokenType.FLOAT &&
                token.getType() != TokenType.STRING &&
                token.getType() != TokenType.CHAR &&
                token.getType() != TokenType.IDENTIFIER)) {
            throw new ErroSintatico("Esperado um termo numérico ou variável, encontrado: " + (token != null ? token.getValue() : "EOF"));
        }
        consumirToken();
    }

    // verifica o simbolo de um operador
    private void analisarExpressao() throws ErroSintatico {
        verificarTermo(); // Primeiro termo da expressão

        while (tokenAtual() != null && tokenAtual().getType() == TokenType.OPERATOR) {
            consumirToken(); // Consome o operador (+, -, *, /)
            verificarTermo(); // Consome o próximo termo
        }
    }

}
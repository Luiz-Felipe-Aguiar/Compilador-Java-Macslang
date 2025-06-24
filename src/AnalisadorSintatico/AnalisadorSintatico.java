package AnalisadorSintatico;

import AnalisadorLexico.Token;
import AnalisadorLexico.TokenType;
import java.util.List;

public class AnalisadorSintatico {
    //Atributo principal que é o array list criado pela classe do analisador lexico.
    private List<Token> tokens;
    private int posicaoAtual = 0;

    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
    }

    //Retorna o token que esta sendo analisado no arrayllist, retorna null se a posição for maior que o array
    private Token tokenAtual() {
        return posicaoAtual < tokens.size() ? tokens.get(posicaoAtual) : null;
    }

    private void consumirToken() {
        //System.out.println("Consumindo token: " + tokenAtual() + " na posição " + posicaoAtual);
        posicaoAtual++;
    }

    //Percorre todos os tokens e analisa declarações
    public void analisar() throws ErroSintatico {
        while (posicaoAtual < tokens.size()) {
            analisarDeclaracao();
        }
        //System.out.println("Análise sintática concluída sem erros!");
    }

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


    private void analisarDeclaracaoVariavel() throws ErroSintatico {
        verificarToken(TokenType.IDENTIFIER, null);
        verificarToken(TokenType.DELIMITER, ":");
        verificarToken(TokenType.TYPE, null);
        verificarToken(TokenType.OPERATOR, "=");
        analisarExpressao();
        verificarToken(TokenType.DELIMITER, ";");
    }


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

    private void analisarParametros() throws ErroSintatico {
        while (tokenAtual() != null && tokenAtual().getType() == TokenType.IDENTIFIER) { // se for do tipo identificador ele consome o toke,
            consumirToken();
            verificarToken(TokenType.DELIMITER, ":"); // ve se tem o delimitador (consome o token no metodo de verificação)
            verificarToken(TokenType.TYPE, null); // verifica se retorna o tipo do token igual a type
            if (tokenAtual() != null && tokenAtual().getValue().equals(",")) { // apenas verifica se tem mais parametros, senão encerra
                consumirToken();
            } else {
                break;
            }
        }
    }

    private void analisarComando() throws ErroSintatico {
        Token token = tokenAtual();

        if (token != null && token.getType() == TokenType.IDENTIFIER) { // É um comando de atribuição
            consumirToken();
            verificarToken(TokenType.OPERATOR, "=");
            analisarExpressao();
            verificarToken(TokenType.DELIMITER, ";");
        } else if (token != null && token.getValue().equals("var")) { // é uma declaração de variavel
            consumirToken();
            analisarDeclaracaoVariavel();
        } else if (token != null && token.getValue().equals("return")) { // esta chamando o return
            consumirToken();
            analisarExpressao();
            verificarToken(TokenType.DELIMITER, ";");
        } else {
            throw new ErroSintatico("Comando inválido: " + token.getValue());
        }
    }

    private void verificarToken(TokenType esperado, String valorEsperado) throws ErroSintatico {
        Token token = tokenAtual();

        boolean tipoCorreto = token != null && token.getType() == esperado; // verifica se é o tipo esperado
        boolean valorCorreto = valorEsperado == null || (token != null && token.getValue().equals(valorEsperado)); // verifica se é o valor espoerado

        if (!tipoCorreto || !valorCorreto) {
            String esperadoStr = valorEsperado != null ? "'" + valorEsperado + "'" : esperado.toString();
            throw new ErroSintatico("Esperado " + esperadoStr + ", encontrado: " + (token != null ? token.getValue() : "EOF"));
        }

        consumirToken();
    }

    private void verificarTermo() throws ErroSintatico { //verifica se o token pertence algum tipo desses senão lança uma exception
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

    private void analisarExpressao() throws ErroSintatico {
        verificarTermo(); // Primeiro termo da expressão

        while (tokenAtual() != null && tokenAtual().getType() == TokenType.OPERATOR) {
            consumirToken(); // Consome o operador (+, -, *, /)
            verificarTermo(); // Consome o próximo termo
        }
    }

}

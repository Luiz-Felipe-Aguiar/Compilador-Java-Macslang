package AnalisadorSintatico;

import AnalisadorLexico.Token;
import AnalisadorLexico.TokenType;
import java.util.List;

public class AnalisadorSintatico {
    //atributo principal é o array list criado pela classe do analisador lexico
    private List<Token> tokens;
    //apenas um gravador de posiçã
    private int posicaoAtual = 0;

    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
    }

    //retorna o token que esta sendo analisado no arrayllist, se aposição passar do tamanho do arraylist, retorna null
    private Token tokenAtual() {
        return posicaoAtual < tokens.size() ? tokens.get(posicaoAtual) : null;
    }

    //apenas para verificar os tokens que foram consumidos nas posições
    private void consumirToken() {
        System.out.println("Consumindo token: " + tokenAtual() + " na posição " + posicaoAtual);
        posicaoAtual++;
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

    //metodo que pervorre todos os tokens e se estiver errado ele lança o erro do metodo analisa declaração, senao ele avisa que a analise esta correta
    public void analisar() throws ErroSintatico {
        while (posicaoAtual < tokens.size()) {
            analisarDeclaracao();
        }
        System.out.println("Análise sintática concluída sem erros!");
    }

    private void analisarDeclaracaoVariavel() throws ErroSintatico {
        verificarToken(TokenType.IDENTIFIER);
        verificarToken(TokenType.DELIMITER, ":");
        verificarToken(TokenType.TYPE);
        verificarToken(TokenType.OPERATOR, "=");
        analisarExpressao();
        verificarToken(TokenType.DELIMITER, ";");
    }


    private void analisarDeclaracaoFuncao() throws ErroSintatico {
        verificarToken(TokenType.IDENTIFIER);
        verificarToken(TokenType.DELIMITER, "(");
        analisarParametros();
        verificarToken(TokenType.DELIMITER, ")");
        verificarToken(TokenType.DELIMITER, ":");
        verificarToken(TokenType.TYPE);
        verificarToken(TokenType.DELIMITER, "{");

        while (tokenAtual() != null && !tokenAtual().getValue().equals("}")) {
            analisarComando();
        }

        verificarToken(TokenType.DELIMITER, "}");
    }

    private void analisarParametros() throws ErroSintatico {
        while (tokenAtual() != null && tokenAtual().getType() == TokenType.IDENTIFIER) {
            consumirToken();
            verificarToken(TokenType.DELIMITER, ":");
            verificarToken(TokenType.TYPE);
            if (tokenAtual() != null && tokenAtual().getValue().equals(",")) {
                consumirToken();
            } else {
                break;
            }
        }
    }

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


    private void verificarToken(TokenType esperado) throws ErroSintatico {
        Token token = tokenAtual();
        System.out.println("Verificando token: " + token);
        if (token == null || token.getType() != esperado) {
            throw new ErroSintatico("Esperado token do tipo " + esperado + ", encontrado: " + (token != null ? token.getValue() : "EOF"));
        }
        consumirToken();
    }

    private void verificarToken(TokenType esperado, String valorEsperado) throws ErroSintatico {
        Token token = tokenAtual();
        System.out.println("Verificando token: " + token);
        if (token == null || token.getType() != esperado || !token.getValue().equals(valorEsperado)) {
            throw new ErroSintatico("Esperado '" + valorEsperado + "', encontrado: " + (token != null ? token.getValue() : "EOF"));
        }
        consumirToken();
    }

    private void verificarValor() throws ErroSintatico {
        Token token = tokenAtual();
        System.out.println("Verificando valor: " + token);

        if (token == null ||
                (token.getType() != TokenType.NUMBER &&
                        token.getType() != TokenType.STRING &&
                        token.getType() != TokenType.CHAR &&
                        token.getType() != TokenType.IDENTIFIER)) { // Permitir identificadores para constantes
            throw new ErroSintatico("Esperado um valor (number, string, char), encontrado: " + (token != null ? token.getValue() : "EOF"));
        }

        consumirToken();
    }

    private void verificarTermo() throws ErroSintatico {
        Token token = tokenAtual();
        if (token == null ||
                (token.getType() != TokenType.NUMBER &&
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
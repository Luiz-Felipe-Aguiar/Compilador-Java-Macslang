package AnalisadorSemantico;

import AnalisadorLexico.Token;
import AnalisadorLexico.TokenType;
import AnalisadorSintatico.ErroSintatico;

import java.util.*;

public class AnalisadorSemantico {
    private List<Token> tokens;
    private int posicaoAtual = 0;

    // tebela de variaveis que utilizamos para validar a semantica do codigo
    private Map<String, String> tabelaVariaveis = new HashMap<>();
    private String tipoDeRetornoAtual = null;

    // classe recebe uma List<Tokens> que vem to analisador lexico
    public AnalisadorSemantico(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Ponto inicial da validação semantica, percorre por todos os tokens
    public boolean analisar() {
        try {
            while (posicaoAtual < tokens.size()) {
                analisarDeclaracao();
            }
            //System.out.println("Análise semântica concluída sem erros!");
            return true;
        } catch (ErroSemantico | ErroSintatico e) {
            //System.err.println("Erro semântico: " + e.getMessage());
            return false;
        }
    }

    // Analisa a declaração e a valida baseado no tipo da sentença, declaração de variavel ou função
    private void analisarDeclaracao() throws ErroSemantico, ErroSintatico {
        Token token = tokenAtual();

        if (token != null && token.getType() == TokenType.KEYWORD) {
            if (token.getValue().equals("var")) {
                consumirToken();
                analisarDeclaracaoVariavel();
            } else if (token.getValue().equals("func")) {
                consumirToken();
                analisarDeclaracaoFuncao();
            } else {
                throw new ErroSemantico("Palavra-chave inesperada: " + token.getValue());
            }
        } else {
            throw new ErroSemantico("Esperado 'var' ou 'func', encontrado: " + (token != null ? token.getValue() : "EOF"));
        }
    }

    // analisador de declarador de função
    private void analisarDeclaracaoFuncao() throws ErroSemantico, ErroSintatico {
        Token nomeFuncao = tokenAtual();
        verificarToken(TokenType.IDENTIFIER, null);

        verificarToken(TokenType.DELIMITER, "(");

        Map<String, String> escopoAnterior = new HashMap<>(tabelaVariaveis);
        tabelaVariaveis.clear();

        while (tokenAtual() != null && tokenAtual().getType() == TokenType.IDENTIFIER) {
            Token param = tokenAtual();
            consumirToken();
            verificarToken(TokenType.DELIMITER, ":");
            Token tipo = tokenAtual();
            verificarToken(TokenType.TYPE, null);

            if (tabelaVariaveis.containsKey(param.getValue())) {
                throw new ErroSemantico("Parâmetro duplicado: " + param.getValue());
            }

            tabelaVariaveis.put(param.getValue(), tipo.getValue());

            if (tokenAtual().getValue().equals(",")) {
                consumirToken();
            } else {
                break;
            }
        }

        verificarToken(TokenType.DELIMITER, ")");

        verificarToken(TokenType.DELIMITER, ":");
        Token tipoRetorno = tokenAtual();
        verificarToken(TokenType.TYPE, null);
        tipoDeRetornoAtual = tipoRetorno.getValue();

        verificarToken(TokenType.DELIMITER, "{");

        boolean encontrouReturn = false;

        while (tokenAtual() != null && !tokenAtual().getValue().equals("}")) {
            Token token = tokenAtual();

            if (token.getValue().equals("return")) {
                if (tipoDeRetornoAtual == null) {
                    throw new ErroSemantico("'return' fora de função.");
                }

                consumirToken();
                String tipoExpr = analisarExpressao();

                if (!tipoExpr.equals(tipoDeRetornoAtual)) {
                    throw new ErroSemantico("Tipo de retorno incompatível. Esperado " + tipoDeRetornoAtual + ", retornado " + tipoExpr);
                }

                verificarToken(TokenType.DELIMITER, ";");
                encontrouReturn = true;
            } else if (token.getValue().equals("var")) {
                consumirToken();
                analisarDeclaracaoVariavel();
            } else {
                consumirToken(); // avanço simples
            }
        }

        if (!encontrouReturn) {
            throw new ErroSemantico("Função '" + nomeFuncao.getValue() + "' não possui comando 'return'.");
        }

        verificarToken(TokenType.DELIMITER, "}");

        tabelaVariaveis = escopoAnterior;
        tipoDeRetornoAtual = null;
    }

    // analisador de declaração de variavel
    private void analisarDeclaracaoVariavel() throws ErroSemantico, ErroSintatico {
        Token identificador = tokenAtual();
        verificarToken(TokenType.IDENTIFIER, null);

        if (tabelaVariaveis.containsKey(identificador.getValue())) {
            throw new ErroSemantico("Variável '" + identificador.getValue() + "' já foi declarada.");
        }

        verificarToken(TokenType.DELIMITER, ":");

        Token tipoToken = tokenAtual();
        verificarToken(TokenType.TYPE, null);

        verificarToken(TokenType.OPERATOR, "=");

        String tipoExpressao = analisarExpressao();

        if (!tipoToken.getValue().equals(tipoExpressao)) {
            throw new ErroSemantico("Tipo incompatível: esperado " + tipoToken.getValue() + ", encontrado " + tipoExpressao);
        }

        verificarToken(TokenType.DELIMITER, ";");

        tabelaVariaveis.put(identificador.getValue(), tipoToken.getValue());
    }

    // analisador de expressão (operadores)
    private String analisarExpressao() throws ErroSemantico, ErroSintatico {
        Token termo = tokenAtual();
        if (termo == null) throw new ErroSemantico("Expressão incompleta.");

        String tipo = resolverTipoLiteral(termo);
        consumirToken();

        while (tokenAtual() != null && tokenAtual().getType() == TokenType.OPERATOR) {
            consumirToken(); // operador
            String tipoDireito = analisarExpressao();
            if (!tipo.equals(tipoDireito)) {
                throw new ErroSemantico("Operação com tipos incompatíveis: " + tipo + " e " + tipoDireito);
            }
        }

        return tipo;
    }

    // valida tipo da variavel
    private String resolverTipoLiteral(Token token) throws ErroSemantico {
        switch (token.getType()) {
            case NUMBER: return "int";
            case FLOAT: return "float";
            case STRING: return "string";
            case CHAR: return "char";
            case IDENTIFIER:
                if (!tabelaVariaveis.containsKey(token.getValue())) {
                    throw new ErroSemantico("Variável '" + token.getValue() + "' não declarada.");
                }
                return tabelaVariaveis.get(token.getValue());
            default:
                throw new ErroSemantico("Expressão inválida: tipo " + token.getType() + ", valor '" + token.getValue() + "'");
        }
    }

    // validador de token
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

    //retorna o token verificado atualmente
    private Token tokenAtual() {
        return posicaoAtual < tokens.size() ? tokens.get(posicaoAtual) : null;
    }

    private void consumirToken() {
        posicaoAtual++;
    }
}

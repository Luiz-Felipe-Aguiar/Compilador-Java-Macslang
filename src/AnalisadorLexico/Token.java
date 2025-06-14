package AnalisadorLexico;

/*
Movi para uma classe separada para tornar public e ser acessivel fora do pacote
enum TokenType {
    //esses são os tipos de Token que o analisador vai reconhecer
    KEYWORD, IDENTIFIER, TYPE, OPERATOR, DELIMITER, LITERAL, NUMBER, STRING, CHAR, ASSIGN, RETURN, UNKNOWN
}
*/
public class Token {
    //aqui temos o tipo e o valor qe o token recebe
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    //retorna o tipo do token
    public TokenType getType() {
        return type;
    }

    //retorna o valor do token
    public String getValue() {
        return value;
    }

    @Override
    //To string para retornar otipo e o valor do token em uma sentença
    public String toString() {
        return type + "('" + value + "')";
    }
}

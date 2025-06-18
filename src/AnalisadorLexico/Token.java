package AnalisadorLexico;

public class Token {
    //aqui temos o tipo e o valor qe o token recebe
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    //Retorna o tipo do token.
    public TokenType getType() {
        return type;
    }

    //Retorna o valor do token.
    public String getValue() {
        return value;
    }

    @Override
    //To string para retornar o tipo e o valor do token.
    public String toString() {
        return type + "('" + value + "')";
    }
}

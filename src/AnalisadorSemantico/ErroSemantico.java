package AnalisadorSemantico;

// Classe para retornar o erro semantico
public class ErroSemantico extends RuntimeException {
    public ErroSemantico(String mensagem) {
        super(mensagem);
    }
}
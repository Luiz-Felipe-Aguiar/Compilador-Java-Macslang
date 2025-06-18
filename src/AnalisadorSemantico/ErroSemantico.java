package AnalisadorSemantico;


public class ErroSemantico extends RuntimeException {
    public ErroSemantico(String mensagem) {
        super("Erro Semântico: " + mensagem);
    }
}
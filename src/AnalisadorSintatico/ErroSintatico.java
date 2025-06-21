package AnalisadorSintatico;

// Classe para retornar o erro sintatico
public class ErroSintatico extends Exception {
    public ErroSintatico(String mensagem) {
        super(mensagem);
    }
}
package AnalisadorSintatico;

//Classe apenas para lançar uma mensagem de erro personalisada
public class ErroSintatico extends Exception {
    public ErroSintatico(String mensagem) {
        super("Erro Sintático: " + mensagem);
    }
}
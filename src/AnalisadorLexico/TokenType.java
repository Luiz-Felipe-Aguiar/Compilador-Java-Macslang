package AnalisadorLexico;

//essa e uma classe apenas que armazena os tipos que um token pode ser
public enum TokenType {
    KEYWORD, IDENTIFIER, TYPE, OPERATOR, DELIMITER, NUMBER, STRING, CHAR, UNKNOWN
}

//keywords - var,func e return
//Identifier - nomes que vem após var ou após func
// Type - String, int, char e floar
//Operator - (+,-,*,/)
//Delimiter - ;, : {} ()
//Number - [0-9] uma ou mais vezes
//String - qualquer sequenciade caracter
//Char caracter qualquer
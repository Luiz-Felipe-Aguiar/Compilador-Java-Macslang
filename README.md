# Compilador-Java-Macslang

Gramática MACSlang:


<Programa> -> <Declarações>

<Declarações> -> <Decl> <Declarações> | <Decl>

<Decl> -> <var> <Ident> : <Tipo> = <Valor> ; 
        | <func> <Ident> <Param> : <Tipo> { <Declarações> <Comandos> }

<Tipo> -> <int> | <float> | <string> | <char>

<Param> -> <Ident> : <Tipo> , <Param> | <Ident> : <Tipo>

<Comandos> -> <Comando> <Comandos> | <Comando>

<Comando> -> <Atribuição> | <ChamadaFunção> | <Retorno> | <Bloco>

<Atribuição> -> <Ident> = <Expr> ;

<Bloco> -> { <Comandos> }

<ChamadaFunção> -> <Ident> ( <Argumentos> ) ;

<Argumentos> -> <Expr> , <Argumentos> | <Expr>

<Expr> -> <Termo> <Op> <Expr> | <Termo>

<Termo> -> <Ident> | <Valor>

<Op> -> + | - | * | /

<Valor> -> <Numero> | <Texto> | <Char>

<Numero> -> [0-9]+

<Texto> -> " .* "

<Char> -> ' . '

<Retorno> -> return <Expr> ;

<Ident> -> [a-zA-Z_] <Ident> | [a-zA-Z_] | [a-zA-Z_] \w*

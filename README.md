# Interpretador_miniLua

## Integrantes:

* Luísa Oliveira Vicente
* Stéphanie Fonseca de Oliveira Gomes Magalhães

## Características do Interpretador

### Comandos

#### if
Executar comandos se a expressão for verdadeira.
#### while
Repetir comandos enquanto a expressão for verdadeira.
#### repeat
Executar comandos e repetir se a expressão for falsa.
#### for
* Numérico: 
```
for var = expr1, expr2, expr3 do
    ...
    end
```
Itera de **expr1** até **expr2** usando **expr3** como passo para incrementar **var**; **expr3** é opcional, assume-se o valor 1 se não estiver presente.
* Genérico:
```
for var1, var2 in expr do
    ...
    end
```
**expr** deve ser uma tabela, onde cada iteração **var1** recebe um índice e **var2** seu correspondente; **var2** é opcional, assim apenas chave **var1** é usada.
#### print
Imprimir na tela com nova linha
#### atribuição
```
min, max = max, min; (trocam-se seus valores).
a, b, c = 5, 10; (a recebe 5, b recebe 10 e c recebe nil)
x, y = 1, 2, 3; (x recebe 1, y recebe 2 e o valor 3 não é utilizado)
```
Atribuir os valores das expressões do lado direito às expressões do lado esquerdo. As quantidades de ambos os lados não necessáriamente precisam ser iguais.


### Constantes

#### nil
Valor nulo.
#### Lógico
* true:
Verdadeiro.
* false:
Falso.
#### Número
Valores formados por número em ponto flutuante.
#### String
Uma sequência de caracteres separados por aspas duplas.
#### Tabela
```
tbl = {}
tbl = {"one", "two", "three"}
tbl = {["one"] = 1, ["two"] = 2, ["three"] = 3}
```
Elementos separados por vírgulas entre abre e fecha chaves. Os valores são indexados usando sintaxe de colchetes **(tbl["one"])** ou usando a sintaxe de propriedades **(tbl.one)**. O índice pode ser de qualquer tipo, exceto **nil**. A tabela pode ser inicializada vazia, com valores (indexados a partir do índice 1), ou com pares chave/valor, ou com uma combinação dessas.


### Valores

#### Variáveis
Começam com **_** ou letras, seguidos de **_**, letras ou digitos.
#### Literais
Númeors, strings e lógicos.
#### Dinâmicos
Tabelas.


### Operadores

#### Numéricos
* Adição: **+**
* Subtração: **-**
* Multiplicação: *
* Divisão: **/**
* Resto: **%**
#### String
* Concatenação: **..**
Funciona apenas com strings e números.
#### Lógico
* Igualdade: **==**
Funciona com todos os tipos.
* Diferença: **~=**
Funciona com todos os tipos.
* Menor: **<**
Funciona apenas entre números.
* Maior: **>**
Funciona apenas entre números.
* Menor Igual: **<==**
Funciona apenas entre números.
* Maior Igual: **>==**
Funciona apenas entre números.
* Negação: **not**
Funciona com todos os tipos.
### Funções
#### read
```
var1 = read("")
var2 = read("Entre com o proximo valor:")
```
Ler uma linha do teclado, sem o caracter de nova linha **(\n)**. Obrigatóriamente deve haver conteúdo entre os parênteses.
#### tonumber
```
var1 = tonumber(exp[i])
var2 = tonumber("2")
```
Converter o número ou string em número, **nil** se não for possível ou for outro tipo.
#### tostring
```
var1 = tostring(true)
var2 = tostring(3)
```
Converter lógico, número ou string em string, **nil** se for outro tipo.

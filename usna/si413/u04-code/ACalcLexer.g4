lexer grammar ACalcLexer;

OPA   : [+-] ;
OPM   : [*/] ;
NUM   : ([-]|)[0-9]+ ;
LP    : '(' ;
RP    : ')' ;
STOP  : ';' ;
SPACE : [ \r\n] -> skip ;

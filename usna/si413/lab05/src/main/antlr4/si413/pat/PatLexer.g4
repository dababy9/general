/** Lexer (Scanner) specification for the Pat language.
 *
 * This file should contain regular expressions for all tokens
 * in the Pat language.
 *
 * CALEB WALKER
 */

lexer grammar PatLexer;

SYM    : [a-z][a-zA-Z0-9]*           ;
FOLD   : '*'                         ;
STOP   : ';'                         ;
COLON  : ':'                         ;
NAME   : [A-Z][a-zA-Z0-9]*           ;
REV    : '_r'                        ;
LB     : '['                         ;
RB     : ']'                         ;
WHITE  : [ \t\r\n]+        -> skip   ;

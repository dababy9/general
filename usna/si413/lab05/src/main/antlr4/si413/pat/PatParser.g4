/* Grammar rules (parser specification) for the Pat language.
 *
 * This file should contain grammar rules defining the
 * non-terminal symbols in the Pat language, using the token
 * names from PatLexer.
 *
 * CALEB WALKER
 */

parser grammar PatParser;
options { tokenVocab=PatLexer; }

prog
  : seq STOP prog       # NonemptyProg
  | EOF                 # EmptyProg
  ;

seq
  : SYM             # Symbol
  | NAME            # Variable
  | LB seq RB       # Braces
  | seq REV         # Reverse
  | seq COLON NAME  # VarAssign
  | seq seq         # Concat
  | seq FOLD seq    # Fold
  ;
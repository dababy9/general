parser grammar ACalcParser;
options { tokenVocab = ACalcLexer; }

prog
  : prog stmt  # Program
  |            # EmptyProg
  ;

stmt : exp STOP ;

exp
  : exp OPA term  # AddSub
  | term          # SingleTerm
  ;

term
  : term OPM factor  # MulDiv
  | factor           # SingleFactor
  ;

factor
  : NUM        # Num
  | LP exp RP  # Parens
  ;

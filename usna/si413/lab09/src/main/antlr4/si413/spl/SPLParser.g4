parser grammar SPLParser;
options { tokenVocab=SPLLexer; }

prog: stlist EOF ;

stlist
  : stlist stmt # NormalStmt
  | # NullStmt
  ;

stmt
  : NEW ID ASN exp STOP      # NewVar
  | ID ASN exp STOP          # Asn
  | WRITE exp STOP           # Write
  | IF exp block             # IfStmt
  | IFELSE exp block block   # IfElseStmt
  | WHILE exp block          # WhileStmt
  | block                    # BlockStmt
  | DEBUG                    # DebugStmt
  | exp STOP                 # ExpStmt
  ;

block : LC stlist RC ;

exp
  : NUM              # Num
  | BOOL             # Bool
  | ID               # Id
  | LP exp RP        # Parens
  | READ             # Read
  | LAMBDA ID block  # Lambda
  | exp FUNARG exp   # FunCall
  | exp OPM exp      # MulOp
  | OPA exp          # NegOp
  | exp OPA exp      # AddOp
  | exp COMP exp     # CompOp
  | NOT exp          # NotOp
  | exp BOP exp      # BoolOp
  ;

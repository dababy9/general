package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Lab07Test extends SPLTestBase {
    @Test
    void write10() {
        checkAST("write 10;", "(Write (Num:10))");
    }

    @Test
    void newVar() {
        checkAST("new x := 13;", "(NewStmt:x (Num:13))");
    }

    @Test
    void writeArith() {
        checkAST("write x * 6 + 5;", "(Write (ArithOp:+ (ArithOp:* (Id:x) (Num:6)) (Num:5)))");
    }

    @Test
    void writeComp1() {
        checkAST("write 1 < 2;", "(Write (CompOp:< (Num:1) (Num:2)))");
    }

    @Test
    void ifElse1() {
        checkAST("ifelse not true { x := 100; } { y := 200; }",
                "(IfElse (NotOp (Bool:true)) (Block (Asn:x (Num:100))) (Block (Asn:y (Num:200))))");
    }

    @Test
    void whileStmt1() {
        checkAST("while var < 100 { new blah := var * 10; }",
                "(WhileStmt (CompOp:< (Id:var) (Num:100)) (Block (NewStmt:blah (ArithOp:* (Id:var) (Num:10)))))");
    }

    @Test
    void ifElse2() {
        checkAST("if 3 = 4 { write 10; write 11; write 12; }",
                "(IfElse (CompOp:= (Num:3) (Num:4)) (Block (Write (Num:10)) (Write (Num:11)) (Write (Num:12))) (Block))");
    }

    @Test
    void numArith() {
        checkAST("write 123;", "(Write (Num:123))");
        checkAST("write 3 * 7;", "(Write (ArithOp:* (Num:3) (Num:7)))");
        checkAST("write 10 - (1 + 2 * 3);",
                "(Write (ArithOp:- (Num:10) (ArithOp:+ (Num:1) (ArithOp:* (Num:2) (Num:3)))))");
    }

    @Test
    void neg1() {
        checkAST("write -(5);", "(Write (NegOp (Num:5)))");
        checkAST("write -+-(((17)));", "(Write (NegOp (NegOp (Num:17))))");
    }

    @Test
    void boolOps() {
        checkAST("write true;", "(Write (Bool:true))");
        checkAST("write false;", "(Write (Bool:false))");
        checkAST("write true and false;", "(Write (BoolOp:and (Bool:true) (Bool:false)))");
        checkAST("write false and false or false;",
                "(Write (BoolOp:or (BoolOp:and (Bool:false) (Bool:false)) (Bool:false)))");
    }

    @Test
    void not() {
        checkAST("write not true;", "(Write (NotOp (Bool:true)))");
    }

    @Test
    void compArith3() {
        checkAST("write 1 >= 2;", "(Write (CompOp:>= (Num:1) (Num:2)))");
        checkAST("write 2 * 3 < 5 and 6 = 7;",
                "(Write (BoolOp:and (CompOp:< (ArithOp:* (Num:2) (Num:3)) (Num:5)) (CompOp:= (Num:6) (Num:7))))");
    }

    @Test
    void vars() {
        checkAST("new x := false;", "(NewStmt:x (Bool:false))");
        checkAST("x := 17;", "(Asn:x (Num:17))");
        checkAST("write neato;", "(Write (Id:neato))");
        checkAST("new v1 := 20; v1 := v1 * 3;",
                List.of("(NewStmt:v1 (Num:20))", "(Asn:v1 (ArithOp:* (Id:v1) (Num:3)))"));
    }

    @Test
    void blocks() {
        checkAST("{}", "(Block)");
        checkAST("{write 1; write 2;}", "(Block (Write (Num:1)) (Write (Num:2)))");
        checkAST("""
                write 1;
                {   write 2;
                    write 3;
                    write 4;
                    { write 5; }
                }
                { { write 6; } }
                """,
                List.of("(Write (Num:1))",
                    "(Block (Write (Num:2)) (Write (Num:3)) (Write (Num:4)) (Block (Write (Num:5))))",
                    "(Block (Block (Write (Num:6))))"));
    }

    @Test
    void ifElse3() {
        checkAST("if true { write false; }", "(IfElse (Bool:true) (Block (Write (Bool:false))) (Block))");
        checkAST("new x := 10; ifelse x = 3 { x := 6; } { x := 7; write x; } write true;",
                List.of("(NewStmt:x (Num:10))",
                    "(IfElse (CompOp:= (Id:x) (Num:3)) (Block (Asn:x (Num:6))) (Block (Asn:x (Num:7)) (Write (Id:x))))",
                    "(Write (Bool:true))"));
    }

    @Test
    void whileStmt2() {
        checkAST("while 1 < 2 { }", "(WhileStmt (CompOp:< (Num:1) (Num:2)) (Block))");
        checkAST("while true { while false { write 7; } while true { new x := 10; } }",
                "(WhileStmt (Bool:true) (Block (WhileStmt (Bool:false) (Block (Write (Num:7)))) (WhileStmt (Bool:true) (Block (NewStmt:x (Num:10))))))");
    }

    @Test
    void read1() {
        checkAST("write read;", "(Write (Read))");
        checkAST("new x := read*3 + read*4;",
                "(NewStmt:x (ArithOp:+ (ArithOp:* (Read) (Num:3)) (ArithOp:* (Read) (Num:4))))");
    }

    @Test
    void write123() {
        runSpli("write 123;", "123");
    }

    @Test
    void writeNeg() {
        runSpli("write -(17);", "-17");
    }

    @Test
    void plus() {
        runSpli("write 5 + 3;", "8");
    }

    @Test
    void minusDiv() {
        runSpli("write 18 - 15 / 3;", "13");
    }

    @Test
    void negAdd() {
        runSpli("write -(5 + 5);", "-10");
    }

    @Test
    void writeComp2() {
        runSpli("write 4 > 3;", getTrue());
    }

    @Test
    void writeEquals() {
        runSpli("write 11 = 12;", getFalse());
    }

    @Test
    void compArith1() {
        runSpli("write 6 + 3 != 2 and not 6 + 3 <= 2;", getTrue());
    }

    @Test
    void num() {
        runSpli("write 123;", "123");
        runSpli("write 456; write 7;", List.of("456", "7"));
    }

    @Test
    void arith() {
        runSpli("write 1 + 2;", "3");
        runSpli("write 100 - 3;", "97");
        runSpli("write 4 * 6;", "24");
        runSpli("write 75 / 5;", "15");
    }

    @Test
    void neg2() {
        runSpli("write -52;", "-52");
        runSpli("write --3;", "3");
        runSpli("write +7;", "7");
        runSpli("write --+--1234;", "1234");
        runSpli("write --++---1234;", "-1234");
    }

    @Test
    void arithNeg() {
        runSpli("write -52; write --52; write -----52;",
                List.of("-52", "52", "-52"));
        runSpli("write 7 * -3;", "-21");
        runSpli("write -30 / 7;", "-4");
        runSpli("write 2+-+8;", "-6");
    }

    @Test
    void boolExp() {
        runSpli("write true;", getTrue());
        runSpli("write false;", getFalse());
    }

    @Test
    void comp() {
        runSpli("write 10 = 10;", getTrue());
        runSpli("write 7 = 9;", getFalse());
        runSpli("write 35 != 42;", getTrue());
        runSpli("write 88 != 88;", getFalse());
        runSpli("write 7 < 9;", getTrue());
        runSpli("write 9 < 7;", getFalse());
        runSpli("write 1000 < 1000;", getFalse());
        runSpli("write 7 <= 9;", getTrue());
        runSpli("write 9 <= 7;", getFalse());
        runSpli("write 1000 <= 1000;", getTrue());
        runSpli("write 7 > 9;", getFalse());
        runSpli("write 9 > 7;", getTrue());
        runSpli("write 1000 > 1000;", getFalse());
        runSpli("write 7 >= 9;", getFalse());
        runSpli("write 9 >= 7;", getTrue());
        runSpli("write 1000 >= 1000;", getTrue());
    }

    @Test
    void compArith2() {
        runSpli("write 22 * 1000 = 3141 * 7;", getFalse());
        runSpli("write 22 * 1000 >= 3141 * 7;", getTrue());
        runSpli("write 22 * 1000 < 3141 * 7;", getFalse());
    }

    @Test
    void boolOp() {
        runSpli("write true and true;", getTrue());
        runSpli("write true and false;", getFalse());
        runSpli("write false and true;", getFalse());
        runSpli("write false and false;", getFalse());
        runSpli("write true or true;", getTrue());
        runSpli("write true or false;", getTrue());
        runSpli("write false or true;", getTrue());
        runSpli("write false or false;", getFalse());
    }

    @Test
    void notOp() {
        runSpli("write not true;", getFalse());
        runSpli("write not false;", getTrue());
    }

    @Test
    void boolArith() {
        runSpli("write false or true and (not false and true);", getTrue());
        runSpli("write true and false or (not true or false);", getFalse());
        runSpli("write 5*6 > 3 and 19 / 2 = 9 and not false;", getTrue());
    }

    @Test
    void divideByZero() {
        spliError("write 1 / 0;");
        spliError("write 3 + 7 / (2 - 1 - 1);");
    }

    @Test
    void newStmt1() {
        runSpli("new x := 5;");
    }

    @Test
    void newAsn() {
        runSpli("""
                new x := 5;
                new other := x * 3;
                x := -10;
                """);
    }

    @Test
    void errBind() {
        spliError("new what := 20; new what := 100;");
    }

    @Test
    void errRebind() {
        spliError("ok := 17;");
    }

    @Test
    void newWrite() {
        runSpli("new x := 5; write x;", "5");
    }

    @Test
    void newAsnWrite() {
        runSpli("""
                new x := 5;
                write x;
                new other := x * 3;
                write other;
                x := -10;
                write x + other;
                """,
                List.of("5", "15", "5"));
    }

    @Test
    void errLookup() {
        spliError("write what * 3;");
    }

    @Test
    void newStmt2() {
        runSpli("new varname := 10;");
        runSpli("new x := 3; new y := 4;");
    }

    @Test
    void asn() {
        runSpli("new var := 20; var := 21;");
        runSpli("new x := 3; x := 4; x := 5;");
        runSpli("""
                new x := 3;
                x := 4;
                new y := 5;
                y := 6;
                y := 7;
                x := 8;
                """);
    }

    @Test
    void newError() {
        spliError("new x := 3; new x := 4;");
        spliError("new x := 3; new y := 4; new x := 15;");
    }

    @Test
    void asnError() {
        spliError("what := 17;");
        spliError("new y := 8; x := false;");
    }

    @Test
    void newLookupSingle() {
        runSpli("new neat := 20; write neat;", "20");
    }

    @Test
    void asnLookupSingle() {
        runSpli("""
                new howdy := 101;
                write howdy;
                howdy := 53;
                write howdy;
                """,
                List.of("101", "53"));
    }

    @Test
    void newLookup() {
        runSpli("""
                new x := 13;
                new y := 14;
                write y;
                new z := 15;
                write x;
                write z;
                """,
                List.of("14", "13", "15"));
        runSpli("new b := true and 1 < 100; write b;", getTrue());
    }

    @Test
    void asnLookup() {
        runSpli("""
                new one := 1;
                write one;
                new two := 2;
                write two * 10;
                new three := one + two;
                write three;
                two := 1000;
                write three;
                write two;
                """,
                List.of("1", "20", "3", "3", "1000"));
    }

    @Test
    void lookupError() {
        spliError("write nothing;");
        spliError("new y := 20; y := x - 3;");
    }

    @Test
    void block() {
        runSpli("""
                {   new var := 10;
                    var := var * var;
                    write var;
                }
                """,
                "100");
    }

    @Test
    void ifElse4() {
        runSpli("""
                ifelse 40 < 11
                  { write true; }
                  { write false;
                    if 2 != 2 { write 2; }
                  }
                """,
                "0");
    }

    @Test
    void whileStmt3() {
        runSpli("""
                new i := 3;
                while i > 0 {
                    write i;
                    i := i - 1;
                }
                """,
                List.of("3", "2", "1"));
    }

    @Test
    void emptyBlock() {
        runSpli("{ }");
    }

    @Test
    void simpleBlock() {
        runSpli("{ write 10; write 20; }", List.of("10", "20"));
    }

    @Test
    void nestedBlocks() {
        runSpli("""
                write 1 + 2;
                {
                    write 3 + 4;
                    write 5 + 6;
                    {
                        write 7 + 8;
                    }
                }
                write 9 + 10;
                """,
                List.of("3", "7", "11", "15", "19"));
    }

    @Test
    void simpleIf() {
        runSpli("if true { write 100; }", "100");
        runSpli("if false { write 100; }");
    }

    @Test
    void simpleIfElse() {
        runSpli("ifelse true { write 10; } { write 20; }", "10");
        runSpli("ifelse false { write 10; } { write 20; }", "20");
    }

    @Test
    void nestedIfElse() {
        runSpli("""
                write 1 + 2 + 3;
                ifelse 5 < 10
                    {   if false { write 100; }
                        write 200;
                        ifelse true { write 99; } { }
                    }
                    { write 99*99; }
                if 3=3 { write -10; write -20; }
                """,
                List.of("6", "200", "99", "-10", "-20"));
    }

    @Test
    void ifNoError() {
        runSpli("if false { write 1 / 0; }");
        runSpli("new x := 10; ifelse x = 3 { y := 99; } { x := 100; } write x;", "100");
    }

    @Test
    void ifElseVars() {
        runSpli("""
                new hello := 10;
                if hello < 100 { hello := hello + 3; }
                ifelse false
                    { hello := -1; }
                    {   new hum := 17;
                        hello := hello + hum;
                    }
                write hello;
                """,
                "30");
    }

    @Test
    void whileFalse() {
        runSpli("while false { write true; }");
        runSpli("while 5=5 and 5=6 { write true; }");
    }

    @Test
    void whileOnce() {
        runSpli("new flag := true; while flag { write 43; flag := false; } write -1;",
                List.of("43", "-1"));
    }

    @Test
    void whileCountUp() {
        runSpli("new x := 3; while x < 8 { x := x + 2; write x; }",
                List.of("5", "7", "9"));
    }

    @Test
    void whileCollatz() {
        runSpli("""
                new n := 15;
                while n > 1 {
                    write n;
                    ifelse (n/2)*2 = n
                        { n := n/2; }
                        { n := 3*n+1; }
                }
                write n;
                write -10;
                """,
                List.of("15","46","23","70","35","106","53",
                    "160","80","40","20","10","5","16","8","4","2","1","-10"));
    }

    @Test
    void read2() {
        runSpli("""
                new secret := 42;
                new numtries := 0;
                while read != secret { numtries := numtries + 1; }
                write numtries;
                """,
                List.of(15, -3, 21, 42),
                "3");
    }

    @Test
    void readOnce() {
        runSpli("write read;", List.of(123), "123");
    }

    @Test
    void readMul() {
        runSpli("write read * read;", List.of(7, 8), "56");
    }

    @Test
    void readMany() {
        runSpli("""
                new sum := 0;
                new x := read;
                while x > 0 {
                    sum := sum + x;
                    x := read;
                }
                write sum;
                """,
                List.of(1,2,10,0),
                "13");
    }

    @Test
    void shortCircuit1() {
        runSpli("write false and 1/0;", getFalse());
    }

    @Test
    void shortCircuit2() {
        runSpli("write true or what;", getTrue());
    }

    @Test
    void shortCircuitDivZero() {
        runSpli("write true or (1 / 0);", getTrue());
        runSpli("write 2 < 1 and (5 / (5 - 5));", getFalse());
    }

    @Test
    void shortCircuitRead() {
        runSpli("write read > 3 and read < 10; write read > 0 or read < 0;",
                List.of(5, -5, 5),
                List.of(getTrue(), getTrue()));
    }

    @Test
    void debugStmt1() {
        runSpli("write 5; \"hello!\" write 6;",
                List.of("5", "hello!", "6"));
    }

    @Test
    void debugStmt2() {
        runSpli("\"toodaloo\"", "toodaloo");
        runSpli("if false { \"toodaloo\" }");
    }

    @Test
    void debugAST() {
        checkAST("\"it works!\"", "(Debug:\"it works!\")");
        checkAST("{ write 1; \"good\" \"great\" }",
                "(Block (Write (Num:1)) (Debug:\"good\") (Debug:\"great\"))");
    }

    @Test
    void debugStmt() {
        runSpli("\"hello\"", "hello");
    }

    @Test
    void debugLoop() {
        runSpli("""
                new count := 10;
                while count > 0 {
                    "go"
                    count := count / 2;
                }
                "stop"
                """,
                List.of("go", "go", "go", "go", "stop"));
    }

    @Test
    void lambdaAST() {
        checkAST("write lambda xyz { q := 3+7; write true; };",
            "(Write (Lambda:xyz (Block (Asn:q (ArithOp:+ (Num:3) (Num:7))) (Write (Bool:true)))))");
    }

    @Test
    void funcallAST() {
        checkAST("write f@g@(5 < 10);",
            "(Write (FunCall (FunCall (Id:f) (Id:g)) (CompOp:< (Num:5) (Num:10))))");
    }
}

package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex1Test extends SPLTestBase {
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
    void writeComp() {
        checkAST("write 1 < 2;", "(Write (CompOp:< (Num:1) (Num:2)))");
    }

    @Test
    void ifElse() {
        checkAST("ifelse not true { x := 100; } { y := 200; }",
                "(IfElse (NotOp (Bool:true)) (Block (Asn:x (Num:100))) (Block (Asn:y (Num:200))))");
    }

    @Test
    void whileStmt() {
        checkAST("while var < 100 { new blah := var * 10; }",
                "(WhileStmt (CompOp:< (Id:var) (Num:100)) (Block (NewStmt:blah (ArithOp:* (Id:var) (Num:10)))))");
    }

    @Test
    void ifElse2() {
        checkAST("if 3 = 4 { write 10; write 11; write 12; }",
                "(IfElse (CompOp:= (Num:3) (Num:4)) (Block (Write (Num:10)) (Write (Num:11)) (Write (Num:12))) (Block))");
    }
}

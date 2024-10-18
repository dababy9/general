package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex4Test extends SPLTestBase {
    @Test
    void funCallArg() {
        runSpli("new ignored := lambda x { write x * 2; } @ -5;", "-10");
    }

    @Test
    void funCallRet() {
        runSpli("write lambda unused { ret := 1+2+3+4+5; } @ false;", "15");
    }

    @Test
    void funCallFun1() {
        runSpli("""
                new fun := lambda a { ret := lambda b { ret := 100*a + b; }; };
                write fun@5@6;
                write fun@33@4;
                """,
                List.of("506", "3304"));
    }

    @Test
    void funCallFun2() {
        runSpli("""
                new fun := lambda a { ret := lambda b { ret := 100*a + b; }; };
                write fun@200@(fun@5@6);
                """,
                "20506");
    }

    @Test
    void funCallPow1() {
        runSpli("""
                new pow := lambda a { ret := lambda b {
                    ifelse b = 0 { ret := 1; } { ret := a * pow@a@(b-1); }
                }; };
                write pow@10@7;
                write pow@2@20;
                """,
                List.of("10000000", "1048576"));
    }

    @Test
    void funCallPow2() {
        runSpli("""
                new pow := lambda a { ret := lambda b {
                    ifelse b = 0 { ret := 1; } { ret := a * pow@a@(b-1); }
                }; };
                write pow@(-1)@101;
                write pow@(pow@2@3)@(pow@4@1);
                """,
                List.of("-1", "4096"));
    }
}

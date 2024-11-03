package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Lab08Test extends SPLTestBase {
    @Test
    void nestedAssign() {
        runSpli("""
                new a := 100;
                { new x := a; write a + x; }
                """,
                "200");
    }

    @Test
    void outOfScopeError() {
        spliError("""
                new a := 100;
                { new x := a; write a + x; }
                write a + x;
                """);
    }

    @Test
    void nestedReassign() {
        runSpli("""
                new a := 100;
                { new x := a; write a + x; }
                { new a := 50; while a < 100 { a := a + 30; } write a; }
                write a;
                """,
                List.of("200", "110", "100"));
    }

    @Test
    void outerInnerScope() {
        runSpli("""
                new outer := 11;
                new outer2 := 12;
                write outer * outer2;
                {
                    new inner := 13;
                    outer := 14;
                    write outer * inner;
                }
                write outer * outer2;
                """,
                List.of("132", "182", "168"));
    }

    @Test
    void ifScope() {
        runSpli("""
                new x := 5;
                if true {
                    new x := 7;
                    while x > 3 { x := x - 1; write x; }
                }
                write x;
                """,
                List.of("6", "5", "4", "3", "5"));
    }

    @Test
    void asnScopeError() {
        spliError("""
                new a := 10;
                { new b := 20; }
                b := 30;
                """);
    }

    @Test
    void deepNest() {
        runSpli("{{ new huh := -1; {{{{{{{{{ new x := huh * 7; write x; huh := 8; new huh := 9; }}}}}}} write huh; }}}}",
                List.of("-7", "8"));
    }

    @Test
    void lambdaExpression() {
        runSpli("new f := lambda x { write -123; };");
    }

    @Test
    void lambdaLoop() {
        runSpli("""
                new c := 4;
                new p := -2001;
                while c > 0 {
                    new p2 := lambda param { };
                    p := p2;
                    c := c - 1;
                }
                "ok"
                """,
                "ok"
                );
    }

    @Test
    void lambdaNoExec() {
        runSpli("""
                new f := lambda x { ret := 1 / 0; };
                new g := lambda x { write noSuchVar; };
                write 51;
                """,
                "51");
    }

    @Test
    void basicFunCall() {
        runSpli("""
                new f := lambda x {
                    write 1 + 2;
                    "We did it!"
                };
                new trash := f@100;
                """,
                List.of("3", "We did it!"));
    }

    @Test
    void invalidFunCall() {
        spliError("write (-5) @ 6;");
    }

    @Test
    void funLoop() {
        runSpli("""
                new x := lambda y {
                    new z := 0;
                    new a := 1;
                    while z < 100 {
                        z := z + a*a;
                        a := a + 1;
                    }
                    write z;
                } @ false;
                """,
                "140");
    }

    @Test
    void twoFun() {
        runSpli("""
                new f := lambda x { write 15; };
                new trash := f@f;
                trash := lambda x { write 19; } @ false;
                trash := f@f;
                """,
                List.of("15", "19", "15"));
    }

    @Test
    void funScope() {
        runSpli("""
                new mm := -5;
                new mars := lambda x { mm := mm * -2; };
                write mm;
                new g := mars@0;
                write mm;
                g := mars@0;
                g := mars@0;
                write mm;
                """,
                List.of("-5", "10", "40"));
        runSpli("""
                new a := 2;
                new b := lambda x { new a := 3; b := 4; };
                write a;
                new c := b@6;
                write a; write b;
                """,
                List.of("2", "2", "4"));
    }

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

    @Test
    void funRetScope() {
        runSpli("""
                new a := 8;
                new f := lambda x { ret := x + a; };
                write f@5;
                {   new a := 10;
                    write f@17;
                }
                """,
                List.of("13", "25"));
    }

    @Test
    void funArgScope() {
        runSpli("""
                new a := 5;
                new f := lambda x { a := a + x + 2; };
                new t := f@3;
                write a;
                t := f@8;
                write a;
                """,
                List.of("10", "20"));
    }

    @Test
    void prevNextFun() {
        runSpli("""
                new f := lambda prev {
                    ret := lambda next { new temp := prev; prev := next; ret := temp; };
                };
                new g := f@1;
                write g@2;
                write g@3;
                new h := f@4;
                write g@5;
                write h@6;
                write h@7;
                write g@8;
                """,
                List.of("1", "2", "3", "4", "6", "5"));
    }

    @Test
    void axpbFun() {
        runSpli("""
		new axpb := lambda a {
		    ret := lambda b {
			ret := lambda x {
			    ret := a*x + b;
			};
		    };
		};
		new f := axpb@3@6;
		write f@1 + f@8;
		new g := axpb@0@10;
		write g@12 - f@3;
		""",
                List.of("39", "-5"));
    }

    @Test
    void expStmt1() {
        runSpli("""
                new f := lambda x { write x + 5; };
                f@3;
                """,
                "8");
    }

    @Test
    void expStmt2() {
        runSpli("100;");
    }

    @Test
    void expStmt3() {
        spliError("x * 7;");
    }

    @Test
    void countDown() {
        runSpli("""
                new countDown := lambda start { if start > 0 { write start; countDown@(start-1); } };
                countDown@5;
                countDown@2;
                """,
                List.of("5","4","3","2","1", "2","1"));
    }

    @Test
    void expStmt1b() {
        runSpli("""
                lambda y { write 18; } @ true;
                write lambda z { ret := 20; } @ 12;
                """,
                List.of("18", "20"));
    }

    @Test
    void expStmt2b() {
        runSpli("""
                true;
                1;
                3+4;
                8-7+6;
                10 < 20;
                """);
    }

    @Test
    void expStmt3b() {
        runSpli("lambda x { write x*3; ret := false; } @ 4;", "12");
    }

    @Test
    void expStmt4() {
        runSpli("""
                new f := lambda x {
                    write x;
                    ret := lambda y {
                        ret := x + y;
                    };
                };

                new g := lambda z {
                    write z;
                    ret := z;
                };
                # Note that the parameters both have to be 3 for this test to work,
                # since we never specified which would be evaluated first, the funexp or arg.
                write (f@3)@(g@3);
                """,
                List.of("3", "3", "6"));
    }
}

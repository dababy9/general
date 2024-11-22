package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Lab09Test extends SPLTestBase {
    @Test
    void numericValues() {
        Value one = Value.fromNum(1);
        Value ten = Value.fromNum(10);
        assertEquals(1, one.getNum());
        assertEquals(10, ten.getNum());
        assertEquals("10", ten.toString());
    }

    @Test
    void booleanValues() {
        Value t = Value.fromBool(true);
        assertTrue(t.getBool());
        assertEquals("true", t.toString());
    }

    @Test
    void unsetValues() {
        Value u = Value.unset();
        assertEquals("UNSET", u.toString());
    }

    @Test
    void invalidValueGet() {
        Value n = Value.fromNum(17);
        Value f = Value.fromBool(false);
        Value u = Value.unset();
        interpreterError(() -> { boolean b = n.getBool(); });
        interpreterError(() -> { int x = u.getNum(); });
        interpreterError(() -> { Closure c = f.getFun(); });
    }

    @Test
    void numValue() {
        assertEquals("1985", Value.fromNum(1985).toString());
    }

    @Test
    void boolValue() {
        assertEquals("false", Value.fromBool(false).toString());
        assertEquals("true", Value.fromBool(true).toString());
    }

    @Test
    void unsetValue() {
        assertEquals("UNSET", Value.unset().toString());
    }

    @Test
    void valueGetSetNum() {
        Value x = Value.fromNum(13);
        Value y = Value.fromNum(-8);
        assertEquals(13, x.getNum());
        assertEquals(-8, y.getNum());
    }

    @Test
    void valueGetSetBool() {
        Value x = Value.fromBool(true);
        Value y = Value.fromBool(false);
        assertTrue(x.getBool());
        assertFalse(y.getBool());
    }

    @Test
    void valueGetSetFun() {
        Closure c1 = new Closure(null, null);
        Closure c2 = new Closure(null, null);
        Value x = Value.fromFun(c1);
        Value y = Value.fromFun(c2);
        assertSame(c1, x.getFun());
        assertSame(c2, y.getFun());
    }

    @Test
    void valueGetNumError() {
        Value x = Value.fromBool(true);
        Value y = Value.fromFun(new Closure(null, null));
        Value z = Value.unset();
        interpreterError(() -> { x.getNum(); });
        interpreterError(() -> { y.getNum(); });
        interpreterError(() -> { z.getNum(); });
    }

    @Test
    void valueGetBoolError() {
        Value x = Value.fromNum(13);
        Value y = Value.fromFun(new Closure(null, null));
        Value z = Value.unset();
        interpreterError(() -> { x.getBool(); });
        interpreterError(() -> { y.getBool(); });
        interpreterError(() -> { z.getBool(); });
    }

    @Test
    void valueGetFunError() {
        Value x = Value.fromNum(13);
        Value y = Value.fromBool(false);
        Value z = Value.unset();
        interpreterError(() -> { x.getFun(); });
        interpreterError(() -> { y.getFun(); });
        interpreterError(() -> { z.getFun(); });
    }

    @Test
    void arithWithValues() {
        runSpli("""
                write 1 + 2 * 3 - 4;
                write 2 < 6 and 3 = -(1 - 4);
                write not true;
                """,
                List.of("3", "true", "false"));
    }

    @Test
    void controlWithValues() {
        runSpli("""
                if 1 < 2 { write true; }
                while false { write 16; }
                false or -3 = -(3);
                """,
                List.of("true"));
    }

    @Test
    void readValue() {
        runSpli("""
                write read * 3 < 20;
                write read * 3 < 20;
                """,
                List.of(7, 6),
                List.of("false", "true"));
    }

    @Test
    void arithTypeErrors() {
        spliError("write 1 and 2 and 3;");
        spliError("write lambda x { } - 20;");
        spliError("true < false;");
        spliError("write true * 3;");
    }

    @Test
    void writeBool() {
        runSpli("write false;", "false");
    }

    @Test
    void writeLambda() {
        runSpli("write lambda x { ret := x / 6; };",
                "Closure.*");
    }

    @Test
    void arithToBool() {
        runSpli("""
                write 2 + 3 = --6;
                write not (1 < 3) or true;
                """,
                List.of("false", "true"));
    }

    @Test
    void numToBoolError() {
        spliError("write 2 and false;");
        spliError("write not 10;");
        spliError("if 2*2 { write -1; }");
        spliError("while 0 { write -1; }");
    }

    @Test
    void boolToNumError() {
        spliError("write true * 3;");
        spliError("write -false;");
        spliError("write 1 = true;");
    }

    @Test
    void lambdaToBoolOrNumError() {
        spliError("write lambda x { write 17; } * 6;");
        spliError("write not lambda y { };");
    }

    @Test
    void unsetReturn() {
        runSpli("""
                new f := lambda x { write -x > 2; };
                new x := f @ 3;
                write x;
                write f @ -5;
                """,
                List.of("false", "UNSET", "true", "UNSET"));
    }

    @Test
    void typedVariables() {
        runSpli("""
                new x := 4 * 5;
                new y := x = 20;
                write x > 10 and y;
                """,
                "true");
    }

    @Test
    void typeVarErrors() {
        spliError("new x := 10; x and x;");
        spliError("lambda y { y @ 5; } @ 0;");
    }

    @Test
    void writeUnset() {
        runSpli("write lambda x { } @ 0;", "UNSET");
    }

    @Test
    void typedBoolFunctions() {
        runSpli("""
                write lambda x { ret := x or not x; } @ (2 < 1);
                write not lambda x { ret := x != 16; } @ 16;
                """,
                List.of("true", "true"));
    }

    @Test
    void typedNestedFunctions() {
        runSpli("""
                new a := lambda b { ret := lambda c { ret := b < c; }; };
                write a@19@50;
                new x := a@3;
                write a@20@16;
                write x;
                write x@5;
                """,
                List.of("true", "false", "Closure.*", "true"));
    }

    @Test
    void numBoolToFunError() {
        spliError("new f := lambda x { write 10; }; write (3-3)@4;");
        spliError("new f := lambda x { write 10; }; write (not true)@16;");
    }

    @Test
    void unsetToNumBoolFunError() {
        spliError("new x := lambda y { write y; }; write 7 - x@5;");
        spliError("new x := lambda y { write y; }; write not x@5;");
        spliError("new x := lambda y { write y; }; write x@5@6;");
    }

    @Test
    void randExists() {
        runSpli("new x := rand@10;");
    }

    @Test
    void rand1000() {
        runSpli("""
                new a := rand@1000;
                new b := rand@1000;
                new c := rand@1000;
                write a >= 1 and a <= 1000;
                write b >= 1 and b <= 1000;
                write c >= 1 and c <= 1000;
                write a = b and a = c;
                """,
                List.of(getTrue(), getTrue(), getTrue(), getFalse()));
    }

    @Test
    void rand3() {
        runSpli("""
                while rand@3 != 1 { }
                while rand@3 != 2 { }
                while rand@3 != 3 { }
                while rand@3 != 1 { }
                write 1;
                """,
                "1");
    }

    @Test
    void randReassign() {
        runSpli("""
                new fixed := lambda x { ret := 42; };
                write rand@1000000 = rand@1000000;
                """,
                getFalse());
        runSpli("""
                new fixed := lambda x { ret := 42; };
                rand := fixed;
                write rand@1000000 = rand@1000000;
                """,
                getTrue());
    }

    @Test
    void rand1() {
        runSpli("""
                write rand@1;
                write rand@1;
                write rand@1;
                """,
                List.of("1", "1", "1"));
    }

    @Test
    void rand30() {
        runSpli("""
                new count := 0;
                while rand@30 != 1 { count := count + 1; }
                while rand@30 != 20 { count := count + 1; }
                while rand@30 != 30 { count := count + 1; }
                while rand@30 != 20 { count := count + 1; }
                while rand@30 != 20 { count := count + 1; }
                write count < 8;
                write count <= 1000;
                """,
                List.of(getFalse(), getTrue()));
    }

    @Test
    void randReturn() {
        runSpli("""
                new rr := lambda x { ifelse x = 3 { ret := rand; } { ret := lambda y { ret := -2; }; } };
                write rr@2@3;
                write rr@3@1;
                write rr@3@10 >= 1;
                write rr@3@10 <= 10;
                """,
                List.of("-2", "1", getTrue(), getTrue()));
    }

    @Test
    void beepExists() {
        runSpli("beep;");
    }

    @Test
    void beep440() {
        runSpli("beep@440;", "<tone at 440hz 0.250sec .*vol>");
    }

    @Test
    void beepLoop() {
        runSpli("""
                new i := 0;
                while i < 5 {
                    beep@(100+i*10);
                    i := i + 1;
                }
                """,
                List.of(
                    "<tone at 100hz 0.250sec .*vol>",
                    "<tone at 110hz 0.250sec .*vol>",
                    "<tone at 120hz 0.250sec .*vol>",
                    "<tone at 130hz 0.250sec .*vol>",
                    "<tone at 140hz 0.250sec .*vol>"));
    }

    @Test
    void beepBeep() {
        runSpli("beep@400; beep@400;",
                List.of("<tone at 400hz 0.250sec .*vol>",
                        "<tone at 400hz 0.250sec .*vol>"));
    }

    @Test
    void beepNoReturn() {
        runSpli("write beep@100;",
                List.of("<tone at 100hz 0.250sec .*vol>", "UNSET"));
    }

    @Test
    void noteExists() {
        runSpli("note;");
    }

    @Test
    void noteReturnsClosure() {
        runSpli("write note@440;", "Closure.*");
    }

    @Test
    void note440() {
        runSpli("note@440@1000;", "<tone at 440hz 1.000sec .*vol>");
    }

    @Test
    void noteLoop() {
        runSpli("""
                new i := 0;
                while i < 6 {
                    note@(100+i*10)@(100+i*100);
                    i := i + 1;
                }
                """,
                List.of(
                    "<tone at 100hz 0.100sec .*vol>",
                    "<tone at 110hz 0.200sec .*vol>",
                    "<tone at 120hz 0.300sec .*vol>",
                    "<tone at 130hz 0.400sec .*vol>",
                    "<tone at 140hz 0.500sec .*vol>",
                    "<tone at 150hz 0.600sec .*vol>"));
    }

    void noteFun() {
        runSpli("""
                new triad := lambda p {
                    note@p@500;
                    note@(p*5/4)@500;
                    note@(p*3/2)@500;
                    note@(p*2)@1000;
                };
                triad@220;
                triad@256;
                """,
                List.of(
                    "<tone at 220hz 0.500sec .*vol>",
                    "<tone at 275hz 0.500sec .*vol>",
                    "<tone at 330hz 0.500sec .*vol>",
                    "<tone at 440hz 1.000sec .*vol>",
                    "<tone at 256hz 0.500sec .*vol>",
                    "<tone at 320hz 0.500sec .*vol>",
                    "<tone at 384hz 0.500sec .*vol>",
                    "<tone at 512hz 1.000sec .*vol>"));
    }

    @Test
    void noteMemory() {
        runSpli("""
                new a := note@220;
                new c := note@256;
                a@100;
                c@200;
                a@300;
                """,
                List.of(
                    "<tone at 220hz 0.100sec .*vol>",
                    "<tone at 256hz 0.200sec .*vol>",
                    "<tone at 220hz 0.300sec .*vol>"));
    }
}

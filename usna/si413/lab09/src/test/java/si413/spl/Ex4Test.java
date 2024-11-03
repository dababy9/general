package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex4Test extends SPLTestBase {
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
}

package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex8910Test extends SPLTestBase {
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
    void ifElse() {
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
    void whileStmt() {
        runSpli("""
                new i := 3;
                while i > 0 {
                    write i;
                    i := i - 1;
                }
                """,
                List.of("3", "2", "1"));
    }
}

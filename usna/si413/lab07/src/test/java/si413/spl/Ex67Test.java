package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex67Test extends SPLTestBase {
    @Test
    void newStmt() {
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
}

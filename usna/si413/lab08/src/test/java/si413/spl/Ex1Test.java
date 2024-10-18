package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex1Test extends SPLTestBase {
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
}

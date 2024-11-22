package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex1Test extends SPLTestBase {
    @Test
    void classDecl() {
        runSpli("""
                new A := class {
                new some_field := 100;
                new some_method := lambda x { some_field := x; };
                };
                new B := class { };
                """,
                List.of());
    }

    @Test
    void classDecl2() {
        runSpli("""
                class { };
                class { write 100; };
                lambda c { ret := class { "inner" }; } @ class { "outer" };
                """,
                List.of());
    }

    @Test
    void classWrite() {
        MockInterpreter interp = new MockInterpreter(
            """
            new A := class { write 15; };
            new B := class { new x := false; };
            write A;
            write B;
            write A;
            """,
            List.of());
        interp.run();
        List<String> out = interp.getOutputs();
        assertLinesMatch(List.of("Class.*", "Class.*", "Class.*"), out);
        assertEquals(out.get(0), out.get(2));
        assertNotEquals(out.get(0), out.get(1));
    }
}

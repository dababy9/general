package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex2Test extends SPLTestBase {
    @Test
    void constructMe() {
        runSpli("""
                new ConstructMe := class {
                write 42;
                "hooray"
                };
                new instance := ConstructMe!;
                ConstructMe!;
                """,
                List.of(
                    "42",
                    "hooray",
                    "42",
                    "hooray"
                ));
    }

    @Test
    void constructorScope() {
        runSpli("""
                new builder := lambda val {
                new other := 100;
                ret := class {
                write val * other;
                };
                };
                (builder@7)!;
                """,
                List.of("700"));
    }

    @Test
    void instanceWrite() {
        MockInterpreter interp = new MockInterpreter(
            """
            new A := class { "makeA" };
            new B := class { "makeB" };
            write A!;
            write B!;
            write A!;
            """,
            List.of());
        interp.run();
        List<String> out = interp.getOutputs();
        assertLinesMatch(List.of("makeA", "Instance.*", "makeB", "Instance.*", "makeA", "Instance.*"), out);
        assertNotEquals(out.get(1), out.get(3));
        assertNotEquals(out.get(1), out.get(5));
    }
}

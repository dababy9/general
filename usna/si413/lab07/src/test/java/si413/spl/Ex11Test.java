package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex11Test extends SPLTestBase {
    @Test
    void read() {
        runSpli("""
                new secret := 42;
                new numtries := 0;
                while read != secret { numtries := numtries + 1; }
                write numtries;
                """,
                List.of(15, -3, 21, 42),
                "3");
    }
}

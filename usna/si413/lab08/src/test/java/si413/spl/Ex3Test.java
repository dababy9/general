package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex3Test extends SPLTestBase {
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
        spliError("write 5 @ 6;");
    }
}

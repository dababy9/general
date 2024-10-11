package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex13Test extends SPLTestBase {
    @Test
    void debugStmt1() {
        runSpli("write 5; \"hello!\" write 6;",
                List.of("5", "hello!", "6"));
    }

    @Test
    void debugStmt2() {
        runSpli("\"toodaloo\"", "toodaloo");
        runSpli("if false { \"toodaloo\" }");
    }
}

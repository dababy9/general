package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex5Test extends SPLTestBase {
    @Test
    void expStmt1() {
        runSpli("""
                new f := lambda x { write x + 5; };
                f@3;
                """,
                "8");
    }

    @Test
    void expStmt2() {
        runSpli("100;");
    }

    @Test
    void expStmt3() {
        spliError("x * 7;");
    }

    @Test
    void countDown() {
        runSpli("""
                new countDown := lambda start { if start > 0 { write start; countDown@(start-1); } };
                countDown@5;
                countDown@2;
                """,
                List.of("5","4","3","2","1", "2","1"));
    }
}

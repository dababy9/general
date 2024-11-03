package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex3Test extends SPLTestBase {
    @Test
    void unsetReturn() {
        runSpli("""
                new f := lambda x { write -x > 2; };
                new x := f @ 3;
                write x;
                write f @ -5;
                """,
                List.of("false", "UNSET", "true", "UNSET"));
    }

    @Test
    void typedVariables() {
        runSpli("""
                new x := 4 * 5;
                new y := x = 20;
                write x > 10 and y;
                """,
                "true");
    }

    @Test
    void typeVarErrors() {
        spliError("new x := 10; x and x;");
        spliError("lambda y { y @ 5; } @ 0;");
    }
}

package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex2Test extends SPLTestBase {
    @Test
    void lambdaExpression() {
        runSpli("new f := lambda x { write -123; };");
    }

    @Test
    void lambdasAreDistinct() {
        runSpli("""
                new f := lambda x { write 1 + 2; };
                new g := lambda x { write 1 + 2; };
                write f = f;
                write f = g;
                """,
                List.of(getTrue(), getFalse()));
    }
}

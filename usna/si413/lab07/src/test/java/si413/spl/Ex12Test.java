package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex12Test extends SPLTestBase {
    @Test
    void shortCircuit1() {
        runSpli("write false and 1/0;", getFalse());
    }

    @Test
    void shortCircuit2() {
        runSpli("write true or what;", getTrue());
    }
}

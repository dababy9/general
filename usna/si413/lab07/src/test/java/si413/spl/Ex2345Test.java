package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex2345Test extends SPLTestBase {
    @Test
    void write123() {
        runSpli("write 123;", "123");
    }

    @Test
    void writeNeg() {
        runSpli("write -(17);", "-17");
    }

    @Test
    void plus() {
        runSpli("write 5 + 3;", "8");
    }

    @Test
    void minusDiv() {
        runSpli("write 18 - 15 / 3;", "13");
    }

    @Test
    void negAdd() {
        runSpli("write -(5 + 5);", "-10");
    }

    @Test
    void writeComp() {
        runSpli("write 4 > 3;", getTrue());
    }

    @Test
    void writeEquals() {
        runSpli("write 11 = 12;", getFalse());
    }

    @Test
    void compArith() {
        runSpli("write 6 + 3 != 2 and not 6 + 3 <= 2;", getTrue());
    }
}

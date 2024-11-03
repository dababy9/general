package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex1Test extends SPLTestBase {
    @Test
    void numericValues() {
        Value one = Value.fromNum(1);
        Value ten = Value.fromNum(10);
        assertEquals(1, one.getNum());
        assertEquals(10, ten.getNum());
        assertEquals("10", ten.toString());
    }

    @Test
    void booleanValues() {
        Value t = Value.fromBool(true);
        assertTrue(t.getBool());
        assertEquals("true", t.toString());
    }

    @Test
    void unsetValues() {
        Value u = Value.unset();
        assertEquals("UNSET", u.toString());
    }

    @Test
    void invalidValueGet() {
        Value n = Value.fromNum(17);
        Value f = Value.fromBool(false);
        Value u = Value.unset();
        interpreterError(() -> { boolean b = n.getBool(); });
        interpreterError(() -> { int x = u.getNum(); });
        interpreterError(() -> { Closure c = f.getFun(); });
    }
}

package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex2Test extends SPLTestBase {
    @Test
    void arithWithValues() {
        runSpli("""
                write 1 + 2 * 3 - 4;
                write 2 < 6 and 3 = -(1 - 4);
                write not true;
                """,
                List.of("3", "true", "false"));
    }

    @Test
    void controlWithValues() {
        runSpli("""
                if 1 < 2 { write true; }
                while false { write 16; }
                false or -3 = -(3);
                """,
                List.of("true"));
    }

    @Test
    void readValue() {
        runSpli("""
                write read * 3 < 20;
                write read * 3 < 20;
                """,
                List.of(7, 6),
                List.of("false", "true"));
    }

    @Test
    void arithTypeErrors() {
        spliError("write 1 and 2 and 3;");
        spliError("write lambda x { } - 20;");
        spliError("true < false;");
        spliError("write true * 3;");
    }
}

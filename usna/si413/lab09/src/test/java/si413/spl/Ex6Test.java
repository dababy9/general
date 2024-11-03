package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex6Test extends SPLTestBase {
    @Test
    void noteExists() {
        runSpli("note;");
    }

    @Test
    void noteReturnsClosure() {
        runSpli("write note@440;", "Closure.*");
    }

    @Test
    void note440() {
        runSpli("note@440@1000;", "<tone at 440hz 1.000sec .*vol>");
    }

    @Test
    void noteLoop() {
        runSpli("""
                new i := 0;
                while i < 6 {
                    note@(100+i*10)@(100+i*100);
                    i := i + 1;
                }
                """,
                List.of(
                    "<tone at 100hz 0.100sec .*vol>",
                    "<tone at 110hz 0.200sec .*vol>",
                    "<tone at 120hz 0.300sec .*vol>",
                    "<tone at 130hz 0.400sec .*vol>",
                    "<tone at 140hz 0.500sec .*vol>",
                    "<tone at 150hz 0.600sec .*vol>"));
    }
}

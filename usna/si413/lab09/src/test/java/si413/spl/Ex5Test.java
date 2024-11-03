package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;

public class Ex5Test extends SPLTestBase {
    @Test
    void beepExists() {
        runSpli("beep;");
    }

    @Test
    void beep440() {
        runSpli("beep@440;", "<tone at 440hz 0.250sec .*vol>");
    }

    @Test
    void beepLoop() {
        runSpli("""
                new i := 0;
                while i < 5 {
                    beep@(100+i*10);
                    i := i + 1;
                }
                """,
                List.of(
                    "<tone at 100hz 0.250sec .*vol>",
                    "<tone at 110hz 0.250sec .*vol>",
                    "<tone at 120hz 0.250sec .*vol>",
                    "<tone at 130hz 0.250sec .*vol>",
                    "<tone at 140hz 0.250sec .*vol>"));
    }
}

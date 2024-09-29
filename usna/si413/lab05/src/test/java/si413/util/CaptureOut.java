package si413.util;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Arrays;

/** Helper class to capture standard out during testing.
 */
public class CaptureOut {
    /** Runs the given action and return what it prints to standard out.
     */
    public static List<String> run(Runnable action) {
        PrintStream origOut = System.out;
        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capture));
        action.run();
        System.setOut(origOut);
        return Arrays.asList(capture.toString().split("\\n"));
    }
}

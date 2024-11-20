package si413.spl.ast;

import si413.spl.*;
import java.util.TreeMap;

/** A debug statement in SPL.
 * Example: "hello world"
 */
public class Debug extends Statement {
    private String msg;
    private int id;

    public static TreeMap<Integer, String> strings = new TreeMap<>();
    private static int counter = 1;

    public Debug(String msg) {
        this.msg = msg;
        id = counter++;
        strings.put(id, msg);
    }

    @Override
    public String astInfo() {
        return "\"%s\"".formatted(msg);
    }

    @Override
    public void compile(Frame env, Context ctx) {
        // Call printf on global constant string literal
        ctx.code("call i32 @printf(ptr @literal%d)".formatted(id));
    }
}

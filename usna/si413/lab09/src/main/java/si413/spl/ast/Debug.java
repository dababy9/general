package si413.spl.ast;

import si413.spl.*;

/** A debug statement in SPL.
 * Example: "hello world"
 */
public class Debug extends Statement {
    private String msg;

    public Debug(String msg) {
        this.msg = msg;
    }

    @Override
    public String astInfo() {
        return "\"%s\"".formatted(msg);
    }

    @Override
    public void execute(Frame env) {
        Interpreter.current().write(msg);
    }
}

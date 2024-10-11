package si413.spl.ast;

import si413.spl.Frame;
import si413.spl.Interpreter;

/** A write statement in SPL.
 * Example: write 15;
 */
public class Write extends Statement {
    private Expression exp;

    public Write(Expression exp) {
        this.exp = exp;
    }

    @Override
    public void execute() {
        int result = exp.evaluate();
        Interpreter.current().write(result);
    }
}

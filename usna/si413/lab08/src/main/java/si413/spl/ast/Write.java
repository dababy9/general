package si413.spl.ast;

import si413.spl.*;

/** A write statement in SPL.
 * Example: write 15;
 */
public class Write extends Statement {
    private Expression exp;

    public Write(Expression exp) {
        this.exp = exp;
    }

    @Override
    public void execute(Frame env) {
        int result = exp.evaluate(env);
        Interpreter.current().write(result);
    }
}

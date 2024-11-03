package si413.spl.ast;

import si413.spl.*;

/** An expression statement in SPL.
 * Example: 20;
 */
public class ExpressionStatement extends Statement {
    private Expression exp;

    public ExpressionStatement(Expression exp) {
        this.exp = exp;
    }

    @Override
    public void execute(Frame env) {
        exp.evaluate(env);
    }
}

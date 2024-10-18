package si413.spl.ast;

import si413.spl.*;

/** An expression as a statement.
 * Example: 100;
 */
public class ExpStmt extends Statement {
    Expression exp;

    public ExpStmt(Expression exp) {
        this.exp = exp;
    }

    @Override
    public void execute(Frame env) {
        exp.evaluate(env);
    }
}

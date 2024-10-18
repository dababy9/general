package si413.spl.ast;

import si413.spl.*;

/** A while loop in SPL.
 * Example: while x != 10 { x := x + 1; }
 */
public class WhileStmt extends Statement {
    private Expression condExp;
    private Statement body;

    public WhileStmt(Expression condExp, Statement body) {
        this.condExp = condExp;
        this.body = body;
    }

    @Override
    public void execute(Frame env) {
        while (true) {
            boolean cond = condExp.evaluate(env) != 0;
            if (!cond) break;
            body.execute(env);
        }
    }
}

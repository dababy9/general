package si413.spl.ast;

import si413.spl.*;

/** An if/else statement in SPL.
 * Example: ifelse x = 10 { write x; } { write 0; }
 */
public class IfElse extends Statement {
    private Expression condExp;
    private Statement ifStmt;
    private Statement elseStmt;

    public IfElse(Expression condExp, Statement ifStmt, Statement elseStmt) {
        this.condExp = condExp;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public void execute(Frame env) {
        boolean cond = condExp.evaluate(env).getBool();
        if (cond) ifStmt.execute(env);
        else elseStmt.execute(env);
    }
}

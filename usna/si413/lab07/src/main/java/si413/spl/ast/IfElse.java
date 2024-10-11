package si413.spl.ast;

import si413.spl.Frame;

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
    public void execute() {
        if(condExp.evaluate() == 1)
            ifStmt.execute();
        else
            elseStmt.execute();
    }
}

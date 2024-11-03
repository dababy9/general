package si413.spl.ast;

import si413.spl.*;

/** A variable declaration (binding) and assignment.
 * Example: new x := 10;
 */
public class NewStmt extends Statement {
    private String varname;
    private Expression rhs;

    public NewStmt(String varname, Expression rhs) {
        this.varname = varname;
        this.rhs = rhs;
    }

    @Override
    public String astInfo() {
        return varname;
    }

    @Override
    public void execute(Frame env) {
        Value value = rhs.evaluate(env);
        env.bind(varname, value);
    }
}

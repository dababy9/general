package si413.spl.ast;

import si413.spl.Interpreter;
import si413.spl.Frame;

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
    public void execute() {
        Frame g = Interpreter.current().getGlobal();
        g.bind(varname, rhs.evaluate());
    }
}

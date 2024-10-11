package si413.spl.ast;

import si413.spl.Interpreter;
import si413.spl.Frame;

/** An re-assignment operation in SPL.
 * Example: x := 21;
 */
public class Asn extends Statement {
    private String varname;
    private Expression rhs;

    public Asn(String varname, Expression rhs) {
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
        g.rebind(varname, rhs.evaluate());
    }
}

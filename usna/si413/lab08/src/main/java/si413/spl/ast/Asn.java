package si413.spl.ast;

import si413.spl.*;

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
    public void execute(Frame env) {
        int value = rhs.evaluate(env);
        env.rebind(varname, value);
    }
}

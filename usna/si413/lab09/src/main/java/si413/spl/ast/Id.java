package si413.spl.ast;

import si413.spl.*;

/** A variable name used in an Expression context.
 */
public class Id extends Expression {
    private String varname;

    public Id(String varname) {
        this.varname = varname;
    }

    @Override
    public String astInfo() {
        return varname;
    }

    @Override
    public Value evaluate(Frame env) {
        return env.lookup(varname);
    }
}

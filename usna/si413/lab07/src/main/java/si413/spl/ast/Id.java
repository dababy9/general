package si413.spl.ast;

import si413.spl.Interpreter;
import si413.spl.Frame;

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
    public int evaluate() {
        Frame g = Interpreter.current().getGlobal();
        return g.lookup(varname);
    }
}

package si413.spl.ast;

import si413.spl.*;

/** A read expression in an SPL progarm.
 * Example: read
 */
public class Read extends Expression {
    @Override
    public int evaluate(Frame env) {
        return Interpreter.current().read();
    }
}

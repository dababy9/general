package si413.spl.ast;

import si413.spl.*;

/** A read expression in an SPL progarm.
 * Example: read
 */
public class Read extends Expression {
    @Override
    public Value evaluate(Frame env) {
        return Value.fromNum(Interpreter.current().read());
    }
}

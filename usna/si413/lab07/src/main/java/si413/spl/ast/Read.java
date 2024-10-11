package si413.spl.ast;

import si413.spl.Frame;
import si413.spl.Interpreter;

/** A read expression in an SPL progarm.
 * Example: read
 */
public class Read extends Expression {

    @Override
    public int evaluate() {
        return Interpreter.current().read();
    }
}

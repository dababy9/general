package si413.spl.ast;

import si413.spl.*;

/** A literal boolean in SPL.
 * Example: true
 */
public class Bool extends Expression {
    private boolean value;

    public Bool(boolean value) {
        this.value = value;
    }

    @Override
    public String astInfo() {
        return String.valueOf(value);
    }

    @Override
    public Value evaluate(Frame env) {
        return Value.fromBool(value);
    }
}

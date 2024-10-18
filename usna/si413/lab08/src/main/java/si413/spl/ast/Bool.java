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
    public int evaluate(Frame env) {
        if (value) return 1;
        else return 0;
    }
}

package si413.spl.ast;

import si413.spl.Frame;

/** Base class for SPL expressions.
 * Every Expression must evalueate to a single value,
 * which means overriding the evaluate() method.
 */
public abstract class Expression extends ASTNode {
    public int evaluate() {
        throw new UnsupportedOperationException(
            "evaluate() method not yet written for %s class"
            .formatted(getClass().getSimpleName()));
    }
}

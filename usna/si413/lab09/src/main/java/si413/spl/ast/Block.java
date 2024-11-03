package si413.spl.ast;

import si413.spl.*;
import java.util.List;
import java.util.ArrayList;

/** A block of SPL statements, usually surrounded with curly braces.
 * Example: { write 1; write 2; }
 */
public class Block extends Statement {
    /** A list of all statements inside the block. */
    private List<Statement> children;

    /** Creates an empty block with no statements. */
    public Block() {
        this(List.of());
    }

    /** Creates a block with the given list of child statements. */
    public Block(List<Statement> children) {
        this.children = children;
    }

    @Override
    public List<Statement> getChildren() {
        return children;
    }

    @Override
    public void execute(Frame env) {
        Frame inner = new Frame(env);
        for (Statement child : children) {
            child.execute(inner);
        }
    }
}

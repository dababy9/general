package si413.spl;

import java.util.Map;

/** Run-time information on bindings from variable names to values.
 */
public class Frame {
    private Map<String, String> bindings = new java.util.HashMap<>();
    private Frame parent;

    public Frame(Frame parent) {
        this.parent = parent;
    }

    /** Constructs and returns a new global frame.
     * Note that this is a static method, so it can be called like
     * Frame.makeGlobal().
     */
    public static Frame makeGlobal() {
        Frame g = new Frame(null);
        return g;
    }

    /** Retrieves the value of the variable with the given name.
     * Recursively searches through parent frames as necessary.
     * If no such binding is found, error() is called on the current interpreter.
     */
    public String lookup(String variable) {
        if (bindings.containsKey(variable))
            return bindings.get(variable);
        else if (parent != null)
            return parent.lookup(variable);
        else
            throw new SPLError("Reference to undefined variable '%s'".formatted(variable));
    }

    /** Creates a new name/value mapping in this Frame.
     * If there is alredy a binding with the same name, error()
     * is called on the current interpreter.
     */
    public void bind(String variable, String val) {
        if (bindings.containsKey(variable))
            throw new SPLError("Redeclaration of variable '%s'".formatted(variable));
        else bindings.put(variable, val);
    }
}

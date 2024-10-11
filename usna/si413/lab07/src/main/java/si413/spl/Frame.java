package si413.spl;

import java.util.Map;

/** Run-time information on bindings from variable names to values.
 */
public class Frame {
    private Map<String, Integer> bindings = new java.util.HashMap<>();

    /** Constructs and returns a new global frame.
     * Note that this is a static method, so it can be called like
     * Frame.makeGlobal().
     */
    public static Frame makeGlobal() {
        Frame g = new Frame();
        return g;
    }

    /** Retrieves the value of the variable with the given name.
     * Recursively searches through parent frames as necessary.
     * If no such binding is found, error() is called on the current interpreter.
     */
    public int lookup(String variable) {
        if (bindings.containsKey(variable))
            return bindings.get(variable);
        else {
            Interpreter.current().error("No binding for variable '%s'".formatted(variable));
            return 0;
        }
    }

    /** Creates a new name/value mapping in this Frame.
     * If there is alredy a binding with the same name, error()
     * is called on the current interpreter.
     */
    public void bind(String variable, int val) {
        if (bindings.containsKey(variable))
            Interpreter.current().error("Cannot bind '%s', already set to '%s'"
                .formatted(variable, bindings.get(variable)));
        else bindings.put(variable, val);
    }

    /** Reassigns the given name to a new value.
     * Recursiveley searches through parent frames as necessary.
     * If no such binding exists, error() is called on the current interpreter.
     */
    public void rebind(String variable, int val) {
        if (bindings.containsKey(variable))
            bindings.put(variable, val);
        else
            Interpreter.current().error("Variable '%s' not yet bound; cannot rebind".formatted(variable));
    }
}

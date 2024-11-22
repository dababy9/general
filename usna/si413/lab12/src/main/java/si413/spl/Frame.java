package si413.spl;

import java.util.Map;
import si413.spl.ast.*;

/** Run-time information on bindings from variable names to values.
 */
public class Frame {
    private Map<String, Value> bindings = new java.util.HashMap<>();
    private Frame parent;
    private Frame superClass = null;

    public Frame(Frame parent) {
        this.parent = parent;
    }

    public Frame(Frame parent, Frame superClass) {
        this.parent = parent;
        this.superClass = superClass;
    }

    /** Constructs and returns a new global frame.
     * Note that this is a static method, so it can be called like
     * Frame.makeGlobal().
     */
    public static Frame makeGlobal() {
        Frame g = new Frame(null);
        new Rand().bindTo(g);
        Sound commonSound = new Sound();
        new Note(commonSound).bindTo(g);
        new Beep(commonSound).bindTo(g);
        return g;
    }

    /** Retrieves the value of the variable with the given name.
     * Recursively searches through superclass and parent frames as necessary.
     * If no such binding is found, error() is called on the current interpreter.
     */
    public Value lookup(String variable) {
        Value ret = null;
        if (bindings.containsKey(variable))
            ret = bindings.get(variable);
        else {
            if (superClass != null)
                ret = superClass.lookup(variable);
            if (ret == null && parent != null)
                ret = parent.lookup(variable);
        }
        if (ret == null){
            Interpreter.current().error("No binding for variable '%s'".formatted(variable));
            return Value.unset();
        } else return ret;
    }

    /** Creates a new name/value mapping in this Frame.
     * If there is alredy a binding with the same name, error()
     * is called on the current interpreter.
     */
    public void bind(String variable, Value val) {
        if (bindings.containsKey(variable))
            Interpreter.current().error("Cannot bind '%s', already set to '%s'"
                .formatted(variable, bindings.get(variable)));
        else bindings.put(variable, val);
    }

    /** Reassigns the given name to a new value.
     * Recursiveley searches through superclass and parent frames as necessary.
     * If no such binding exists, error() is called on the current interpreter.
     */
    public void rebind(String variable, Value val) {
        if (bindings.containsKey(variable))
            bindings.put(variable, val);
        else if (parent != null)
            parent.rebind(variable, val);
        else
            Interpreter.current().error("Variable '%s' not yet bound; cannot rebind".formatted(variable));
    }
}

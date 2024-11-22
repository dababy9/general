package si413.spl;

/** A run-time value in SPL, incorporating the type.
 */
public class Value {
    /** An enumeration for the possible SPL types.
     * An enum in Java is basically a restricted type that has to be one
     * of the named (constant) values.
     */
    public enum Type {
        NUM, BOOL, FUN, CLASS, OBJ, UNSET
    }

    private int value;
    private Type type;

    /** Creates a new Value with the given int storage and type.
     * This constructor is private because the correct way to make
     * a new Value is to call one of the static fromX() methods.
     */
    private Value(int value, Type type) {
        this.value = value;
        this.type = type;
    }

    /** Creates a new Value holding the given number. */
    public static Value fromNum(int x) {
        return new Value(x, Type.NUM);
    }

    /** Creates a new Value holding the given true/false. */
    public static Value fromBool(boolean b) {
        return new Value((b ? 1 : 0), Type.BOOL);
    }

    /** Creates a new Value holding the given function closure. */
    public static Value fromFun(Closure c) {
        return new Value(c.getId(), Type.FUN);
    }

    /** Creates a new Value holding the given class closure */
    public static Value fromCC(ClassClosure c) {
        return new Value(c.getId(), Type.CLASS);
    }

    /** Creates a new Value holding the given object instance */
    public static Value fromInstance(Instance i) {
        return new Value(i.getId(), Type.OBJ);
    }

    /** Creates a new "UNSET" value.
     * This is a special fourth type to represent Values in SPL that haven't
     * been assigned to anything yet, or the result from evaluations where an
     * error occurred.
     */
    public static Value unset() {
        return new Value(0, Type.UNSET);
    }

    private boolean typeCheck(Type expected) {
        if (type == expected) return true;
        else {
            Interpreter.current().error("Expected %s, got %s".formatted(expected, type));
            return false;
        }
    }

    /** Retrieves the underlying numeric value, if the type is NUM.
     * In case of a type mismatch, an error is sent to the current interpreter.
     */
    public int getNum() {
        typeCheck(Type.NUM);
        return value;
    }

    /** Retrieves the underlying boolean value, if the type is BOOL.
     * In case of a type mismatch, an error is sent to the current interpreter.
     */
    public boolean getBool() {
        typeCheck(Type.BOOL);
        return value != 0;
    }

    /** Retrieves the underlying closure, if the type is FUN.
     * In case of a type mismatch, an error is sent to the current interpreter.
     */
    public Closure getFun() {
        if (typeCheck(Type.FUN)) return Closure.fromId(value);
        else return null;
    }

    /** Retrieves the underlying class, if the type is CLASS.
     * In case of a type mismatch, an error is sent to the current interpreter.
     */
    public ClassClosure getCC() {
        if (typeCheck(Type.CLASS)) return ClassClosure.fromId(value);
        else return null;
    }

    /** Retrieves the underlying object, if the type is OBJ.
     * In case of a type mismatch, an error is sent to the current interpreter.
     */
    public Instance getObj() {
        if (typeCheck(Type.OBJ)) return Instance.fromId(value);
        else return null;
    }

    /** Returns a string representing this value, depending on its type. */
    @Override
    public String toString() {
        switch (type) {
            case NUM:
                return Integer.toString(value);
            case BOOL:
                if (value == 0) return "false";
                else return "true";
            case FUN:
                return "Closure#%d".formatted(value);
            case CLASS:
                return "Class#%d".formatted(value);
            case OBJ:
                return "Instance#%d".formatted(value);
            default:
                return "UNSET";
        }
    }
}

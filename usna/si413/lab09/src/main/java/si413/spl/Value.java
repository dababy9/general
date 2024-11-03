package si413.spl;

/** A run-time value in SPL, incorporating the type.
 */
public class Value {

    // represent the type of this 'Value'
    // 0 = NUM, 1 = BOOL, 2 = FUN, 3 = UNSET
    private int type;

    // hold actual data, dependent on type of the 'Value'
    private int numVal;
    private boolean boolVal;
    private Closure funVal;

    // constructors for various types
    public Value(int n){
        numVal = n;
        type = 0;
    }
    public Value(boolean b){
        boolVal = b;
        type = 1;
    }
    public Value(Closure c){
        funVal = c;
        type = 2;
    }
    public Value(){
        type = 3;
    }


    /** Creates a new Value holding the given number. */
    public static Value fromNum(int x) {
        return new Value(x);
    }

    /** Creates a new Value holding the given true/false. */
    public static Value fromBool(boolean b) {
        return new Value(b);
    }

    /** Creates a new Value holding the given function closure. */
    public static Value fromFun(Closure c) {
        return new Value(c);
    }

    /** Creates a new "UNSET" value.
     * This is a special fourth type to represent Values in SPL that haven't
     * been assigned to anything yet, or the result from evaluations where an
     * error occurred.
     */
    public static Value unset() {
        return new Value();
    }

    /** Retrieves the underlying numeric value, if the type is NUM.
     * In case of a type mismatch, an error is sent to the current interpreter.
     */
    public int getNum() {
        if(type != 0) Interpreter.current().error("Cannot convert " + getType() + " to NUM");
        
        return numVal;
    }

    /** Retrieves the underlying boolean value, if the type is BOOL.
     * In case of a type mismatch, an error is sent to the current interpreter.
     */
    public boolean getBool() {
        if(type != 1) Interpreter.current().error("Cannot convert " + getType() + " to BOOL");
        return boolVal;
    }

    /** Retrieves the underlying boolean value, if the type is FUN.
     * In case of a type mismatch, an error is sent to the current interpreter.
     */
    public Closure getFun() {
        if(type != 2) Interpreter.current().error("Cannot convert " + getType() + " to FUN");
        return funVal;
    }

    /** Returns a string representing this value, depending on its type. */
    @Override
    public String toString() {
        switch(type){
            case 0: return Integer.toString(numVal);
            case 1: return boolVal ? "true" : "false";
            case 2: return "Closure#" + funVal.getId();
            default: return "UNSET";
        }
    }

    // get the string literal for the Value type based on integer 'type'
    private String getType(){
        switch(type){
            case 0: return "NUM";
            case 1: return "BOOL";
            case 2: return "FUN";
            default: return "UNSET";
        }
    }
}

package si413.spl;

import java.util.List;
import si413.spl.ast.*;

/** A Closure to contain a lambda function and the frame in which it was called */
public class Closure {

    /** STATIC list of all closures */
    private static List<Closure> list = new java.util.ArrayList<>();

    /** STATIC method to retrieve a closure given the id (index) */
    public static Closure fromId(int id){
        try {
            return list.get(id);
        } catch(IndexOutOfBoundsException e) {
            Interpreter.current().error("Closure with id " + id + " does not exist");
        }
        return null;
    }

    /** Each Closure has an id, frame, and lambda reference. */
    private int id;
    private Frame frame;
    private Lambda lambda;

    /** Constructor for a new Closure.
     * Closure requires a frame and lambda function, and
     * the constructor will assign an unused id.
     * Will also add this closure to global (static) list.
     */
    public Closure(Frame f, Lambda l){
        id = list.size();
        list.add(this);
        frame = f;
        lambda = l;
    }

    /** Get id for this closure. */
    public int id(){
        return id;
    }

    /** Retrieves lambda function. */
    public Lambda getFunc(){
        return lambda;
    }

    /** Retrieves the closure frame */
    public Frame getFrame(){
        return frame;
    }
}

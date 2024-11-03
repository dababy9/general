package si413.spl;

import java.util.*;
import si413.spl.ast.Lambda;

public class Closure {
    private static List<Closure> saved = new ArrayList<>();

    public static Closure fromId(int id) {
        try {
            return saved.get(id);
        }
        catch (IndexOutOfBoundsException e) {
            Interpreter.current().error("Closure with id %d does not exist".formatted(id));
            return null;
        }
    }

    private Lambda func;
    private Frame env;
    private int id;

    public Closure(Lambda func, Frame env) {
        this.func = func;
        this.env = env;
        id = saved.size();
        saved.add(this);
    }

    public int getId() {
        return id;
    }

    public Lambda getFunc() {
        return func;
    }

    public Frame getEnv() {
        return env;
    }

    @Override
    public String toString() {
        return "Closure#%d".formatted(id);
    }
}

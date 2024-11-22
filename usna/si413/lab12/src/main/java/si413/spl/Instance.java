package si413.spl;

import java.util.*;
import si413.spl.ast.ClassDecl;

public class Instance {
    private static List<Instance> saved = new ArrayList<>();

    public static Instance fromId(int id) {
        try {
            return saved.get(id);
        }
        catch (IndexOutOfBoundsException e) {
            Interpreter.current().error("Object with id %d does not exist".formatted(id));
            return null;
        }
    }

    private Frame env;
    private int id;

    public Instance(Frame env) {
        this.env = env;
        id = saved.size();
        saved.add(this);
    }

    public int getId() {
        return id;
    }

    public Frame getEnv() {
        return env;
    }

    @Override
    public String toString() {
        return "Object#%d".formatted(id);
    }
}

package si413.spl;

import java.util.*;
import si413.spl.ast.ClassDecl;

public class ClassClosure {
    private static List<ClassClosure> saved = new ArrayList<>();

    public static ClassClosure fromId(int id) {
        try {
            return saved.get(id);
        }
        catch (IndexOutOfBoundsException e) {
            Interpreter.current().error("Class with id %d does not exist".formatted(id));
            return null;
        }
    }

    private ClassDecl cls;
    private Frame env;
    private int id;

    public ClassClosure(ClassDecl cls, Frame env) {
        this.cls = cls;
        this.env = env;
        id = saved.size();
        saved.add(this);
    }

    public int getId() {
        return id;
    }

    public ClassDecl getCls() {
        return cls;
    }

    public Frame getEnv() {
        return env;
    }

    @Override
    public String toString() {
        return "Class#%d".formatted(id);
    }
}

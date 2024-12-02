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

    private ClassDecl cls, superCls;
    private Frame env, superEnv = null;
    private int id;

    public ClassClosure(ClassDecl cls, Frame env) {
        this.cls = cls;
        this.env = env;
        id = saved.size();
        saved.add(this);
    }

    public ClassClosure(ClassDecl cls, ClassDecl superCls, Frame env, Frame superEnv) {
        this.cls = cls;
        this.superCls = superCls;
        this.env = env;
        this.superEnv = superEnv;
        id = saved.size();
        saved.add(this);
    }

    public int getId() {
        return id;
    }

    public ClassDecl getCls() {
        return cls;
    }

    public ClassDecl getSuperCls() {
        return superCls;
    }

    public Frame getEnv() {
        return env;
    }

    public Frame getSuperEnv() {
        return superEnv;
    }

    @Override
    public String toString() {
        return "Class#%d".formatted(id);
    }
}

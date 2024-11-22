package si413.spl.ast;

import si413.spl.*;
import java.util.Random;

/** Built-in function for random number generation.
 */
public class Rand extends Builtin {
    private Random rng = new Random();

    public Rand() {
        super("rand");
    }

    @Override
    public void execute(Frame env) {
        int m = env.lookup(argName).getNum();
        int result = rng.nextInt(m) + 1;
        env.rebind("ret", Value.fromNum(result));
    }
}

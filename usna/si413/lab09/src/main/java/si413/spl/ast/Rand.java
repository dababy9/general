package si413.spl.ast;

import si413.spl.*;
import java.util.Random;

/** A built-in random statement in SPL.
 */
public class Rand extends Statement {

    @Override
    public void execute(Frame env) {
        Value n = env.lookup("n");
        Random r = new Random();
        env.rebind(FunCall.retName, Value.fromNum(1 + r.nextInt(n.getNum())));
    }
}

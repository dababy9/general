package si413.spl.ast;

import si413.spl.*;

/** A built-in 'beep' statement in SPL, to play a given note for 1/4 second.
 */
public class Beep extends Statement {

    @Override
    public void execute(Frame env) {
        Value freq = env.lookup("freq");
        Sound s = new Sound();
        s.tone(freq.getNum(), 0.25, 1);
    }
}

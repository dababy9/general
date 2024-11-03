package si413.spl.ast;

import si413.spl.*;

/** A built-in 'note' statement in SPL, to play a given note for a given amount of seconds.
 */
public class Note extends Statement {

    private static Sound s = new Sound();

    @Override
    public void execute(Frame env) {
        Value freq = env.lookup("freq");
        Value duration = env.lookup("duration");
        s.tone(freq.getNum(), duration.getNum()/1000.0, 1);
    }
}

package si413.spl.ast;

import si413.spl.*;

/** Built-in function to make tones at a given frequency and 1/4 second
 */
public class Beep extends Builtin {
    private Sound sound;

    public Beep(Sound sound) {
        super("beep");
        this.sound = sound;
    }

    @Override
    public void execute(Frame env) {
        int hz = env.lookup(argName).getNum();
        sound.tone((double)hz, .25, .5);
    }
}

package si413.spl.ast;

import si413.spl.*;

/** Built-in function to make tones at a given frequency and duration
 */
public class Note extends Builtin {
    private Sound sound;

    private class NotePlay extends Builtin {
        private int hz;

        public NotePlay(int hz) {
            super("ret");
            this.hz = hz;
        }

        @Override
        public void execute(Frame env) {
            int ms = env.lookup(argName).getNum();
            sound.tone((double)hz, ms / 1000.0, .5);
        }
    }

    public Note(Sound sound) {
        super("note");
        this.sound = sound;
    }

    @Override
    public void execute(Frame env) {
        int hz = env.lookup(argName).getNum();
        Statement baby = new NotePlay(hz);
        Lambda lambda = new Lambda(argName, baby);
        Value babyClosure = lambda.evaluate(null);
        env.rebind("ret", babyClosure);
    }
}

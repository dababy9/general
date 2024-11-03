package si413.spl;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.LineUnavailableException;

/** Java class to play sounds at given frequency, duration, and volume.
 *
 * You should not need to follow the details! Just construct a new
 * instance of the class and then call tone(freq, dur, vol) to play
 * a note at the given frequency (in Hz), duration (in seconds), and volume (from 0 to 1).
 */
public class Sound {
    public static float SAMPLE_RATE = 48000f;
    private static boolean makeNoise = true;
    private long curSamp = 0;
    private double curTime = 0d;
    private SourceDataLine line;
    private byte[] samples;

    /** Creates a new Sound object ready to play tones.  */
    public Sound() {
        if (makeNoise) {
            samples = new byte[(int)SAMPLE_RATE];
            AudioFormat aform = new AudioFormat(SAMPLE_RATE,
                8 /* bits per sample */, 1 /* channels */, true /* signed */, false /* bigEndian */);
            try {
                line = AudioSystem.getSourceDataLine(aform);
                line.open(aform);
            }
            catch (LineUnavailableException e) {
                throw new RuntimeException("could not access audio", e);
            }
            line.start();
        }
        else {
            line = null;
            samples = null;
        }
    }

    /** Plays a square-wave tone.
     * @param hz The frequency in Hz (e.g. 256 for middle C).
     * @param duration How long the tone should last, in (fractional) seconds.
     * @param volume, How loud it should be, from 0 (silent) up to 1 (full volume).
     */
    public void tone(double hz, double duration, double volume) {
        curTime += duration;
        if (makeNoise) {
            long endSamp = (long)Math.ceil(curTime * SAMPLE_RATE);
            int nsamp = (int)(endSamp - curSamp);
            if (nsamp > samples.length) samples = new byte[nsamp];
            // square wave, just lo and hi frequencies alternating according to hz
            byte lo = (byte)Math.round(-127 * volume), hi = (byte)Math.round(127 * volume);
            for (int i = 0; curSamp < endSamp; ++i, ++curSamp) {
                long half_period_index = (long)(((double)curSamp / SAMPLE_RATE) * 2 * hz);
                samples[i] = (half_period_index % 2l == 0l) ? lo : hi;
            }
            line.write(samples, 0, nsamp);
        }
        else {
            Interpreter.current().write("<tone at %dhz %.3fsec %.2fvol>"
                    .formatted(Math.round(hz), duration, volume));
        }
    }

    /** Wait until all sound output has finished before returning. */
    public void finish() {
        if (makeNoise) line.drain();
    }

    /** For testing purposes, have the class display messages rather than actually plaing audio. */
    public static void goSilent() {
        makeNoise = false;
    }

    /** Simple test to make sure the sound works. */
    public static void main(String[] args) {
        System.out.println("Written by Koji Kondo in 1985");
        Sound sound = new Sound();
        sound.tone(375, .12, .5);
        sound.tone(1,   .03,  0);
        sound.tone(375, .12, .5);
        sound.tone(1,   .18,  0);
        sound.tone(375, .12, .5);
        sound.tone(1,   .18,  0);
        sound.tone(300, .12, .5);
        sound.tone(1,   .03,  0);
        sound.tone(375, .12, .5);
        sound.tone(1,   .18,  0);
        sound.tone(450, .3,  .5);
        sound.tone(1,   .3,   0);
        sound.tone(225, .3,  .5);
        sound.finish();
        System.exit(0);
    }
}

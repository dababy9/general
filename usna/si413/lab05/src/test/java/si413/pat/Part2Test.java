package si413.pat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStreams;

@Timeout(value = 300, unit = TimeUnit.MILLISECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class Part2Test {
    void checkTree(String source, String expected) {
        Part1Test.getParse(source, false, expected);
    }

    @Test
    void oneSym() {
        checkTree("hello;",
            "(prog (seq hello) ; (prog <EOF>))"
        );
    }

    @Test
    void concatSyms() {
        checkTree("programming is so much fun;",
            "(prog (seq (seq (seq (seq (seq programming) (seq is)) (seq so)) (seq much)) (seq fun)) ; (prog <EOF>))"
        );
    }

    @Test
    void oneName() {
        checkTree("ThisIsAVariable;",
            "(prog (seq ThisIsAVariable) ; (prog <EOF>))"
        );
    }

    @Test
    void brackets1() {
        checkTree("[ok];",
            "(prog (seq [ (seq ok) ]) ; (prog <EOF>))"
        );
    }

    @Test
    void brackets2() {
        checkTree("[it works] [or [does it]];",
            "(prog (seq (seq [ (seq (seq it) (seq works)) ]) (seq [ (seq (seq or) (seq [ (seq (seq does) (seq it)) ])) ])) ; (prog <EOF>))"
        );
    }

    @Test
    void multiStatements() {
        checkTree("a b c; d e;\nf g h i;",
            "(prog (seq (seq (seq a) (seq b)) (seq c)) ; (prog (seq (seq d) (seq e)) ; (prog (seq (seq (seq (seq f) (seq g)) (seq h)) (seq i)) ; (prog <EOF>))))"
        );
    }

    @Test
    void rev1() {
        checkTree("something_r;",
            "(prog (seq (seq something) _r) ; (prog <EOF>))"
        );
    }

    @Test
    void rev2() {
        checkTree("[two things]_r;",
            "(prog (seq (seq [ (seq (seq two) (seq things)) ]) _r) ; (prog <EOF>))"
        );
    }

    @Test
    void assign1() {
        checkTree("X:X;",
            "(prog (seq (seq X) : X) ; (prog <EOF>))"
        );
    }

    @Test
    void assign2() {
        checkTree("[a b c] : Alphabet;",
            "(prog (seq (seq [ (seq (seq (seq a) (seq b)) (seq c)) ]) : Alphabet) ; (prog <EOF>))"
        );
    }

    @Test
    void fold1() {
        checkTree("left * right;",
            "(prog (seq (seq left) * (seq right)) ; (prog <EOF>))"
        );
    }

    @Test
    void fold2() {
        checkTree("do re mi * fa * sol la;",
            "(prog (seq (seq (seq (seq (seq do) (seq re)) (seq mi)) * (seq fa)) * (seq (seq sol) (seq la))) ; (prog <EOF>))"
        );
    }

    @Test
    void all1() {
        checkTree("a [b c]_r:X_r * d:Y X * X Y X;",
            "(prog (seq (seq (seq (seq a) (seq (seq (seq (seq [ (seq (seq b) (seq c)) ]) _r) : X) _r)) * (seq (seq (seq d) : Y) (seq X))) * (seq (seq (seq X) (seq Y)) (seq X))) ; (prog <EOF>))"
        );
    }

    @Test
    void all2() {
        checkTree("[ uno dos : TWO [ tres ] ] : SIX _r * quatro cinco * SIX ; SIX TWO ; seis ;",
            "(prog (seq (seq (seq (seq (seq [ (seq (seq (seq uno) (seq (seq dos) : TWO)) (seq [ (seq tres) ])) ]) : SIX) _r) * (seq (seq quatro) (seq cinco))) * (seq SIX)) ; (prog (seq (seq SIX) (seq TWO)) ; (prog (seq seis) ; (prog <EOF>))))"
        );
    }
}

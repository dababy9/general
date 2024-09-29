package si413.pat;

import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStreams;
import si413.util.CaptureOut;

@Timeout(value = 300, unit = TimeUnit.MILLISECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class Part3Test {
    void checkRun(String source, List<String> expected) {
        PatParser.ProgContext tree = Part1Test.getParse(source, false, null);
        PatEvaluator eval = new PatEvaluator();
        List<String> actual = CaptureOut.run(() -> { eval.visit(tree); });
        assertLinesMatch(expected, actual);
    }

    void checkRun(String source, String expected) {
        checkRun(source, List.of(expected));
    }

    void checkError(String source) {
        PatParser.ProgContext tree = Part1Test.getParse(source, false, null);
        PatEvaluator eval = new PatEvaluator();
        assertThrows(PatError.class,
            (() -> { CaptureOut.run(() -> { eval.visit(tree); }); }),
            "should throw PatError on input '%s'".formatted(source));
    }

    @Test
    void oneSym() {
        checkRun("hello;", "hello");
    }

    @Test
    void concatSyms() {
        checkRun("programming is so much fun;", "programming is so much fun");
    }

    @Test
    void brackets1() {
        checkRun("[ok];", "ok");
    }

    @Test
    void brackets2() {
        checkRun("[it works] [or [does it]];", "it works or does it");
    }

    @Test
    void multiStatements() {
        checkRun("a b c; d e;\nf g h i;",
            List.of("a b c", "d e", "f g h i"));
    }

    @Test
    void rev1() {
        checkRun("something_r;", "something");
    }

    @Test
    void rev2() {
        checkRun("[two things]_r;", "things two");
    }

    @Test
    void assign1() {
        checkRun("what:X X;", "what what");
    }

    @Test
    void assign2() {
        checkRun("[a b c] : Alphabet;", "a b c");
    }

    @Test
    void assign3() {
        checkError("Undefined;");
    }

    @Test
    void fold1() {
        checkRun("left * right;", "left right");
    }

    @Test
    void fold2() {
        checkRun("do re mi * fa * sol la;", "do sol fa la re mi");
    }

    @Test
    void all1() {
        checkRun("a [b c]_r:X_r * d:Y X * X Y X;", "a c d b b d c c c b b");
    }

    @Test
    void all2() {
        checkRun("[ uno dos : TWO [ tres ] ] : SIX _r * quatro cinco * SIX ; SIX TWO ; seis ;",
            List.of("tres uno quatro dos dos tres cinco uno", "uno dos tres dos", "seis"));
    }
}

package si413.spl;

import si413.spl.ast.ASTNode;
import java.util.List;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Timeout(value = 300, unit = TimeUnit.MILLISECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
class SPLTestBase {
    private String trueVal = null;
    private String falseVal = null;

    protected void checkAST(String source, List<String> expected) {
        MockInterpreter interp = new MockInterpreter(source, List.of());
        assertLinesMatch(expected, ASTNode.dumpTrees(interp.getStmts()));
    }

    protected void checkAST(String source, String expected) {
        checkAST(source, List.of(expected));
    }

    protected void runSpli(String source, List<Integer> inputs, List<String> expected) {
        MockInterpreter interp = new MockInterpreter(source, inputs);
        interp.run();
        assertLinesMatch(expected, interp.getOutputs());
    }

    protected void runSpli(String source, List<Integer> inputs, String expected) {
        runSpli(source, inputs, List.of(expected));
    }

    protected void runSpli(String source, List<String> expected) {
        runSpli(source, List.of(), expected);
    }

    protected void runSpli(String source, String expected) {
        runSpli(source, List.of(), List.of(expected));
    }

    protected void runSpli(String source) {
        runSpli(source, List.of(), List.of());
    }

    protected void spliError(String source, List<Integer> inputs) {
        MockInterpreter interp = new MockInterpreter(source, inputs);
        assertThrows(MockInterpreter.Error.class, () -> { interp.run(); });
    }

    protected void spliError(String source) {
        spliError(source, List.of());
    }

    protected String getTrue() {
        if (trueVal == null) {
            MockInterpreter interp = new MockInterpreter("write true;", List.of());
            interp.run();
            trueVal = interp.getOutputs().get(0);
        }
        return trueVal;
    }

    protected String getFalse() {
        if (falseVal == null) {
            MockInterpreter interp = new MockInterpreter("write false;", List.of());
            interp.run();
            falseVal = interp.getOutputs().get(0);
        }
        return falseVal;
    }
}

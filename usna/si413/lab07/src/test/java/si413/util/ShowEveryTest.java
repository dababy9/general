package si413.util;

import java.util.stream.Collectors;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;


public class ShowEveryTest extends SummaryGeneratingListener {
    @Override
    public void testPlanExecutionStarted(TestPlan plan) {
        System.err.format("Running tests from %s%n",
            plan.getChildren(plan.getRoots().iterator().next())
                .stream()
                .map(TestIdentifier::getDisplayName)
                .collect(Collectors.joining(" and ")));
        super.testPlanExecutionStarted(plan);
    }

    @Override
    public void executionStarted(TestIdentifier whichTest) {
        super.executionStarted(whichTest);
        if (whichTest.isTest())
            System.err.format("Test case %s...", whichTest.getDisplayName());
    }

    @Override
    public void executionFinished(TestIdentifier whichTest, TestExecutionResult result) {
        super.executionFinished(whichTest, result);
        if (!whichTest.isTest()) return;
        switch (result.getStatus()) {
            case SUCCESSFUL:
                System.err.format("passed!%n");
                break;
            default:
                System.err.format("FAILED%n");
                result.getThrowable().ifPresent(e -> {
                    System.err.format("  %s%n", e.getMessage());
                });
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan plan) {
        super.testPlanExecutionFinished(plan);
        TestExecutionSummary totals = getSummary();
        long nstarted = totals.getTestsStartedCount();
        long npassed = totals.getTestsSucceededCount();
        System.err.format("RESULT: passed %d of %d tests%n", npassed, nstarted);
    }
}

package edu.illinois.cs.testrunner.execution;

import edu.illinois.cs.testrunner.data.results.TestResult;
import edu.illinois.cs.testrunner.data.results.TestResultFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

// This class is used to track result of each tests during execution.
public class JUnit5TestListener implements TestExecutionListener {

    private Map<String, TestResult> results;  // key is the full qualified method name
    private Map<String, Long> startTimesNano;
    private List<String> testOrder;  // a list of full qualified method names

    public Map<String, TestResult> getResults() {
        return this.results;
    }

    public List<String> getTestOrder() {
        return this.testOrder;
    }

    @Override
    public void testPlanExecutionStarted(TestPlan testplan) {
        this.results = new HashMap<>();
        this.startTimesNano = new HashMap<>();
        this.testOrder = new ArrayList<>();
    }

    @Override
    public void executionSkipped(TestIdentifier identifier, String reason) {
        if (!identifier.isTest()) {
            return;
        }
        String fullQualifiedName = Utils.toFullQualifiedName(identifier.getUniqueId());
        TestResult result = TestResultFactory.ignored(fullQualifiedName);
        this.results.put(fullQualifiedName, result);
        this.testOrder.add(fullQualifiedName);
    }

    @Override
    public void executionStarted(TestIdentifier identifier) {
        if (!identifier.isTest()) {
            return;
        }

        this.startTimesNano.put(Utils.toFullQualifiedName(identifier.getUniqueId()),
                                System.nanoTime());
    }

    @Override
    public void executionFinished(TestIdentifier identifier, TestExecutionResult executionResult) {
        if (!identifier.isTest()) {
            return;
        }

        String fullQualifiedName = Utils.toFullQualifiedName(identifier.getUniqueId());
        double runtime = (System.nanoTime() - this.startTimesNano.get(fullQualifiedName)) / 1E9;
        this.testOrder.add(fullQualifiedName);
        if (executionResult.getStatus() == Status.FAILED) {
            executionResult.getThrowable().get().printStackTrace();
            this.results.put(
                    fullQualifiedName,
                    TestResultFactory.failOrError(executionResult.getThrowable().get(),
                                                  runtime,
                                                  fullQualifiedName));
        } else {
            // passed
            this.results.put(fullQualifiedName,
                             TestResultFactory.passing(runtime, fullQualifiedName));
        }
    }
}

package edu.illinois.cs.testrunner.execution;

import edu.illinois.cs.testrunner.data.results.TestRunResult;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

// Execute JUnit 5 tests
public class JUnit5TestExecutor {

    public static TestRunResult run(final String testRunId, final List<String> testList) {
        List<MethodSelector> testMethods =
                testList.stream()
                        .map(name -> DiscoverySelectors.selectMethod(name))
                        .collect(Collectors.toList());
        JUnit5TestListener listener = new JUnit5TestListener();
        LauncherDiscoveryRequest request =
                LauncherDiscoveryRequestBuilder.request().selectors(testMethods).build();
        LauncherFactory.create().execute(request, listener);
        return new TestRunResult(testRunId, listener.getTestOrder(), listener.getResults());
    }

}

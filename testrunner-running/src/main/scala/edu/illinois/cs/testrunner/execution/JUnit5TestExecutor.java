package edu.illinois.cs.testrunner.execution;

import edu.illinois.cs.testrunner.data.results.TestRunResult;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

// Use JUnit 5 API to run tests, including JUnit 4 and 5 tests.
public class JUnit5TestExecutor {

    public static TestRunResult runTestsSeparately(
            final String testRunId, final List<String> tests) {
        // this is a workaround, which has full control of test order
        List<MethodSelector> methods =
                tests.stream()
                     .map(name -> DiscoverySelectors.selectMethod(name))
                     .collect(Collectors.toList());
        JUnit5TestListener listener = new JUnit5TestListener();
        Launcher launcher = LauncherFactory.create();
        for (MethodSelector method : methods) {
            LauncherDiscoveryRequest request =
                    LauncherDiscoveryRequestBuilder
                            .request().selectors(method).build();
            launcher.execute(request, listener);
        }
        return new TestRunResult(testRunId,
                                 listener.getTestOrder(),
                                 listener.getResults());
    }

}

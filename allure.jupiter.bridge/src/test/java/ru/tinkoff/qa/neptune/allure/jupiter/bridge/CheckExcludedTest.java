package ru.tinkoff.qa.neptune.allure.jupiter.bridge;

import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.model.TestResultContainer;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported;

import java.lang.reflect.Field;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.allure.jupiter.bridge.NeptuneAllureExcludeTest.changeResultWriter;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;

public class CheckExcludedTest {

    @Mock
    private AllureResultsWriter mockWriter;
    private Field excludedField;

    @BeforeClass
    public void beforeClass() throws Exception {
        openMocks(this);
        changeResultWriter(mockWriter);

        excludedField = ItemsToNotBeReported.class.getDeclaredField("EXCLUDED");
        excludedField.setAccessible(true);
    }

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void beforeMethod() throws Exception {
        clearInvocations(mockWriter);

        var excluded = (Set<Object>) excludedField.get(ItemsToNotBeReported.class);
        excluded.clear();
    }

    @DataProvider
    public static Object[][] excludedClasses() {
        return new Object[][] {
            {"ru.tinkoff.qa.neptune.allure.jupiter.bridge.fully.excluded"},
            {"ru.tinkoff.qa.neptune.allure.jupiter.bridge.excluded.clazz"}
        };
    }

    @Test(dataProvider = "excludedClasses")
    public void excludedTest(String pack) {
        var request = LauncherDiscoveryRequestBuilder.request()
            .selectors(selectPackage(pack))
            .build();
        var launcher = LauncherFactory.create();
        launcher.discover(request);
        launcher.execute(request);

        verifyNoInteractions(mockWriter);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void excludedMethodsTest() throws Exception {
        var request = LauncherDiscoveryRequestBuilder.request()
            .selectors(selectPackage("ru.tinkoff.qa.neptune.allure.jupiter.bridge.excluded.methods"))
            .build();
        var launcher = LauncherFactory.create();
        launcher.discover(request);
        launcher.execute(request);

        verify(mockWriter, times(9)).write(any(TestResult.class));
        verify(mockWriter, times(13)).write(any(TestResultContainer.class));

        var excluded = (Set<Object>) excludedField.get(ItemsToNotBeReported.class);

        var excludedResultsAndContainers = excluded
            .stream()
            .filter(o -> (o instanceof TestResultContainer) || (o instanceof TestResult))
            .map(o -> {
                if (o instanceof TestResultContainer) {
                    return ((TestResultContainer) o).getName();
                }

                return ((TestResult) o).getName();
            })
            .distinct()
            .collect(toList());

        assertThat(excludedResultsAndContainers,
            iterableOf("test1()", "test2()", "test6()", "test7()", "test21()", "test22()"));


        var excludedFixtureNames = excluded
            .stream()
            .filter(o -> o instanceof ItemsToNotBeReported.ExcludedFixtureWrapper)
            .map(o -> ((ItemsToNotBeReported.ExcludedFixtureWrapper) o).getFixtureResult().getName())
            .distinct()
            .collect(toList());

        assertThat(excludedFixtureNames,
            iterableOf("beforeEach", "afterEachExcluded", "beforeEachNested", "beforeEach2", "afterEachExcluded2"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void notExcludedTest() throws Exception {
        var request = LauncherDiscoveryRequestBuilder.request()
            .selectors(selectPackage("ru.tinkoff.qa.neptune.allure.jupiter.bridge.not.excluded"))
            .build();
        var launcher = LauncherFactory.create();
        launcher.discover(request);
        launcher.execute(request);

        verify(mockWriter, times(12)).write(any(TestResult.class));
        verify(mockWriter, times(16)).write(any(TestResultContainer.class));

        var excluded = (Set<Object>) excludedField.get(ItemsToNotBeReported.class);

        assertThat(excluded, emptyIterable());
    }
}

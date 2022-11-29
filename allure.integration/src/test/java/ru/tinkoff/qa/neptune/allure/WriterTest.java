package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.FileSystemResultsWriter;
import io.qameta.allure.model.FixtureResult;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.model.TestResultContainer;
import org.mockito.Mock;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported;
import ru.tinkoff.qa.neptune.allure.lifecycle.NeptuneResultWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.qameta.allure.aspects.StepsAspects.getLifecycle;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.isExcludedFromReport;

public class WriterTest {

    @Mock
    private FileSystemResultsWriter mockWriter;
    private Set<Object> set;

    @BeforeClass
    @SuppressWarnings("unchecked")
    public void beforeClass() throws Exception{
        openMocks(this);
        Allure.setLifecycle(new AllureLifecycle(new NeptuneResultWriter(mockWriter)));

        var excludedField = ItemsToNotBeReported.class.getDeclaredField("EXCLUDED");
        excludedField.setAccessible(true);

        set = (Set<Object>) excludedField.get(ItemsToNotBeReported.class);

    }

    @BeforeMethod
    public void beforeTest() {
        clearInvocations(mockWriter);
    }

    @AfterMethod
    public void afterTest() {
        set.clear();
    }

    @AfterClass
    public void afterClass() {
        openMocks(this);
        Allure.setLifecycle(new AllureLifecycle());
    }


    private static TestResultContainer getTestResultContainer() {
        return new TestResultContainer().setName(randomAlphabetic(15)).setUuid(randomUUID().toString());
    }

    private static TestResult getTestResult() {
        return new TestResult().setName(randomAlphabetic(15)).setUuid(randomUUID().toString());
    }

    private static FixtureResult getFixtureResult() {
        return new FixtureResult().setName(randomAlphabetic(15));
    }

    @Test
    public void containerWithoutChildrenTest() {
        var container = getTestResultContainer();
        container
            .setAfters(new ArrayList<>(List.of(getFixtureResult(), getFixtureResult())))
            .setBefores(new ArrayList<>(List.of(getFixtureResult(), getFixtureResult())));

        getLifecycle().startTestContainer(container);
        getLifecycle().writeTestContainer(container.getUuid());

        verifyNoInteractions(mockWriter);
        assertThat(isExcludedFromReport(container), is(true));
        assertThat(container.getBefores(), hasSize(2));
        assertThat(container.getAfters(), hasSize(2));
    }

    @Test
    public void containerExcludedFromReport() {
        var container = getTestResultContainer();
        set.add(container);

        getLifecycle().startTestContainer(container);
        getLifecycle().writeTestContainer(container.getUuid());

        verifyNoInteractions(mockWriter);
    }

    @Test
    public void notExcludedContainer() {
        var container = getTestResultContainer();
        var childContainer = getTestResultContainer();
        container.setChildren(new ArrayList<>(List.of(
            getTestResult().getUuid(),
            getTestResultContainer().getUuid())));

        getLifecycle().startTestContainer(container);
        getLifecycle().startTestContainer(container.getUuid(), childContainer);
        getLifecycle().writeTestContainer(container.getUuid());
        verify(mockWriter, times(1)).write(container);
    }

    @Test
    public void testResultExcludedFromReport() {
        var container = getTestResultContainer();
        var testResult = getTestResult();

        getLifecycle().startTestContainer(container);
        getLifecycle().scheduleTestCase(testResult);
        getLifecycle().startTestCase(testResult.getUuid());

        set.add(testResult);
        getLifecycle().writeTestCase(testResult.getUuid());
        verifyNoInteractions(mockWriter);
    }

    @Test
    public void testResultNotExcludedFromReport() {
        var testResult = getTestResult();
        getLifecycle().scheduleTestCase(testResult);
        getLifecycle().startTestCase(testResult.getUuid());

        getLifecycle().writeTestCase(testResult.getUuid());

        verify(mockWriter, times(1)).write(testResult);
    }

    @Test
    public void partiallyExcludedTestResultContainer() {

        var root = getTestResultContainer();
        var notExcludedContainer = getTestResultContainer();
        var excludedContainer = getTestResultContainer();

        var notExcludedBeforeFixture = getFixtureResult();
        var excludedBeforeFixture = getFixtureResult();
        var notExcludedAfterFixture = getFixtureResult();
        var excludedAfterFixture = getFixtureResult();

        var notExcludedTestResult = getTestResult();
        var excludedTestResult = getTestResult();

        set.add(excludedContainer);
        set.add(new ItemsToNotBeReported.ExcludedFixtureWrapper(excludedBeforeFixture));
        set.add(new ItemsToNotBeReported.ExcludedFixtureWrapper(excludedAfterFixture));
        set.add(excludedTestResult);

        root.setBefores(new ArrayList<>(List.of(notExcludedBeforeFixture, excludedBeforeFixture)))
            .setAfters(new ArrayList<>(List.of(notExcludedAfterFixture, excludedAfterFixture)));

        getLifecycle().startTestContainer(root);

        getLifecycle().startTestContainer(root.getUuid(), notExcludedContainer);
        getLifecycle().startTestContainer(root.getUuid(), excludedContainer);

        getLifecycle().scheduleTestCase(root.getUuid(), notExcludedTestResult);
        getLifecycle().scheduleTestCase(root.getUuid(), excludedTestResult);

        getLifecycle().writeTestContainer(root.getUuid());

        verify(mockWriter, times(1)).write(root);
        assertThat(root.getBefores(), contains(notExcludedBeforeFixture));
        assertThat(root.getAfters(), contains(notExcludedAfterFixture));
        assertThat(root.getChildren(), contains(notExcludedContainer.getUuid(), notExcludedTestResult.getUuid()));
    }
}

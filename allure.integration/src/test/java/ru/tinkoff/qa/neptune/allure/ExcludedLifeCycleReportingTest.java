package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.model.FixtureResult;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.model.TestResultContainer;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.allure.excluded.clazz.SomeExcludedClass;
import ru.tinkoff.qa.neptune.allure.excluded.fully.SomePackExcludedClass;
import ru.tinkoff.qa.neptune.allure.excluded.inherited.ExcludedByInheritance;
import ru.tinkoff.qa.neptune.allure.excluded.method.SomeExcludedMethodClass;
import ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static io.qameta.allure.Allure.getLifecycle;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.excludeFixtureIfNecessary;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.excludeTestResultIfNecessary;
import static ru.tinkoff.qa.neptune.allure.lifecycle.LifeCycleItemItemStorage.CURRENT_FIXTURE;
import static ru.tinkoff.qa.neptune.allure.lifecycle.LifeCycleItemItemStorage.CURRENT_TEST_RESULT;

public class ExcludedLifeCycleReportingTest {

    private static final TestResultContainer TEST_RESULT_CONTAINER = new TestResultContainer().setUuid(randomUUID().toString()).setName("Root");
    private static final FixtureResult FIXTURE_RESULT = new FixtureResult().setName("Fixture 1");
    private static final TestResult TEST_RESULT = new TestResult().setName("Result 1").setUuid(randomUUID().toString());

    private static final StringBuilder TEST_TEXT = new StringBuilder("ABCD");
    private File fileAttach;
    private BufferedImage imageAttach;
    private final AllureEventLogger allureEventLogger = new AllureEventLogger();

    @Mock
    private AllureEventLogger.StepEventLogger stepEventLogger;

    @BeforeClass
    public void beforeClass() throws Exception {
        openMocks(this);

        fileAttach = Paths.get("src/test/resources/test.json").toFile();
        imageAttach = ImageIO.read(Paths.get("src/test/resources/picture.jpeg").toFile());
        allureEventLogger.setDelegate(stepEventLogger);
    }

    @BeforeMethod
    public void beforeTest() {
        clearInvocations(stepEventLogger);
    }

    @AfterMethod
    public void afterTest() throws Exception {
        var excluded = ItemsToNotBeReported.class.getDeclaredField("EXCLUDED");
        excluded.setAccessible(true);

        ((Set<?>) excluded.get(ItemsToNotBeReported.class)).clear();

        CURRENT_FIXTURE.removeItem();
        CURRENT_TEST_RESULT.removeItem();
    }

    private void verifyThatReportingWasNotInvoked() {
        allureEventLogger.fireTheEventStarting("Start something", Map.of());
        allureEventLogger.fireReturnedValue("Return something", new Object());
        allureEventLogger.fireThrownException(new RuntimeException());
        allureEventLogger.fireEventFinishing();
        allureEventLogger.addParameters(Map.of("Key", "Value"));

        verifyNoInteractions(stepEventLogger);

        try (MockedStatic<Allure> allureStatic = mockStatic(Allure.class)) {
            new AllureFileInjector().inject(fileAttach, "Some attach");
            new AllureImageInjector().inject(imageAttach, "Some attach");
            new AllureStringInjector().inject(TEST_TEXT, "some attach");
            allureStatic.verifyNoInteractions();
        }
    }

    @DataProvider
    public static Object[][] excludedClassesAndMethods()  {
        return new Object[][] {
            {SomePackExcludedClass.class},
            {SomePackExcludedClass.PackageExcludedInnerClass.class},
            {SomeExcludedClass.class},
            {SomeExcludedClass.ExcludedInnerClass.class},
            {SomeExcludedMethodClass.class},
            {ExcludedByInheritance.class},
            {ExcludedByInheritance.InnerExtenderClass.class}
        };
    }

    @Test
    public void noCurrentItemTest() {
        verifyThatReportingWasNotInvoked();
    }

    @Test(dataProvider = "excludedClassesAndMethods")
    public void excludedFixtureTest(Class<?> clazz) throws Exception {
        var fixture = clazz.getMethod("someMethod");
        getLifecycle().startTestContainer(TEST_RESULT_CONTAINER);
        getLifecycle().startPrepareFixture(TEST_RESULT_CONTAINER.getUuid(), randomUUID().toString(), FIXTURE_RESULT);
        excludeFixtureIfNecessary(clazz, fixture);

        verifyThatReportingWasNotInvoked();
    }

    @Test(dataProvider = "excludedClassesAndMethods")
    public void excludedFixtureTestInverted(Class<?> clazz) throws Exception {
        var fixture = clazz.getMethod("someMethod");
        excludeFixtureIfNecessary(clazz, fixture);

        getLifecycle().startTestContainer(TEST_RESULT_CONTAINER);
        getLifecycle().startPrepareFixture(TEST_RESULT_CONTAINER.getUuid(), randomUUID().toString(), FIXTURE_RESULT);

        verifyThatReportingWasNotInvoked();
    }

    @Test(dataProvider = "excludedClassesAndMethods")
    public void excludedTestResult(Class<?> clazz) throws Exception {
        var fixtureUUID = randomUUID().toString();
        var test = clazz.getMethod("someMethod");

        getLifecycle().startTestContainer(TEST_RESULT_CONTAINER);
        getLifecycle().startPrepareFixture(TEST_RESULT_CONTAINER.getUuid(), fixtureUUID, FIXTURE_RESULT);
        getLifecycle().stopFixture(fixtureUUID);
        getLifecycle().scheduleTestCase(TEST_RESULT);
        getLifecycle().startTestCase(TEST_RESULT.getUuid());

        excludeTestResultIfNecessary(clazz, test);
        verifyThatReportingWasNotInvoked();
    }

    @Test(dataProvider = "excludedClassesAndMethods")
    public void excludedTestResultInverted(Class<?> clazz) throws Exception {
        var fixtureUUID = randomUUID().toString();
        var test = clazz.getMethod("someMethod");
        excludeTestResultIfNecessary(clazz, test);

        getLifecycle().startTestContainer(TEST_RESULT_CONTAINER);
        getLifecycle().startPrepareFixture(TEST_RESULT_CONTAINER.getUuid(), fixtureUUID, FIXTURE_RESULT);
        getLifecycle().stopFixture(fixtureUUID);
        getLifecycle().scheduleTestCase(TEST_RESULT);
        getLifecycle().startTestCase(TEST_RESULT.getUuid());

        verifyThatReportingWasNotInvoked();
    }
}

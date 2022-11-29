package ru.tinkoff.qa.neptune.allure.testng.bridge;

import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.FileSystemResultsWriter;
import org.testng.*;
import ru.tinkoff.qa.neptune.allure.lifecycle.NeptuneResultWriter;

import java.nio.file.Paths;

import static io.qameta.allure.Allure.setLifecycle;
import static io.qameta.allure.util.PropertiesUtils.loadAllureProperties;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.excludeFixtureIfNecessary;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.excludeTestResultIfNecessary;

public class NeptuneAllureExcludeTestListener implements IInvokedMethodListener {

    private static final NeptuneResultWriter NEPTUNE_RESULT_WRITER = init();

    private static NeptuneResultWriter init() {
        var properties = loadAllureProperties();
        var path = properties.getProperty("allure.results.directory", "allure-results");
        var allureWriter = new FileSystemResultsWriter(Paths.get(path));
        var neptuneWriter = new NeptuneResultWriter(allureWriter);
        setLifecycle(new AllureLifecycle(neptuneWriter));

        return neptuneWriter;
    }

    static void changeResultWriter(AllureResultsWriter writer) {
        NEPTUNE_RESULT_WRITER.setResultWriter(writer);
    }

    public NeptuneAllureExcludeTestListener() {
        super();
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        var testClazz = testResult.getInstance().getClass();
        if (method.isTestMethod()) {
            excludeTestResultIfNecessary(testClazz, method.getTestMethod().getConstructorOrMethod().getMethod());
        }
        else {
            excludeFixtureIfNecessary(testClazz, method.getTestMethod().getConstructorOrMethod().getMethod());
        }
    }
}

package ru.tinkoff.qa.neptune.allure.jupiter.bridge;

import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.FileSystemResultsWriter;
import org.junit.platform.launcher.TestExecutionListener;
import ru.tinkoff.qa.neptune.allure.lifecycle.NeptuneResultWriter;

import java.nio.file.Paths;

import static io.qameta.allure.Allure.setLifecycle;
import static io.qameta.allure.util.PropertiesUtils.loadAllureProperties;

public class NeptuneAllureExcludeTest implements TestExecutionListener {

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
}

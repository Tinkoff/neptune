package ru.tinkoff.qa.neptune.allure.jupiter.bridge;

import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.FileSystemResultsWriter;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import ru.tinkoff.qa.neptune.allure.lifecycle.NeptuneResultWriter;

import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static io.qameta.allure.Allure.setLifecycle;
import static io.qameta.allure.util.PropertiesUtils.loadAllureProperties;
import static java.util.Optional.ofNullable;

public class NeptuneAllureExcludeTest implements TestExecutionListener {

    private static final NeptuneResultWriter NEPTUNE_RESULT_WRITER = init();
    private static final ThreadLocal<LinkedHashSet<Class<?>>> TEST_CLASS_THREAD_LOCAL = new ThreadLocal<>();

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

    @Override
    public void executionStarted(final TestIdentifier testIdentifier) {
        var source = testIdentifier.getSource().orElse(null);

        if (source instanceof ClassSource) {
            var clazz = ((ClassSource) source).getJavaClass();
            var queue = ofNullable(TEST_CLASS_THREAD_LOCAL.get()).orElseGet(() -> {
                var newQueue = new LinkedHashSet<Class<?>>();
                TEST_CLASS_THREAD_LOCAL.set(newQueue);
                return newQueue;
            });

            if (queue.isEmpty() || !queue.contains(clazz)) {
                queue.add(clazz);
            }
        }
    }

    @Override
    public void executionFinished(final TestIdentifier testIdentifier,
                                  final TestExecutionResult testExecutionResult) {
        var source = testIdentifier.getSource().orElse(null);

        if (source instanceof ClassSource) {
            var queue = TEST_CLASS_THREAD_LOCAL.get();
            if (!queue.isEmpty()) {
                queue.remove(((ClassSource) source).getJavaClass());
            }

            if (queue.isEmpty()) {
                TEST_CLASS_THREAD_LOCAL.remove();
            }
        }
    }

    public static Class<?> getCurrentTestClass() {
        return ofNullable(TEST_CLASS_THREAD_LOCAL.get()).map(set -> {
            if (set.isEmpty()) {
                return null;
            }
            return new LinkedList<>(set).getLast();
        }).orElse(null);
    }
}

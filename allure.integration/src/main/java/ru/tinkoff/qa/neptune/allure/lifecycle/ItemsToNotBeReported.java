package ru.tinkoff.qa.neptune.allure.lifecycle;

import io.qameta.allure.model.FixtureResult;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.model.TestResultContainer;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections.SetUtils.synchronizedSet;
import static ru.tinkoff.qa.neptune.allure.lifecycle.LifeCycleItemItemStorage.*;

/**
 * Stores lifecycle item to be excluded from the report.
 *
 * @see TestResultContainer
 * @see FixtureResult
 * @see TestResult
 */
public final class ItemsToNotBeReported {

    private static final Set<Object> EXCLUDED = synchronizedSet(new HashSet<>());

    private ItemsToNotBeReported() {
        super();
    }

    /**
     * Checks an object that is expected to be excluded
     *
     * @param expectedToBeExcluded object that is expected to be excluded
     * @return {@code true} when checked object is excluded from the report,
     * {@code false} is returned otherwise
     */
    public static boolean isExcludedFromReport(Object expectedToBeExcluded) {
        if ((expectedToBeExcluded instanceof TestResultContainer)
            || (expectedToBeExcluded instanceof TestResult)) {
            return EXCLUDED.contains(expectedToBeExcluded);
        }

        var fixture = (FixtureResult) expectedToBeExcluded;
        return new ArrayList<>(EXCLUDED)
            .stream()
            .anyMatch(o -> ((o instanceof ExcludedFixtureWrapper) && (((ExcludedFixtureWrapper) o).isThisFixtureExcluded(fixture))));
    }

    /**
     * @return list of {@link TestResultContainer}/{@link TestResult} UUIDs to be excluded from the report
     */
    public static List<String> getExcludedUUIDs() {
        return new ArrayList<>(EXCLUDED).stream()
            .filter(o -> (o instanceof TestResultContainer) || (o instanceof TestResult))
            .map(o -> {
                if (o instanceof TestResultContainer) {
                    return ((TestResultContainer) o).getUuid();
                }

                return ((TestResult) o).getUuid();
            }).collect(toList());
    }

    public static boolean toReport() {
        var currentItem = getCurrentLifecycleItem();
        if (isNull(currentItem)) {
            return false;
        }
        return !isExcludedFromReport(currentItem);
    }

    private static <T, R> void excludeIfNecessary(LifeCycleItemItemStorage<T> storage,
                                                  Class<?> clazz,
                                                  Method method,
                                                  Function<T, R> transformation) {
        storage.setCheck(t -> {
            if (!isClassExcluded(clazz) && !isMethodExcluded(method)) {
                return;
            }

            EXCLUDED.add(transformation.apply(t));
        });
    }

    public static void excludeFixtureIfNecessary(Class<?> clazz, Method method) {
        excludeIfNecessary(CURRENT_FIXTURE, clazz, method, ExcludedFixtureWrapper::new);
    }

    public static void excludeTestResultIfNecessary(Class<?> clazz, Method method) {
        excludeIfNecessary(CURRENT_TEST_RESULT, clazz, method, tr -> tr);
    }

    static void excludeTestResultContainer(TestResultContainer toBeExcluded) {
        EXCLUDED.add(toBeExcluded);
    }

    private static boolean isClassExcluded(Class<?> toExclude) {
        if (toExclude.getPackage().isAnnotationPresent(ExcludeFromAllureReport.class)) {
            return true;
        }

        var clazz = toExclude;
        while (nonNull(clazz)) {
            if (clazz.isAnnotationPresent(ExcludeFromAllureReport.class)) {
                return true;
            }
            clazz = clazz.getDeclaringClass();
        }
        return false;
    }

    private static boolean isMethodExcluded(Method method) {
        return method.isAnnotationPresent(ExcludeFromAllureReport.class)
            || isClassExcluded(method.getDeclaringClass());
    }

    public static final class ExcludedFixtureWrapper {

        private final FixtureResult fixtureResult;

        public ExcludedFixtureWrapper(FixtureResult fixtureResult) {
            this.fixtureResult = fixtureResult;
        }

        boolean isThisFixtureExcluded(FixtureResult fixtureResult) {
            return this.fixtureResult == fixtureResult;
        }

        public FixtureResult getFixtureResult() {
            return fixtureResult;
        }
    }
}

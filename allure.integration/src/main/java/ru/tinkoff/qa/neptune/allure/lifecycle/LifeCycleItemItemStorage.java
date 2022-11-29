package ru.tinkoff.qa.neptune.allure.lifecycle;

import io.qameta.allure.model.FixtureResult;
import io.qameta.allure.model.TestResult;

import java.util.function.Consumer;

import static io.qameta.allure.model.Stage.RUNNING;
import static java.util.Objects.nonNull;

/**
 * Keeps current lifecycle item and currently being executed test class and test/fixture method
 *
 * @see FixtureResult
 * @see TestResult
 */
public final class LifeCycleItemItemStorage<T> {

    public static final LifeCycleItemItemStorage<FixtureResult> CURRENT_FIXTURE = new LifeCycleItemItemStorage<>();

    public static final LifeCycleItemItemStorage<TestResult> CURRENT_TEST_RESULT = new LifeCycleItemItemStorage<>();

    final ThreadLocal<T> itemThreadLocal = new ThreadLocal<>();
    private ThreadLocal<T> isWaitingForCheck = new ThreadLocal<>();
    private ThreadLocal<Consumer<T>> check = new ThreadLocal<>();

    private LifeCycleItemItemStorage() {
        super();
    }

    private void check() {
        var t = isWaitingForCheck.get();
        var consumer = check.get();

        if (nonNull(t) && nonNull(consumer)) {
            consumer.accept(t);
            isWaitingForCheck.remove();
            check.remove();
        }
    }

    public void setItem(T item) {
        itemThreadLocal.set(item);
        isWaitingForCheck.set(item);
        check();
    }

    void setCheck(Consumer<T> check) {
        this.check.set(check);
        check();
    }

    public void removeItem() {
        itemThreadLocal.remove();
    }

    public static Object getCurrentLifecycleItem() {
        var fixtureResult = CURRENT_FIXTURE.itemThreadLocal.get();

        if (nonNull(fixtureResult) && (fixtureResult.getStage().equals(RUNNING))) {
            return fixtureResult;
        }

        var testResult = CURRENT_TEST_RESULT.itemThreadLocal.get();
        if (nonNull(testResult) && testResult.getStage().equals(RUNNING)) {
            return testResult;
        }

        return null;
    }
 }

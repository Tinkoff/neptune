package ru.tinkoff.qa.neptune.allure.lifecycle;

import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;

import static ru.tinkoff.qa.neptune.allure.lifecycle.LifeCycleItemItemStorage.CURRENT_TEST_RESULT;

public class NeptuneTestLifecycleListener implements TestLifecycleListener {

    @Override
    public void afterTestStart(TestResult result) {
        CURRENT_TEST_RESULT.setItem(result);
    }

    @Override
    public void afterTestStop(TestResult result) {
        CURRENT_TEST_RESULT.removeItem();
    }
}

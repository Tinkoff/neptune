package ru.tinkoff.qa.neptune.allure.lifecycle;

import io.qameta.allure.listener.FixtureLifecycleListener;
import io.qameta.allure.model.FixtureResult;


import static ru.tinkoff.qa.neptune.allure.lifecycle.LifeCycleItemItemStorage.CURRENT_FIXTURE;

public class NeptuneFixtureLifecycleListener implements FixtureLifecycleListener {

    @Override
    public void afterFixtureStart(FixtureResult result) {
        CURRENT_FIXTURE.setItem(result);
    }

    @Override
    public void beforeFixtureStop(FixtureResult result) {
        CURRENT_FIXTURE.removeItem();
    }
}

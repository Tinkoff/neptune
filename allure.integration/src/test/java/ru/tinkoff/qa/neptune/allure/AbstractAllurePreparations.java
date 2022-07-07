package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.internal.AllureStorage;
import io.qameta.allure.model.TestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.UUID;

import static io.qameta.allure.Allure.getLifecycle;
import static java.util.UUID.randomUUID;

public class AbstractAllurePreparations {

    final AllureLifecycle lifeCycle = getLifecycle();
    final UUID testCaseUUID = randomUUID();
    AllureStorage storage;

    private static AllureStorage getAllureStorage(AllureLifecycle lifecycle) throws Exception {
        var field = lifecycle.getClass().getDeclaredField("storage");
        field.setAccessible(true);
        return (AllureStorage) field.get(lifecycle);
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        storage = getAllureStorage(lifeCycle);
        storage.put(testCaseUUID.toString(), new TestResult());
        lifeCycle.startTestCase(testCaseUUID.toString());
    }

    @AfterClass
    public void afterClass() {
        lifeCycle.stopTestCase(testCaseUUID.toString());
    }
}

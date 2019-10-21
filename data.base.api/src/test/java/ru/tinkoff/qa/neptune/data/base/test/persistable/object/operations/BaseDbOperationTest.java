package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.testng.annotations.BeforeSuite;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;

public abstract class BaseDbOperationTest {

    private static final String TEST_BASE = "TestBase";
    private static final String TEST_BASE2 = "TestBase2";
    static File testDB1;
    static File testDB2;
    protected static DataBaseStepContext dataBaseSteps;

    @BeforeSuite
    public void copyDB() throws IOException {
        ClassLoader classLoader = currentThread().getContextClassLoader();
        File db1 = new File(classLoader.getResource(format("%s.db", TEST_BASE)).getFile());
        File db2 = new File(classLoader.getResource(format("%s.db", TEST_BASE2)).getFile());
        Files.copy(db1.toPath(), (testDB1 = new File(format("%s.db", TEST_BASE))).toPath(), REPLACE_EXISTING);
        Files.copy(db2.toPath(), (testDB2 = new File(format("%s.db", TEST_BASE2))).toPath(), REPLACE_EXISTING);
        dataBaseSteps = getProxied(DataBaseStepContext.class);
        testDB1.deleteOnExit();
        testDB2.deleteOnExit();
    }
}

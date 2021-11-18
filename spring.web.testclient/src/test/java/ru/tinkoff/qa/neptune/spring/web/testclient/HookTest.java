package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

public class HookTest extends BaseTest {

    private static final PseudoTest PSEUDO_TEST = new PseudoTest();
    private static final SpringWebTestClientExecutionHook HOOK = new SpringWebTestClientExecutionHook();

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {true},
                {false}
        };
    }

    @Test(dataProvider = "data", expectedExceptions = IllegalStateException.class)
    public void test1(boolean isTest) throws Exception {
        PSEUDO_TEST.setWebTestClient(null);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        PSEUDO_TEST.test1();
        fail("Exception was expected");
    }

    @Test(dataProvider = "data")
    public void test2(boolean isTest) throws Exception {
        PSEUDO_TEST.setWebTestClient(client);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        assertThat(PSEUDO_TEST.test1(), is(client));
    }

    @Test(dataProvider = "data", dependsOnMethods = "test2")
    public void test3(boolean isTest) throws Exception {
        PSEUDO_TEST.setWebTestClient(client);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        assertThat(PSEUDO_TEST.test2(), arrayWithSize(20));
    }
}

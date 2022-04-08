package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.ofclass.OfClassMatcher.isObjectOfClass;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilderFactory.PUT;

public class NegativeTests {

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {"file://some/file", IllegalArgumentException.class, "Invalid URI file://some/file. invalid URI scheme file;"},
                {"https:/", IllegalArgumentException.class, "Invalid URI https:/. empty host URI https:/;"},
                {"/some/path", IllegalStateException.class, "Value of the property END_POINT_OF_TARGET_API is not defined"},
        };
    }

    @Test(dataProvider = "data")
    public void invalidStringURITest(String uri, Class<? extends Exception> exceptionClass, String message) {
        try {
            http().responseOf(PUT().endPoint(uri));
        } catch (Exception e) {
            assertThat(e, isObjectOfClass(exceptionClass));
            assertThat(e, throwableHasMessage(message));
            return;
        }

        fail("Exception was expected");
    }
}

package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.ofclass.OfClassMatcher.isObjectOfClass;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilderFactory.PUT;

public class URITests {

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {"file://some/file", "Invalid URI file://some/file. invalid URI scheme file;"},
                {"https:/", "Invalid URI https:/. empty host URI;"},
                {"/some/path", "Invalid URI /some/path. empty URI scheme;empty host URI;"},
        };
    }

    @DataProvider
    public static Object[][] data2() throws Exception {
        return new Object[][]{
                {"http://my.api.com/some/path?q1=val1&q2=val2", "http://my.api.com/some/path?q1=val1&q2=val2"},
                {URI.create("http://my.api.com/some/path?q1=val1&q2=val2"), "http://my.api.com/some/path?q1=val1&q2=val2"},
                {new URL("http://my.api.com/some/path?q1=val1&q2=val2"), "http://my.api.com/some/path?q1=val1&q2=val2"},
                {"http://my.api.com/some/path?q1=val1&q2=АБВ", "http://my.api.com/some/path?q1=val1&q2=АБВ"},
                {URI.create("http://my.api.com/some/path?q1=val1&q2=АБВ"), "http://my.api.com/some/path?q1=val1&q2=АБВ"},
                {new URL("http://my.api.com/some/path?q1=val1&q2=АБВ"), "http://my.api.com/some/path?q1=val1&q2=АБВ"},
        };
    }

    @Test(dataProvider = "data")
    public void invalidStringURITest(String uri, String message) {
        try {
            PUT().baseURI(uri).build();
        } catch (Exception e) {
            assertThat(e, isObjectOfClass(IllegalArgumentException.class));
            assertThat(e, throwableHasMessage(message));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void emptyBaseURITest() {
        try {
            PUT().relativePath("/some/path").build();
        } catch (Exception e) {
            assertThat(e, isObjectOfClass(IllegalStateException.class));
            assertThat(e, throwableHasMessage("Base end point URI and value of the property END_POINT_OF_TARGET_API are not defined"));
            return;
        }

        fail("Exception was expected");
    }

    @Test(dataProvider = "data2")
    public void abilityToDefineFullUrl(Object object, String expected) {
        String uri;
        if (object instanceof URI) {
            uri = PUT().baseURI((URI) object).build().uri().toString();
        } else if (object instanceof URL) {
            uri = PUT().baseURI((URL) object).build().uri().toString();
        } else {
            uri = PUT().baseURI((String) object).build().uri().toString();
        }

        assertThat(uri, is(expected));
    }
}

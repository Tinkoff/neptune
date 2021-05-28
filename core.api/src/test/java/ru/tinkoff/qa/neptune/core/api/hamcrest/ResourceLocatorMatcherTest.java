package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasReferenceMatcher;

import java.net.URI;

import static java.net.URI.create;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.urlHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.urlHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.urlHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasProtocolMatcher.urlHasProtocol;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryMatcher.uriHasQuery;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryMatcher.urlHasQuery;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasReferenceMatcher.urlHasReference;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasUserInfoMatcher.uriHasUserInfo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasUserInfoMatcher.urlHasUserInfo;

public class ResourceLocatorMatcherTest {

    private static final URI testURI = create("https://user:password@www.google.com:666/search?q=how+to+create+e2e+test#test");

    @DataProvider
    public static Object[][] data1() throws Exception {
        return new Object[][]{
                {testURI, uriHasHost("www.google.com")},
                {testURI, uriHasHost(containsString("google"))},
                {testURI.toURL(), urlHasHost("www.google.com")},
                {testURI.toURL(), urlHasHost(containsString("google"))},

                {testURI, uriHasPath("/search")},
                {testURI, uriHasPath(containsString("search"))},
                {testURI.toURL(), urlHasPath("/search")},
                {testURI.toURL(), urlHasPath(containsString("search"))},

                {testURI, uriHasPort(666)},
                {testURI, uriHasPort(greaterThan(665))},
                {testURI.toURL(), urlHasPort(666)},
                {testURI.toURL(), urlHasPort(greaterThan(665))},

                {testURI, uriHasScheme("https")},
                {testURI.toURL(), urlHasProtocol("https")},

                {testURI, uriHasQuery("q=how+to+create+e2e+test")},
                {testURI.toURL(), urlHasQuery("q=how+to+create+e2e+test")},

                {testURI.toURL(), urlHasReference("test")},

                {testURI, uriHasUserInfo("user:password")},
                {testURI.toURL(), urlHasUserInfo("user:password")},

        };
    }

    @DataProvider
    public static Object[][] data2() throws Exception {
        return new Object[][]{
                {testURI, uriHasHost("www.github.com"), "Tested resource locator\n" +
                        "Expected: Host is \"www.github.com\"\n" +
                        "     but: was \"www.google.com\""},
                {testURI, uriHasHost(containsString("github")), "Tested resource locator\n" +
                        "Expected: Host is a string containing \"github\"\n" +
                        "     but: was \"www.google.com\""},
                {testURI.toURL(), urlHasHost("www.github.com"), "Tested resource locator\n" +
                        "Expected: Host is \"www.github.com\"\n" +
                        "     but: was \"www.google.com\""},
                {testURI.toURL(), urlHasHost(containsString("github")), "Tested resource locator\n" +
                        "Expected: Host is a string containing \"github\"\n" +
                        "     but: was \"www.google.com\""},
                {1, uriHasHost("www.github.com"), "Tested resource locator\n" +
                        "Expected: Host is \"www.github.com\"\n     " +
                        "but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URI\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {1, urlHasHost(containsString("github")), "Tested resource locator\n" +
                        "Expected: Host is a string containing \"github\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URL\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {null, uriHasHost("www.github.com"), "Tested resource locator\n" +
                        "Expected: Host is \"www.github.com\"\n" +
                        "     but: Null value. All checks were stopped"},
                {null, urlHasHost(containsString("github")), "Tested resource locator\n" +
                        "Expected: Host is a string containing \"github\"\n" +
                        "     but: Null value. All checks were stopped"},


                {testURI, uriHasPath("www.github.com"), "Tested resource locator\n" +
                        "Expected: Path is \"www.github.com\"\n" +
                        "     but: was \"/search\""},
                {testURI, uriHasPath(containsString("github")), "Tested resource locator\n" +
                        "Expected: Path is a string containing \"github\"\n" +
                        "     but: was \"/search\""},
                {testURI.toURL(), urlHasPath("www.github.com"), "Tested resource locator\n" +
                        "Expected: Path is \"www.github.com\"\n" +
                        "     but: was \"/search\""},
                {testURI.toURL(), urlHasPath(containsString("github")), "Tested resource locator\n" +
                        "Expected: Path is a string containing \"github\"\n" +
                        "     but: was \"/search\""},
                {1, uriHasPath("www.github.com"), "Tested resource locator\n" +
                        "Expected: Path is \"www.github.com\"\n     " +
                        "but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URI\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {1, urlHasPath(containsString("github")), "Tested resource locator\n" +
                        "Expected: Path is a string containing \"github\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URL\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {null, uriHasPath("www.github.com"), "Tested resource locator\n" +
                        "Expected: Path is \"www.github.com\"\n" +
                        "     but: Null value. All checks were stopped"},
                {null, urlHasPath(containsString("github")), "Tested resource locator\n" +
                        "Expected: Path is a string containing \"github\"\n" +
                        "     but: Null value. All checks were stopped"},


                {testURI, uriHasPath("www.github.com"), "Tested resource locator\n" +
                        "Expected: Path is \"www.github.com\"\n" +
                        "     but: was \"/search\""},
                {testURI, uriHasPath(containsString("github")), "Tested resource locator\n" +
                        "Expected: Path is a string containing \"github\"\n" +
                        "     but: was \"/search\""},
                {testURI.toURL(), urlHasPath("www.github.com"), "Tested resource locator\n" +
                        "Expected: Path is \"www.github.com\"\n" +
                        "     but: was \"/search\""},
                {testURI.toURL(), urlHasPath(containsString("github")), "Tested resource locator\n" +
                        "Expected: Path is a string containing \"github\"\n" +
                        "     but: was \"/search\""},
                {1, uriHasPath("www.github.com"), "Tested resource locator\n" +
                        "Expected: Path is \"www.github.com\"\n     " +
                        "but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URI\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {1, urlHasPath(containsString("github")), "Tested resource locator\n" +
                        "Expected: Path is a string containing \"github\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URL\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {null, uriHasPath("www.github.com"), "Tested resource locator\n" +
                        "Expected: Path is \"www.github.com\"\n" +
                        "     but: Null value. All checks were stopped"},
                {null, urlHasPath(containsString("github")), "Tested resource locator\n" +
                        "Expected: Path is a string containing \"github\"\n" +
                        "     but: Null value. All checks were stopped"},


                {testURI, uriHasPort(667), "Tested resource locator\n" +
                        "Expected: Port is <667>\n" +
                        "     but: was <666>"},
                {testURI, uriHasPort(lessThan(666)), "Tested resource locator\n" +
                        "Expected: Port is a value less than <666>\n" +
                        "     but: <666> was equal to <666>"},
                {testURI.toURL(), urlHasPort(667), "Tested resource locator\n" +
                        "Expected: Port is <667>\n" +
                        "     but: was <666>"},
                {testURI.toURL(), urlHasPort(lessThan(666)), "Tested resource locator\n" +
                        "Expected: Port is a value less than <666>\n" +
                        "     but: <666> was equal to <666>"},
                {1, uriHasPort(667), "Tested resource locator\n" +
                        "Expected: Port is <667>\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URI\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {1, urlHasPort(lessThan(666)), "Tested resource locator\n" +
                        "Expected: Port is a value less than <666>\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URL\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {null, uriHasPort(667), "Tested resource locator\n" +
                        "Expected: Port is <667>\n" +
                        "     but: Null value. All checks were stopped"},
                {null, urlHasPort(lessThan(666)), "Tested resource locator\n" +
                        "Expected: Port is a value less than <666>\n" +
                        "     but: Null value. All checks were stopped"},


                {testURI, uriHasScheme("http"), "Tested resource locator\n" +
                        "Expected: Scheme is \"http\"\n" +
                        "     but: was \"https\""},
                {testURI.toURL(), urlHasProtocol("http"), "Tested resource locator\n" +
                        "Expected: Protocol is \"http\"\n" +
                        "     but: was \"https\""},
                {1, uriHasScheme("http"), "Tested resource locator\n" +
                        "Expected: Scheme is \"http\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URI\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {1, urlHasProtocol("http"), "Tested resource locator\n" +
                        "Expected: Protocol is \"http\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URL\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {null, uriHasScheme("http"), "Tested resource locator\n" +
                        "Expected: Scheme is \"http\"\n" +
                        "     but: Null value. All checks were stopped"},
                {null, urlHasProtocol("http"), "Tested resource locator\n" +
                        "Expected: Protocol is \"http\"\n" +
                        "     but: Null value. All checks were stopped"},


                {testURI, uriHasQuery("e2e"), "Tested resource locator\n" +
                        "Expected: Query is \"e2e\"\n" +
                        "     but: was \"q=how+to+create+e2e+test\""},
                {testURI.toURL(), urlHasQuery("e2e"), "Tested resource locator\n" +
                        "Expected: Query is \"e2e\"\n" +
                        "     but: was \"q=how+to+create+e2e+test\""},
                {1, uriHasQuery("e2e"), "Tested resource locator\n" +
                        "Expected: Query is \"e2e\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URI\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {1, urlHasQuery("e2e"), "Tested resource locator\n" +
                        "Expected: Query is \"e2e\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URL\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {null, uriHasQuery("e2e"), "Tested resource locator\n" +
                        "Expected: Query is \"e2e\"\n" +
                        "     but: Null value. All checks were stopped"},
                {null, urlHasQuery("e2e"), "Tested resource locator\n" +
                        "Expected: Query is \"e2e\"\n" +
                        "     but: Null value. All checks were stopped"},


                {testURI.toURL(), HasReferenceMatcher.urlHasReference("e2e"), "Tested resource locator\n" +
                        "Expected: Url reference is \"e2e\"\n" +
                        "     but: was \"test\""},
                {1, HasReferenceMatcher.urlHasReference("e2e"), "Tested resource locator\n" +
                        "Expected: Url reference is \"e2e\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URL\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {null, HasReferenceMatcher.urlHasReference("e2e"), "Tested resource locator\n" +
                        "Expected: Url reference is \"e2e\"\n" +
                        "     but: Null value. All checks were stopped"},


                {testURI, uriHasUserInfo("user"), "Tested resource locator\n" +
                        "Expected: User info is \"user\"\n" +
                        "     but: was \"user:password\""},
                {testURI.toURL(), urlHasUserInfo("user"), "Tested resource locator\n" +
                        "Expected: User info is \"user\"\n" +
                        "     but: was \"user:password\""},
                {1, uriHasUserInfo("user"), "Tested resource locator\n" +
                        "Expected: User info is \"user\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URI\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {1, urlHasUserInfo("user"), "Tested resource locator\n" +
                        "Expected: User info is \"user\"\n" +
                        "     but: Type mismatch. Object of class that equals or extends following types was expected: \r\n" +
                        "class java.net.URL\r\n" +
                        "Class of passed value is `class java.lang.Integer`. All checks were stopped"},
                {null, uriHasUserInfo("user"), "Tested resource locator\n" +
                        "Expected: User info is \"user\"\n" +
                        "     but: Null value. All checks were stopped"},
                {null, urlHasUserInfo("user"), "Tested resource locator\n" +
                        "Expected: User info is \"user\"\n" +
                        "     but: Null value. All checks were stopped"},
        };
    }

    @Test(dataProvider = "data1")
    public void test1(Object resourceLocator, Matcher<Object> matcher) {
        assertThat("Tested resource locator", resourceLocator, matcher);
    }

    @Test(dataProvider = "data2")
    public void test2(Object resourceLocator, Matcher<Object> matcher, String errorText) {
        try {
            assertThat("Tested resource locator", resourceLocator, matcher);
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(errorText));
            return;
        }

        fail("Exception was expected");
    }
}

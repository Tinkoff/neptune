package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.cookies.CommonHttpCookieCriteria.*;

public class CookieTest extends BaseHttpTest {

    @DataProvider
    public static Object[][] uris() throws Exception {
        return new Object[][]{
            {new URI("http://google.com")},
            {new URL("http://google.com")},
            {"http://google.com"}
        };
    }

    @BeforeMethod
    public void beforeEachMethod() {
        ((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
            .getCookieStore()
            .removeAll();
    }

    @Test
    public void addCookieTestSimple() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");

        httpCookie.setComment("Some comment");
        httpCookie.setHttpOnly(true);
        httpCookie.setDomain("google.com");
        httpCookie.setSecure(true);
        httpCookie.setDiscard(true);
        httpCookie.setPath("/somePath");

        httpCookie2.setComment("Some comment");
        httpCookie2.setHttpOnly(true);
        httpCookie2.setDomain("google.com");
        httpCookie2.setSecure(true);
        httpCookie2.setDiscard(true);
        httpCookie2.setPath("/somePath");

        http().addCookies(httpCookie, httpCookie2);
        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            contains(httpCookie, httpCookie2));

        assertThat(http().getCookies(httpCookieNameMatches("TestSetUpCookieName"),
                httpCookieValueMatches("TestSetUpCookieValue"),
                httpCookieComment("Some comment"),
                httpCookieIsHttpOnly(),
                httpCookieDomain("google.com"),
                httpCookieIsSecure(),
                httpCookieToDiscard(),
                httpCookiePath("/somePath")),
            contains(httpCookie, httpCookie2));
    }

    @Test
    public void addCookieTestList() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        httpCookie.setComment("Some comment 1");
        httpCookie.setHttpOnly(true);
        httpCookie.setDomain("google.com1");
        httpCookie.setSecure(true);
        httpCookie.setDiscard(true);
        httpCookie.setPath("/somePath1");

        httpCookie2.setComment("Some comment2");
        httpCookie2.setHttpOnly(true);
        httpCookie2.setDomain("google.com2");
        httpCookie2.setSecure(true);
        httpCookie2.setDiscard(true);
        httpCookie2.setPath("/somePath2");

        http().addCookies(List.of(httpCookie, httpCookie2));
        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            contains(httpCookie, httpCookie2));

        assertThat(http().getCookies(httpCookieName("TestSetUpCookieName"),
                httpCookieValue("TestSetUpCookieValue"),
                httpCookieCommentMatches("Some comment"),
                httpCookieIsHttpOnly(),
                httpCookieDomainMatches("google.com"),
                httpCookieIsSecure(),
                httpCookieToDiscard(),
                httpCookiePathMatches("/somePath")),
            contains(httpCookie, httpCookie2));
    }

    @Test
    public void addCookieTestString() {
        var httpCookie = new HttpCookie("TestSetUpCookieName3",
            "TestSetUpCookieValue3");

        http().addCookies(httpCookie.toString());
        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            hasItem(httpCookie));

        assertThat(http().getCookies(),
            contains(httpCookie));
    }

    @Test(dataProvider = "uris")
    public void addCookiesSimpleWithURI(Object uri) throws Exception {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");

        httpCookie.setPortlist("1000, 2000, 3000");
        httpCookie.setCommentURL("Some comment");

        httpCookie2.setPortlist("1000, 2000, 3000");
        httpCookie2.setCommentURL("Some comment");

        if (uri instanceof URI) {
            http().addCookies((URI) uri, httpCookie, httpCookie2);
        } else if (uri instanceof URL) {
            http().addCookies((URL) uri, httpCookie, httpCookie2);
        } else {
            http().addCookies((String) uri, httpCookie, httpCookie2);
        }

        if (uri instanceof URI) {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get((URI) uri),
                contains(httpCookie, httpCookie2));

            assertThat(http().getCookies((URI) uri,
                    httpCookieNameMatches("TestSetUpCookieName"),
                    httpCookieValueMatches("TestSetUpCookieValue"),
                    httpCookiePortList("1000, 2000, 3000"),
                    httpCookieURLComment("Some comment")),
                contains(httpCookie, httpCookie2));
        } else if (uri instanceof URL) {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get(((URL) uri).toURI()),
                contains(httpCookie, httpCookie2));

            assertThat(http().getCookies((URL) uri,
                    httpCookieNameMatches("TestSetUpCookieName"),
                    httpCookieValueMatches("TestSetUpCookieValue"),
                    httpCookiePortList("1000, 2000, 3000"),
                    httpCookieURLComment("Some comment")),
                contains(httpCookie, httpCookie2));
        } else {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get(URI.create((String) uri)),
                contains(httpCookie, httpCookie2));

            assertThat(http().getCookies((String) uri,
                    httpCookieNameMatches("TestSetUpCookieName"),
                    httpCookieValueMatches("TestSetUpCookieValue"),
                    httpCookiePortList("1000, 2000, 3000"),
                    httpCookieURLComment("Some comment")),
                contains(httpCookie, httpCookie2));
        }
    }

    @Test(dataProvider = "uris")
    public void addCookiesListWithURI(Object uri) throws Exception {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");

        httpCookie.setPortlist("1100, 2000, 3100");
        httpCookie.setCommentURL("Some comment1");

        httpCookie2.setPortlist("1200, 2000, 3200");
        httpCookie2.setCommentURL("Some comment2");

        if (uri instanceof URI) {
            http().addCookies((URI) uri, List.of(httpCookie, httpCookie2));
        } else if (uri instanceof URL) {
            http().addCookies((URL) uri, List.of(httpCookie, httpCookie2));
        } else {
            http().addCookies((String) uri, List.of(httpCookie, httpCookie2));
        }

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            contains(httpCookie, httpCookie2));

        if (uri instanceof URI) {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get((URI) uri),
                contains(httpCookie, httpCookie2));

            assertThat(http().getCookies((URI) uri,
                    httpCookieNameMatches("TestSetUpCookieName"),
                    httpCookieValueMatches("TestSetUpCookieValue"),
                    httpCookiePortListMatches("2000"),
                    httpCookieURLCommentMatches("Some comment")),
                contains(httpCookie, httpCookie2));
        } else if (uri instanceof URL) {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get(((URL) uri).toURI()),
                contains(httpCookie, httpCookie2));

            assertThat(http().getCookies((URL) uri,
                    httpCookieNameMatches("TestSetUpCookieName"),
                    httpCookieValueMatches("TestSetUpCookieValue"),
                    httpCookiePortListMatches("2000"),
                    httpCookieURLCommentMatches("Some comment")),
                contains(httpCookie, httpCookie2));
        } else {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get(URI.create((String) uri)),
                contains(httpCookie, httpCookie2));

            assertThat(http().getCookies((String) uri,
                    httpCookieNameMatches("TestSetUpCookieName"),
                    httpCookieValueMatches("TestSetUpCookieValue"),
                    httpCookiePortListMatches("2000"),
                    httpCookieURLCommentMatches("Some comment")),
                contains(httpCookie, httpCookie2));
        }
    }

    @Test(dataProvider = "uris")
    public void addCookiesStringWithURI(Object uri) throws Exception {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        if (uri instanceof URI) {
            http().addCookies((URI) uri, httpCookie.toString());
        } else if (uri instanceof URL) {
            http().addCookies((URL) uri, httpCookie.toString());
        } else {
            http().addCookies((String) uri, httpCookie.toString());
        }

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            contains(httpCookie));

        if (uri instanceof URI) {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get((URI) uri),
                contains(httpCookie));

            assertThat(http().getCookies((URI) uri),
                contains(httpCookie));
        } else if (uri instanceof URL) {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get(((URL) uri).toURI()),
                contains(httpCookie));

            assertThat(http().getCookies((URL) uri),
                contains(httpCookie));
        } else {
            assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                    .getCookieStore()
                    .get(URI.create((String) uri)),
                contains(httpCookie));

            assertThat(http().getCookies((String) uri),
                contains(httpCookie));
        }
    }

    @Test
    public void clearCookiesTest() {
        var httpCookie = new HttpCookie("TestSetUpCookieName4",
            "TestSetUpCookieValue4");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");


        var httpCookie3 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");


        http().addCookies(httpCookie, httpCookie2)
            .addCookies("https://google.com", httpCookie3)
            .removeCookies();

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            emptyIterable());
    }

    @Test
    public void clearCookiesTest2() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");

        http().addCookies(httpCookie, httpCookie2);
        http().removeCookies(httpCookieValue("TestSetUpCookieValue"));
        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            contains(httpCookie2));
    }

    @Test
    public void clearCookiesTest3() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");


        var httpCookie3 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");


        http().addCookies(httpCookie, httpCookie2)
            .addCookies("https://google.com", httpCookie3)
            .removeCookies(httpCookie, httpCookie2, httpCookie3);

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            emptyIterable());
    }

    @Test
    public void clearCookiesTest4() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");


        var httpCookie3 = new HttpCookie("TestSetUpCookieName3",
            "TestSetUpCookieValue3");

        var httpCookie4 = new HttpCookie("TestSetUpCookieName4",
            "TestSetUpCookieValue4");


        http().addCookies(httpCookie, httpCookie2)
            .addCookies("https://google.com", httpCookie3, httpCookie4)
            .removeCookies(URI.create("https://google.com"));

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            contains(httpCookie, httpCookie2));

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .get(URI.create("https://google.com")),
            emptyIterable());
    }

    @Test
    public void clearCookiesTest5() {
        var httpCookie = new HttpCookie("TestSetUpCookieName4",
            "TestSetUpCookieValue4");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");


        var httpCookie3 = new HttpCookie("TestSetUpCookieName3",
            "TestSetUpCookieValue3");


        http().addCookies(httpCookie, httpCookie2)
            .addCookies("https://google.com", httpCookie3)
            .removeCookies(List.of(httpCookie, httpCookie2, httpCookie3));

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            emptyIterable());
    }

    @Test
    public void clearCookiesTest6() {
        var httpCookie = new HttpCookie("TestSetUpCookieName4",
            "TestSetUpCookieValue4");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");


        var httpCookie3 = new HttpCookie("TestSetUpCookieName3",
            "TestSetUpCookieValue3");


        http().addCookies(httpCookie, httpCookie2)
            .addCookies("https://google.com", httpCookie3)
            .removeCookies(httpCookie3.toString());

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            contains(httpCookie, httpCookie2));
    }

    @Test
    public void clearCookiesTest7() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        var httpCookie2 = new HttpCookie("TestSetUpCookieName2",
            "TestSetUpCookieValue2");


        var httpCookie3 = new HttpCookie("TestSetUpCookieName3",
            "TestSetUpCookieValue3");

        var httpCookie4 = new HttpCookie("TestSetUpCookieName4",
            "TestSetUpCookieValue4");


        http().addCookies(httpCookie, httpCookie2)
            .addCookies("https://google.com", httpCookie3, httpCookie4)
            .removeCookies(URI.create("https://google.com"), httpCookieName("TestSetUpCookieName4"));

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .getCookies(),
            containsInAnyOrder(httpCookie, httpCookie2, httpCookie3));

        assertThat(((CookieManager) http().getCurrentClient().cookieHandler().orElseThrow())
                .getCookieStore()
                .get(URI.create("https://google.com")),
            contains(httpCookie3));
    }
}

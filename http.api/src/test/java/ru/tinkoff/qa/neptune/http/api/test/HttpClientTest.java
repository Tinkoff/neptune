package ru.tinkoff.qa.neptune.http.api.test;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.properties.authentification.DefaultHttpAuthenticatorProperty;
import ru.tinkoff.qa.neptune.http.api.properties.cookies.DefaultHttpCookieManagerProperty;
import ru.tinkoff.qa.neptune.http.api.properties.executor.DefaultHttpExecutorProperty;
import ru.tinkoff.qa.neptune.http.api.properties.proxy.DefaultHttpProxySelectorProperty;
import ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslContextProperty;
import ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslParametersProperty;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.security.NoSuchAlgorithmException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executor;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.valueOf;
import static java.lang.System.getProperties;
import static java.lang.System.setProperty;
import static java.net.URLEncoder.encode;
import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.Redirect.NEVER;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.of;
import static java.time.Duration.ofSeconds;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.cookies.CommonHttpCookieCriteria.httpCookieValue;
import static ru.tinkoff.qa.neptune.http.api.properties.authentification.DefaultHttpAuthenticatorProperty.DEFAULT_HTTP_AUTHENTICATOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.cookies.DefaultHttpCookieManagerProperty.DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.executor.DefaultHttpExecutorProperty.DEFAULT_HTTP_EXECUTOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.priority.DefaultHttpPriorityProperty.DEFAULT_HTTP_PRIORITY_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.protocol.version.DefaultHttpProtocolVersionProperty.DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.proxy.DefaultHttpProxySelectorProperty.DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.redirect.DefaultHttpRedirectProperty.DEFAULT_HTTP_REDIRECT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslContextProperty.DEFAULT_HTTP_SSL_CONTEXT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslParametersProperty.DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.DefaultConnectTimeOutUnitProperty.DEFAULT_CONNECT_TIME_UNIT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.DefaultConnectTimeOutValueProperty.DEFAULT_CONNECT_TIME_VALUE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilderFactory.GET;

public class HttpClientTest extends BaseHttpTest {

    private static final ChronoUnit DEFAULT_CONNECT_CHRONO_UNIT = MINUTES;
    private static final long DEFAULT_CONNECT_TIME_VALUE = 2;

    private static final Authenticator DEFAULT_AUTHENTICATOR = new Authenticator() {
        @Override
        public PasswordAuthentication requestPasswordAuthenticationInstance(String host, InetAddress addr,
                                                                            int port, String protocol,
                                                                            String prompt, String scheme,
                                                                            URL url, RequestorType reqType) {
            return super.requestPasswordAuthenticationInstance(host, addr, port, protocol, prompt, scheme, url, reqType);
        }
    };
    private static final CookieManager DEFAULT_COOKIE_HANDLER = new CookieManager();
    private static final Executor DEFAULT_EXECUTOR = new TestExecutor();
    private static final int DEFAULT_PRIORITY = 10;
    private static final HttpClient.Version DEFAULT_VERSION = HTTP_1_1;
    private static final ProxySelector DEFAULT_PROXY_SELECTOR = new TestProxySelector();
    private static final HttpClient.Redirect DEFAULT_REDIRECT = ALWAYS;
    private static final SSLContext DEFAULT_SSL_CONTEXT = getSSLContext();
    private static final SSLParameters DEFAULT_SSL_PARAMETERS = new SSLParameters(new String[]{"1", "2", "3"});

    private static SSLContext getSSLContext() {
        try {
            return SSLContext.getInstance("SSLv3");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public void beforeClass() {
        stubFor(get(urlPathEqualTo("/index.html"))
            .willReturn(aResponse()
                .withBody("Hello")
                .withHeader("Set-Cookie", "TestCookieName=TestCookieValue")));
    }

    @Test
    public void useHttpClientWithoutProperties() {
        var client = http().getCurrentClient();

        assertThat(client.authenticator().orElse(null), nullValue());
        assertThat(client.connectTimeout().orElse(null), equalTo(ofSeconds(5)));
        assertThat(client.cookieHandler().orElse(null), instanceOf(CookieManager.class));
        assertThat(client.executor().orElse(null), nullValue());
        assertThat(client.followRedirects(), is(NEVER));
        assertThat(client.proxy().orElse(null), nullValue());
        assertThat(client.sslContext(), instanceOf(SSLContext.class));
        assertThat(client.sslParameters(), not(nullValue()));
        assertThat(client.version(), is(HTTP_2));
    }

    @Test
    public void useHttpClientWithProperties() {
        setProperty(DEFAULT_CONNECT_TIME_UNIT_PROPERTY.getName(), DEFAULT_CONNECT_CHRONO_UNIT.name());
        setProperty(DEFAULT_CONNECT_TIME_VALUE_PROPERTY.getName(), valueOf(DEFAULT_CONNECT_TIME_VALUE));
        setProperty(DEFAULT_HTTP_AUTHENTICATOR_PROPERTY.getName(), TestAuthenticatorSupplier.class.getName());
        setProperty(DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY.getName(), TestCookieHandlerSupplier.class.getName());
        setProperty(DEFAULT_HTTP_EXECUTOR_PROPERTY.getName(), TestExecutorSupplier.class.getName());
        setProperty(DEFAULT_HTTP_PRIORITY_PROPERTY.getName(), valueOf(DEFAULT_PRIORITY));
        setProperty(DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY.getName(), DEFAULT_VERSION.name());
        setProperty(DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY.getName(), TestProxySelectorSupplier.class.getName());
        setProperty(DEFAULT_HTTP_REDIRECT_PROPERTY.getName(), DEFAULT_REDIRECT.name());
        setProperty(DEFAULT_HTTP_SSL_CONTEXT_PROPERTY.getName(), TestSslContextSupplier.class.getName());
        setProperty(DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY.getName(), TestSslParametersSupplier.class.getName());

        try {
            var newContext = new HttpStepContext();
            var client = newContext.getCurrentClient();

            assertThat(client.authenticator().orElse(null), equalTo(DEFAULT_AUTHENTICATOR));
            assertThat(client.connectTimeout().orElse(null), equalTo(of(DEFAULT_CONNECT_TIME_VALUE, DEFAULT_CONNECT_CHRONO_UNIT)));
            assertThat(client.cookieHandler().orElse(null), equalTo(DEFAULT_COOKIE_HANDLER));
            assertThat(client.executor().orElse(null), equalTo(DEFAULT_EXECUTOR));
            assertThat(client.followRedirects(), is(DEFAULT_REDIRECT));
            assertThat(client.proxy().orElse(null), equalTo(DEFAULT_PROXY_SELECTOR));
            assertThat(client.sslContext(), equalTo(DEFAULT_SSL_CONTEXT));
            assertThat(client.sslParameters().getCipherSuites(), arrayContaining("1", "2", "3"));
            assertThat(client.version(), is(DEFAULT_VERSION));
        } finally {
            getProperties().remove(DEFAULT_CONNECT_TIME_UNIT_PROPERTY.getName());
            getProperties().remove(DEFAULT_CONNECT_TIME_VALUE_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_AUTHENTICATOR_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_EXECUTOR_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_PRIORITY_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_REDIRECT_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_SSL_CONTEXT_PROPERTY.getName());
            getProperties().remove(DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY.getName());
        }
    }

    @Test
    public void addCookieTest() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        http().addCookies(httpCookie);
        assertThat(http().getCookies(), hasItem(httpCookie));
    }

    @Test
    public void clearCookiesTest() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        http().addCookies(httpCookie);
        http().removeCookies();
        assertThat(http().getCookies(), emptyIterable());
    }

    @Test
    public void clearCookiesTest2() {
        var httpCookie = new HttpCookie("TestSetUpCookieName",
            "TestSetUpCookieValue");

        http().addCookies(httpCookie);
        http().removeCookies(httpCookieValue("TestSetUpCookieValue"));
        assertThat(http().getCookies(), emptyIterable());
    }

    @Test
    public void queryParameterTest() throws Throwable {
        stubFor(get(urlPathEqualTo("/query"))
            .willReturn(aResponse().withBody("Hello query")));

        var response = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/query")
            .queryParam("date", false, "01-01-1980")
            .queryParam("some word", false, "Word and word again")
            .responseBodyHandler(ofString()));

        assertThat(response.body(),
            Matchers.equalTo("Hello query"));
        assertThat(response.uri(), equalTo(new URI("http://127.0.0.1:8089/query?date=" + encode("01-01-1980", UTF_8) + "&"
            + encode("some word", UTF_8) + "=" + encode("Word and word again", UTF_8))));
    }

    public static class TestAuthenticatorSupplier extends DefaultHttpAuthenticatorProperty.AuthenticatorSupplier {
        public TestAuthenticatorSupplier() {
            super();
        }

        @Override
        public Authenticator get() {
            return DEFAULT_AUTHENTICATOR;
        }
    }

    public static class TestCookieHandlerSupplier extends DefaultHttpCookieManagerProperty.CookieManagerSupplier {

        public TestCookieHandlerSupplier() {
            super();
        }

        @Override
        public CookieManager get() {
            return DEFAULT_COOKIE_HANDLER;
        }
    }

    public static class TestExecutor implements Executor {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    public static class TestExecutorSupplier extends DefaultHttpExecutorProperty.ExecutorSupplier {
        public TestExecutorSupplier() {
            super();
        }

        @Override
        public Executor get() {
            return DEFAULT_EXECUTOR;
        }
    }

    private static class TestProxySelector extends ProxySelector {

        @Override
        public List<Proxy> select(URI uri) {
            return List.of();
        }

        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

        }
    }

    public static class TestProxySelectorSupplier extends DefaultHttpProxySelectorProperty.ProxySelectorSupplier {

        public TestProxySelectorSupplier() {
            super();
        }

        @Override
        public ProxySelector get() {
            return DEFAULT_PROXY_SELECTOR;
        }
    }

    public static class TestSslContextSupplier extends DefaultHttpSslContextProperty.SslContextSupplier {

        public TestSslContextSupplier() {
            super();
        }

        @Override
        public SSLContext get() {
            return DEFAULT_SSL_CONTEXT;
        }
    }

    private static class TestSslParametersSupplier extends DefaultHttpSslParametersProperty.SslParametersSupplier {

        public TestSslParametersSupplier() {
            super();
        }

        @Override
        public SSLParameters get() {
            return DEFAULT_SSL_PARAMETERS;
        }
    }
}

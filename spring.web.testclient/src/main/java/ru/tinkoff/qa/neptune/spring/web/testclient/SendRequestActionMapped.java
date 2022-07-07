package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectMatches;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ResponseBody;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.spring.web.testclient.LogWebTestClientExpectation.logExpectation;

/**
 * Performs the sending of a request. Response body is deserialized.
 *
 * @param <T> is a type of deserialized object
 */
public class SendRequestActionMapped<T> extends SendRequestAction<T, BodySpecFunction.MappedBodySpecFunction<T>, SendRequestActionMapped<T>> {

    SendRequestActionMapped(Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec,
                            BodySpecFunction.MappedBodySpecFunction<T> bodyFormat) {
        super(requestSpec, bodyFormat);
    }

    /**
     * Defines an expectation of deserialized body of response
     *
     * @param matcher is criteria that body of response is expected to match
     * @return self-reference
     */
    public SendRequestActionMapped<T> expectBody(Matcher<? super T> matcher) {
        return expectBody(new ResponseBody().toString(), t -> t, matcher);
    }

    /**
     * Defines an expectation of value taken from/calculated by deserialized body of response
     *
     * @param description is description of value to check
     * @param f           how to calculate value to check
     * @param matcher     is criteria that body of response is expected to match
     * @param <R>         is type of value to check
     * @return self-reference
     */
    public <R> SendRequestActionMapped<T> expectBody(String description, Function<T, R> f, Matcher<? super R> matcher) {
        checkNotNull(matcher);
        return addExpectation(new ExpectMatches(description, matcher).toString(), spec -> {
            var body = bodyFormat.getBody();
            assertThat(description, f.apply(body), matcher);
            return true;
        });
    }

    @Override
    void readBody() {
        var ex = new Expectation.SimpleExpectation<>(bodyFormat.toString(), bodyFormat);
        try {
            ex.verify(responseSpec);
            logExpectation(ex).get().performAction(ex);
        } catch (AssertionError e) {
            errors.add(e);
        }
    }
}

package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import java.net.URL;
import java.time.Duration;
import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

/**
 * Defines criteria of extected response
 *
 * @param <S> subtype of {@link DefinesResponseCriteria}
 */
public interface DefinesResponseCriteria<S extends DefinesResponseCriteria<S>> {

    /**
     * Defines criteria for expected response
     *
     * @param criteria criteria for expected response
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria
     * @see Criteria#condition(String, Predicate)
     */
    S responseCriteria(Criteria<Response> criteria);

    /**
     * Defines criteria for expected response
     *
     * @param description criteria descriptiob
     * @param predicate   describes criteria
     * @return self-reference
     */
    S responseCriteria(String description, Predicate<Response> predicate);

    /**
     * Defines criteria for expected response united in OR-expression
     *
     * @param criteria criteria for expected response
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria
     * @see Criteria#condition(String, Predicate)
     */
    S responseCriteriaOr(Criteria<Response>... criteria);

    /**
     * Defines criteria for expected response united in XOR-expression
     *
     * @param criteria criteria for expected response
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria
     * @see Criteria#condition(String, Predicate)
     */
    S responseCriteriaOnlyOne(Criteria<Response>... criteria);

    /**
     * Defines criteria for expected response. Each defined criteria will be inverted
     *
     * @param criteria criteria for expected response. Each defined criteria will be inverted
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria
     * @see Criteria#condition(String, Predicate)
     */
    S responseCriteriaNot(Criteria<Response>... criteria);

    /**
     * Defines expected status code of the response
     *
     * @param code expected status code of the response
     * @return self-reference
     */
    default S responseStatusCodeIs(int code) {
        return responseCriteria(statusCode(code));
    }


    /**
     * Inversion of {@link #responseStatusCodeIs(int)}
     */
    default S responseStatusCodeIsNot(int code) {
        return responseCriteriaNot(statusCode(code));
    }


    /**
     * Defines expected URL of the response
     *
     * @param url expected URL of the response
     * @return self-reference
     */
    default S responseURLIs(String url) {
        return responseCriteria(responseUrl(url));
    }


    /**
     * Inversion of {@link #responseURLIs(String)}
     */
    default S responseURLIsNot(String url) {
        return responseCriteriaNot(responseUrl(url));
    }


    /**
     * Defines expected URL of the response
     *
     * @param url expected URL of the response
     * @return self-reference
     */
    default S responseURLIs(URL url) {
        return responseCriteria(responseUrl(url));
    }

    /**
     * Inversion of {@link #responseURLIs(URL)}
     */
    default S responseURLIsNot(URL url) {
        return responseCriteriaNot(responseUrl(url));
    }

    /**
     * Defines expected expression of URL of the response
     *
     * @param expression is a substring that url of a response is supposed to have.
     *                   It is possible to pass regexp pattern that url should fit.
     * @return self-reference
     */
    default S responseURLMatches(String expression) {
        return responseCriteria(urlMatches(expression));
    }


    /**
     * Inversion of {@link #responseURLMatches(String)}
     */
    default S responseURLNotMatches(String expression) {
        return responseCriteriaNot(urlMatches(expression));
    }

    /**
     * Defines expected host of URL of the response
     *
     * @param host is the expected host of URL of the response
     * @return self-reference
     */
    default S responseURLHostIs(String host) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLHost(host));
    }

    /**
     * Inversion of {@link #responseURLHostIs(String)}
     */
    default S responseURLHostIsNot(String host) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLHost(host));
    }

    /**
     * Defines expected expression of URL host of the response
     *
     * @param expression is a substring that host is supposed to have.
     *                   It is possible to pass regexp pattern that host should fit.
     * @return self-reference
     */
    default S responseURLHostMatches(String expression) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLHostMatches(expression));
    }

    /**
     * Inversion of {@link #responseURLHostMatches(String)}
     */
    default S responseURLHostNotMatches(String expression) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLHostMatches(expression));
    }


    /**
     * Defines expected protocol of URL of the response
     *
     * @param protocol is the expected protocol of URL of the response
     * @return self-reference
     */
    default S responseURLProtocolIs(String protocol) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLProtocol(protocol));
    }

    /**
     * Inversion of {@link #responseURLProtocolIs(String)}
     */
    default S responseURLProtocolIsNot(String protocol) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLProtocol(protocol));
    }

    /**
     * Defines expected expression of URL protocol of the response
     *
     * @param expression is a substring that protocol is supposed to have.
     *                   It is possible to pass regexp pattern that protocol should fit.
     * @return self-reference
     */
    default S responseURLProtocolMatches(String expression) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLProtocolMatches(expression));
    }

    /**
     * Inversion of {@link #responseURLProtocolMatches(String)}
     */
    default S responseURLProtocolNotMatches(String expression) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLProtocolMatches(expression));
    }


    /**
     * Defines expected path of URL of the response
     *
     * @param path is the expected path of URL of the response
     * @return self-reference
     */
    default S responseURLPathIs(String path) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLPath(path));
    }

    /**
     * Inversion of {@link #responseURLPathIs(String)}
     */
    default S responseURLPathIsNot(String path) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLPath(path));
    }

    /**
     * Defines expected expression of URL path of the response
     *
     * @param expression is a substring that path is supposed to have.
     *                   It is possible to pass regexp pattern that path should fit.
     * @return self-reference
     */
    default S responseURLPathMatches(String expression) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLPathMatches(expression));
    }

    /**
     * Inversion of {@link #responseURLPathMatches(String)}
     */
    default S responseURLPathNotMatches(String expression) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLPathMatches(expression));
    }


    /**
     * Defines expected query of URL of the response
     *
     * @param query is the expected query of URL of the response
     * @return self-reference
     */
    default S responseURLQueryIs(String query) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLQuery(query));
    }

    /**
     * Inversion of {@link #responseURLQueryIs(String)}
     */
    default S responseURLQueryIsNot(String query) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLQuery(query));
    }

    /**
     * Defines expected expression of URL query of the response
     *
     * @param expression is a substring that query is supposed to have.
     *                   It is possible to pass regexp pattern that query should fit.
     * @return self-reference
     */
    default S responseURLQueryMatches(String expression) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLQueryMatches(expression));
    }

    /**
     * Inversion of {@link #responseURLQueryMatches(String)}
     */
    default S responseURLQueryNotMatches(String expression) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLQueryMatches(expression));
    }


    /**
     * Defines expected user info of URL of the response
     *
     * @param userInfo is the expected user info of URL of the response
     * @return self-reference
     */
    default S responseURLUserInfoIs(String userInfo) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLUserInfo(userInfo));
    }

    /**
     * Inversion of {@link #responseURLUserInfoIs(String)}
     */
    default S responseURLUserInfoIsNot(String userInfo) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLUserInfo(userInfo));
    }

    /**
     * Defines expected expression of URL user info of the response
     *
     * @param expression is a substring that user info is supposed to have.
     *                   It is possible to pass regexp pattern that user info should fit.
     * @return self-reference
     */
    default S responseURLUserInfoMatches(String expression) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLUserInfoMatches(expression));
    }

    /**
     * Inversion of {@link #responseURLUserInfoMatches(String)}
     */
    default S responseURLUserInfoNotMatches(String expression) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLUserInfoMatches(expression));
    }


    /**
     * Defines expected port of URL of the response
     *
     * @param port is the expected port of URL of the response
     * @return self-reference
     */
    default S responseURLPortIs(int port) {
        return responseCriteria(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLPort(port));
    }

    /**
     * Inversion of {@link #responseURLPortIs(int)}
     */
    default S responseURLPortIsNot(int port) {
        return responseCriteriaNot(ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.responseURLPort(port));
    }


    /**
     * Defines criteria for the prior response
     *
     * @param responseCriteria is the criteria of expected prior response
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria
     * @see Criteria#condition(String, Predicate)
     */
    default S responsePrior(Criteria<Response> responseCriteria) {
        return responseCriteria(priorResponse(responseCriteria));
    }

    /**
     * Inversion of {@link #responsePrior(Criteria)}
     */
    default S responsePriorNot(Criteria<Response> responseCriteria) {
        return responseCriteriaNot(priorResponse(responseCriteria));
    }


    /**
     * The response is expected to have a prior response
     *
     * @return self-reference
     */
    default S responseHasPrior() {
        return responseCriteria(priorResponse());
    }


    /**
     * Inversion of {@link #responseHasPrior()}
     */
    default S responseHasNotPrior() {
        return responseCriteriaNot(priorResponse());
    }


    /**
     * Defines expected message of the response
     *
     * @param message is the expected message of the response
     * @return self-reference
     */
    default S responseMessageIs(String message) {
        return responseCriteria(message(message));
    }

    /**
     * Inversion of {@link #responseMessageIs(String)}
     */
    default S responseMessageIsNot(String message) {
        return responseCriteriaNot(message(message));
    }

    /**
     * Defines expected expression of message of the response
     *
     * @param expression is a substring that message is supposed to have.
     *                   It is possible to pass regexp pattern that message should fit.
     * @return self-reference
     */
    default S responseMessageMatches(String expression) {
        return responseCriteria(messageMatches(expression));
    }

    /**
     * Inversion of {@link #responseMessageMatches(String)}
     */
    default S responseMessageNotMatches(String expression) {
        return responseCriteriaNot(messageMatches(expression));
    }


    /**
     * Defines expected header value of the response
     *
     * @param name  is the name of header response is supposed to have
     * @param value is the value of header response is supposed to have
     * @return self-reference
     */
    default S responseHeaderValueIs(String name, String value) {
        return responseCriteria(headerValue(name, value));
    }

    /**
     * Inversion of {@link #responseHeaderValueIs(String, String)}
     */
    default S responseHeaderValueIsNot(String name, String value) {
        return responseCriteriaNot(headerValue(name, value));
    }


    /**
     * Defines expected expression of the header of the response
     *
     * @param name       is the name of header response is supposed to have
     * @param expression is the substring the header value is supposed to have or
     *                   the RegExp the header value is predicted to match
     * @return self-reference
     */
    default S responseHeaderValueMatches(String name, String expression) {
        return responseCriteria(headerValueMatches(name, expression));
    }

    /**
     * Inversion of {@link #responseHeaderValueMatches(String, String)}
     */
    default S responseHeaderValueNotMatches(String name, String expression) {
        return responseCriteriaNot(headerValueMatches(name, expression));
    }


    /**
     * The response is expected to be successful
     *
     * @return self-reference
     */
    default S responseIsSuccessful() {
        return responseCriteria(isSuccessful());
    }

    /**
     * Inversion of {@link #responseIsSuccessful()}
     */
    default S responseIsNotSuccessful() {
        return responseCriteriaNot(isSuccessful());
    }


    /**
     * The response is expected to be received from redirection
     *
     * @return self-reference
     */
    default S responseIsRedirect() {
        return responseCriteria(isRedirect());
    }

    /**
     * Inversion of {@link #responseIsRedirect()}
     */
    default S responseIsNotRedirect() {
        return responseCriteriaNot(isRedirect());
    }

    S retryTimeOut(Duration timeOut);
}

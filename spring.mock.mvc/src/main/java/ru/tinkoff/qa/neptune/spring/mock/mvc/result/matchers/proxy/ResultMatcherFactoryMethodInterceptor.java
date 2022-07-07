package ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers.proxy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.Locale;

import static java.util.Locale.US;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle.getFromResourceBundles;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

public final class ResultMatcherFactoryMethodInterceptor<T> {

    private static final Locale DEFAULT_LOCALE = US;

    private final T realObject;

    ResultMatcherFactoryMethodInterceptor(T realObject) {
        this.realObject = realObject;
    }

    private static Object[] substituteArguments(Object[] args) {
        var newArgs = new Object[]{};
        for (var a : args) {
            if (a instanceof MvcUriComponentsBuilder.MethodInvocationInfo) {
                newArgs = add(newArgs, new NeptuneMethodInvocationInfo((MvcUriComponentsBuilder.MethodInvocationInfo) a));
            } else {
                newArgs = add(newArgs, a);
            }
        }
        return newArgs;
    }

    @RuntimeType
    @SuppressWarnings("unused")
    public Object intercept(@This Object obj, @Origin Method method, @AllArguments Object[] args) throws Throwable {
        if (!ResultMatcher.class.isAssignableFrom(method.getReturnType())) {
            return method.invoke(realObject, args);
        }

        var newArgs = substituteArguments(args);

        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                ((ResultMatcher) method.invoke(realObject, newArgs)).match(result);
            }

            public String toString() {
                var firstPart = translate(getFromResourceBundles(DEFAULT_LOCALE, realObject.getClass()), realObject);
                var stringBuilder = new StringBuilder();
                if (isNotBlank(firstPart)) {
                    stringBuilder.append(firstPart).append(". ");
                }
                return stringBuilder.append(translate(getFromResourceBundles(DEFAULT_LOCALE, method), method, newArgs)).toString();
            }
        };
    }
}

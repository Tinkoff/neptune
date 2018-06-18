package com.github.toy.constructor.core.api.proxy;

import com.github.toy.constructor.core.api.StepMark;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class InnerInterceptor {

    private final Logger defaultLogger;

    InnerInterceptor(List<Logger> loggers) {
        defaultLogger = Logger.class.cast(
                newProxyInstance(Logger.class.getClassLoader(),
                        new Class[]{Logger.class}, new LoggerInvocationHandler(loggers)));
    }


    @RuntimeType
    public Object intercept(@SuperCall Callable<?> superMethod, @Origin Method method,
                            @AllArguments Object... args)
            throws Throwable {
        StepMark step = method.getAnnotation(StepMark.class);
        ofNullable(step).ifPresent(toBeReported -> {
            String reportedMessage = EMPTY;
            if (args.length == 1) {
                reportedMessage = valueOf(args[0]);
            }
            else if (args.length > 1) {
                reportedMessage = valueOf(asList(args));
            }
            defaultLogger.log(format("%s %s", toBeReported.constantMessagePart(), reportedMessage).trim());
        });

        return superMethod.call();
    }
}

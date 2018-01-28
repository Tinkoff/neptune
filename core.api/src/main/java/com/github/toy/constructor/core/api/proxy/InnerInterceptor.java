package com.github.toy.constructor.core.api.proxy;

import com.github.toy.constructor.core.api.ToBeReported;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

import static com.github.toy.constructor.core.api.proxy.Substitution.findSuitableConstructor;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class InnerInterceptor<T> {
    private final ThreadLocal<T> threadLocal = new ThreadLocal<>();
    private final Class<T> classToInstantiate;
    private final ConstructorParameters constructorParameters;
    private final Logger defaultLogger;

    InnerInterceptor(Class<T> classToInstantiate,
                     ConstructorParameters constructorParameters,
                     List<Logger> loggers) {
        this.classToInstantiate = classToInstantiate;
        this.constructorParameters = constructorParameters;
        defaultLogger = Logger.class.cast(
                newProxyInstance(Logger.class.getClassLoader(),
                        new Class[]{Logger.class}, new LoggerInvocationHandler(loggers)));
    }


    @RuntimeType
    public Object intercept(@This Object proxy, @SuperCall Callable<?> superMethod, @Origin Method method, @AllArguments Object... args)
            throws Throwable {
        T target = ofNullable(threadLocal.get()).orElseGet(() -> {
            Object[] params = constructorParameters.getParameterValues();
            Constructor<T> c;
            try {
                c = findSuitableConstructor(classToInstantiate, params);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            c.setAccessible(true);
            try {
                T result =  c.newInstance(params);
                threadLocal.set(result);
                return result;
            } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        ToBeReported toBeReportedAnnotation = method.getAnnotation(ToBeReported.class);
        ofNullable(toBeReportedAnnotation).ifPresent(toBeReported -> {
            String reportedMessage = EMPTY;
            if (args.length == 1) {
                reportedMessage = valueOf(args[0]);
            }
            else if (args.length > 1) {
                reportedMessage = valueOf(asList(args));
            }
            defaultLogger.log(format("%s %s", toBeReported.constantMessagePart(), reportedMessage).trim());
        });
        method.setAccessible(true);

        Object result;
        try {
            result = method.invoke(target, args);
        }
        catch (InvocationTargetException e) {
            throw e.getCause();
        }
        superMethod.call();

        return ofNullable(result).map(o -> {
            if (o.getClass().equals(target.getClass())) {
                return proxy;
            }
            return result;
        }).orElse(null);
    }
}

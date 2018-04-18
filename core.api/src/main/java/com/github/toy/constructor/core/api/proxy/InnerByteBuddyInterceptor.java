package com.github.toy.constructor.core.api.proxy;

import net.bytebuddy.implementation.bind.annotation.*;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.github.toy.constructor.core.api.proxy.Substitution.findSuitableConstructor;
import static com.github.toy.constructor.core.api.proxy.ThreadLocals.get;
import static com.github.toy.constructor.core.api.proxy.ThreadLocals.set;
import static java.util.Optional.ofNullable;

public class InnerByteBuddyInterceptor<T> {
    private final Class<T> classToInstantiate;
    private final ConstructorParameters constructorParameters;
    private final List<Logger> loggers;

    InnerByteBuddyInterceptor(Class<T> classToInstantiate,
                              ConstructorParameters constructorParameters,
                              List<Logger> loggers) {
        this.classToInstantiate = classToInstantiate;
        this.constructorParameters = constructorParameters;
        this.loggers = loggers;
    }


    @RuntimeType
    public Object intercept(@This Object proxy, @Origin Method method, @AllArguments Object... args)
            throws Throwable {
        T target = ofNullable(get(classToInstantiate)).orElseGet(() -> {
            Object[] params = constructorParameters.getParameterValues();
            Constructor<T> c;
            try {
                c = findSuitableConstructor(classToInstantiate, params);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            c.setAccessible(true);

            MethodInterceptor interceptor = new InnerCGLIBInterceptor(loggers);
            Enhancer enhancer = new Enhancer();
            enhancer.setCallback(interceptor);
            enhancer.setSuperclass(classToInstantiate);

            T t;
            if (c.getParameterTypes().length == 0) {
                t = (T) enhancer.create();
            }
            else {
                t = (T) enhancer.create(c.getParameterTypes(), constructorParameters.getParameterValues());
            }

            return set(classToInstantiate, t);
        });

        method.setAccessible(true);

        Object result;
        try {
            result = method.invoke(target, args);
        }
        catch (InvocationTargetException e) {
            throw e.getCause();
        }

        return ofNullable(result).map(o -> {
            if (o.getClass().equals(target.getClass())) {
                return proxy;
            }
            return result;
        }).orElse(null);
    }
}

package com.github.toy.constructor.core.api.substitution;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.toy.constructor.core.api.substitution.Substitution.findSuitableConstructor;
import static java.util.Optional.ofNullable;

public class InnerInterceptor<T> {
    private final ThreadLocal<T> threadLocal = new ThreadLocal<>();
    private final Class<T> classToInstantiate;
    private final ConstructorParameters constructorParameters;

    InnerInterceptor(Class<T> classToInstantiate, ConstructorParameters constructorParameters) {
        this.classToInstantiate = classToInstantiate;
        this.constructorParameters = constructorParameters;
    }


    @RuntimeType
    public Object intercept(@Origin Method method, @AllArguments Object... args) throws Exception {
        T target = ofNullable(threadLocal.get()).orElseGet(() -> {
            Object[] params = constructorParameters.getParameterValues();
            Constructor<T> c = findSuitableConstructor(classToInstantiate, params);
            c.setAccessible(true);
            try {
                T result =  c.newInstance(params);
                threadLocal.set(result);
                return result;
            } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        return method.invoke(target, args);
    }
}

package com.github.toy.constructor.core.api.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.reflection.ConstructorUtil.findSuitableConstructor;
import static java.util.Optional.ofNullable;

public class OuterMethodInterceptor<T> implements MethodInterceptor {

    private final Class<T> originalClass;
    private final Class<T> classToInstantiate;
    private final ConstructorParameters constructorParameters;
    private final Function<T, T> manipulationWithObjectToReturn;
    private final ThreadLocal<T> threadLocal;

    OuterMethodInterceptor(Class<T> originalClass, Class<T> classToInstantiate, ConstructorParameters constructorParameters,
                           Function<T, T> manipulationWithObjectToReturn) {
        this.originalClass = originalClass;
        this.classToInstantiate = classToInstantiate;
        this.constructorParameters = constructorParameters;
        this.manipulationWithObjectToReturn = manipulationWithObjectToReturn;
        threadLocal = new ThreadLocal<>();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        T target;
        try {
            target = ofNullable(threadLocal.get()).orElseGet(() -> {
                Object[] params = constructorParameters.getParameterValues();
                Constructor<T> c;
                try {
                    c = findSuitableConstructor(classToInstantiate, params);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                c.setAccessible(true);
                T t;
                try {
                    t = manipulationWithObjectToReturn.apply(c.newInstance(constructorParameters.getParameterValues()));
                    threadLocal.set(t);
                } catch (InstantiationException|IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getCause());
                }
                return t;
            });
        }
        catch (RuntimeException e) {
            throw ofNullable(e.getCause()).orElse(e);
        }

        method.setAccessible(true);

        try {
            return ofNullable(method.invoke(target, args)).map(o -> {
                if (o.getClass().equals(originalClass)) {
                    return obj;
                }
                return o;
            }).orElse(null);
        }
        catch (InvocationTargetException e) {
            throw ofNullable(e.getCause()).orElse(e);
        }
        catch (Throwable e) {
            throw e;
        }
    }
}

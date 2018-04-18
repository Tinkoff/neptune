package com.github.toy.constructor.core.api.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.proxy.Substitution.findSuitableConstructor;
import static com.github.toy.constructor.core.api.proxy.ThreadLocals.get;
import static java.util.Optional.ofNullable;

public class OuterMethodInterceptor<T> implements MethodInterceptor {

    private final Class<T> originalClass;
    private final Class<T> classToInstantiate;
    private final ConstructorParameters constructorParameters;
    private final Function<T, T> manipulationWithObjectToReturn;

    OuterMethodInterceptor(Class<T> originalClass, Class<T> classToInstantiate, ConstructorParameters constructorParameters,
                           Function<T, T> manipulationWithObjectToReturn) {
        this.originalClass = originalClass;
        this.classToInstantiate = classToInstantiate;
        this.constructorParameters = constructorParameters;
        this.manipulationWithObjectToReturn = manipulationWithObjectToReturn;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        T target = ofNullable(get(originalClass)).orElseGet(() -> {
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
                t = ThreadLocals.setNewInstance(originalClass,
                        manipulationWithObjectToReturn.apply(c.newInstance(constructorParameters.getParameterValues())));
            } catch (InstantiationException|IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
            return t;
        });

        Object result = method.invoke(target, args);
        return ofNullable(result).map(o -> {
            if (o.getClass().equals(originalClass)) {
                return obj;
            }
            return o;
        }).orElse(null);
    }
}

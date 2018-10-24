package ru.tinkoff.qa.neptune.core.api.proxy;

import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.cleaning.StoppableOnJVMShutdown;
import ru.tinkoff.qa.neptune.core.api.concurency.ObjectContainer;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static java.lang.Runtime.getRuntime;
import static ru.tinkoff.qa.neptune.core.api.concurency.ObjectContainer.*;
import static ru.tinkoff.qa.neptune.core.api.utils.ConstructorUtil.findSuitableConstructor;
import static java.util.Optional.ofNullable;

public class MethodInterceptor<T> {

    private final Class<T> originalClass;
    private final Class<T> classToInstantiate;
    private final ConstructorParameters constructorParameters;
    private final Function<T, T> manipulationWithObjectToReturn;
    private final ThreadLocal<ObjectContainer<T>> threadLocal;

    MethodInterceptor(Class<T> originalClass, Class<T> classToInstantiate, ConstructorParameters constructorParameters,
                      Function<T, T> manipulationWithObjectToReturn) {
        this.originalClass = originalClass;
        this.classToInstantiate = classToInstantiate;
        this.constructorParameters = constructorParameters;
        this.manipulationWithObjectToReturn = manipulationWithObjectToReturn;
        threadLocal = new ThreadLocal<>();
    }

    @RuntimeType
    @SuppressWarnings("unused")
    public Object intercept(@This Object obj, @Origin Method method, @AllArguments Object[] args) throws Throwable {
        T target;
        try {
            target = ofNullable(threadLocal.get()).map(ObjectContainer::getWrappedObject).orElseGet(() ->
                    ofNullable(setObjectBusy(originalClass)).map(tObjectContainer -> {
                        threadLocal.set(tObjectContainer);
                        return tObjectContainer.getWrappedObject();
                    }).orElseGet(() -> {
                        var params = constructorParameters.getParameterValues();
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
                            if (StoppableOnJVMShutdown.class.isAssignableFrom(t.getClass())) {
                                getRuntime().addShutdownHook(((StoppableOnJVMShutdown) t).getHookOnJvmStop());
                            }
                            var container = new ObjectContainer<>(t);
                            threadLocal.set(container);
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e.getCause());
                        }
                        return t;
                    }));
        } catch (RuntimeException e) {
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
        } catch (InvocationTargetException e) {
            throw ofNullable(e.getCause()).orElse(e);
        }
    }
}

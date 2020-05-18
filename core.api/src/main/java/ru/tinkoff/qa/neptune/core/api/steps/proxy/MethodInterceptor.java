package ru.tinkoff.qa.neptune.core.api.steps.proxy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.concurrency.ObjectContainer;
import ru.tinkoff.qa.neptune.core.api.steps.context.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.utils.ConstructorUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.Runtime.getRuntime;
import static java.util.Optional.ofNullable;

public class MethodInterceptor<T> {

    private final Class<T> classToInstantiate;
    private final ConstructorParameters constructorParameters;
    private final ThreadLocal<ObjectContainer<T>> threadLocal;

    public MethodInterceptor(Class<T> classToInstantiate, ConstructorParameters constructorParameters) {
        this.classToInstantiate = classToInstantiate;
        this.constructorParameters = constructorParameters;
        threadLocal = new ThreadLocal<>();
    }

    @RuntimeType
    @SuppressWarnings("unused")
    public Object intercept(@This Object obj, @Origin Method method, @AllArguments Object[] args) throws Throwable {
        T target;
        try {
            target = ofNullable(threadLocal.get()).map(ObjectContainer::getWrappedObject).orElseGet(() ->
                    ofNullable(ObjectContainer.setObjectBusy(classToInstantiate)).map(tObjectContainer -> {
                        threadLocal.set(tObjectContainer);
                        return tObjectContainer.getWrappedObject();
                    }).orElseGet(() -> {
                        var params = constructorParameters.getParameterValues();
                        Constructor<T> c;

                        try {
                            c = ConstructorUtil.findSuitableConstructor(classToInstantiate, params);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        c.setAccessible(true);

                        T t;
                        try {
                            t = c.newInstance(constructorParameters.getParameterValues());
                            if (Stoppable.class.isAssignableFrom(t.getClass())) {
                                getRuntime().addShutdownHook(new Thread(((Stoppable) t)::stop));
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
                if (o.getClass().equals(classToInstantiate)) {
                    return obj;
                }
                return o;
            }).orElse(null);
        } catch (InvocationTargetException e) {
            throw ofNullable(e.getCause()).orElse(e);
        }
    }
}

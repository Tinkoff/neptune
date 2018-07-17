package com.github.toy.constructor.core.api.proxy;

import com.github.toy.constructor.core.api.ConstructorParameters;
import com.github.toy.constructor.core.api.cleaning.Stoppable;
import com.github.toy.constructor.core.api.concurency.ObjectContainer;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.toy.constructor.core.api.concurency.GroupingObjects.getGroupingObject;
import static com.github.toy.constructor.core.api.concurency.ObjectContainer.*;
import static com.github.toy.constructor.core.api.utils.ConstructorUtil.findSuitableConstructor;
import static java.lang.Thread.currentThread;
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

    private ObjectContainer<T> getTarget() {
        return ofNullable(getGroupingObject())
                .map(o -> setObjectBusy(originalClass, o)).orElseGet(() -> setObjectBusy(originalClass));
    }

    @SuppressWarnings("unchecked")
    private List<ObjectContainer<T>> getAllInstancesToShutDown() {
        return ofNullable(getGroupingObject()).map(o -> getAllObjects(originalClass, o))
                .orElseGet(() -> getAllObjects(originalClass)).stream()
                .map(objectContainer -> (ObjectContainer<T>) objectContainer)
                .collect(Collectors.toList());
    }

    @RuntimeType
    @SuppressWarnings("unused")
    public Object intercept(@This Object obj, @Origin Method method, @AllArguments Object[] args) throws Throwable {
        T target;

        boolean toShutDown;

        try {
            toShutDown = Stoppable.class.getMethod(method.getName(), method.getParameterTypes()) != null;
        }
        catch (Exception e) {
            toShutDown = false;
        }

        if (toShutDown) {
            List<ObjectContainer<T>> containers = getAllInstancesToShutDown();
            try {
                for (ObjectContainer<T> objectContainer: containers) {
                    method.invoke(objectContainer.getWrappedObject(), args);
                }
                return null;
            }
            finally {
                List<ObjectContainer<?>> toBeRemoved = new ArrayList<>(containers);
                remove(toBeRemoved);
            }
        }

        try {
            target = ofNullable(threadLocal.get()).map(ObjectContainer::getWrappedObject).orElseGet(() ->
                    ofNullable(getTarget()).map(tObjectContainer -> {
                        threadLocal.set(tObjectContainer);
                        return tObjectContainer.getWrappedObject();
                    }).orElseGet(() -> {
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
                            ObjectContainer<T> container = new ObjectContainer<>(t);
                            ofNullable(getGroupingObject()).ifPresent(container::groupBy);
                            threadLocal.set(container);
                            container.setBusy(currentThread());
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

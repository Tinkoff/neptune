package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.commons.lang3.ClassUtils.getAllInterfaces;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Select by method invocation")
public final class SelectionByMethod<RESULT> implements Function<Class<?>, RESULT> {

    private final Function<Class<?>, RESULT> delegate;
    private Method invoked;
    private Object[] parameters;

    public SelectionByMethod(Function<Class<?>, RESULT> delegate) {
        checkNotNull(delegate);
        this.delegate = delegate;
    }

    @Override
    public RESULT apply(Class<?> t) {
        var interfaces = getAllInterfaces(t);
        var proxyEntity = (Class<?>) newProxyInstance(getSystemClassLoader(),
                interfaces.toArray(new Class<?>[0]),
                new EntityMethodInvocationHandler(this, t));
        return delegate.apply(proxyEntity);
    }

    public Method getInvoked() {
        return invoked;
    }

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    public static class EntityMethodInvocationHandler implements InvocationHandler {

        private final SelectionByMethod<?> selection;
        private final Class<?> entity;

        public EntityMethodInvocationHandler(SelectionByMethod<?> selection, Class<?> entity) {
            this.selection = selection;
            this.entity = entity;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            selection.invoked = method;
            selection.parameters = args;
            return method.invoke(entity, args);
        }
    }
}

package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import ru.tinkoff.qa.neptune.selenium.api.widget.NeedToScrollIntoView;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.openqa.selenium.support.PageFactory.initElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchProxyBuilder.createProxy;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.ToStringFormer.widgetToString;

public final class WidgetInterceptor extends AbstractElementInterceptor {

    private final Class<? extends Widget> widgetClass;
    private final Set<Class<?>> plainClassSet = new HashSet<>();
    private final By by;

    WidgetInterceptor(WebElement webElement, Class<? extends Widget> widgetClass, By by) {
        super(webElement);
        this.widgetClass = widgetClass;
        this.by = by;

        Class<?> clazz = widgetClass;
        while (!clazz.equals(Object.class)) {
            plainClassSet.add(clazz);
            plainClassSet.addAll(Arrays.asList(clazz.getInterfaces()));
            clazz = clazz.getSuperclass();
        }
    }

    private static Method findScrollableSuperMethod(Method method, Class<?> findFrom) {
        return stream(findFrom.getDeclaredMethods())
                .filter(m -> m.getName().equals(method.getName())
                        && m.getParameterCount() == method.getParameterCount()
                        && (!isStatic(m.getModifiers())) && !isStatic(method.getModifiers())
                        && areClassArraysCompatible(method, m))
                .findFirst()
                .orElse(null);
    }

    @Override
    Object createRealObject() {
        var widgetConstructor = stream(widgetClass.getDeclaredConstructors())
                .filter(constructor -> {
                    Class<?>[] paramTypes = constructor.getParameterTypes();
                    return paramTypes.length == 1 &&
                            paramTypes[0].isAssignableFrom(element.getClass());
                }).findFirst()
                .orElseThrow(() -> new RuntimeException(new NoSuchMethodException(format("Can't create instance of %s because " +
                        "it has no convenient constructor", widgetClass.getName()))));
        widgetConstructor.setAccessible(true);

        try {
            var result = widgetConstructor.newInstance(element);
            initElements(new DefaultElementLocatorFactory(element), result);
            return result;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private static boolean areClassArraysCompatible(Method currentMethod, Method overridden) {
        var params = currentMethod.getParameterTypes();
        var params2 = overridden.getParameterTypes();
        var i = 0;
        var result = true;
        for (var c : params) {
            if (!params2[i].isAssignableFrom(c)) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    @RuntimeType
    public Object intercept(@This Object obj, @Origin Method method, @AllArguments Object[] args) throws Throwable {

        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            return widgetToString((Widget) createRealObject());
        }

        if ("getWrappedElement".equals(method.getName())
                && method.getParameterTypes().length == 0
                && WebElement.class.equals(method.getReturnType())) {
            realObject = ofNullable(realObject)
                    .orElseGet(this::createRealObject);
            return createProxy(element.getClass(), new WebElementInterceptor(element, by));
        }

        return super.intercept(obj, method, args);
    }

    @Override
    boolean toPerformTheScrolling(Method method) {
        if (method.isAnnotationPresent(NeedToScrollIntoView.class)) {
            return true;
        }

        for (Class<?> c : plainClassSet) {
            var found = findScrollableSuperMethod(method, c);

            if (isNull(found)) {
                continue;
            }

            if (found.isAnnotationPresent(NeedToScrollIntoView.class)) {
                return true;
            }
        }

        return false;
    }
}

package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.NeedToScrollIntoView;
import ru.tinkoff.qa.neptune.selenium.api.widget.ScrollsIntoView;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.openqa.selenium.support.PageFactory.initElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CGLibProxyBuilder.createProxy;

class WidgetInterceptor extends AbstractElementInterceptor {

    private final Class<? extends Widget> widgetClass;
    private final Set<Class<?>> plainClassSet = new HashSet<>();

    WidgetInterceptor(WebElement webElement, Class<? extends Widget> widgetClass, String description) {
        super(description, webElement);
        this.widgetClass = widgetClass;

        Class<?> clazz = widgetClass;
        while (!clazz.equals(Object.class)) {
            plainClassSet.add(clazz);
            plainClassSet.addAll(Arrays.asList(clazz.getInterfaces()));
            clazz = clazz.getSuperclass();
        }
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        if ("getWrappedElement".equals(method.getName())
                && method.getParameterTypes().length == 0
                && WebElement.class.equals(method.getReturnType())) {
            return createProxy(element.getClass(), new WebElementInterceptor(element, description));
        }

        return super.intercept(obj, method, args, proxy);
    }

    @Override
    void setScroller() {
        if (ScrollsIntoView.class.isAssignableFrom(widgetClass)) {
            scrollsIntoView = (ScrollsIntoView) realObject;
        }
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


    @Override
    boolean toPerformTheScrolling(Method method) {
        if (method.isAnnotationPresent(NeedToScrollIntoView.class)) {
            return true;
        }

        for (Class<?> c: plainClassSet) {
            Method found;
            try {
                found = c.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                continue;
            }

            if (found.isAnnotationPresent(NeedToScrollIntoView.class)) {
                return true;
            }
        }

        return false;
    }
}

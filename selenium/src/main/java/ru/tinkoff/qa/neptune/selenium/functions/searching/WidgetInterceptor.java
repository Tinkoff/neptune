package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.openqa.selenium.support.PageFactory.initElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchingProxyBuilder.createProxy;

class WidgetInterceptor implements MethodInterceptor {

    private final WebElement webElement;
    private final Class<? extends Widget> widgetClass;
    private final String description;
    private Widget widget;

    WidgetInterceptor(WebElement webElement, Class<? extends Widget> widgetClass, String description) {
        this.webElement = webElement;
        this.widgetClass = widgetClass;
        this.description = description;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            return description;
        }

        if ("getWrappedElement".equals(method.getName())
                && method.getParameterTypes().length == 0
                && WebElement.class.equals(method.getReturnType())) {
            return createProxy(webElement.getClass(), new WebElementInterceptor(webElement, description));
        }

        if (widget == null) {
            var widgetConstructor = stream(widgetClass.getDeclaredConstructors())
                    .filter(constructor -> {
                        Class<?>[] paramTypes = constructor.getParameterTypes();
                        return paramTypes.length == 1 &&
                                paramTypes[0].isAssignableFrom(webElement.getClass());
                    }).findFirst().orElseThrow(() -> new NoSuchMethodException(format("Can't create instance of %s because " +
                            "it has no convenient constructor", widgetClass.getName())));
            widgetConstructor.setAccessible(true);
            widget = (Widget) widgetConstructor.newInstance(webElement);
            initElements(new DefaultElementLocatorFactory(webElement), widget);
        }

        Class<?>[] parameters;
        if ("equals".equals(method.getName()) && (parameters = method.getParameterTypes()).length == 1
                && parameters[0].equals(Object.class)) {
            var result = widget.equals(args[0]);
            //it may be another proxy
            if (!result) {
                result = (boolean) proxy.invokeSuper(obj, args);
            }
            return result;
        }

        try {
            return method.invoke(widget, args);
        }
        catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}

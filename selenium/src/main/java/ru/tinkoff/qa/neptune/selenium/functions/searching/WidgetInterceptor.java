package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.reflect.Proxy.newProxyInstance;
import static ru.tinkoff.qa.neptune.core.api.proxy.ToStringDelegateInvocationHandler.getToStringDelegateInvocationHandler;
import static ru.tinkoff.qa.neptune.selenium.api.widget.Widget.getWidgetName;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.openqa.selenium.support.PageFactory.initElements;

class WidgetInterceptor implements MethodInterceptor {

    private final WebElement webElement;
    private final Class<? extends Widget> widgetClass;
    private final String conditionString;
    private Widget widget;

    WidgetInterceptor(WebElement webElement, Class<? extends Widget> widgetClass, String conditionString) {
        this.webElement = webElement;
        this.widgetClass = widgetClass;
        this.conditionString = conditionString;
    }

    private String getElementDescription() {
        String stringDescription = getWidgetName(widgetClass);
        if (!isBlank(conditionString)) {
            stringDescription = format("%s found on conditions '%s'", stringDescription, conditionString);
        }
        return stringDescription;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            return getElementDescription();
        }

        if (widget == null) {
            Constructor<?> widgetConstructor = stream(widgetClass.getDeclaredConstructors())
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
            boolean result = widget.equals(args[0]);
            //it may be another proxy
            if (!result) {
                result = (boolean) proxy.invokeSuper(obj, args);
            }
            return result;
        }

        if ("getWrappedElement".equals(method.getName())
                && method.getParameterTypes().length == 0
                && WebElement.class.equals(method.getReturnType())) {
            WebElement wrappedElement = widget.getWrappedElement();
            return newProxyInstance(getSystemClassLoader(),
                    wrappedElement.getClass().getInterfaces(),
                    getToStringDelegateInvocationHandler(wrappedElement, getElementDescription()));
        }

        try {
            return method.invoke(widget, args);
        }
        catch (InvocationTargetException e) {
            throw e.getCause();
        }

    }
}

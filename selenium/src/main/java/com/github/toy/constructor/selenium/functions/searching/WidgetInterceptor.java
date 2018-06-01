package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Widget;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.toy.constructor.selenium.api.widget.Widget.getWidgetName;
import static java.lang.String.format;
import static java.util.Arrays.stream;
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

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            return format("%s found on condition '%s'", getWidgetName(widgetClass), conditionString);
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
        if ("equals".equals(method.getName()) && (parameters = method.getParameterTypes()).length == 0
                && parameters[0].equals(Object.class)) {
            boolean result = widget.equals(args[0]);
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

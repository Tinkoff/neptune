package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Widget;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

import static com.github.toy.constructor.selenium.api.widget.Widget.getWidgetName;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

class WidgetListInterceptor implements MethodInterceptor {

    private final Class<? extends Widget> targetClass;
    private final List<? extends Widget> widgets;
    private final String description;

    WidgetListInterceptor(Class<? extends Widget> targetClass, List<? extends Widget> widgets, String description) {
        this.targetClass = targetClass;
        this.widgets = widgets;
        this.description = description;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            String stringDescription = format("%s elements of type %s", widgets.size(), getWidgetName(targetClass));
            if (!isBlank(description)) {
                stringDescription = format("%s found on condition '%s'", stringDescription, description);
            }
            return stringDescription;
        }

        Class<?>[] parameters;
        if ("equals".equals(method.getName()) && (parameters = method.getParameterTypes()).length == 1
                && parameters[0].equals(Object.class)) {
            boolean result = widgets.equals(args[0]);
            //it may be another proxy
            if (!result) {
                result = (boolean) proxy.invokeSuper(obj, args);
            }
            return result;
        }

        return method.invoke(widgets, args);
    }
}

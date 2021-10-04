package ru.tinkoff.qa.neptune.spring.mock.mvc.aspect;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;

class NeptuneMethodInvocationInfo implements MvcUriComponentsBuilder.MethodInvocationInfo {

    private final MvcUriComponentsBuilder.MethodInvocationInfo delegate;

    NeptuneMethodInvocationInfo(MvcUriComponentsBuilder.MethodInvocationInfo delegate) {
        this.delegate = delegate;
    }

    @Override
    public Class<?> getControllerType() {
        return delegate.getControllerType();
    }

    @Override
    public Method getControllerMethod() {
        return delegate.getControllerMethod();
    }

    @Override
    public Object[] getArgumentValues() {
        return delegate.getArgumentValues();
    }

    @Override
    public String toString() {
        return "controller type '" + getControllerType()
                + "'; controller method '" + getControllerMethod().getName()
                + "'; args " + ArrayUtils.toString(getArgumentValues());
    }
}

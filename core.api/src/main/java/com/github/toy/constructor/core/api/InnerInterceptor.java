package com.github.toy.constructor.core.api;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class InnerInterceptor {

    @RuntimeType
    public Object intercept(@SuperCall Callable<?> superMethod, @Origin Method method,
                            @AllArguments Object... args)
            throws Throwable {
        return superMethod.call();
    }
}

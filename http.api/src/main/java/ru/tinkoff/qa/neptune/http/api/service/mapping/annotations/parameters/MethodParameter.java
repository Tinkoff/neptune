package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a type of an object that is designed to represent a parameter (header, query parameter or path variable)
 * of http request.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface MethodParameter {

    /**
     * Checks is a class represents a parameter (header, query parameter or path variable)
     * of http request or not.
     */
    final class MethodParameterDetector {

        private MethodParameterDetector() {
            super();
        }

        /**
         * Checks is a class represents a parameter (header, query parameter or path variable)
         * of http request or not.
         *
         * @param cls is a class to check
         * @return is a class represents method parameter or not
         */
        public static boolean isAMethodParameter(Class<?> cls) {
            var superCls = cls;
            var isMethodParameter = superCls.getAnnotation(MethodParameter.class) != null;

            while (!isMethodParameter && !superCls.equals(Object.class)) {
                superCls = superCls.getSuperclass();
                isMethodParameter = superCls.getAnnotation(MethodParameter.class) != null;
            }

            return isMethodParameter;
        }
    }
}

package ru.tinkoff.qa.neptune.retrofit2.service.setup;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation marks a non-static field whose type
 * describes http-service in Retrofit style. It defines specific settings of retrofit.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface UseRetrofitSettings {

    /**
     * @return a subclass of {@link RetrofitBuilderSupplier}.
     * <p>WARNING!!!!!</p>
     * Defined class should not be abstract. Also it should not have declared constructor
     * or it should declare a public constructor with no parameters.
     */
    Class<? extends RetrofitBuilderSupplier> value();

    final class UseRetrofitSettingsReader {

        private UseRetrofitSettingsReader() {
            super();
        }

        public static RetrofitBuilderSupplier getRetrofit(UseRetrofitSettings useRetrofitSettings) {
            try {
                var c = useRetrofitSettings.value().getConstructor();
                c.setAccessible(true);
                return c.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

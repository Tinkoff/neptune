package ru.tinkoff.qa.neptune.core.api.localization;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static java.util.Optional.ofNullable;

public interface ToIncludeClassDescription {

    /**
     * Determines to include description of a class to generated resource bundle or not
     *
     * @param clazz class to include its description to generated resource bundle
     * @return to include description of a class to generated bundle or not
     * @see ResourceBundleGenerator
     * @see StepLocalization
     * @see LocalizationByResourceBundle
     */
    boolean toIncludeClass(Class<?> clazz);

    /**
     * Creates description of a class which is included to generated resource bundle
     *
     * @param clazz a class to create description which is included to generated resource bundle
     * @return description which is included to generated resource bundle or {@literal null}
     * @see ResourceBundleGenerator
     * @see StepLocalization
     * @see LocalizationByResourceBundle
     */
    String description(Class<?> clazz);

    static final class DefaultToIncludeClassDescriptionImpl implements ToIncludeClassDescription {

        @Override
        public boolean toIncludeClass(Class<?> clazz) {
            return clazz.getAnnotation(Description.class) != null;
        }

        @Override
        public String description(Class<?> clazz) {
            return ofNullable(clazz.getAnnotation(Description.class))
                    .map(Description::value)
                    .orElse(null);
        }
    }
}

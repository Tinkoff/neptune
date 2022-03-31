package ru.tinkoff.qa.neptune.core.api.localization.bundle.extensions;

import ru.tinkoff.qa.neptune.core.api.localization.ToIncludeClassDescription;

public class ToIncludeClassDescriptionTest implements ToIncludeClassDescription {

    @Override
    public boolean toIncludeClass(Class<?> clazz) {
        return clazz.getSimpleName().equals("ClassD");
    }

    @Override
    public String description(Class<?> clazz) {
        return "Some test class";
    }
}

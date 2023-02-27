package ru.tinkoff.qa.neptune.hibernate.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;
import ru.tinkoff.qa.neptune.hibernate.session.factory.SessionFactorySource;

@PropertyName("SESSION_FACTORY_SOURCE")
@PropertyDefaultValue("ru.tinkoff.qa.neptune.hibernate.session.factory.DefaultSessionFactorySource")
@PropertyDescription(section = "Database. Hibernate properties",
    description = {"Defines a subclass of SessionFactorySource. ",
        "The object of this class creates or fetches instances of Hibernate session factories"})
public final class SessionFactorySourceProperty implements ObjectByClassPropertySupplier<SessionFactorySource> {

    public static final SessionFactorySourceProperty SESSION_FACTORY_SOURCE_PROPERTY = new SessionFactorySourceProperty();

    private SessionFactorySourceProperty() {
        super();
    }
}

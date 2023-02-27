package ru.tinkoff.qa.neptune.hibernate.session.factory;

import org.hibernate.SessionFactory;

import java.util.Set;

/**
 * Instantiates or gets {@link org.hibernate.SessionFactory}
 */
public abstract class SessionFactorySource {

    /**
     * Adds instantiated {@link SessionFactory} to provided set
     *
     * @param toFill is provided set of {@link SessionFactory} be filled with instantiated
     *               objects
     * @return resulted filled set
     */
    public Set<SessionFactory> fillSessionFactories(Set<SessionFactory> toFill) {
        toFill.addAll(getSetOfSessionFactories());
        return toFill;
    }

    /**
     * Creates or gets a set of {@link SessionFactory}
     *
     * @return created or fetched set of {@link SessionFactory}
     */
    protected abstract Set<SessionFactory> getSetOfSessionFactories();
}

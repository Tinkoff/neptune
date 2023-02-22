package ru.tinkoff.qa.neptune.hibernate.session.factory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.tinkoff.qa.neptune.hibernate.exception.HibernateConfigurationException;

import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.hibernate.properties.ConnectionConfig.HIBERNATE_CONFIG_FILENAMES;
import static ru.tinkoff.qa.neptune.hibernate.properties.ConnectionConfig.PERSISTENCE_UNITS;
import static ru.tinkoff.qa.neptune.hibernate.properties.UseJpaConfig.USE_JPA_CONFIG;

public final class DefaultSessionFactorySource extends SessionFactorySource {

    @Override
    public Set<SessionFactory> fillSessionFactories(Set<SessionFactory> sessionFactorySet) {
        if (sessionFactorySet.isEmpty()) {
            return super.fillSessionFactories(sessionFactorySet);
        }

        return sessionFactorySet;
    }

    @Override
    protected Set<SessionFactory> getSetOfSessionFactories() {
        var result = new HashSet<SessionFactory>();
        if (USE_JPA_CONFIG.get()) {
            if (nonNull(PERSISTENCE_UNITS.get())) {
                PERSISTENCE_UNITS.get().forEach(unit -> {
                    var entityManagerFactory = Persistence.createEntityManagerFactory(unit);
                    var sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
                    result.add(sessionFactory);
                });
            } else {
                throw new HibernateConfigurationException("Persistence units are not defined in properties file");
            }
        } else {
            if (nonNull(HIBERNATE_CONFIG_FILENAMES.get())) {
                HIBERNATE_CONFIG_FILENAMES.get().forEach(configFile -> {
                    var configuration = new Configuration().addFile(configFile);
                    var sessionFactory = configuration.buildSessionFactory();
                    result.add(sessionFactory);
                });
            } else {
                throw new HibernateConfigurationException("Hibernate configuration files are not defined in " +
                    "properties file");
            }
        }

        return result;
    }
}

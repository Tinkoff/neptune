package ru.tinkoff.qa.neptune.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.exception.HibernateConfigurationException;
import ru.tinkoff.qa.neptune.hibernate.session.factory.SessionFactorySource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.SessionFactorySourceTest.CustomSessionFactorySource.FACTORIES;
import static ru.tinkoff.qa.neptune.hibernate.properties.ConnectionConfig.HIBERNATE_CONFIG_FILENAMES;
import static ru.tinkoff.qa.neptune.hibernate.properties.ConnectionConfig.PERSISTENCE_UNITS;
import static ru.tinkoff.qa.neptune.hibernate.properties.SessionFactorySourceProperty.SESSION_FACTORY_SOURCE_PROPERTY;
import static ru.tinkoff.qa.neptune.hibernate.properties.UseJpaConfig.USE_JPA_CONFIG;

public class SessionFactorySourceTest {

    @Mock
    private SessionFactory factoryFromHibernateConfig;

    @BeforeClass
    public void prepare() {
        openMocks(this);
    }

    @Test
    public void customSessionFactorySourceTest() {
        SESSION_FACTORY_SOURCE_PROPERTY.accept(CustomSessionFactorySource.class);
        var factories = hibernate().getSessionFactories();
        assertThat(factories, contains(FACTORIES.get(0), FACTORIES.get(1)));
    }

    @Test
    public void hibernateConfigTests() {
        HIBERNATE_CONFIG_FILENAMES.accept("someFile");

        try (var mocked = mockConstruction(Configuration.class,
            (mock, context) -> {
                when(mock.addFile("someFile")).thenReturn(mock);
                when(mock.buildSessionFactory()).thenReturn(factoryFromHibernateConfig);
            })) {

            var factories = hibernate().getSessionFactories();
            assertThat(factories, contains(factoryFromHibernateConfig));
        }
    }

    @Test(expectedExceptions = HibernateConfigurationException.class,
        expectedExceptionsMessageRegExp = "Persistence units are not defined in properties file")
    public void negativeTestJPA() {
        USE_JPA_CONFIG.accept(true);
        hibernate().getSessionFactories();
        fail("Exception was expected");
    }

    @Test(expectedExceptions = HibernateConfigurationException.class,
        expectedExceptionsMessageRegExp = "Hibernate configuration files are not defined in properties file")
    public void negativeTestConfig() {
        hibernate().getSessionFactories();
        fail("Exception was expected");
    }

    @AfterMethod
    @BeforeMethod
    public void clear() {
        USE_JPA_CONFIG.accept(null);
        PERSISTENCE_UNITS.accept(null);
        SESSION_FACTORY_SOURCE_PROPERTY.accept(null);
        HIBERNATE_CONFIG_FILENAMES.accept(null);
        hibernate().stop();
    }

    public static final class CustomSessionFactorySource extends SessionFactorySource {

        final static List<SessionFactory> FACTORIES = new ArrayList<>();

        @Mock
        private SessionFactory mockSessionFactory1;

        @Mock
        private SessionFactory mockSessionFactory2;

        public CustomSessionFactorySource() {
            openMocks(this);
            FACTORIES.add(mockSessionFactory1);
            FACTORIES.add(mockSessionFactory2);
        }

        @Override
        protected Set<SessionFactory> getSetOfSessionFactories() {
            return new HashSet<>(FACTORIES);
        }
    }
}

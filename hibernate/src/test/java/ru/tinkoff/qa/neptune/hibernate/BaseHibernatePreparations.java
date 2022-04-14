package ru.tinkoff.qa.neptune.hibernate;

import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.System.setProperty;
import static java.util.List.of;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.hibernate.properties.PersistenceUnits.PERSISTENCE_UNITS;
import static ru.tinkoff.qa.neptune.hibernate.properties.UseJpaConfig.USE_JPA_CONFIG;

public class BaseHibernatePreparations {

    static final List<TestEntity> TEST_ENTITIES = new ArrayList<>(of(
            new TestEntity().setId(1L)
                    .setName("Test Name 1")
                    .setArrayData(new String[]{"A", "B", "C", "D"})
                    .setListData(new ArrayList<>(of("A", "B", "C", "D"))),

            new TestEntity().setId(2L)
                    .setName("Test Name 2")
                    .setArrayData(new String[]{"E", "F", "G", "H"})
                    .setListData(new ArrayList<>(of("E", "F", "G", "H")))
    ));

    @Mock
    static EntityManagerFactory entityManagerFactory;
    @Mock
    static SessionFactory sessionFactory;
    @Mock
    static Metamodel metamodel;
    @Mock
    static EntityType<TestEntity> entityType;
    @Mock
    static Session session;
    @Mock
    static Transaction transaction;
    @Mock
    static CriteriaBuilder criteriaBuilder;
    @Mock
    static CriteriaQuery<TestEntity> criteriaQuery;
    @Mock
    static Query<TestEntity> query;

    @BeforeTest
    public void initMocks() {
        openMocks(this);
    }

    @BeforeClass
    public void prepareClass() {
        setProperty(USE_JPA_CONFIG.getName(), "true");
        setProperty(PERSISTENCE_UNITS.getName(), "testUnit");

        when(entityManagerFactory.unwrap(SessionFactory.class)).thenReturn(sessionFactory);
        when(sessionFactory.getMetamodel()).thenReturn(metamodel);
        when(metamodel.getEntities()).thenReturn(Set.of(entityType));
        when(entityType.getJavaType()).thenReturn(TestEntity.class);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(TestEntity.class, 1L)).thenReturn(TEST_ENTITIES.get(0));
        when(session.get(TestEntity.class, 2L)).thenReturn(TEST_ENTITIES.get(1));

        when(session.beginTransaction()).thenReturn(transaction);
        when(session.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).commit();

        when(sessionFactory.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(TestEntity.class)).thenReturn(criteriaQuery);
        when(session.createQuery(criteriaQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(TEST_ENTITIES);
        when(query.getSingleResult()).thenReturn(TEST_ENTITIES.get(0));
    }

    void mockPersistence(MockedStatic<Persistence> mockedStatic) {
        mockedStatic.when(() -> Persistence.createEntityManagerFactory("testUnit"))
                .thenReturn(entityManagerFactory);
    }
}

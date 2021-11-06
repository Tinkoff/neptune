package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import io.github.classgraph.ClassGraph;
import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.datanucleus.metadata.TransactionType;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.PersistenceCapable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.datanucleus.metadata.TransactionType.RESOURCE_LOCAL;
import static ru.tinkoff.qa.neptune.data.base.api.ConnectionDataReader.usesConnection;

/**
 * This class is designed to prepare data of the connection to be created and used
 * for further interaction with a data base.
 */
@Deprecated(forRemoval = true)
public abstract class DBConnectionSupplier implements Supplier<DBConnection> {

    private static final List<Class<? extends PersistableObject>> persistableClasses = new ArrayList<>();
    private static final TransactionType DEFAULT_TRANSACTION_TYPE = RESOURCE_LOCAL;

    private DBConnection connectionData;
    private final List<String> tableClassNames;


    public DBConnectionSupplier() {
        var thisClass = this.getClass();
        tableClassNames = initListOfPersistableClasses().stream()
                .filter(clazz -> usesConnection(clazz, thisClass))
                .map(Class::getName)
                .collect(toList());
    }

    private static synchronized List<Class<? extends PersistableObject>> initListOfPersistableClasses() {
        if (persistableClasses.size() > 0) {
            return persistableClasses;
        }

        persistableClasses.addAll(new ClassGraph()
                .enableAllInfo()
                .scan()
                .getSubclasses(PersistableObject.class.getName())
                .loadClasses(PersistableObject.class)
                .stream()
                .filter(clazz -> nonNull(clazz.getAnnotation(PersistenceCapable.class))
                        && !isAbstract(clazz.getModifiers())).collect(toList()));
        return persistableClasses;
    }

    /**
     * Fills given persistence unit with properties/parameters and returns filled object.
     *
     * @param toBeFilled a persistence unit to be filled
     * @return filled instance of {@link PersistenceUnitMetaData}
     */
    protected abstract PersistenceUnitMetaData fillPersistenceUnit(PersistenceUnitMetaData toBeFilled);


    @Override
    public DBConnection get() {
        connectionData = ofNullable(connectionData).orElseGet(() -> {
            var persistenceUnitMetaData = fillPersistenceUnit(new PersistenceUnitMetaData(this.getClass().getName(),
                    DEFAULT_TRANSACTION_TYPE.name(),
                    null));
            tableClassNames.forEach(persistenceUnitMetaData::addClassName);
            return new DBConnection(persistenceUnitMetaData);
        });
        return connectionData;
    }

    @Override
    public boolean equals(Object obj) {
        return ofNullable(obj).map(o ->
                this.getClass().equals(o.getClass()))
                .orElse(false);
    }
}

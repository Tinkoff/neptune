package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.datanucleus.metadata.TransactionType;
import org.reflections.Reflections;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.PersistenceCapable;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.datanucleus.metadata.TransactionType.RESOURCE_LOCAL;
import static ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection.connectionData;

/**
 * This class is designed to prepare data of the connection to be created and used
 * for further interaction with a data base.
 */
public abstract class DBConnectionSupplier implements Supplier<DBConnection> {

    private static TransactionType DEFAULT_TRANSACTION_TYPE = RESOURCE_LOCAL;
    private static final Reflections REFLECTIONS = new Reflections("");

    private DBConnection connectionData;

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
            REFLECTIONS.getSubTypesOf(PersistableObject.class).stream()
                    .filter(clazz -> nonNull(clazz.getAnnotation(PersistenceCapable.class)))
                    .map(Class::getName).collect(Collectors.toList()).forEach(persistenceUnitMetaData::addClassName);
            return connectionData(persistenceUnitMetaData);
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

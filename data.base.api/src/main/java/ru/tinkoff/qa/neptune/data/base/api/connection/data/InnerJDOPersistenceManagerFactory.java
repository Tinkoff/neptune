package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.datanucleus.PropertyNames.PROPERTY_DELETION_POLICY;

public final class InnerJDOPersistenceManagerFactory extends JDOPersistenceManagerFactory {

    private final int DEPTH = 1000000000;
    private final DBConnection connection;

    InnerJDOPersistenceManagerFactory(DBConnection connection) {
        super(connection.getData(), null);
        this.connection = connection;

        connection.getData().getClassNames()
                .forEach(this::setUpFetchGroupForClass);

    }

    private void setUpFetchGroupForClass(String className) {
        try {
            var clazz = Class.forName(className);

            stream(clazz.getDeclaredFields()).forEach(f -> {
                var annotationColumn = f.getAnnotation(Column.class);
                var annotationPersistent = f.getAnnotation(Persistent.class);

                if (annotationColumn != null || annotationPersistent != null) {
                    var defaultFetchGroup =  getFetchGroup(clazz, format("%s_%s", className, f.getName()));
                    defaultFetchGroup.addMember(f.getName());
                    defaultFetchGroup.setRecursionDepth(f.getName(), DEPTH);
                    addFetchGroups(defaultFetchGroup);
                }
            });
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public DBConnection getConnection() {
        return connection;
    }

    @Override
    public PersistenceManager getPersistenceManager() {
        var manager = super.getPersistenceManager();
        ofNullable(getFetchGroups()).ifPresent(groups -> groups.forEach(group -> manager
                .getFetchPlan()
                .addGroup(group.getName())));
        manager.getFetchPlan().setMaxFetchDepth(DEPTH);
        return manager;
    }
}

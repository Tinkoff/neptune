package ru.tinkoff.qa.neptune.data.base.api;

import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.String.format;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionStore.getKnownConnection;

/**
 * This annotation marks a class that extends {@link PersistableObject} to define proper database connection.
 * Also this annotation marks whole package to define proper database connection for all {@link PersistableObject} extenders
 * from the marked package and its subpackages.
 */
@Retention(RUNTIME) @Target({TYPE, PACKAGE})
public @interface ConnectionToUse {
    /**
     * @return a class that describes a DB connection.
     * @see  DBConnectionSupplier
     */
    Class<? extends DBConnectionSupplier> connectionSupplier();

    /**
     * This is utility that tries to read info about proper connection from {@link PersistableObject}
     */
    class ConnectionDataReader {

        /**
         * This method used to return an instance of {@link DBConnection} from the class of {@link PersistableObject}.
         *
         * @param clazz that is supposed to have information abot db connection. It has such information when
         *              the class is annotated by {@link ConnectionToUse}. Also package that contains given class
         *              is allowed to be annotated by {@link ConnectionToUse}.
         * @return an instance of {@link DBConnection}
         * @throws IllegalArgumentException when given class and its package is not annotated by {@link ConnectionToUse}
         */
        public static DBConnection getConnection(Class<? extends PersistableObject> clazz) {
            return ofNullable(clazz.getAnnotation(ConnectionToUse.class))
                    .map(connectionToUse -> getKnownConnection(connectionToUse.connectionSupplier(), true))
                    .orElseGet(() -> {
                        var pack = clazz.getPackage();
                        var connectionToUse = pack.getAnnotation(ConnectionToUse.class);

                        if (connectionToUse != null) {
                            return getKnownConnection(connectionToUse.connectionSupplier(), true);
                        }

                        for (Package p: clazz.getClassLoader().getDefinedPackages()) {
                            connectionToUse = p.getAnnotation(ConnectionToUse.class);
                            if (connectionToUse != null) {
                                return getKnownConnection(connectionToUse.connectionSupplier(), true);
                            }
                        }

                        throw new IllegalArgumentException(format("No annotation %s is defined for class %s/its packages",
                                ConnectionToUse.class,
                                clazz.getName()));
                    });
        }
    }
}

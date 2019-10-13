package ru.tinkoff.qa.neptune.data.base.api;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
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
    final class ConnectionDataReader {

        private static final ScanResult SCAN_RESULT = new ClassGraph().enableClassInfo()
                .enableAllInfo()
                .scan();

        private ConnectionDataReader() {
            super();
        }

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
                        var connectionToUse = getConnectionInfoFromPackageOf(clazz);

                        if (connectionToUse != null) {
                            return getKnownConnection(connectionToUse.connectionSupplier(), true);
                        }

                        throw new IllegalArgumentException(format("No annotation %s is defined for class %s/its packages",
                                ConnectionToUse.class,
                                clazz.getName()));
                    });
        }

        public static <T  extends DBConnectionSupplier> boolean usesConnection(Class<? extends PersistableObject> clazz,
                                                                               Class<T> supplierClass) {
            return ofNullable(clazz.getAnnotation(ConnectionToUse.class))
                    .map(connectionToUse -> connectionToUse.connectionSupplier().equals(supplierClass))
                    .orElseGet(() -> {
                        var connectionToUse = getConnectionInfoFromPackageOf(clazz);
                        return connectionToUse != null && connectionToUse.connectionSupplier().equals(supplierClass);
                    });
        }

        private static ConnectionToUse getConnectionInfoFromPackageOf(Class<? extends PersistableObject> clazz) {
            return ofNullable(clazz.getPackage())
                    .map(aPackage -> {
                        var packInfo = SCAN_RESULT.getPackageInfo(aPackage.getName());
                        while (packInfo != null) {
                            var annotationInfo = packInfo.getAnnotationInfo(ConnectionToUse.class.getName());
                            if (annotationInfo != null) {
                                return (ConnectionToUse) annotationInfo.loadClassAndInstantiate();
                            }
                            packInfo = packInfo.getParent();
                        }
                        return null;
                    })
                    .orElse(null);
        }
    }
}

package ru.tinkoff.qa.neptune.data.base.api;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionStore.getKnownConnection;

/**
 * This is utility that tries to read info about proper connection from {@link PersistableObject}
 */
@Deprecated(forRemoval = true)
public class ConnectionDataReader {

    private static final ScanResult SCAN_RESULT = new ClassGraph()
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
                    var connectionInfo = getConnectionInfoFromPackageOf(clazz);
                    if (connectionInfo != null) {
                        return getKnownConnection(connectionInfo, true);
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
                    var connectionInfo = getConnectionInfoFromPackageOf(clazz);
                    return connectionInfo != null && connectionInfo.equals(supplierClass);
                });
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends DBConnectionSupplier> getConnectionInfoFromPackageOf(Class<? extends PersistableObject> clazz) {
        return ofNullable(clazz.getPackage())
                .map(aPackage -> {
                    var packInfo = SCAN_RESULT.getPackageInfo(aPackage.getName());
                    while (packInfo != null) {
                        var annotationInfo = packInfo.getAnnotationInfo(ConnectionToUse.class.getName());
                        if (annotationInfo != null) {
                            try {
                                return (Class<? extends DBConnectionSupplier>) Class.forName(annotationInfo
                                        .getParameterValues()
                                        .getValue("connectionSupplier")
                                        .toString()
                                        .replace(".class", EMPTY));
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        }
                        packInfo = packInfo.getParent();
                    }
                    return null;
                })
                .orElse(null);
    }
}

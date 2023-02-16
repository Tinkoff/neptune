package ru.tinkoff.qa.neptune.mockito;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public final class VerificationExecutionData {

    private static final ThreadLocal<Object> WANTED = new ThreadLocal<>();

    private static final ThreadLocal<Throwable> THROWN = new ThreadLocal<>();

    private static final ThreadLocal<Object> STEP_NAME = new ThreadLocal<>();

    private static final ThreadLocal<Object> MODE = new ThreadLocal<>();

    private VerificationExecutionData() {
        super();
    }

    public static void setWanted(Object wanted) {
        WANTED.set(wanted);
    }

    public static void setThrown(Throwable throwable) {
        THROWN.set(throwable);
    }

    public static void setName(Object name) {
        STEP_NAME.set(name);
    }

    public static void setMode(Object mode) {
        MODE.set(mode);
    }

    public static void runStep() {
        try {
            var mode = MODE.get();
            var stepName = STEP_NAME.get();
            var wanted = WANTED.get();
            var thrown = THROWN.get();

            try {
                $(stepName + " " + wanted + ofNullable(mode).map(o -> SPACE + o).orElse(EMPTY), () -> {
                    if (nonNull(thrown)) {
                        if (thrown instanceof RuntimeException) {
                            throw (RuntimeException) thrown;
                        }

                        if (thrown instanceof Error) {
                            throw (Error) thrown;
                        }

                        throw new IllegalStateException(thrown);
                    }
                });
            } catch (Throwable throwable) {
                //do nothing here
            }
        } finally {
            WANTED.remove();
            THROWN.remove();
            STEP_NAME.remove();
            MODE.remove();
        }
    }
}

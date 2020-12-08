package ru.tinkoff.qa.neptune.core.api.hooks;

import java.lang.reflect.Method;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.hooks.KnownHooks.*;

/**
 * Wraps common algorithms pre-executed before some methods.
 * This is designed to be used by Junit Rules or Testng Listeners.
 */
public interface ExecutionHook {


    static List<ExecutionHook> getHooks() {
        synchronized (KNOWN_HOOKS_INITIATED_FLAG) {
            if (KNOWN_HOOKS_INITIATED_FLAG.areKnownHooksInitiated()) {
                return getKnownHooks();
            }

            initHooks();
            KNOWN_HOOKS_INITIATED_FLAG.knownHooksInitiated();
            return getKnownHooks();
        }
    }

    /**
     * Method to be executed before some other method
     *
     * @param method method to be executed after this hook
     * @param on     is an object from which {@code method} is invoked
     * @param isTest is it test method or not
     */
    void executeMethodHook(Method method, Object on, boolean isTest);
}

package ru.tinkoff.qa.neptune.core.api.hooks;

import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Map.Entry.comparingByValue;
import static java.util.Optional.ofNullable;
import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

final class KnownHooks {

    static final KnownHooksInitiated KNOWN_HOOKS_INITIATED_FLAG = new KnownHooksInitiated();
    private static final List<ExecutionHook> HOOKS = new LinkedList<>();

    static void initHooks() {
        var iterator = load(ExecutionHook.class).iterator();
        Iterable<ExecutionHook> iterable = () -> iterator;
        stream(iterable.spliterator(), false)
                .collect(toMap(executionHook -> executionHook, executionHook -> {
                    var cls = executionHook.getClass();
                    var a = cls.getAnnotation(HookOrder.class);
                    return ofNullable(a).map(hookOrder -> {
                        var priority = hookOrder.priority();
                        if (priority <= 0) {
                            throw new IllegalArgumentException(format("Value of priority should be greater than 0. " +
                                    "Please improve the %s", cls.getName()));
                        }
                        return priority;
                    }).orElse(Byte.MAX_VALUE);
                }))
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .forEachOrdered(x -> {
                    var hook = x.getKey();
                    HOOKS.add(hook);
                });
    }

    static List<ExecutionHook> getKnownHooks() {
        return HOOKS;
    }


    static final class KnownHooksInitiated {

        private boolean areKnownHooksInitiated = false;

        boolean areKnownHooksInitiated() {
            return areKnownHooksInitiated;
        }

        void knownHooksInitiated() {
            this.areKnownHooksInitiated = true;
        }
    }
}

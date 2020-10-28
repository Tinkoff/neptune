package ru.tinkoff.qa.neptune.core.api.hooks;

import io.github.classgraph.ClassGraph;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Map.Entry.comparingByValue;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

final class KnownHooks {

    static final KnownHooksInitiated KNOWN_HOOKS_INITIATED_FLAG = new KnownHooksInitiated();
    private static final List<ExecutionHook> HOOKS = new LinkedList<>();

    static void initHooks() {
        new ClassGraph()
                .enableAllInfo()
                .scan().getClassesImplementing(ExecutionHook.class.getName())
                .loadClasses(ExecutionHook.class)
                .stream()
                .collect(toMap(executionHookClass -> executionHookClass, executionHookClass -> {
                    var cls = (Class<?>) executionHookClass;
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
                    var cls = x.getKey();
                    try {
                        var constructor = cls.getConstructor();
                        constructor.setAccessible(true);
                        HOOKS.add(constructor.newInstance());
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
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

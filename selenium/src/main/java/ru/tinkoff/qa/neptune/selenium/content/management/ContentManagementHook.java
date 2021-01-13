package ru.tinkoff.qa.neptune.selenium.content.management;

import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;
import ru.tinkoff.qa.neptune.core.api.hooks.HookOrder;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_TEST_METHOD;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.ONCE;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementCommand.setCurrentCommand;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementHook.GetContentUsage.getContentUsage;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementHook.GetFrameSwitches.getFrameSwitches;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementHook.GetNavigationURL.getNavigationURL;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementHook.GetWindow.getWindow;
import static ru.tinkoff.qa.neptune.selenium.content.management.Navigate.NavigateToReader.getBrowserPageToNavigate;
import static ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame.SwitchToFrameReader.getFrame;

@HookOrder(priority = 10)
public final class ContentManagementHook implements ExecutionHook {

    private static final ThreadLocal<Map<Class<?>, BrowserContentUsage>> DEFAULT_CONTENT_USAGE
            = new ThreadLocal<>();
    private static final ThreadLocal<Map<Class<?>, ContentManagementCommand>> DEFAULT_CONTENT_MANAGEMENT_COMMAND
            = new ThreadLocal<>();

    private static <T> T getFromClass(Class<?> clazz, Object on, BiFunction<Class<?>, Object, T> toGet) {
        var cls = clazz;
        T result = null;
        while (isNull(result) && nonNull(cls)) {
            result = toGet.apply(clazz, on);
            cls = cls.getSuperclass();
        }
        return result;
    }

    private static <T> T getFromClass(Class<?> clazz, Function<Class<?>, T> toGet) {
        return getFromClass(clazz, null, (aClass, o) -> toGet.apply(aClass));
    }

    private static ContentManagementCommand getDefaultContentManagementCommand(Class<?> cls, Object on, boolean isTest) {
        if (isNull(DEFAULT_CONTENT_USAGE.get())) {
            DEFAULT_CONTENT_USAGE.set(new HashMap<>());
            DEFAULT_CONTENT_MANAGEMENT_COMMAND.set(new HashMap<>());
        }

        var usageMap = DEFAULT_CONTENT_USAGE.get();
        var defaultCommands = DEFAULT_CONTENT_MANAGEMENT_COMMAND.get();

        if (usageMap.containsKey(cls)) {
            return ofNullable(usageMap.get(cls))
                    .map(browserContentUsage -> {
                        var defaultCommand = defaultCommands.get(cls);
                        if (isNull(defaultCommand)) {
                            return null;
                        }

                        if (browserContentUsage.equals(ONCE) && defaultCommand.isExecuted()) {
                            return null;
                        }

                        if (!isTest && browserContentUsage.equals(FOR_EVERY_TEST_METHOD)) {
                            return null;
                        }

                        return defaultCommand;
                    })
                    .orElse(null);
        }

        var usage = ofNullable(getFromClass(cls, getContentUsage())).orElse(ONCE);
        var getWindow = getFromClass(cls, getWindow());
        var navigationURLSupplier = getFromClass(cls, on, getNavigationURL());
        var switchesToFrames = getFromClass(cls, getFrameSwitches());

        usageMap.put(cls, usage);
        if (isNull(getWindow) && isNull(navigationURLSupplier) && isNull(switchesToFrames)) {
            return null;
        }

        var command = new ContentManagementCommand()
                .setWindowSupplier(getWindow)
                .setNavigateTo(navigationURLSupplier)
                .setFrameSuppliers(switchesToFrames);

        defaultCommands.put(cls, command);

        if (!isTest && usage.equals(FOR_EVERY_TEST_METHOD)) {
            return null;
        }

        return command;
    }

    @Override
    public void executeMethodHook(Method method, Object on, boolean isTest) {
        var defaultCommand = getDefaultContentManagementCommand(on.getClass(), on, isTest);

        var getWindow = getWindow().apply(method);
        var navigationURLSupplier = getNavigationURL().apply(method, on);
        var switchesToFrames = getFrameSwitches().apply(method);
        ContentManagementCommand methodCommand;
        if (isNull(getWindow) && isNull(navigationURLSupplier) && isNull(switchesToFrames)) {
            methodCommand = null;
        } else {
            methodCommand = new ContentManagementCommand()
                    .setWindowSupplier(getWindow)
                    .setNavigateTo(navigationURLSupplier)
                    .setFrameSuppliers(switchesToFrames);
        }

        if (isNull(defaultCommand)) {
            setCurrentCommand(methodCommand);
            return;
        }

        if (isNull(methodCommand)) {
            setCurrentCommand(defaultCommand);
            return;
        }

        setCurrentCommand(defaultCommand.mergeTo(methodCommand));
    }

    static class GetContentUsage<T extends AnnotatedElement> implements Function<T, BrowserContentUsage> {

        static <T extends AnnotatedElement> GetContentUsage<T> getContentUsage() {
            return new GetContentUsage<>();
        }

        @Override
        public BrowserContentUsage apply(T annotatedElement) {
            return ofNullable(annotatedElement.getAnnotation(UseDefaultBrowserContent.class))
                    .map(UseDefaultBrowserContent::value)
                    .orElse(null);
        }
    }

    static class GetWindow<T extends AnnotatedElement> implements Function<T, GetWindowSupplier> {

        static <T extends AnnotatedElement> GetWindow<T> getWindow() {
            return new GetWindow<>();
        }

        @Override
        public GetWindowSupplier apply(T t) {
            return ofNullable(t.getAnnotation(SwitchToWindow.class))
                    .map(SwitchToWindow.SwitchToWindowReader::getWindow)
                    .orElse(null);
        }
    }

    static class GetNavigationURL<T extends AnnotatedElement> implements BiFunction<T, Object, Supplier<String>> {

        static <T extends AnnotatedElement> GetNavigationURL<T> getNavigationURL() {
            return new GetNavigationURL<>();
        }

        @Override
        public Supplier<String> apply(T t, Object o) {
            if (nonNull(t.getAnnotation(Navigate.class))) {
                return () -> getBrowserPageToNavigate(o, t);
            } else {
                return null;
            }
        }
    }

    static class GetFrameSwitches<T extends AnnotatedElement> implements Function<T, List<GetFrameSupplier>> {

        static <T extends AnnotatedElement> GetFrameSwitches<T> getFrameSwitches() {
            return new GetFrameSwitches<>();
        }

        @Override
        public List<GetFrameSupplier> apply(T t) {
            var sf = t.getAnnotationsByType(SwitchToFrame.class);
            if (sf.length == 0) {
                return null;
            }
            return stream(sf).map(switchToFrame -> getFrame(switchToFrame, t)).collect(toList());
        }
    }
}

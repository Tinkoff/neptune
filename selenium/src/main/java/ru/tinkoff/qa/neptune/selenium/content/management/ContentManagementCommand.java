package ru.tinkoff.qa.neptune.selenium.content.management;

import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

public final class ContentManagementCommand extends SequentialActionSupplier<WebDriver, WebDriver, ContentManagementCommand> {

    private static final ThreadLocal<ContentManagementCommand> CURRENT_COMMAND = new ThreadLocal<>();

    private GetWindowSupplier getWindowSupplier;
    private Supplier<String> navigateTo;
    private List<GetFrameSupplier> getFrameSuppliers;
    private boolean isExecuted;

    private boolean addNavigationParams;
    private boolean addWindowParams;
    private boolean addFrameParams;

    ContentManagementCommand() {
        super("Change active browser content");
        performOn(driver -> driver);
    }

    private static <T extends SequentialGetStepSupplier<?, ?, WebDriver, ?, ?>> T
    setDriver(T setTo, WebDriver driver) {
        try {
            var m = SequentialGetStepSupplier.class.getDeclaredMethod("from", Object.class);
            m.setAccessible(true);
            m.invoke(setTo, driver);
            return setTo;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static ContentManagementCommand getCurrentCommand() {
        var result = CURRENT_COMMAND.get();
        CURRENT_COMMAND.set(null);
        return result;
    }

    static void setCurrentCommand(ContentManagementCommand currentCommand) {
        CURRENT_COMMAND.set(currentCommand);
    }

    @Override
    protected void performActionOn(WebDriver value) {
        var window = ofNullable(getWindowSupplier)
                .map(w -> inBrowser().get(setDriver(w, value)))
                .orElse(null);

        ofNullable(window).ifPresent(w -> inBrowser().switchTo(w));
        ofNullable(navigateTo).ifPresent(s -> inBrowser().navigateTo(s.get(), setDriver(currentWindow(), value)));

        ofNullable(getFrameSuppliers).ifPresent(ss -> {
            value.switchTo().defaultContent();
            ss.forEach(s -> inBrowser().switchTo(setDriver(s, value)));
        });

        isExecuted = true;
    }

    ContentManagementCommand setWindowSupplier(GetWindowSupplier getWindowSupplier) {
        this.getWindowSupplier = getWindowSupplier;
        return this;
    }

    ContentManagementCommand setNavigateTo(Supplier<String> navigateTo) {
        this.navigateTo = navigateTo;
        return this;
    }

    ContentManagementCommand setFrameSuppliers(List<GetFrameSupplier> getFrameSuppliers) {
        this.getFrameSuppliers = getFrameSuppliers;
        return this;
    }

    boolean isExecuted() {
        return isExecuted;
    }

    ContentManagementCommand mergeTo(ContentManagementCommand mergeTo) {
        if (nonNull(getWindowSupplier) && isNull(mergeTo.getWindowSupplier) && addWindowParams) {
            mergeTo.setWindowSupplier(getWindowSupplier);
            isExecuted = true;
        }

        if (nonNull(navigateTo) && isNull(mergeTo.navigateTo) && addNavigationParams) {
            mergeTo.setNavigateTo(navigateTo);
            isExecuted = true;
        }

        if (nonNull(getFrameSuppliers) && isNull(mergeTo.getFrameSuppliers) && addFrameParams) {
            mergeTo.setFrameSuppliers(getFrameSuppliers);
            isExecuted = true;
        }
        return mergeTo;
    }

    ContentManagementCommand setAddNavigationParams(boolean addNavigationParams) {
        this.addNavigationParams = addNavigationParams;
        return this;
    }

    ContentManagementCommand setAddWindowParams(boolean addWindowParams) {
        this.addWindowParams = addWindowParams;
        return this;
    }

    ContentManagementCommand setAddFrameParams(boolean addFrameParams) {
        this.addFrameParams = addFrameParams;
        return this;
    }
}

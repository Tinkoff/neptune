package ru.tinkoff.qa.neptune.selenium.content.management;

import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

public final class ContentManagementCommand extends SequentialActionSupplier<WebDriver, WebDriver, ContentManagementCommand> {

    private static final ThreadLocal<ContentManagementCommand> CURRENT_COMMAND = new ThreadLocal<>();

    private GetWindowSupplier getWindowSupplier;
    private String navigateTo;
    private List<GetFrameSupplier> getFrameSuppliers;
    private boolean isExecuted;

    protected ContentManagementCommand() {
        super("Change active browser content");
    }

    private static <T extends SequentialGetStepSupplier<?, ?, WebDriver, ?, ?>> T
    setDriver(T setTo, WebDriver driver) {
        var tClass = setTo.getClass();
        try {
            var m = tClass.getMethod("from", Object.class);
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
        checkNotNull(currentCommand);
        CURRENT_COMMAND.set(currentCommand);
    }

    @Override
    protected void performActionOn(WebDriver value) {
        var window = ofNullable(getWindowSupplier)
                .map(w -> inBrowser().get(setDriver(w, value)))
                .orElse(null);

        if (isNotBlank(navigateTo)) {
            var w = ofNullable(window)
                    .orElseGet(() -> inBrowser().get(setDriver(currentWindow(), value)));
            inBrowser().navigateTo(navigateTo, w);
        }


        ofNullable(getFrameSuppliers).ifPresent(ss -> {
            value.switchTo().defaultContent();
            ss.forEach(s -> inBrowser().switchTo(setDriver(s, value)));
        });

        isExecuted = true;
    }

    ContentManagementCommand setGetWindowSupplier(GetWindowSupplier getWindowSupplier) {
        this.getWindowSupplier = getWindowSupplier;
        return this;
    }

    ContentManagementCommand setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
        return this;
    }

    ContentManagementCommand setGetFrameSuppliers(List<GetFrameSupplier> getFrameSuppliers) {
        this.getFrameSuppliers = getFrameSuppliers;
        return this;
    }

    boolean isExecuted() {
        return isExecuted;
    }
}

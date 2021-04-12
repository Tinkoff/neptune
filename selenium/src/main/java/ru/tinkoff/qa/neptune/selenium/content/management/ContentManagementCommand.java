package ru.tinkoff.qa.neptune.selenium.content.management;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.ToUrl.toUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchActionSupplier.to;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;

@Description("Change active browser content")
@MaxDepthOfReporting(0)
@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebDriverImageCaptor.class)
public final class ContentManagementCommand extends SequentialActionSupplier<SeleniumStepContext, SeleniumStepContext, ContentManagementCommand> {

    private static final ThreadLocal<ContentManagementCommand> CURRENT_COMMAND = new ThreadLocal<>();

    private GetWindowSupplier getWindowSupplier;
    private Supplier<String> navigateTo;
    private List<GetFrameSupplier> getFrameSuppliers;
    private boolean isExecuted;

    private boolean addNavigationParams;
    private boolean addWindowParams;
    private boolean addFrameParams;

    ContentManagementCommand() {
        super();
        performOn(context -> context);
    }

    @Description("Change active browser content")
    public static ContentManagementCommand getCurrentCommand() {
        var result = CURRENT_COMMAND.get();
        CURRENT_COMMAND.set(null);
        return result;
    }

    static void setCurrentCommand(ContentManagementCommand currentCommand) {
        CURRENT_COMMAND.set(currentCommand);
    }

    @Override
    protected void howToPerform(SeleniumStepContext value) {
        ofNullable(getWindowSupplier).ifPresent(w -> to(w).get().performAction(value));
        ofNullable(navigateTo).ifPresent(s -> toUrl(s.get()).get().performAction(value));

        ofNullable(getFrameSuppliers).ifPresent(ss -> {
            to(defaultContent()).get().performAction(value);
            ss.forEach(s -> to(s).get().performAction(value));
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

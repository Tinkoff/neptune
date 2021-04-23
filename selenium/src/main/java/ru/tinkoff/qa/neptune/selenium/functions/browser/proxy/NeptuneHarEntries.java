package ru.tinkoff.qa.neptune.selenium.functions.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.ArrayList;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

@Description("Caught traffic from browser")
class NeptuneHarEntries extends ArrayList<HarEntry> {

    @Override
    public String toString() {
        if (size() == 0) {
            return "<>";
        }

        return translate(this);
    }
}

package ru.tinkoff.qa.neptune.core.api.logical.lexemes;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * It is used to link descriptions of operators in descriptions of NOT-expressions
 */
@Description("not")
public final class Not {
    public static final Not NOT_LEXEME = new Not();

    private Not() {
        super();
    }

    public String toString() {
        return translate(this);
    }
}

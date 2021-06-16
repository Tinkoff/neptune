package ru.tinkoff.qa.neptune.core.api.logical.lexemes;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * It is used to link descriptions of operators in descriptions of OR-expressions
 */
@Description("or")
public final class Or {

    public static final Or OR_LEXEME = new Or();

    private Or() {
        super();
    }

    public String toString() {
        return translate(this);
    }
}

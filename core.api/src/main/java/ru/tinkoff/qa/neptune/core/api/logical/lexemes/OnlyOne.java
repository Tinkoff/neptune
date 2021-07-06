package ru.tinkoff.qa.neptune.core.api.logical.lexemes;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * It is used to link descriptions of operators in descriptions of XOR-expressions
 */
@Description("xor")
public final class OnlyOne {

    public static final OnlyOne ONLY_ONE_LEXEME = new OnlyOne();

    private OnlyOne() {
        super();
    }

    public String toString() {
        return translate(this);
    }
}

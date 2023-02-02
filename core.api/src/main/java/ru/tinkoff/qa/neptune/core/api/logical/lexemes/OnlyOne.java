package ru.tinkoff.qa.neptune.core.api.logical.lexemes;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

/**
 * It is used to link descriptions of operators in descriptions of XOR-expressions
 */
@Description("xor")
public final class OnlyOne extends SelfDescribed {

    public static final OnlyOne ONLY_ONE_LEXEME = new OnlyOne();

    private OnlyOne() {
        super();
    }
}

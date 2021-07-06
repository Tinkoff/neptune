package ru.tinkoff.qa.neptune.rabbit.mq.function;

import com.google.common.base.Supplier;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;

public final class ArgSupplier implements Supplier<AdditionalArguments> {
    private AdditionalArguments args;

    @Override
    public AdditionalArguments get() {
        return args;
    }

    public ArgSupplier setArgs(AdditionalArguments args) {
        this.args = args;
        return this;
    }
}

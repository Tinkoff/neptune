package com.github.toy.constructor.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public interface PerformStep<THIS extends PerformStep<THIS>> {

    private THIS perform(@Nullable String description, @NotNull Consumer<THIS> stepsConsumer) {
        checkNotNull(description);
        checkNotNull(stepsConsumer);
        stepsConsumer.accept((THIS) this);
        return (THIS) this;
    }
}

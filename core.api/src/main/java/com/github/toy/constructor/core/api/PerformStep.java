package com.github.toy.constructor.core.api;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface PerformStep<THIS extends PerformStep<THIS>> {

    default THIS perform(String description, Consumer<THIS> stepsConsumer) {
        checkArgument(isNotBlank(description), "Description should not be empty");
        checkNotNull(stepsConsumer);
        stepsConsumer.accept((THIS) this);
        return (THIS) this;
    }
}

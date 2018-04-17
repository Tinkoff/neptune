package com.github.toy.constructor.core.api.test;

import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.core.api.Refreshable;
import com.github.toy.constructor.core.api.proxy.CreateWith;
import com.github.toy.constructor.core.api.proxy.ProviderOfEmptyParameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.toy.constructor.core.api.Refreshable.refresh;
import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;

public class RefreshTest {

    private RefreshableStep refreshableStep;

    @Test
    public void checkRefreshTest() throws Exception {
        refreshableStep = getSubstituted(RefreshableStep.class);
        refreshableStep.perform(action("Add elements to some list", refreshableStep1 -> {
            refreshableStep.getListToRefresh().add(1);
            refreshableStep.getListToRefresh().add(true);
            refreshableStep.getListToRefresh().add("String");
        }));

        assertThat("Check added values",
                refreshableStep.get(toGet("Get elements", RefreshableStep::getListToRefresh)),
                contains(1, true, "String"));

        refresh(this);
        assertThat("Check values",
                refreshableStep.get(toGet("Get elements", RefreshableStep::getListToRefresh)),
                emptyCollectionOf(Object.class));
    }

    @CreateWith(provider = ProviderOfEmptyParameters.class)
    private static class RefreshableStep implements PerformStep<RefreshableStep>, GetStep<RefreshableStep>, Refreshable {

        private final List<Object> listToRefresh = new ArrayList<>();

        @Override
        public void refresh() {
            listToRefresh.clear();
        }

        public List<Object> getListToRefresh() {
            return listToRefresh;
        }
    }
}

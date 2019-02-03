package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.ProviderOfEmptyParameters;

import java.util.ArrayList;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable.refresh;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;

public class RefreshTest {

    private RefreshableStep refreshableStep;

    @Test
    public void checkRefreshTest() {
        refreshableStep = getProxied(RefreshableStep.class);
        refreshableStep.perform(StoryWriter.action("Add elements to some list", refreshableStep1 -> {
            refreshableStep.getListToRefresh().add(1);
            refreshableStep.getListToRefresh().add(true);
            refreshableStep.getListToRefresh().add("String");
        }));

        assertThat("Check added values",
                refreshableStep.get(StoryWriter.toGet("Get elements", RefreshableStep::getListToRefresh)),
                contains(1, true, "String"));

        refresh(this);
        assertThat("Check values",
                refreshableStep.get(StoryWriter.toGet("Get elements", RefreshableStep::getListToRefresh)),
                emptyCollectionOf(Object.class));
    }

    @CreateWith(provider = ProviderOfEmptyParameters.class)
    private static class RefreshableStep implements ActionStepContext<RefreshableStep>, GetStepContext<RefreshableStep>, Refreshable {

        private final List<Object> listToRefresh = new ArrayList<>();

        protected RefreshableStep() {
            super();
        }

        @Override
        public void refresh() {
            listToRefresh.clear();
        }

        List<Object> getListToRefresh() {
            return listToRefresh;
        }
    }
}

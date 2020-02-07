package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.util.ArrayList;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable.refreshContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;

public class RefreshTest {

    @Test
    public void checkRefreshTest() {
        RefreshableStep.refreshable.getListToRefresh().add(1);
        RefreshableStep.refreshable.getListToRefresh().add(true);
        RefreshableStep.refreshable.getListToRefresh().add("String");

        assertThat("Check added values",
                RefreshableStep.refreshable.getListToRefresh(),
                contains(1, true, "String"));

        refreshContext(RefreshableStep.class);
        assertThat("Check values",
                RefreshableStep.refreshable.getListToRefresh(),
                emptyCollectionOf(Object.class));
    }

    private static class RefreshableStep extends Context<RefreshableStep> implements ContextRefreshable {

        static final RefreshableStep refreshable = getInstance(RefreshableStep.class);

        private final List<Object> listToRefresh = new ArrayList<>();

        protected RefreshableStep() {
            super();
        }

        @Override
        public void refreshContext() {
            listToRefresh.clear();
        }

        List<Object> getListToRefresh() {
            return listToRefresh;
        }
    }
}

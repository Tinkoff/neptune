package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.test.*;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByQuerySupplier.list;

public class SelectByTypedQuery extends BaseDbOperationTest {

    @Test
    public void selectListTestWithoutAnyCondition() {
        assertThat(dataBaseSteps.select(list(ofType(Author.class))), hasSize(2));
    }
}

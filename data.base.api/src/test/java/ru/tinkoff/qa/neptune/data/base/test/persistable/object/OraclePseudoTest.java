package ru.tinkoff.qa.neptune.data.base.test.persistable.object;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle.PlanningGroup;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle.QSkill;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle.Skill;

import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;

public class OraclePseudoTest extends BaseDbOperationTest {

    @Test
    public void test() {
        var defaultSkill = dataBaseSteps.select(oneOf(Skill.class, byJDOQuery(QSkill.class)
                .where(qSkill -> qSkill.title.eq("1 - Обработка входящих звонков в УП"))));

        var mainSkill = dataBaseSteps.select(oneOf(Skill.class,
                byJDOQuery(QSkill.class).where(qSkill -> qSkill.title.eq("1 - multiform"))));

        var extraSkill = dataBaseSteps.select(oneOf(Skill.class,
                byJDOQuery(QSkill.class).where(qSkill -> qSkill.title.eq("1 - agreement"))));

        var pg = dataBaseSteps.insert(new PlanningGroup("Автосмена скилла 2",
                false,
                extraSkill,
                mainSkill,
                defaultSkill,
                true,
                1));
    }
}

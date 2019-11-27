package ru.tinkoff.qa.neptune.data.base.test.persistable.object;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.WofOracleConnectionSupplier;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Calendar.*;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static ru.tinkoff.qa.neptune.data.base.api.data.operations.UpdateExpression.change;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.row;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.listOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.rows;
import static ru.tinkoff.qa.neptune.data.base.api.queries.ids.Id.id;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLResultQueryParams.byJDOResultQuery;

public class OraclePseudoTest extends BaseDbOperationTest {

    @Test(enabled = false)
    public void test() {
        var defaultSkill = dataBaseSteps.select(oneOf(Skill.class, byJDOQuery(QSkill.class)
                .addWhere(qSkill -> qSkill.title.eq("1 - Обработка входящих звонков в УП"))));

        var mainSkill = dataBaseSteps.select(oneOf(Skill.class,
                byJDOQuery(QSkill.class).addWhere(qSkill -> qSkill.title.eq("1 - multiform"))));

        var mainSkill2 = dataBaseSteps.select(oneOf(Skill.class,
                byJDOQuery(QSkill.class).addWhere(qSkill -> qSkill.title.eq("1 - multiform"))));

        mainSkill.equals(mainSkill2);

        var pg = dataBaseSteps.insert(new PlanningGroup("Автосмена скилла 2",
                false,
                new Skill(
                        "Новый экстра скилл",
                        mainSkill.getTaskType(),
                        false,
                        true,
                        false),
                mainSkill,
                defaultSkill,
                true,
                1));

        var skill = pg.getExtraSkill();

        var skill2 = dataBaseSteps.select(oneOf(Skill.class, byJDOQuery(QSkill.class)
                .addWhere(qSkill -> qSkill.title.eq("Новый экстра скилл 2"))));

        var pg2 = dataBaseSteps.update(pg,
                change("Сменить название экстра скилла на Новый экстра скилл 3",
                        planningGroup -> planningGroup.getExtraSkill().setTitle("Новый экстра скилл 3")));

        var deleted = dataBaseSteps.delete(pg2, skill2);
    }

    @Test
    public void pseudoTest2() {
        var unitType = dataBaseSteps.select(oneOf(OrganizationUnitType.class, id(2)));

        var orgUnit = dataBaseSteps.insert(new OrganizationUnit("ManagementForAuto" + randomNumeric(3), unitType, 0, false,
                false, "managementforauto" + randomNumeric(3)));

        dataBaseSteps.delete(orgUnit);

        unitType = dataBaseSteps.insert(new OrganizationUnitType().setTitleEng("Какой-то тип орг структуры")
                .setTitleEng("Something")
                .setTitleRus("Что-то")
                .setUnitTypeId(10));

        dataBaseSteps.delete(unitType);
    }

    @Test
    public void pseudoTest3() {

        var user = dataBaseSteps.select(oneOf(User.class, byJDOQuery(QUser.class)
                .addWhere(qUser -> qUser.login.eq("notplangroup2"))
                .orderBy(qUser -> qUser.userId.asc())));

        var editor = dataBaseSteps.select(oneOf(User.class, id(3)));


        var rec = dataBaseSteps.insert(new DwhPlanningGroupSchedule(new PlanningGroupShedule(user,
                setTime(new java.util.Date(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2, 0),
                setTime(new java.util.Date(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2, 30),
                true, addDays(new Date(), 1), editor, 0),
                "notplangroup2",
                setTime(new java.util.Date(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2, 0),
                setTime(new java.util.Date(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2, 30),
                true, new java.util.Date(), "sbear", "Нет плана", "Входящие продажи"));

        var planningGroup1 = dataBaseSteps.select(oneOf(PlanningGroupShedule.class, "select * from " +
                        "PLANNING_GROUP_SCHEDULE where USER_ID = ? " +
                        "and IS_WISHED = 1 and STATUS_ID = 0 and INTERVAL_START > ?",
                user.getUserId(),
                new java.sql.Date(System.currentTimeMillis())));

        var planningGroup2 = dataBaseSteps.select(oneOf(PlanningGroupShedule.class, byJDOQuery(QPlanningGroupShedule.class)
                .addWhere(qPlanningGroupShedule -> qPlanningGroupShedule.user.eq(user)
                        .and(qPlanningGroupShedule.isWished.eq(true))
                        .and(qPlanningGroupShedule.statusId.eq(0))
                        .and(qPlanningGroupShedule.intervalStart.gt(new Date())))));

        var planningGroup3 = dataBaseSteps.select(oneOf(PlanningGroupShedule.class, byJDOQuery(QPlanningGroupShedule.class)
                .addWhere(qPlanningGroupShedule -> qPlanningGroupShedule.user.eq(user)
                        .and(qPlanningGroupShedule.isWished.eq(true))
                        .and(qPlanningGroupShedule.statusId.eq(0))
                        .and(qPlanningGroupShedule.intervalStart.lt(new Date(System.currentTimeMillis()))))));

        var deleted = dataBaseSteps.delete(rec, rec.getPlanningGroupShedule());
    }

    @Test
    public void test4() {
        //20309158#126907078
        var hiTask = dataBaseSteps.select(oneOf(HiTask.class, id("20309158#126907078")));
        var hiTaskContactInformation = dataBaseSteps.select(oneOf(HiTaskContactInformation.class, id(hiTask)));
        var hiTaskContactPhoneList = dataBaseSteps.select(listOf(HiTaskContactPhone.class,
                byJDOQuery(QHiTaskContactPhone.class).addWhere(qHiTaskContactPhone -> qHiTaskContactPhone
                        .hiTask
                        .eq(hiTask))));
    }


    @Test
    public void test5() {
        var USER_LOGIN = "notplangroup1";

        var user = dataBaseSteps.select(oneOf(User.class, byJDOQuery(QUser.class)
                .addWhere(qUser -> qUser.login.eq(USER_LOGIN))));

        var editor = dataBaseSteps.select(oneOf(User.class, byJDOQuery(QUser.class)
                .addWhere(qUser -> qUser.userId.eq(3))));

        dataBaseSteps.delete(listOf(PlanningGroupShedule.class, byJDOQuery(QPlanningGroupShedule.class)
                .addWhere(qShedule -> qShedule.user.eq(user).and(qShedule.intervalStart.gt(new Date())))));

        dataBaseSteps.delete(listOf(DwhPlanningGroupSchedule.class, byJDOQuery(QDwhPlanningGroupSchedule.class)
                .addWhere(qDWHShedule -> qDWHShedule.operatorLogin.eq(USER_LOGIN).and(qDWHShedule.intervalStart.gt(new Date())))));

        dataBaseSteps.delete(listOf(OperatorSchedule.class, byJDOQuery(QOperatorSchedule.class)
                .addWhere(qShedule -> qShedule.user.eq(user).and(qShedule.intervalStart.gt(new Date())))));

        dataBaseSteps.delete(listOf(UserToPlanningGroup.class,
                byJDOQuery(QUserToPlanningGroup.class).addWhere(qPg -> qPg.user.eq(user))));

        var hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        dataBaseSteps.insert(
                new OperatorSchedule(user,
                        setTime(new Date(), hour + 2, 0),
                        setTime(new Date(), hour + 2, 30),
                        true, new Date(), editor),
                new OperatorSchedule(user,
                        setTime(new Date(), hour + 2, 30),
                        setTime(new Date(), hour + 3, 0),
                        true, new Date(), editor));

        var count = dataBaseSteps.select(listOf(OperatorSchedule.class, byJDOQuery(QOperatorSchedule.class)
                .addWhere(qOperatorSchedule -> qOperatorSchedule.user.eq(user))
                .addWhere(qOperatorSchedule -> qOperatorSchedule.intervalStart.gt(new Date()))
                .addWhere(qOperatorSchedule -> qOperatorSchedule.isActive)))
                .size();

        var count2 = count;
    }

    @Test
    public void test6() {
        var calcPeriodDuration = dataBaseSteps.select(row(CalculationPeriod.class, byJDOResultQuery(QCalculationPeriod.class)
                .resultField(qCalculationPeriod -> qCalculationPeriod.startDate.max())
                .resultField(qCalculationPeriod -> qCalculationPeriod.endDate.max())));
    }

    @Test
    public void test7() {
        var USER_LOGIN = "notplangroup2";

        var user = dataBaseSteps.select(oneOf(User.class, byJDOQuery(QUser.class)
                .addWhere(qUser -> qUser.login.eq(USER_LOGIN))));

        var editor = dataBaseSteps.select(oneOf(User.class, byJDOQuery(QUser.class)
                .addWhere(qUser -> qUser.userId.eq(3))));

        dataBaseSteps.delete(listOf(PlanningGroupShedule.class, byJDOQuery(QPlanningGroupShedule.class)
                .addWhere(qShedule -> qShedule.user.eq(user).and(qShedule.intervalStart.gt(new Date())))));

        dataBaseSteps.delete(listOf(DwhPlanningGroupSchedule.class, byJDOQuery(QDwhPlanningGroupSchedule.class)
                .addWhere(qDWHShedule -> qDWHShedule.operatorLogin.eq(USER_LOGIN).and(qDWHShedule.intervalStart.gt(new Date())))));

        dataBaseSteps.delete(listOf(OperatorSchedule.class, byJDOQuery(QOperatorSchedule.class)
                .addWhere(qShedule -> qShedule.user.eq(user).and(qShedule.intervalStart.gt(new Date())))));

        dataBaseSteps.delete(listOf(UserToPlanningGroup.class,
                byJDOQuery(QUserToPlanningGroup.class).addWhere(qPg -> qPg.user.eq(user))));

        var planningGroup = dataBaseSteps.select(oneOf(PlanningGroup.class, id(1)));
        dataBaseSteps.insert(new UserToPlanningGroup(user, planningGroup,
                null, false));

        var hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        var pg1 = new PlanningGroupShedule(user,
                setTime(new Date(), hour + 2, 0),
                setTime(new Date(), hour + 2, 30),
                true, addDays(new Date(), 1), editor, 0);

        var pg2 = new PlanningGroupShedule(user,
                setTime(new Date(), hour + 2, 30),
                setTime(new Date(), hour + 3, 0),
                false, new Date(), editor, 1);

        var pg3 = new PlanningGroupShedule(user,
                setTime(new Date(), hour + 3, 0),
                setTime(new Date(), hour + 3, 30),
                true, new Date(), editor, 1);

        var pg4 = new PlanningGroupShedule(user,
                setTime(new Date(), hour + 3, 30),
                setTime(new Date(), hour + 4, 0),
                false, new Date(), editor, 0);

        dataBaseSteps.insert(new DwhPlanningGroupSchedule(pg1, "notplangroup2",
                        setTime(new Date(), hour + 2, 0),
                        setTime(new Date(), hour + 2, 30),
                        true, new Date(), "sbear", "Нет плана", "Входящие продажи"),
                new DwhPlanningGroupSchedule(pg2, "notplangroup2",
                        setTime(new Date(), hour + 2, 30),
                        setTime(new Date(), hour + 3, 0),
                        false, new Date(), "sbear", "Одобрено", "Входящие продажи"),
                new DwhPlanningGroupSchedule(pg3, "notplangroup2",
                        setTime(new Date(), hour + 3, 0),
                        setTime(new Date(), hour + 3, 30),
                        true, new Date(), "sbear", "Одобрено", "Входящие продажи"),
                new DwhPlanningGroupSchedule(pg4, "notplangroup2",
                        setTime(new Date(), hour + 3, 30),
                        setTime(new Date(), hour + 4, 0),
                        false, new Date(), "sbear", "Отменено", "Входящие продажи")
        );
    }

    @Test
    public void test8() {
        var MANAGEMENT_NAME = "OperatorGroupTests";

        var organization = dataBaseSteps.select(oneOf(OrganizationUnit.class, id(21)));
        var unitType = dataBaseSteps.select(oneOf(OrganizationUnitType.class, id(2)));

        var management = dataBaseSteps.select(oneOf(OrganizationUnit.class, byJDOQuery(QOrganizationUnit.class)
                .addWhere(qOrganizationUnit -> qOrganizationUnit.unitCode.eq(MANAGEMENT_NAME.toLowerCase()))));
    }

    @Test
    public void test9() {
        var taskTypeIds = dataBaseSteps.select(rows("Select HI_TASK_TYPE.hi_task_type_id " +
                        "from HI_TASK_TYPE " +
                        "left join SKILL on skill.hi_task_type_id = HI_TASK_TYPE.hi_task_type_id AND skill.is_deleted = 0 " +
                        "left join queue_group ON queue_group.hi_task_type_id =  HI_TASK_TYPE.hi_task_type_id " +
                        "where HI_TASK_TYPE.is_deleted = 0 " +
                        "GROUP BY HI_TASK_TYPE.hi_task_type_id " +
                        "Having COUNT(SKILL.hi_task_type_id) > 0 and COUNT(queue_group.hi_task_type_id) = 0",
                WofOracleConnectionSupplier.class));

        var taskTypeId = taskTypeIds.get(new Random().nextInt(taskTypeIds.size() - 1)).get(0);
        var taskIdInt = Integer.valueOf(((Number) taskTypeId).intValue());
        var taskType = dataBaseSteps.select(oneOf(TaskType.class, id(taskIdInt)));
    }

    @Test
    public void test10() {
        var list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);

        var list2 = new ArrayList<>(list.stream().collect(Collectors.toList()));
    }

    @Test
    public void test12() {
        var result = dataBaseSteps.select(rows("SELECT TELEPHONY_QUEUE_ID\n" +
                        "FROM SKILL_TELEPHONY_INCOMING_QUEUE\n" +
                        "WHERE SKILL_ID = :skillId \n" +
                        "UNION\n" +
                        "SELECT PDS_TELEPHONY_QUEUE_ID\n" +
                        "FROM SKILL\n" +
                        "WHERE SKILL_ID = :skillId \n" +
                        "  AND PDS_TELEPHONY_QUEUE_ID is not null\n" +
                        "  AND IS_DELETED = 0\n" +
                        "UNION\n" +
                        "SELECT INCOMING_CALLS_QUEUE_ID\n" +
                        "FROM SKILL\n" +
                        "WHERE SKILL_ID = :skillId \n" +
                        "  AND INCOMING_CALLS_QUEUE_ID is not null\n" +
                        "  AND IS_DELETED = 0",
                WofOracleConnectionSupplier.class,
                Map.of("skillId", 5017)));
    }

    @Test
    public void test11() {
        var result = dataBaseSteps.select(rows(SkillQueueGroupLink.class, byJDOResultQuery(QSkillQueueGroupLink.class)
                .resultField(q -> q.queueGroupId)
                .addWhere(q -> q.skillId.skillId.eq(41))
                .addWhere(q -> q.queueGroupId.isDeleted.eq(false))));
    }


    public static java.util.Date setTime(java.util.Date date, int hours, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(HOUR_OF_DAY, hours);
        cal.set(MINUTE, minutes);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
        return cal.getTime();
    }
}

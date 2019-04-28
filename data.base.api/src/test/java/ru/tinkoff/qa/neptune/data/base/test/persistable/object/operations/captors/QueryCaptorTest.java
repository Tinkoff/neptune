package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.captors;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Book;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Catalog;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.QCatalog;

import java.util.List;
import java.util.function.Function;
import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.data.base.api.query.GetSelectedFunction.selected;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SQLQueryBuilderFunction.bySQL;
import static ru.tinkoff.qa.neptune.data.base.api.query.SQLQueryBuilderFunction.byTypedSQL;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier.listOfTypeByIds;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier.aSingleByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier.aSingleOfTypeById;
import static ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.captors.TestStringInjector.INJECTED;

public class QueryCaptorTest extends BaseDbOperationTest {

    private static final String QUERY = "Select * from Books join Authors on Books.Author = Authors.Id " +
            "where Books.YearOfFinishing >= 1820 order by YearOfFinishing asc";

    @BeforeClass
    public static void beforeClass() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
    }

    @BeforeMethod
    public void beforeEachTest() {
        INJECTED.clear();
    }


    @Test
    public void testOfSelectSingleItemByTypedQuery() {
        QCatalog c = QCatalog.candidate();
        var querySpec = ofType(Catalog.class).where(c.book.name.eq("Ruslan and Ludmila")
                .or(c.book.author.lastName.eq("Castaneda")))
                .orderBy(c.book.id.desc());
        var query = querySpec.apply(dataBaseSteps.getCurrentPersistenceManager());


        Catalog catalogItem = dataBaseSteps.get(selected(aSingleByQuery(querySpec)));
        assertThat(INJECTED, contains("Query:" + query));

        var secondaryReturned = toGet("Stored value", (Function<DataBaseStepContext, Catalog>)
                dataBaseStepContext -> catalogItem)
                .makeStringCaptureOnFinish();

        dataBaseSteps.get(secondaryReturned);
        assertThat(INJECTED, contains("Query:" + query));
    }

    @Test
    public void testOfSelectMultipleItemsByTypedQuery() {
        QCatalog c = QCatalog.candidate();
        var querySpec = ofType(Catalog.class).where(c.book.name.eq("Ruslan and Ludmila")
                .or(c.book.author.lastName.eq("Castaneda")))
                .orderBy(c.book.id.desc());
        var query = querySpec.apply(dataBaseSteps.getCurrentPersistenceManager());


        List<Catalog> catalogItems = dataBaseSteps.get(selected(listByQuery(querySpec)));
        assertThat(INJECTED, containsInAnyOrder(equalTo("Query:" + query),
                containsString("Resulted collection:")));

        var secondaryReturned = toGet("Stored value", (Function<DataBaseStepContext, List<Catalog>>)
                dataBaseStepContext -> catalogItems)
                .makeStringCaptureOnFinish();

        dataBaseSteps.get(secondaryReturned);
        assertThat(INJECTED, containsInAnyOrder(equalTo("Query:" + query),
                containsString("Resulted collection:"),
                containsString("Resulted collection:")));
    }

    @Test
    public void testOfSelectSingleItemByTypedSqlQuery() {
        var querySpec = byTypedSQL(Book.class, QUERY);
        var query = querySpec.apply(dataBaseSteps.getCurrentPersistenceManager());


        Book book = dataBaseSteps.get(selected(aSingleByQuery(querySpec)));
        assertThat(INJECTED, contains("Query:" + query));

        var secondaryReturned = toGet("Stored value", (Function<DataBaseStepContext, Book>)
                dataBaseStepContext -> book)
                .makeStringCaptureOnFinish();

        dataBaseSteps.get(secondaryReturned);
        assertThat(INJECTED, contains("Query:" + query));
    }

    @Test
    public void testOfSelectMultipleItemsByTypedSqlQuery() {
        var querySpec = byTypedSQL(Book.class, QUERY);
        var query = querySpec.apply(dataBaseSteps.getCurrentPersistenceManager());


        List<Book> books = dataBaseSteps.get(selected(listByQuery(querySpec)));
        assertThat(INJECTED, containsInAnyOrder(equalTo("Query:" + query),
                containsString("Resulted collection:")));

        var secondaryReturned = toGet("Stored value", (Function<DataBaseStepContext, List<Book>>)
                dataBaseStepContext -> books)
                .makeStringCaptureOnFinish();

        dataBaseSteps.get(secondaryReturned);
        assertThat(INJECTED, containsInAnyOrder(equalTo("Query:" + query),
                containsString("Resulted collection:"),
                containsString("Resulted collection:")));
    }

    @Test
    public void testOfSelectSingleItemByUntypedSqlQuery() {
        var querySpec = bySQL(QUERY);
        var query = querySpec.apply(dataBaseSteps.getCurrentPersistenceManager());


        List<Object> bookRecord = dataBaseSteps.get(selected(aSingleByQuery(querySpec)));
        assertThat(INJECTED, containsInAnyOrder(equalTo("Query:" + query),
                containsString("Resulted collection:")));

        var secondaryReturned = toGet("Stored value", (Function<DataBaseStepContext, List<Object>>)
                dataBaseStepContext -> bookRecord)
                .makeStringCaptureOnFinish();

        dataBaseSteps.get(secondaryReturned);
        assertThat(INJECTED, containsInAnyOrder(equalTo("Query:" + query),
                containsString("Resulted collection:"),
                containsString("Resulted collection:")));
    }

    @Test
    public void testOfSelectMultipleItemsByUntypedSqlQuery() {
        var querySpec = bySQL(QUERY);
        var query = querySpec.apply(dataBaseSteps.getCurrentPersistenceManager());


        List<List<Object>> bookRecords = dataBaseSteps.get(selected(listByQuery(querySpec)));
        assertThat(INJECTED, containsInAnyOrder(equalTo("Query:" + query),
                containsString("Resulted collection:")));

        var secondaryReturned = toGet("Stored value", (Function<DataBaseStepContext, List<List<Object>>>)
                dataBaseStepContext -> bookRecords)
                .makeStringCaptureOnFinish();

        dataBaseSteps.get(secondaryReturned);
        assertThat(INJECTED, containsInAnyOrder(equalTo("Query:" + query),
                containsString("Resulted collection:"),
                containsString("Resulted collection:")));
    }

    @Test
    public void testOfSelectSingleItemById() {
        var book = dataBaseSteps.get(selected(aSingleOfTypeById(Book.class, 1)));
        assertThat(INJECTED, contains("Query: Known Id: 1"));

        var secondaryReturned = toGet("Stored value", (Function<DataBaseStepContext, Book>)
                dataBaseStepContext -> book)
                .makeStringCaptureOnFinish();

        dataBaseSteps.get(secondaryReturned);
        assertThat(INJECTED,contains("Query: Known Id: 1"));
    }

    @Test
    public void testOfSelectMultipleItemsByIds() {
        List<Book> books = dataBaseSteps.get(selected(listOfTypeByIds(Book.class, 1, 2)));
        assertThat(INJECTED, contains(equalTo("Query: Known Ids: 1, 2"),
                containsString("Resulted collection:")));

        var secondaryReturned = toGet("Stored value", (Function<DataBaseStepContext, List<Book>>)
                dataBaseStepContext -> books)
                .makeStringCaptureOnFinish();

        dataBaseSteps.get(secondaryReturned);
        assertThat(INJECTED, contains(equalTo("Query: Known Ids: 1, 2"),
                containsString("Resulted collection:"),
                containsString("Resulted collection:")));
    }

    @AfterClass
    public static void afterClass() {
        getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
    }
}

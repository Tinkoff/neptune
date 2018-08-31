package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.DBSequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.test.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByIdsSupplier.listOfTypeByIds;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByQuerySupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectSingleObjectByQuerySupplier.aSingleByQuery;

@SuppressWarnings({"ConstantConditions"})
public class MultiThreadTest extends BaseDbOperationTest {

    @DataProvider(parallel = true)
    public Object[][] query() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        return new Object[][]{
                {aSingleByQuery(ofType(Author.class)
                        .where(qAuthor.firstName.eq("Carlos")
                                .and(qAuthor.lastName.eq("Castaneda"))))},

                {aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila")))},

                {aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila")))},

                {aSingleByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.name.eq("Journey to Ixtlan")
                                .and(qCatalog.book.author.firstName.eq("Carlos"))))},

                {listByQuery(ofType(Author.class)
                        .where(qAuthor.firstName.eq("Jack")
                                .and(qAuthor.lastName.eq("London"))))},

                {aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila")))},

                {aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila")))},

                {listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.name.eq("The Sea-Wolf")
                                .and(qCatalog.book.author.firstName.eq("Jack"))))},

                {listOfTypeByIds(Catalog.class, 1)},

                {aSingleByQuery(ofType(Catalog.class).where(qCatalog.book.name.eq("Ruslan and Ludmila")
                        .or(qCatalog.isbn.eq("0-671-73246-3")) //<Journey to Ixtlan
                        .or(qCatalog.book.author.lastName.eq("Pushkin")))
                        .orderBy(qCatalog.recordId.desc()))},

                {aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila")))},

                {aSingleByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.name.eq("Journey to Ixtlan")
                                .and(qCatalog.book.author.firstName.eq("Carlos"))))},

                {aSingleByQuery(ofType(Author.class)
                        .where(qAuthor.firstName.eq("Carlos")
                                .and(qAuthor.lastName.eq("Castaneda"))))},

                {aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila")))},

                {aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila")))},

                {listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.name.eq("The Old Man and the Sea")
                                .and(qCatalog.book.author.firstName.eq("Ernest"))))},

                {listOfTypeByIds(Catalog.class, 1)},

                {aSingleByQuery(ofType(Catalog.class).where(qCatalog.book.name.eq("Ruslan and Ludmila")
                        .or(qCatalog.isbn.eq("0-671-73246-3")) //<Journey to Ixtlan
                        .or(qCatalog.book.author.lastName.eq("Pushkin")))
                        .orderBy(qCatalog.recordId.desc()))}
        };
    }


    @Test(threadPoolSize = 4, dataProvider = "query")
    public <T> void multiThreadTest(DBSequentialGetStepSupplier<T, ?, ?> dbSequentialGetStepSupplier) {
        assertThat(dataBaseSteps.get(dbSequentialGetStepSupplier), not(nullValue()));
    }
}

package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.query.GetSelectedFunction;
import ru.tinkoff.qa.neptune.data.base.api.test.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.data.base.api.query.GetSelectedFunction.selected;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier.listOfTypeByIds;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier.aSingleByQuery;

@SuppressWarnings({"ConstantConditions"})
public class MultiThreadTest extends BaseDbOperationTest {

    @DataProvider(parallel = true)
    public Object[][] query() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        return new Object[][]{
                {selected(aSingleByQuery(ofType(Author.class)
                        .where(qAuthor.firstName.eq("Carlos")
                                .and(qAuthor.lastName.eq("Castaneda")))))},

                {selected(aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila"))))},

                {selected(aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila"))))},

                {selected(aSingleByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.name.eq("Journey to Ixtlan")
                                .and(qCatalog.book.author.firstName.eq("Carlos")))))},

                {selected(listByQuery(ofType(Author.class)
                        .where(qAuthor.firstName.eq("Jack")
                                .and(qAuthor.lastName.eq("London")))))},

                {selected(aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila"))))},

                {selected(aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila"))))},

                {selected(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.name.eq("The Sea-Wolf")
                                .and(qCatalog.book.author.firstName.eq("Jack")))))},

                {selected(listOfTypeByIds(Catalog.class, 1))},

                {selected(aSingleByQuery(ofType(Catalog.class).where(qCatalog.book.name.eq("Ruslan and Ludmila")
                        .or(qCatalog.book.author.lastName.eq("Castaneda")))
                        .orderBy(qCatalog.book.id.desc())))},

                {selected(aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila"))))},

                {selected(aSingleByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.name.eq("Journey to Ixtlan")
                                .and(qCatalog.book.author.firstName.eq("Carlos")))))},

                {selected(aSingleByQuery(ofType(Author.class)
                        .where(qAuthor.firstName.eq("Carlos")
                                .and(qAuthor.lastName.eq("Castaneda")))))},

                {selected(aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila"))))},

                {selected(aSingleByQuery(ofType(Book.class)
                        .where(qBook.name.eq("Ruslan and Ludmila"))))},

                {selected(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.name.eq("The Old Man and the Sea")
                                .and(qCatalog.book.author.firstName.eq("Ernest")))))},

                {selected(listOfTypeByIds(Catalog.class, 1))},

                {selected(aSingleByQuery(ofType(Catalog.class).where(qCatalog.book.name.eq("Ruslan and Ludmila")
                        .or(qCatalog.book.author.lastName.eq("Castaneda")))
                        .orderBy(qCatalog.book.id.desc())))}
        };
    }


    @Test(threadPoolSize = 4, dataProvider = "query")
    public void multiThreadTest(GetSelectedFunction<?> selectedFunction) {
        assertThat(dataBaseSteps.get(selectedFunction), not(nullValue()));
    }
}

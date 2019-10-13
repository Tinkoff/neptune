package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.datanucleus.store.rdbms.query.JDOQLQuery;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.ConnectionDataSupplierForTestBase1;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Author;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Book;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.QBook;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.listOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.GetSelectedFunction.selected;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SQLQueryBuilderFunction.bySQL;
import static ru.tinkoff.qa.neptune.data.base.api.query.SQLQueryBuilderFunction.byTypedSQL;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier.aSingleByQuery;

public class SelectBySqlQuery extends BaseDbOperationTest {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000000");

    private static final String QUERY = "Select * from Books join Authors on Books.Author = Authors.Id " +
            "where Books.YearOfFinishing >= 1820 order by YearOfFinishing asc";
    private static final String QUERY2 = "Select MIN(YearOfFinishing) from Books";

    private static final QBook Q_BOOK = QBook.candidate();

    @Test
    public void selectListByTypedSqlTest() {
        List<Author> authors = dataBaseSteps.get(selected(listByQuery(byTypedSQL(Author.class, QUERY))));
        List<Book> books = dataBaseSteps.get(selected(listByQuery(byTypedSQL(Book.class, QUERY))));

        List<Book> books2 = dataBaseSteps.get(selected(listByQuery(ofType(Book.class)
                .where(Q_BOOK.yearOfFinishing.gteq(1820))
                .orderBy(Q_BOOK.yearOfFinishing.asc()))));
        List<Author> authors2 = books2.stream().map(Book::getAuthor).collect(toList());

        assertThat(authors2, contains(authors.toArray()));
        assertThat(books2, contains(books.toArray()));
    }

    @Test
    public void selectSingleByTypedSqlTest() {
        Author author = dataBaseSteps.get(selected(aSingleByQuery(byTypedSQL(Author.class, QUERY))));
        Book book = dataBaseSteps.get(selected(aSingleByQuery(byTypedSQL(Book.class, QUERY))));

        Book book2 = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(Q_BOOK.yearOfFinishing.gteq(1820))
                .orderBy(Q_BOOK.yearOfFinishing.asc()))));
        Author author2 = book2.getAuthor();

        assertThat(author2, equalTo(author));
        assertThat(book2, equalTo(book));
    }

    @Test
    public void selectListByUnTypedSqlTest() {
        List<List<Object>> booksAndAuthors = dataBaseSteps.get(selected(listByQuery(bySQL(QUERY)))).subList(0, 1);
        List<Book> books = dataBaseSteps.get(selected(listByQuery(ofType(Book.class)
                .where(Q_BOOK.yearOfFinishing.gteq(1820))
                .orderBy(Q_BOOK.yearOfFinishing.asc()).range(0, 1))));
        List<List<Object>> booksAndAuthors2 = books.stream()
                .map(book -> {
                    List<Object> result = new ArrayList<>();
                    result.add(book.getId());
                    result.add(book.getName());
                    result.add(book.getAuthor().getId());
                    result.add(book.getYearOfFinishing());

                    result.add(book.getAuthor().getId());
                    result.add(book.getAuthor().getFirstName());
                    result.add(book.getAuthor().getLastName());
                    result.add(SIMPLE_DATE_FORMAT.format(book.getAuthor().getBirthDate()));
                    result.add(SIMPLE_DATE_FORMAT.format(book.getAuthor().getDeathDate()));
                    result.add(book.getAuthor().getBiography());
                    return result;
                }).collect(toList());

        assertThat(booksAndAuthors2, contains(booksAndAuthors.toArray()));
    }

    @Test
    public void selectSingleByUnTypedSqlTest() {
        List<Object> bookAndAuthor = dataBaseSteps.get(selected(aSingleByQuery(bySQL(QUERY))));
        Book book = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(Q_BOOK.name.eq("Ruslan and Ludmila")))));

        List<Object> bookAndAuthor2 = new ArrayList<>();
        bookAndAuthor2.add(book.getId());
        bookAndAuthor2.add(book.getName());
        bookAndAuthor2.add(book.getAuthor().getId());
        bookAndAuthor2.add(book.getYearOfFinishing());

        bookAndAuthor2.add(book.getAuthor().getId());
        bookAndAuthor2.add(book.getAuthor().getFirstName());
        bookAndAuthor2.add(book.getAuthor().getLastName());
        bookAndAuthor2.add(SIMPLE_DATE_FORMAT.format(book.getAuthor().getBirthDate()));
        bookAndAuthor2.add(SIMPLE_DATE_FORMAT.format(book.getAuthor().getDeathDate()));
        bookAndAuthor2.add(book.getAuthor().getBiography());

        assertThat(bookAndAuthor2, contains(bookAndAuthor.toArray()));

        byJDOQuery(QBook.class).where(qBook -> qBook.author.eq(book.getAuthor())
                .and(qBook.id.eq(1))
                .and(qBook.name.eq("")))
                .addOrderBy(qBook -> qBook.id.asc())
                .setGroupBy(qBook -> qBook.author)
                .having(qBook -> qBook.id.lteq(1))
                .range(0,1);

        new DataBaseStepContext().select(listOf(Book.class, byJDOQuery(QBook.class)
                .where(qBook -> qBook.author.eq(book.getAuthor())
                        .and(qBook.id.eq(1))
                        .and(qBook.name.eq("")))
                .addOrderBy(qBook -> qBook.id.asc())
                .setGroupBy(qBook -> qBook.author)
                .having(qBook -> qBook.id.lteq(1))
                .range(0,1))
                .timeOut(ofSeconds(20)));

        new DataBaseStepContext().select(listOf(Book.class, 1, 2, 3));

        new DataBaseStepContext().select(listOf(Book.class, "Select * from Books " +
                "join Authors on Books.Author = Authors.Id " +
                "where Books.YearOfFinishing >= 1820 order by YearOfFinishing asc"));

        new DataBaseStepContext().select(listOf("Select * from Books " +
                "join Authors on Books.Author = Authors.Id " +
                "where Books.YearOfFinishing >= 1820 order by YearOfFinishing asc",
                ConnectionDataSupplierForTestBase1.class));
    }

    @Test
    public void aggregatedListResultSelect() {
        List<List<Object>> result = dataBaseSteps.get(selected(listByQuery(bySQL(QUERY2))));
        assertThat(result, contains(contains(1820)));
    }

    @Test
    public void aggregatedSingleResultSelect() {
        List<Object> result = dataBaseSteps.get(selected(aSingleByQuery(bySQL(QUERY2))));
        assertThat(result, contains(1820));
    }
}

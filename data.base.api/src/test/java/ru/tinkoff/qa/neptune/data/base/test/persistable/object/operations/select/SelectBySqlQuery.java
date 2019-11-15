package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.ConnectionDataSupplierForTestBase1;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Book;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.QBook;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Author;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.row;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.listOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.rows;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;

public class SelectBySqlQuery extends BaseDbOperationTest {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000000");

    private static final String QUERY = "Select * from Books join Authors on Books.Author = Authors.Id " +
            "where Books.YearOfFinishing >= ? order by YearOfFinishing asc";
    private static final String QUERY2 = "Select MIN(YearOfFinishing) from Books";

    @Test
    public void selectListByTypedSqlTest() {
        List<Author> authors = dataBaseSteps.select(listOf(Author.class, QUERY, 1820));
        List<Book> books = dataBaseSteps.select(listOf(Book.class, QUERY, 1820));

        List<Book> books2 = dataBaseSteps.get(listOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())));

        List<Author> authors2 = books2.stream().map(Book::getAuthor).collect(toList());
        assertThat(authors2, contains(authors.toArray()));
        assertThat(books2, contains(books.toArray()));
    }

    @Test
    public void selectSingleByTypedSqlTest() {
        Author author = dataBaseSteps.select(oneOf(Author.class, QUERY, 1820));
        Book book = dataBaseSteps.select(oneOf(Book.class, QUERY, 1820));

        Book book2 = dataBaseSteps.select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())));
        Author author2 = book2.getAuthor();

        assertThat(author2, equalTo(author));
        assertThat(book2, equalTo(book));
    }

    @Test
    public void selectListByUnTypedSqlTest() {
        var booksAndAuthors = dataBaseSteps.select(rows(QUERY,
                ConnectionDataSupplierForTestBase1.class,
                1820))
                .subList(0, 1);
        List<Book> books = dataBaseSteps.select(listOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())
                .range(0, 1)));

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
        var bookAndAuthor = dataBaseSteps.select(row(QUERY,
                ConnectionDataSupplierForTestBase1.class,
                1820));

        Book book = dataBaseSteps.select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.name.eq("Ruslan and Ludmila"))));

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
    }

    @Test
    public void aggregatedListResultSelect() {
        var result = dataBaseSteps.select(rows(QUERY2, ConnectionDataSupplierForTestBase1.class));
        assertThat(result, contains(contains(1820)));
    }

    @Test
    public void aggregatedSingleResultSelect() {
        var result = dataBaseSteps.select(row(QUERY2, ConnectionDataSupplierForTestBase1.class));
        assertThat(result, contains(1820));
    }
}

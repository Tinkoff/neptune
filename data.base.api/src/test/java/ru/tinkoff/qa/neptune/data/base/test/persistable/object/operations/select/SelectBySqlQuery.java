package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.ConnectionDataSupplierForTestBase1;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Book;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.QBook;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Author;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext.inDataBase;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.row;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.listOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.rows;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;

public class SelectBySqlQuery extends BaseDbOperationTest {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000000");

    private static final String QUERY = "Select * from Books join Authors on Books.Author = Authors.Id " +
            "where Books.YearOfFinishing >= ? order by YearOfFinishing asc";

    private static final String QUERY1 = "Select * from Books join Authors on Books.Author = Authors.Id " +
            "where Books.YearOfFinishing >= :year order by YearOfFinishing asc";

    private static final String QUERY2 = "Select MIN(YearOfFinishing) from Books";

    @Test
    public void selectListByTypedSqlTest() {
        List<Author> authors = inDataBase().select(listOf(Author.class, QUERY, 1820));
        List<Book> books = inDataBase().select(listOf(Book.class, QUERY, 1820));

        List<Book> books2 = inDataBase().select(listOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())));

        List<Author> authors2 = books2.stream().map(Book::getAuthor).collect(toList());
        assertThat(authors2, contains(authors.toArray()));
        assertThat(books2, contains(books.toArray()));
    }

    @Test
    public void selectListByTypedSqlTest2() {
        List<Author> authors = inDataBase().select(listOf(Author.class, QUERY1, Map.of("year", 1820)));
        List<Book> books = inDataBase().select(listOf(Book.class, QUERY1, Map.of("year", 1820)));

        List<Book> books2 = inDataBase().select(listOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())));

        List<Author> authors2 = books2.stream().map(Book::getAuthor).collect(toList());
        assertThat(authors2, contains(authors.toArray()));
        assertThat(books2, contains(books.toArray()));
    }

    @Test
    public void selectSingleByTypedSqlTest() {
        Author author = inDataBase().select(oneOf(Author.class, QUERY, 1820));
        Book book = inDataBase().select(oneOf(Book.class, QUERY, 1820));

        Book book2 = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())));
        Author author2 = book2.getAuthor();

        assertThat(author2, equalTo(author));
        assertThat(book2, equalTo(book));
    }

    @Test
    public void selectSingleByTypedSqlTest2() {
        Author author = inDataBase().select(oneOf(Author.class, QUERY1, Map.of("year", 1820)));
        Book book = inDataBase().select(oneOf(Book.class, QUERY1, Map.of("year", 1820)));

        Book book2 = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())));
        Author author2 = book2.getAuthor();

        assertThat(author2, equalTo(author));
        assertThat(book2, equalTo(book));
    }

    @Test
    public void selectListByUnTypedSqlTest() {
        var booksAndAuthors = inDataBase().select(rows(QUERY,
                ConnectionDataSupplierForTestBase1.class,
                1820));

        List<Book> books = inDataBase().select(listOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())));

        assertThat(booksAndAuthors.getColumn(1),
                contains(books.stream().map(Book::getName).toArray(String[]::new)));

        assertThat(booksAndAuthors.getColumn(5),
                contains(books.stream().map(book -> book.getAuthor().getFirstName()).toArray(String[]::new)));

        assertThat(booksAndAuthors.getColumn(6),
                contains(books.stream().map(book -> book.getAuthor().getLastName()).toArray(String[]::new)));

        assertThat(booksAndAuthors.getColumn(7, o -> {
                    try {
                        return SIMPLE_DATE_FORMAT.parse(o.toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }),
                contains(books.stream().map(book -> book.getAuthor().getBirthDate()).toArray(Date[]::new)));

        assertThat(booksAndAuthors.getColumn(8, o -> {
                    try {
                        return SIMPLE_DATE_FORMAT.parse(o.toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }),
                contains(books.stream().map(book -> book.getAuthor().getDeathDate()).toArray(Date[]::new)));

        assertThat(booksAndAuthors.getColumn(9),
                contains(books.stream().map(book -> book.getAuthor().getBiography()).toArray(String[]::new)));
    }

    @Test
    public void selectListByUnTypedSqlTest2() {
        var booksAndAuthors = inDataBase().select(rows(QUERY1,
                ConnectionDataSupplierForTestBase1.class,
                Map.of("year", 1820)));

        List<Book> books = inDataBase().select(listOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.yearOfFinishing.gteq(1820))
                .orderBy(qBook -> qBook.yearOfFinishing.asc())));

        assertThat(booksAndAuthors.getColumn(1),
                contains(books.stream().map(Book::getName).toArray(String[]::new)));

        assertThat(booksAndAuthors.getColumn(5),
                contains(books.stream().map(book -> book.getAuthor().getFirstName()).toArray(String[]::new)));

        assertThat(booksAndAuthors.getColumn(6),
                contains(books.stream().map(book -> book.getAuthor().getLastName()).toArray(String[]::new)));

        assertThat(booksAndAuthors.getColumn(7, o -> {
                    try {
                        return SIMPLE_DATE_FORMAT.parse(o.toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }),
                contains(books.stream().map(book -> book.getAuthor().getBirthDate()).toArray(Date[]::new)));

        assertThat(booksAndAuthors.getColumn(8, o -> {
                    try {
                        return SIMPLE_DATE_FORMAT.parse(o.toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }),
                contains(books.stream().map(book -> book.getAuthor().getDeathDate()).toArray(Date[]::new)));

        assertThat(booksAndAuthors.getColumn(9),
                contains(books.stream().map(book -> book.getAuthor().getBiography()).toArray(String[]::new)));
    }

    @Test
    public void selectSingleByUnTypedSqlTest() {
        var bookAndAuthor = inDataBase().select(row(QUERY,
                ConnectionDataSupplierForTestBase1.class,
                1820));

        Book book = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
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
    public void selectSingleByUnTypedSqlTest2() {
        var bookAndAuthor = inDataBase().select(row(QUERY1,
                ConnectionDataSupplierForTestBase1.class,
                Map.of("year", 1820)));

        Book book = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
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
        var result = inDataBase().select(rows(QUERY2, ConnectionDataSupplierForTestBase1.class));
        assertThat(result, contains(contains(1820)));
    }

    @Test
    public void aggregatedSingleResultSelect() {
        var result = inDataBase().select(row(QUERY2, ConnectionDataSupplierForTestBase1.class));
        assertThat(result, contains(1820));
    }
}

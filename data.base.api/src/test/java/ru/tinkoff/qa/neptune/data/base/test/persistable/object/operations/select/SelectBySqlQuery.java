package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.test.Author;
import ru.tinkoff.qa.neptune.data.base.api.test.Book;
import ru.tinkoff.qa.neptune.data.base.api.test.QBook;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SQLQueryBuilderFunction.bySQL;
import static ru.tinkoff.qa.neptune.data.base.api.query.SQLQueryBuilderFunction.byTypedSQL;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByQuerySupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectSingleObjectByQuerySupplier.aSingleByQuery;

public class SelectBySqlQuery extends BaseDbOperationTest {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000000");

    private static final String QUERY = "Select * from Books join Authors on Books.Author = Authors.Id " +
            "where Books.YearOfFinishing >= 1820 order by YearOfFinishing asc";

    private static final QBook Q_BOOK = QBook.candidate();

    @Test
    public void selectListByTypedSqlTest() {
        List<Author> authors = dataBaseSteps.get(listByQuery(byTypedSQL(Author.class, QUERY)));
        List<Book> books = dataBaseSteps.get(listByQuery(byTypedSQL(Book.class, QUERY)));

        List<Book> books2 = dataBaseSteps.get(listByQuery(ofType(Book.class).where(Q_BOOK.yearOfFinishing.gteq(1820))
                .orderBy(Q_BOOK.yearOfFinishing.asc())));
        List<Author> authors2 = books2.stream().map(Book::getAuthor).collect(toList());

        assertThat(authors2, contains(authors.toArray()));
        assertThat(books2, contains(books.toArray()));
    }

    @Test
    public void selectSingleByTypedSqlTest() {
        Author author = dataBaseSteps.get(aSingleByQuery(byTypedSQL(Author.class, QUERY)));
        Book book = dataBaseSteps.get(aSingleByQuery(byTypedSQL(Book.class, QUERY)));

        Book book2 = dataBaseSteps.get(aSingleByQuery(ofType(Book.class).where(Q_BOOK.yearOfFinishing.gteq(1820))
                .orderBy(Q_BOOK.yearOfFinishing.asc())));
        Author author2 = book2.getAuthor();

        assertThat(author2, equalTo(author));
        assertThat(book2, equalTo(book));
    }

    @Test
    public void selectListByUnTypedSqlTest() {
        List<List<Object>> booksAndAuthors = dataBaseSteps.get(listByQuery(bySQL(QUERY))).subList(0, 1);
        List<Book> books = dataBaseSteps.get(listByQuery(ofType(Book.class)
                .where(Q_BOOK.yearOfFinishing.gteq(1820))
                .orderBy(Q_BOOK.yearOfFinishing.asc()).range(0, 1)));
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
        List<Object> bookAndAuthor = dataBaseSteps.get(aSingleByQuery(bySQL(QUERY)));
        Book book = dataBaseSteps.get(aSingleByQuery(ofType(Book.class).where(Q_BOOK.name.eq("Ruslan and Ludmila"))));

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
}

package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.store.and.delete;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.test.*;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static java.util.Calendar.*;
import static org.apache.commons.lang3.time.DateUtils.addHours;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByQuerySupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectSingleObjectByQuerySupplier.aSingleByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.store.StoreSequentialGetStepSupplier.storedObjects;

public class StoreAndDeleteTest extends BaseDbOperationTest {

    private static final String HUGO_BIOGRAPHY = "was a French poet, novelist, and dramatist of the Romantic movement. Hugo is considered to be one of the greatest and best-known French writers. " +
            "Outside of France, his most famous works are the novels Les Misérables, 1862, and The Hunchback of Notre-Dame (French: Notre-Dame de Paris), 1831. " +
            "In France, Hugo is known primarily for his poetry collections, such as Les Contemplations (The Contemplations) and La Légende des siècles (The Legend of the Ages).\n" +
            "\n" +
            "Hugo was at the forefront of the romantic literary movement with his play Cromwell and drama Hernani. " +
            "Many of his works have inspired music, both during his lifetime and after his death, including the musicals Notre-Dame de Paris and Les Misérables. " +
            "He produced more than 4,000 drawings in his lifetime, and campaigned for social causes such as the abolition of capital punishment.\n" +
            "\n" +
            "Though a committed royalist when he was young, Hugo's views changed as the decades passed, and he became a passionate supporter of republicanism; " +
            "his work touches upon most of the political and social issues and the artistic trends of his time." +
            " He is buried in the Panthéon in Paris. His legacy has been honoured in many ways, including his portrait being placed on French currency.";

    private Date birthDate;
    private Date deathDate;

    private Author victorHugo;

    private Book theHunchbackOfNotreDame;
    private Book theLegendOfTheAges;

    private Publisher wordsworthEditions;
    private Publisher createSpaceIndependent;

    private Catalog catalogItemOfNotreDame;
    private Catalog catalogItemOfLegendOfTheAges;

    @BeforeSuite
    public void beforeSuite() {
        Calendar c1 = Calendar.getInstance();
        c1.set(YEAR, 1802);
        c1.set(MONTH, 0);
        c1.set(DAY_OF_MONTH, 7);
        c1.set(HOUR, 0);
        c1.set(MINUTE, 0);
        c1.set(SECOND, 0);
        c1.set(MILLISECOND, 0);
        birthDate = c1.getTime();

        Calendar c2 = Calendar.getInstance();
        c1.set(YEAR, 1885);
        c1.set(MONTH, 4);
        c1.set(DAY_OF_MONTH, 22);
        c1.set(HOUR, 0);
        c1.set(MINUTE, 0);
        c1.set(SECOND, 0);
        c1.set(MILLISECOND, 0);
        deathDate = c1.getTime();

        victorHugo = new Author().setFirstName("Victor").setLastName("Hugo")
                .setBirthDate(birthDate)
                .setDeathDate(deathDate).setBiography(HUGO_BIOGRAPHY);

        theHunchbackOfNotreDame = new Book().setName("The Hunchback of Notre-Dame")
                .setYearOfFinishing(1831)
                .setAuthor(victorHugo);
        theLegendOfTheAges = new Book().setName("The Legend of the Ages")
                .setYearOfFinishing(1883)
                .setAuthor(victorHugo);

        wordsworthEditions = new Publisher().setName("Wordsworth Editions Ltd");
        createSpaceIndependent = new Publisher().setName("CreateSpace Independent Publishing Platform");

        catalogItemOfNotreDame = new Catalog().setBook(theHunchbackOfNotreDame)
                .setIsbn("978-1853260681")
                .setPublisher(wordsworthEditions).setYearOfPublishing(1998);
        catalogItemOfLegendOfTheAges = new Catalog().setBook(theLegendOfTheAges)
                .setIsbn("978-1539940296")
                .setPublisher(createSpaceIndependent).setYearOfPublishing(2016);
    }

    @Test
    public void storeANewObject() {
        List<Catalog> catologItems = dataBaseSteps.get(storedObjects(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));
        assertThat(victorHugo.getId(), greaterThan(0));
        assertThat(theHunchbackOfNotreDame.getId(), greaterThan(0));
        assertThat(theLegendOfTheAges.getId(), greaterThan(0));
        assertThat(wordsworthEditions.getId(), greaterThan(0));
        assertThat(createSpaceIndependent.getId(), greaterThan(0));
        assertThat(catalogItemOfNotreDame.getRecordId(), greaterThan(0));
        assertThat(catalogItemOfLegendOfTheAges.getRecordId(), greaterThan(0));

        QCatalog qCatalog = QCatalog.candidate();

        assertThat(dataBaseSteps.select(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                                .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                                .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                                .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                                .and(qCatalog.book.author.biography.eq(HUGO_BIOGRAPHY))
                                .and(qCatalog.publisher.eq(wordsworthEditions).or(qCatalog.publisher.eq(createSpaceIndependent)))
                        ))
                        .withCondition(condition("Author was born in 1802 and died in 1885", catalog -> {
                            Author author = catalog.getBook().getAuthor();
                            return author.getBirthDate().equals(birthDate) && author.getDeathDate().equals(deathDate);
                        }))),
                containsInAnyOrder(catologItems.toArray()));
    }

    @Test(dependsOnMethods = "storeANewObject")
    public void storeChanges() {
        String newBiography = format("%s\n %s", HUGO_BIOGRAPHY, "Hugo published his first novel the year following his marriage (Han d'Islande, 1823), " +
                "and his second three years later (Bug-Jargal, 1826). Between 1829 and 1840, he published five more volumes of poetry " +
                "(Les Orientales, 1829; Les Feuilles d'automne, 1831; Les Chants du crépuscule, 1835; Les Voix intérieures, " +
                "1837; and Les Rayons et les Ombres, 1840), cementing his reputation as one of the greatest elegiac and lyric poets of his time.");
        dataBaseSteps.get(storedObjects(victorHugo
                .setBiography(newBiography)
                .setBirthDate(addHours(birthDate, 1))));

        QAuthor qAuthor = QAuthor.candidate();
        Author victorHugo2 = dataBaseSteps.get(aSingleByQuery(ofType(Author.class).where(qAuthor.id.eq(victorHugo.getId()))));
        assertThat(victorHugo2.getBiography(), equalTo(newBiography));
        assertThat(victorHugo2.getBirthDate(), equalTo(addHours(birthDate, 1)));
    }
}

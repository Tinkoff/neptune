package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.insert.delete.update.insert.and.delete;

import org.datanucleus.enhancement.Persistable;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.test.*;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.*;
import static java.util.List.of;
import static javax.jdo.JDOHelper.isPersistent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByIdsSupplier.listOfTypeByIds;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByQuerySupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.operations.DeletedSequentialGetStepSupplier.deleted;
import static ru.tinkoff.qa.neptune.data.base.api.operations.InsertedSequentialGetStepSupplier.inserted;

public class InsertAndDeleteTest extends BaseDbOperationTest {

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

    private Author victorHugo;
    private Date birthDateHugo;
    private Date deathDateHugo;

    private Book theHunchbackOfNotreDame;
    private Book theLegendOfTheAges;

    private Publisher signet;
    private Publisher createSpaceIndependent;

    private Catalog catalogItemOfNotreDame;
    private Catalog catalogItemOfLegendOfTheAges;

    @BeforeClass
    public void setUpBeforeClass() {
        var c1 = Calendar.getInstance();
        c1.set(YEAR, 1802);
        c1.set(MONTH, 0);
        c1.set(DAY_OF_MONTH, 7);
        c1.set(HOUR, 0);
        c1.set(MINUTE, 0);
        c1.set(SECOND, 0);
        c1.set(MILLISECOND, 0);
        birthDateHugo = c1.getTime();

        var c2 = Calendar.getInstance();
        c2.set(YEAR, 1885);
        c2.set(MONTH, 4);
        c2.set(DAY_OF_MONTH, 22);
        c2.set(HOUR, 0);
        c2.set(MINUTE, 0);
        c2.set(SECOND, 0);
        c2.set(MILLISECOND, 0);
        deathDateHugo = c2.getTime();

        victorHugo = new Author().setFirstName("Victor").setLastName("Hugo")
                .setBirthDate(birthDateHugo)
                .setDeathDate(deathDateHugo).setBiography(HUGO_BIOGRAPHY);

        theHunchbackOfNotreDame = new Book().setName("The Hunchback of Notre-Dame")
                .setYearOfFinishing(1831)
                .setAuthor(victorHugo);
        theLegendOfTheAges = new Book().setName("The Legend of the Ages")
                .setYearOfFinishing(1883)
                .setAuthor(victorHugo);

        signet = new Publisher().setName("Signet");
        createSpaceIndependent = new Publisher().setName("CreateSpace Independent Publishing Platform");

        catalogItemOfNotreDame = new Catalog().setBook(theHunchbackOfNotreDame)
                .setIsbn("978-1853260681")
                .setPublisher(signet).setYearOfPublishing(1998);
        catalogItemOfLegendOfTheAges = new Catalog().setBook(theLegendOfTheAges)
                .setIsbn("978-1539940296")
                .setPublisher(createSpaceIndependent).setYearOfPublishing(2016);
    }

    @Test
    public void insertTest() {
        List<Catalog> catalogItems = dataBaseSteps.get(inserted(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        assertThat(isPersistent(victorHugo), is(true));
        assertThat(isPersistent(theHunchbackOfNotreDame), is(true));
        assertThat(isPersistent(theLegendOfTheAges), is(true));
        assertThat(isPersistent(signet), is(true));
        assertThat(isPersistent(createSpaceIndependent), is(true));
        assertThat(isPersistent(catalogItemOfNotreDame), is(true));
        assertThat(isPersistent(catalogItemOfLegendOfTheAges), is(true));

        assertThat(victorHugo.getId(), greaterThan(0));
        assertThat(theHunchbackOfNotreDame.getId(), greaterThan(0));
        assertThat(theLegendOfTheAges.getId(), greaterThan(0));
        assertThat(signet.getId(), greaterThan(0));
        assertThat(createSpaceIndependent.getId(), greaterThan(0));
        assertThat(((Persistable) catalogItemOfNotreDame).dnGetObjectId(), not(nullValue()));
        assertThat(((Persistable) catalogItemOfLegendOfTheAges).dnGetObjectId(), not(nullValue()));

        assertThat(catalogItems, containsInAnyOrder(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        QCatalog qCatalog = QCatalog.candidate();

        assertThat(dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                                .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                                .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                                .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                                .and(qCatalog.book.author.biography.eq(HUGO_BIOGRAPHY))
                                .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                        ))
                        .withCondition(condition("Author was born in 1802 and died in 1885", catalog -> {
                            Author author = catalog.getBook().getAuthor();
                            return author.getBirthDate().equals(birthDateHugo) && author.getDeathDate().equals(deathDateHugo);
                        }))),
                containsInAnyOrder(catalogItems.toArray()));
    }

    @Test(dependsOnMethods = "insertTest")
    public void insertNegativeTest() {
        var bantamDoubledayDellPublishing = new Publisher().setName("Bantam Doubleday Dell Publishing");

        var catalogItemOfNotreDame2 = new Catalog().setBook(theHunchbackOfNotreDame)
                .setIsbn("978-1853260681")
                .setPublisher(signet).setYearOfPublishing(1998); //Should violate key rules

        var catalogItemOfLegendOfTheAges2 = new Catalog().setBook(theLegendOfTheAges)
                .setIsbn("978-1539940296")
                .setPublisher(createSpaceIndependent).setYearOfPublishing(2016); //Should violate key rules

        List<PersistableObject> persistableObjects;
        var exceptionThrown = false;

        try {
            persistableObjects = dataBaseSteps.get(inserted(bantamDoubledayDellPublishing, catalogItemOfNotreDame2, catalogItemOfLegendOfTheAges2));
        } catch (Throwable t) {
            persistableObjects = of();
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            fail("An exception was expected");
        }

        assertThat(persistableObjects, emptyIterable());

        assertThat(isPersistent(bantamDoubledayDellPublishing), is(false));
        assertThat(isPersistent(catalogItemOfNotreDame2), is(false));
        assertThat(isPersistent(catalogItemOfLegendOfTheAges2), is(false));


        assertThat(((Persistable) bantamDoubledayDellPublishing).dnGetObjectId(), nullValue());
        assertThat(((Persistable) catalogItemOfNotreDame2).dnGetObjectId(), nullValue());
        assertThat(((Persistable) catalogItemOfLegendOfTheAges2).dnGetObjectId(), nullValue());
    }

    @Test(dependsOnMethods = "insertNegativeTest", expectedExceptions = IllegalArgumentException.class)
    public void insertNegativeTestWhenObjectIsAlreadyInserted() {
        try {
            dataBaseSteps.get(inserted(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));
        } catch (Exception e) {
            assertThat(e.getMessage(), equalTo("There are objects already inserted: [Stored item Id=[978-1853260681] " +
                    "table [Catalog], Stored item Id=[978-1539940296] table [Catalog]]"));
            throw e;
        }
        fail("Exception was expected");
    }

    @Test(dependsOnMethods = {"insertTest", "insertNegativeTest", "insertNegativeTestWhenObjectIsAlreadyInserted"})
    public void deleteNegativeTest() {
        var exceptionThrown = false;
        List<PersistableObject> result;
        try {
            result = dataBaseSteps.get(deleted(signet,
                    catalogItemOfNotreDame, //data base has restrictions for external references
                    victorHugo, //such order should make it throw an exception
                    theHunchbackOfNotreDame));
        } catch (Throwable t) {
            result = of();
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            fail("An exception was expected");
        }

        assertThat(result, emptyIterable());
        assertThat(isPersistent(victorHugo), is(true));
        assertThat(isPersistent(theHunchbackOfNotreDame), is(true));
        assertThat(isPersistent(theLegendOfTheAges), is(true));

        assertThat(isPersistent(signet), is(true));
        assertThat(isPersistent(createSpaceIndependent), is(true));
        assertThat(isPersistent(catalogItemOfNotreDame), is(true));
        assertThat(isPersistent(catalogItemOfLegendOfTheAges), is(true));

        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class,
                catalogItemOfNotreDame.getIsbn(),
                catalogItemOfLegendOfTheAges.getIsbn()));

        assertThat(catalogItems, containsInAnyOrder(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        QCatalog qCatalog = QCatalog.candidate();

        assertThat(dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                                .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                                .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                                .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                                .and(qCatalog.book.author.biography.eq(HUGO_BIOGRAPHY))
                                .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                        ))
                        .withCondition(condition("Author was born in 1802 and died in 1885", catalog -> {
                            Author author = catalog.getBook().getAuthor();
                            return author.getBirthDate().equals(birthDateHugo) && author.getDeathDate().equals(deathDateHugo);
                        }))),
                containsInAnyOrder(catalogItems.toArray()));
    }

    @Test(dependsOnMethods = "deleteNegativeTest")
    public void deleteByObjectsPositiveTest() {
        var deleted = dataBaseSteps.get(deleted(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        assertThat(deleted, hasSize(2));
        assertThat(isPersistent(victorHugo), is(true));
        assertThat(isPersistent(theHunchbackOfNotreDame), is(true));
        assertThat(isPersistent(theLegendOfTheAges), is(true));

        assertThat(isPersistent(signet), is(true));
        assertThat(isPersistent(createSpaceIndependent), is(true));
        assertThat(isPersistent(catalogItemOfNotreDame), is(false));
        assertThat(isPersistent(catalogItemOfLegendOfTheAges), is(false));

        QCatalog qCatalog = QCatalog.candidate();
        assertThat(dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                                .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                                .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                                .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                                .and(qCatalog.book.author.biography.eq(HUGO_BIOGRAPHY))
                                .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                        ))
                        .withCondition(condition("Author was born in 1802 and died in 1885", catalog -> {
                            Author author = catalog.getBook().getAuthor();
                            return author.getBirthDate().equals(birthDateHugo) && author.getDeathDate().equals(deathDateHugo);
                        }))),
                emptyIterable());
    }

    @Test(dependsOnMethods = "deleteByObjectsPositiveTest")
    public void deleteByQueryPositiveTest() {
        catalogItemOfNotreDame = new Catalog().setBook(theHunchbackOfNotreDame)
                .setIsbn("978-1853260681")
                .setPublisher(signet).setYearOfPublishing(1998);
        catalogItemOfLegendOfTheAges = new Catalog().setBook(theLegendOfTheAges)
                .setIsbn("978-1539940296")
                .setPublisher(createSpaceIndependent).setYearOfPublishing(2016);

        dataBaseSteps.get(inserted(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        QCatalog qCatalog = QCatalog.candidate();
        var deleted = dataBaseSteps.get(deleted(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                        .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                        .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                        .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                        .and(qCatalog.book.author.biography.eq(HUGO_BIOGRAPHY))
                        .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                ))
                .withCondition(condition("Author was born in 1802 and died in 1885", catalog -> {
                    Author author = catalog.getBook().getAuthor();
                    return author.getBirthDate().equals(birthDateHugo) && author.getDeathDate().equals(deathDateHugo);
                }))));

        assertThat(deleted, hasSize(2));
        assertThat(isPersistent(victorHugo), is(true));
        assertThat(isPersistent(theHunchbackOfNotreDame), is(true));
        assertThat(isPersistent(theLegendOfTheAges), is(true));

        assertThat(isPersistent(signet), is(true));
        assertThat(isPersistent(createSpaceIndependent), is(true));
        assertThat(isPersistent(catalogItemOfNotreDame), is(false));
        assertThat(isPersistent(catalogItemOfLegendOfTheAges), is(false));

        assertThat(dataBaseSteps.get(listByQuery(ofType(Catalog.class).where(
                qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                        .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                        .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                        .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                        .and(qCatalog.book.author.biography.eq(HUGO_BIOGRAPHY))
                        .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                ))
                        .withCondition(condition("Author was born in 1802 and died in 1885", catalog -> {
                            Author author = catalog.getBook().getAuthor();
                            return author.getBirthDate().equals(birthDateHugo) && author.getDeathDate().equals(deathDateHugo);
                        }))),
                emptyIterable());
    }

    @Test(dependsOnMethods = {"deleteByObjectsPositiveTest", "deleteByQueryPositiveTest"}, priority = 1)
    public void emptyDeleteTest() {
        catalogItemOfNotreDame = new Catalog().setBook(theHunchbackOfNotreDame)
                .setIsbn("978-1853260681")
                .setPublisher(signet).setYearOfPublishing(1998);
        catalogItemOfLegendOfTheAges = new Catalog().setBook(theLegendOfTheAges)
                .setIsbn("978-1539940296")
                .setPublisher(createSpaceIndependent).setYearOfPublishing(2016);

        dataBaseSteps.get(inserted(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        QCatalog qCatalog = QCatalog.candidate();
        var deleted = dataBaseSteps.get(deleted(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                        .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                        .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                        .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                        .and(qCatalog.book.author.biography.eq(HUGO_BIOGRAPHY))
                        .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                        .and(qCatalog.yearOfPublishing.gt(3000))
                ))
                .withCondition(condition("Author was born in 1802 and died in 1885", catalog -> {
                    Author author = catalog.getBook().getAuthor();
                    return author.getBirthDate().equals(birthDateHugo) && author.getDeathDate().equals(deathDateHugo);
                }))));

        assertThat(deleted, hasSize(0));
        assertThat(isPersistent(victorHugo), is(true));
        assertThat(isPersistent(theHunchbackOfNotreDame), is(true));
        assertThat(isPersistent(theLegendOfTheAges), is(true));

        assertThat(isPersistent(signet), is(true));
        assertThat(isPersistent(createSpaceIndependent), is(true));
        assertThat(isPersistent(catalogItemOfNotreDame), is(true));
        assertThat(isPersistent(catalogItemOfLegendOfTheAges), is(true));
    }

    @Test(dependsOnMethods = "deleteByQueryPositiveTest")
    public void deleteNotStoredObjectTestTest() {
        catalogItemOfNotreDame = new Catalog().setBook(theHunchbackOfNotreDame)
                .setIsbn("978-1853260681")
                .setPublisher(signet).setYearOfPublishing(1998);
        catalogItemOfLegendOfTheAges = new Catalog().setBook(theLegendOfTheAges)
                .setIsbn("978-1539940296")
                .setPublisher(createSpaceIndependent).setYearOfPublishing(2016);

        dataBaseSteps.get(inserted(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        var riaChristieCollections = new Publisher().setName("Ria Christie Collections");
        var catalogItemOfNotreDame2 = new Catalog().setBook(theHunchbackOfNotreDame)
                .setIsbn("1906230692")
                .setPublisher(riaChristieCollections)
                .setYearOfPublishing(2011);

        var deleted = dataBaseSteps.get(deleted(catalogItemOfNotreDame,
                catalogItemOfNotreDame2,
                catalogItemOfLegendOfTheAges));

        assertThat(deleted, hasSize(2));
        assertThat(isPersistent(riaChristieCollections), is(false));
        assertThat(isPersistent(catalogItemOfNotreDame2), is(false));
    }
}

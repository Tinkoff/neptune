package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.store.and.delete;

import org.datanucleus.store.rdbms.exceptions.MissingTableException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.store.StoreSequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.test.*;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.PersistenceManagerFactorySupplierForTestBase1;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.PersistenceManagerFactorySupplierForTestBase2;

import javax.jdo.JDOUserException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static java.util.Calendar.*;
import static org.apache.commons.lang3.time.DateUtils.addHours;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.data.base.api.delete.GetDeletedSequentialSupplier.deleted;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByQuerySupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectSingleObjectByQuerySupplier.aSingleByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.store.StoreSequentialGetStepSupplier.storedObjects;

@SuppressWarnings({"ConstantConditions", "unchecked"})
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

    private static final String DOSTOEVSKY_BIOGRAPHY = "was a Russian novelist, short story writer, essayist, journalist and philosopher. Dostoevsky's literary works explore human psychology in the troubled political, " +
            "social, and spiritual atmospheres of 19th-century Russia, and engage with a variety of philosophical and religious themes. His most acclaimed works include Crime and Punishment (1866), " +
            "The Idiot (1869), Demons (1872) and The Brothers Karamazov (1880). Dostoevsky's oeuvre consists of 11 novels, three novellas, 17 short stories and numerous other works. Many literary critics " +
            "rate him as one of the greatest psychologists in world literature. His 1864 novella Notes from Underground is considered to be one of the first works of existentialist literature.\n" +
            "\n" +
            "Born in Moscow in 1821, Dostoevsky was introduced to literature at an early age through fairy tales and legends, and through books by Russian and foreign authors. " +
            "His mother died in 1837 when he was 15, and around the same time, he left school to enter the Nikolayev Military Engineering Institute. After graduating, he worked as an engineer and briefly enjoyed " +
            "a lavish lifestyle, translating books to earn extra money. In the mid-1840s he wrote his first novel, Poor Folk, which gained him entry into St. Petersburg's literary circles. Arrested in 1849 for belonging " +
            "to a literary group that discussed banned books critical of \"Tsarist Russia\", he was sentenced to death but the sentence was commuted at the last moment. He spent four years in a Siberian prison camp, " +
            "followed by six years of compulsory military service in exile. In the following years, Dostoevsky worked as a journalist, publishing and editing several magazines of his own and later A Writer's Diary, " +
            "a collection of his writings. He began to travel around western Europe and developed a gambling addiction, which led to financial hardship. For a time, he had to beg for money, but he eventually became " +
            "one of the most widely read and highly regarded Russian writers.\n" +
            "\n" +
            "Dostoevsky was influenced by a wide variety of philosophers and authors including Pushkin, Gogol, Augustine, Shakespeare, Dickens, Balzac, Lermontov, Hugo, Poe, Plato, Cervantes, Herzen, Kant, Belinsky, Hegel, " +
            "Schiller, Solovyov, Bakunin, Sand, Hoffmann, and Mickiewicz. His writings were widely read both within and beyond his native Russia and influenced an equally great number of later writers including Russians " +
            "like Aleksandr Solzhenitsyn and Anton Chekhov as well as philosophers such as Friedrich Nietzsche and Jean-Paul Sartre. His books have been translated into more than 170 languages.";

    private static final String KING_BIOGRAPHY = "is an American author of horror, supernatural fiction, suspense, science fiction and fantasy. " +
            "His books have sold more than 350 million copies, many of which have been adapted into feature films, miniseries, television series, " +
            "and comic books. King has published 58 novels, including seven under the pen name Richard Bachman, and six non-fiction books. He has " +
            "written around 200 short stories, most of which have been published in book collections.\n" +
            "\n" +
            "King has received Bram Stoker Awards, World Fantasy Awards, and British Fantasy Society Awards. In 2003, the National Book Foundation " +
            "awarded him the Medal for Distinguished Contribution to American Letters. He has also received awards for his contribution to literature " +
            "for his entire oeuvre, such as the World Fantasy Award for Life Achievement (2004), and the Grand Master Award from the Mystery Writers of America (2007). " +
            "In 2015, King was awarded with a National Medal of Arts from the United States National Endowment for the Arts for his contributions to literature. " +
            "He has been described as the \"King of Horror\".";

    private Date birthDateHugo;
    private Date deathDateHugo;

    private Author victorHugo;

    private Book theHunchbackOfNotreDame;
    private Book theLegendOfTheAges;

    private Publisher bantamDoubledayDellPublishing;
    private Publisher signet;
    private Publisher createSpaceIndependent;

    private Catalog catalogItemOfNotreDame;
    private Catalog catalogItemOfLegendOfTheAges;


    private Author fyodorDostoevsky;

    private Book crimeAndPunishment;
    private Book theDevils;

    private Publisher wordsworthEditions;
    private Publisher doverPublications;

    private Catalog catalogCrimeAndPunishment;
    private Catalog catalogItemTheDevils;

    private Author stephenKing;

    private Book petSematary;

    private Publisher doubleday;

    private Catalog catalogItemPetSematary;

    private void refreshHugo() {
        Calendar c1 = Calendar.getInstance();
        c1.set(YEAR, 1802);
        c1.set(MONTH, 0);
        c1.set(DAY_OF_MONTH, 7);
        c1.set(HOUR, 0);
        c1.set(MINUTE, 0);
        c1.set(SECOND, 0);
        c1.set(MILLISECOND, 0);
        birthDateHugo = c1.getTime();

        Calendar c2 = Calendar.getInstance();
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
        bantamDoubledayDellPublishing = new Publisher().setName("Bantam Doubleday Dell Publishing");

        catalogItemOfNotreDame = new Catalog().setBook(theHunchbackOfNotreDame)
                .setIsbn("978-1853260681")
                .setPublisher(signet).setYearOfPublishing(1998);
        catalogItemOfLegendOfTheAges = new Catalog().setBook(theLegendOfTheAges)
                .setIsbn("978-1539940296")
                .setPublisher(createSpaceIndependent).setYearOfPublishing(2016);
    }

    private void refreshDostoevsky() {
        Calendar c3 = Calendar.getInstance();
        c3.set(YEAR, 1821);
        c3.set(MONTH, 10);
        c3.set(DAY_OF_MONTH, 11);
        c3.set(HOUR, 0);
        c3.set(MINUTE, 0);
        c3.set(SECOND, 0);
        c3.set(MILLISECOND, 0);
        Date birthDateDostoevsky = c3.getTime();

        Calendar c4 = Calendar.getInstance();
        c4.set(YEAR, 1881);
        c4.set(MONTH, 1);
        c4.set(DAY_OF_MONTH, 9);
        c4.set(HOUR, 0);
        c4.set(MINUTE, 0);
        c4.set(SECOND, 0);
        c4.set(MILLISECOND, 0);
        Date deathDateDostoevsky = c4.getTime();

        fyodorDostoevsky = new Author().setFirstName("Fyodor").setLastName("Dostoevsky")
                .setBirthDate(birthDateDostoevsky)
                .setDeathDate(deathDateDostoevsky).setBiography(DOSTOEVSKY_BIOGRAPHY);

        crimeAndPunishment = new Book().setName("Crime and Punishment").setAuthor(fyodorDostoevsky)
                .setYearOfFinishing(1869);

        theDevils = new Book().setName("The devils")
                .setAuthor(fyodorDostoevsky)
                .setYearOfFinishing(1872);

        wordsworthEditions = new Publisher().setName("Wordsworth Editions Ltd");
        doverPublications = new Publisher().setName("Dover Publications");

        catalogCrimeAndPunishment = new Catalog().setBook(crimeAndPunishment)
                .setIsbn("978-0486454115")
                .setPublisher(doverPublications)
                .setYearOfPublishing(2001);
        catalogItemTheDevils = new Catalog().setBook(theDevils)
                .setIsbn("978-1840220995")
                .setPublisher(wordsworthEditions)
                .setYearOfPublishing(2010);
    }

    private void refreshKing() {
        Calendar c5 = Calendar.getInstance();
        c5.set(YEAR, 1947);
        c5.set(MONTH, 8);
        c5.set(DAY_OF_MONTH, 21);
        c5.set(HOUR, 0);
        c5.set(MINUTE, 0);
        c5.set(SECOND, 0);
        c5.set(MILLISECOND, 0);
        Date birthDateKing = c5.getTime();

        stephenKing = new Author().setFirstName("Stephen").setLastName("King")
                .setBirthDate(birthDateKing).setBiography(KING_BIOGRAPHY);

        petSematary = new Book().setAuthor(stephenKing).setName("Pet Sematary")
                .setYearOfFinishing(1983);

        doubleday = new Publisher().setName("Doubleday & McClure Company");

        catalogItemPetSematary = new Catalog().setBook(petSematary).setIsbn("978-0-385-18244-7")
                .setYearOfPublishing(1983)
                .setPublisher(doubleday);
    }

    @BeforeSuite
    public void beforeSuite() {
        refreshHugo();
        refreshDostoevsky();
        refreshKing();
    }

    @Test
    public void storeANewObject() {
        List<Catalog> catalogItems = dataBaseSteps.get(storedObjects(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));
        assertThat(victorHugo.getId(), greaterThan(0));
        assertThat(theHunchbackOfNotreDame.getId(), greaterThan(0));
        assertThat(theLegendOfTheAges.getId(), greaterThan(0));
        assertThat(signet.getId(), greaterThan(0));
        assertThat(createSpaceIndependent.getId(), greaterThan(0));
        assertThat(catalogItemOfNotreDame.getRecordId(), greaterThan(0));
        assertThat(catalogItemOfLegendOfTheAges.getRecordId(), greaterThan(0));

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

    @Test(dependsOnMethods = "storeANewObject")
    public void storeChanges() {
        String newBiography = format("%s\n %s", HUGO_BIOGRAPHY, "Hugo published his first novel the year following his marriage (Han d'Islande, 1823), " +
                "and his second three years later (Bug-Jargal, 1826). Between 1829 and 1840, he published five more volumes of poetry " +
                "(Les Orientales, 1829; Les Feuilles d'automne, 1831; Les Chants du crépuscule, 1835; Les Voix intérieures, " +
                "1837; and Les Rayons et les Ombres, 1840), cementing his reputation as one of the greatest elegiac and lyric poets of his time.");
        dataBaseSteps.get(storedObjects(victorHugo
                .setBiography(newBiography)
                .setBirthDate(addHours(birthDateHugo, 1))));

        QAuthor qAuthor = QAuthor.candidate();
        Author victorHugo2 = dataBaseSteps.get(aSingleByQuery(ofType(Author.class).where(qAuthor.id.eq(victorHugo.getId()))));
        assertThat(victorHugo2.getBiography(), equalTo(newBiography));
        assertThat(victorHugo2.getBirthDate(), equalTo(addHours(birthDateHugo, 1)));
    }

    @Test(dependsOnMethods = "storeANewObject")
    public void storeWithConnectionChangeByPersistenceUnitName() {
        StoreSequentialGetStepSupplier<Catalog> storeOperation = storedObjects(catalogCrimeAndPunishment,
                catalogItemTheDevils);
        try {
            try {
                dataBaseSteps.get(storeOperation.usePersistenceUnit(PersistenceManagerFactorySupplierForTestBase2.class));
            }
            catch (JDOUserException e) {
                assertThat(e.getCause().getClass(), is(MissingTableException.class));
            }

            refreshDostoevsky();
            storeOperation = storedObjects(catalogCrimeAndPunishment, catalogItemTheDevils);

            dataBaseSteps.get(storeOperation.usePersistenceUnit(PersistenceManagerFactorySupplierForTestBase1.class));
            assertThat(fyodorDostoevsky.getId(), greaterThan(0));
            assertThat(crimeAndPunishment.getId(), greaterThan(0));
            assertThat(theDevils.getId(), greaterThan(0));
            assertThat(doverPublications.getId(), greaterThan(0));
            assertThat(wordsworthEditions.getId(), greaterThan(0));
            assertThat(catalogCrimeAndPunishment.getRecordId(), greaterThan(0));
            assertThat(catalogItemTheDevils.getRecordId(), greaterThan(0));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test(dependsOnMethods = "storeWithConnectionChangeByPersistenceUnitName")
    public void storeWithConnectionChangeByPersistenceManagerFactory() {
        StoreSequentialGetStepSupplier<Catalog> storeOperation = storedObjects(catalogItemPetSematary);
        try {
            try {
                dataBaseSteps.get(storeOperation
                        .usePersistenceUnit(getPersistenceManagerFactory(PersistenceManagerFactorySupplierForTestBase2.class,
                                true)));
            } catch (JDOUserException e) {
                assertThat(e.getCause().getClass(), is(MissingTableException.class));
            }

            refreshKing();
            storeOperation = storedObjects(catalogItemPetSematary);

            dataBaseSteps.get(storeOperation
                    .usePersistenceUnit(getPersistenceManagerFactory(PersistenceManagerFactorySupplierForTestBase1.class,
                            true)));
            assertThat(stephenKing.getId(), greaterThan(0));
            assertThat(petSematary.getId(), greaterThan(0));
            assertThat(doubleday.getId(), greaterThan(0));
            assertThat(catalogItemPetSematary.getRecordId(), greaterThan(0));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test(dependsOnMethods = "storeANewObject")
    public void deletePreviouslyPersistedObjects() {
        dataBaseSteps.get(deleted(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));
        assertThat(catalogItemOfNotreDame.getRecordId(), not(nullValue()));
        assertThat(catalogItemOfLegendOfTheAges.getRecordId(), not(nullValue()));

        QCatalog qCatalog = QCatalog.candidate();
        assertThat(dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                                .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                                .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                                .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                                .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                        ))),
                hasSize(0));

        QBook qBook = QBook.candidate();
        QAuthor qAuthor = QAuthor.candidate();
        QPublisher qPublisher = QPublisher.candidate();

        assertThat(dataBaseSteps
                .get(aSingleByQuery(ofType(Book.class).where(qBook.id.eq(theHunchbackOfNotreDame.getId())))),
                not(nullValue()));
        assertThat(dataBaseSteps
                        .get(aSingleByQuery(ofType(Book.class).where(qBook.id.eq(theLegendOfTheAges.getId())))),
                not(nullValue()));

        assertThat(dataBaseSteps
                        .get(aSingleByQuery(ofType(Author.class).where(qAuthor.id.eq(victorHugo.getId())))),
                not(nullValue()));

        assertThat(dataBaseSteps
                        .get(aSingleByQuery(ofType(Publisher.class).where(qPublisher.id.eq(signet.getId())))),
                not(nullValue()));
        assertThat(dataBaseSteps
                        .get(aSingleByQuery(ofType(Publisher.class).where(qPublisher.id.eq(createSpaceIndependent.getId())))),
                not(nullValue()));
    }

    @Test(dependsOnMethods = {"storeANewObject", "deletePreviouslyPersistedObjects"})
    public void storePersistedDeletedObjects() {
        List<Catalog> catalogItems = dataBaseSteps
                .get(storedObjects(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        QCatalog qCatalog = QCatalog.candidate();

        assertThat(dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                                .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                                .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                        ))),
                containsInAnyOrder(catalogItems.toArray()));
        catalogItemOfNotreDame = catalogItems.get(0);
        catalogItemOfLegendOfTheAges = catalogItems.get(1);
    }

    @Test(dependsOnMethods = {"storeANewObject", "deletePreviouslyPersistedObjects", "storePersistedDeletedObjects"})
    public void storePersistedModifiedObjectThatWasDeletedPreviously() {
        List<Catalog> catalogItems = dataBaseSteps
                .get(deleted(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        QCatalog qCatalog = QCatalog.candidate();
        assertThat(dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                                .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                                .and(qCatalog.book.author.firstName.eq(victorHugo.getFirstName()))
                                .and(qCatalog.book.author.lastName.eq(victorHugo.getLastName()))
                                .and(qCatalog.publisher.eq(signet).or(qCatalog.publisher.eq(createSpaceIndependent)))
                        ))),
                hasSize(0));

        catalogItemOfNotreDame = catalogItems.get(0);
        catalogItemOfLegendOfTheAges = catalogItems.get(1);
        
        catalogItemOfNotreDame
                .setPublisher(bantamDoubledayDellPublishing)
                .setIsbn("0553213709")
                .setYearOfPublishing(2013);

        catalogItems = dataBaseSteps
                .get(storedObjects(catalogItemOfNotreDame, catalogItemOfLegendOfTheAges));

        assertThat(dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                        .where(qCatalog.book.author.books.contains(theHunchbackOfNotreDame)
                                .and(qCatalog.book.author.books.contains(theLegendOfTheAges))
                        ))),
                hasSize(2));

        assertThat(catalogItems.get(0).getPublisher().getName(), is(bantamDoubledayDellPublishing.getName()));
        assertThat(catalogItems.get(0).getYearOfPublishing(), is(2013));
        assertThat(catalogItems.get(0).getIsbn(), is("0553213709"));

        catalogItemOfNotreDame = catalogItems.get(0);
        catalogItemOfLegendOfTheAges = catalogItems.get(1);
    }
}

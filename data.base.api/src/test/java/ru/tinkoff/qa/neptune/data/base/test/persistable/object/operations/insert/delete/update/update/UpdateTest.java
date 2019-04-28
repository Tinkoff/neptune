package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.insert.delete.update.update;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Author;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Book;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.QAuthor;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.QBook;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.*;
import static java.util.Calendar.MILLISECOND;
import static javax.jdo.JDOHelper.isPersistent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.data.base.api.operations.DBGetInsertedFunction.inserted;
import static ru.tinkoff.qa.neptune.data.base.api.query.GetSelectedFunction.selected;
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.operations.DBGetUpdatedFunction.updated;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier.aSingleByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier.aSingleOfTypeById;

public class UpdateTest extends BaseDbOperationTest {

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

    private Author fyodorDostoevsky;
    private Book crimeAndPunishment;

    @BeforeClass
    public void setUpBeforeClass() {
        var c1 = Calendar.getInstance();
        c1.set(YEAR, 1821);
        c1.set(MONTH, 10);
        c1.set(DAY_OF_MONTH, 11);
        c1.set(HOUR, 0);
        c1.set(MINUTE, 0);
        c1.set(SECOND, 0);
        c1.set(MILLISECOND, 0);
        var birthDateDostoevsky = c1.getTime();

        var c2 = Calendar.getInstance();
        c2.set(YEAR, 1881);
        c2.set(MONTH, 1);
        c2.set(DAY_OF_MONTH, 9);
        c2.set(HOUR, 0);
        c2.set(MINUTE, 0);
        c2.set(SECOND, 0);
        c2.set(MILLISECOND, 0);
        Date deathDateDostoevsky = c2.getTime();

        fyodorDostoevsky = new Author().setFirstName("Fyodor").setLastName("Dostoevsky")
                .setBirthDate(birthDateDostoevsky)
                .setDeathDate(deathDateDostoevsky).setBiography(DOSTOEVSKY_BIOGRAPHY);
        crimeAndPunishment = new Book().setName("Crime and Punishment").setAuthor(fyodorDostoevsky)
                .setYearOfFinishing(1869);
        dataBaseSteps.get(inserted(crimeAndPunishment)).get(0);
    }

    @Test
    public void updateObjectsTest() {
        String improvedBiography = DOSTOEVSKY_BIOGRAPHY + "\nDostoevsky's parents were part of a multi-ethnic and multi-denominational noble family, " +
                "its branches including Russian Orthodox Christians, Polish Roman Catholics and Ukrainian Eastern Catholics." +
                "The family traced its roots back to a Tatar, Aslan Chelebi-Murza, who in 1389 defected from the Golden Horde " +
                "and joined the forces of Dmitry Donskoy, the first prince of Muscovy to openly challenge the Mongol authority " +
                "in the region, and whose descendant, Danilo Irtishch, was ennobled and given lands in the Pinsk region " +
                "(for centuries part of the Grand Duchy of Lithuania, now in modern-day Belarus) in 1509 for his services " +
                "under a local prince, his progeny then taking the name \"Dostoevsky\" based on a village there called Dostoïevo.";

        var updated = dataBaseSteps.get(updated(fyodorDostoevsky)
                .set("Improve the biography", author -> author.setBiography(improvedBiography)));

        assertThat(updated, hasSize(1));
        assertThat(updated.get(0).getBiography(),
                equalTo(improvedBiography));
        assertThat(dataBaseSteps.get(selected(aSingleOfTypeById(Author.class, fyodorDostoevsky.getId()))).getBiography(),
                equalTo(improvedBiography));
    }

    @Test
    public void updateByQuery() {
        var qAuthor = QAuthor.candidate();

        var updated = dataBaseSteps.get(updated(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.lastName.eq("Dostoevsky"))))
                .set("Change biography", author -> author.setBiography(DOSTOEVSKY_BIOGRAPHY
                        + "Dostoevsky's immediate ancestors on his mother's side were merchants; the male line on his father's side were priests. "
                        + "His father, Mikhail Andreevich, was expected to join the clergy but instead ran away from home and broke with the family permanently")));

        assertThat(updated, hasSize(1));
        assertThat(updated.get(0).getBiography(), equalTo(DOSTOEVSKY_BIOGRAPHY
                + "Dostoevsky's immediate ancestors on his mother's side were merchants; the male line on his father's side were priests. "
                + "His father, Mikhail Andreevich, was expected to join the clergy but instead ran away from home and broke with the family permanently"));
        assertThat(dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                        .where(qAuthor.biography.eq(DOSTOEVSKY_BIOGRAPHY
                                + "Dostoevsky's immediate ancestors on his mother's side were merchants; the male line on his father's side were priests. "
                                + "His father, Mikhail Andreevich, was expected to join the clergy but instead ran away from home and broke with the family permanently"))))),
                not(nullValue()));
    }

    @Test(dependsOnMethods = {"updateObjectsTest", "updateByQuery"}, expectedExceptions = IllegalArgumentException.class)
    public void negativeTestOfUpdatingNotStoredObject() {
        var theDevils = new Book().setName("The devils")
                .setAuthor(fyodorDostoevsky)
                .setYearOfFinishing(1872);

        List<Book> updated = new ArrayList<>();

        try {
            updated = dataBaseSteps.get(updated(crimeAndPunishment, theDevils)
                    .set("Change dates of the finishing to 1870", book -> book.setYearOfFinishing(1870)));
        } catch (Exception e) {
            assertThat(updated, hasSize(0));
            assertThat(dataBaseSteps.get(selected(aSingleOfTypeById(Book.class, crimeAndPunishment.getId()))).getYearOfFinishing(),
                    not(equalTo(1870)));
            assertThat(isPersistent(theDevils), is(false));
            assertThat(e.getMessage(),
                    equalTo("There are objects that are not stored in DB: " +
                            "[Not stored data base element mapped by " + Book.class.getName() + "]"));
            throw e;
        }
        fail("Exception was expected");
    }

    @Test(dependsOnMethods = "negativeTestOfUpdatingNotStoredObject", expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Test exception")
    public void negativeTestOfUpdatingWithFailedUpdateAction() {
        var theDevils = new Book().setName("The devils")
                .setAuthor(fyodorDostoevsky)
                .setYearOfFinishing(1872);
        dataBaseSteps.get(inserted(theDevils));

        List<Book> updated = new ArrayList<>();

        try {
            updated = dataBaseSteps.get(updated(crimeAndPunishment, theDevils)
                    .set("Change dates of the finishing to 1870", book -> book.setYearOfFinishing(1870))
                    .set("Try to fail updating if book is 'The devils'", book -> {
                        if (book.getName().equals("The devils")) {
                            throw new RuntimeException("Test exception");
                        }
                    }));
        } catch (Exception e) {
            assertThat(updated, hasSize(0));
            assertThat(dataBaseSteps.get(selected(aSingleOfTypeById(Book.class, crimeAndPunishment.getId()))).getYearOfFinishing(),
                    not(equalTo(1870)));
            throw e;
        }
        fail("Exception was expected");
    }

    @Test(dependsOnMethods = "negativeTestOfUpdatingWithFailedUpdateAction", expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Should be defined at least one updating action")
    public void negativeTestOfEmptyUpdating() {
        List<Book> updated = new ArrayList<>();
        try {
            updated = dataBaseSteps.get(updated(crimeAndPunishment));
        } catch (Exception e) {
            assertThat(updated, hasSize(0));
            throw e;
        }
        fail("Exception was expected");
    }

    @Test(dependsOnMethods = "negativeTestOfEmptyUpdating")
    public void negativeTestOfUpdatingEmptyQueryResult() {
        var qBook = QBook.candidate();
        var updated = dataBaseSteps.get(updated(listByQuery(ofType(Book.class)
                .where(qBook.author.eq(fyodorDostoevsky).and(qBook.yearOfFinishing.gt(3000)))))
                .set("Change dates of the finishing to 1870", book -> book.setYearOfFinishing(1870)));

        assertThat(updated.size(), is(0));
        assertThat(dataBaseSteps.get(selected(aSingleOfTypeById(Book.class, crimeAndPunishment.getId()))).getYearOfFinishing(),
                not(equalTo(1870)));
    }
}

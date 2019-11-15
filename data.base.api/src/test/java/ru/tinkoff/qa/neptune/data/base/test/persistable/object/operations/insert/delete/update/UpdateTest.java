package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.insert.delete.update;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Author;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.QAuthor;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.two.tables.CarModel;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.two.tables.QCarModel;

import java.util.Date;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.testng.Assert.*;
import static ru.tinkoff.qa.neptune.data.base.api.data.operations.UpdateExpression.change;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.listOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.ids.Id.id;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;

public class UpdateTest extends BaseDbOperationTest {
    private static final String BIO1 = "sometimes transliterated Dostoyevsky, was a Russian novelist, short story writer, essayist, " +
            "journalist and philosopher. Dostoevsky's literary works explore human psychology in the troubled political, social, and " +
            "spiritual atmospheres of 19th-century Russia, and engage with a variety of philosophical and religious themes. " +
            "His most acclaimed works include Crime and Punishment (1866), The Idiot (1869), Demons (1872), and The Brothers Karamazov (1880). " +
            "Dostoevsky's body of work consists of 11 novels, three novellas, 17 short stories, and numerous other works. " +
            "Many literary critics rate him as one of the greatest psychologists in world literature. His 1864 novella Notes from Underground is considered to be one of the first works of existentialist literature.\n" +
            "\n" +
            "Born in Moscow in 1821, Dostoevsky was introduced to literature at an early age through fairy tales and legends, and through books by Russian and foreign authors. " +
            "His mother died in 1837 when he was 15, and around the same time, he left school to enter the Nikolayev Military Engineering Institute. " +
            "After graduating, he worked as an engineer and briefly enjoyed a lavish lifestyle, translating books to earn extra money. " +
            "In the mid-1840s he wrote his first novel, Poor Folk, which gained him entry into St. Petersburg's literary circles. " +
            "Arrested in 1849 for belonging to a literary group that discussed banned books critical of Tsarist Russia, he was sentenced " +
            "to death but the sentence was commuted at the last moment.";

    private static final String BIO2 = BIO1 + "He spent four years in a Siberian prison camp, " +
            "followed by six years of compulsory military service in exile. In the following years, " +
            "Dostoevsky worked as a journalist, publishing and editing several magazines of his own and later A Writer's Diary, " +
            "a collection of his writings. He began to travel around western Europe and developed a gambling addiction, " +
            "which led to financial hardship. For a time, he had to beg for money, but he eventually " +
            "became one of the most widely read and highly regarded Russian writers.";

    private static final String BIO3 = BIO2 + "\n" +
            "Dostoevsky was influenced by a wide variety of philosophers and authors including Pushkin, Gogol, Augustine, Shakespeare, Dickens, " +
            "Balzac, Lermontov, Hugo, Poe, Plato, Cervantes, Herzen, Kant, Belinsky, Hegel, Schiller, Solovyov, Bakunin, Sand, Hoffmann, " +
            "and Mickiewicz. His writings were widely read both within and beyond his native Russia and influenced an equally great number of later writers including Russians like" +
            " Aleksandr Solzhenitsyn and Anton Chekhov as well as philosophers such as Friedrich Nietzsche and Jean-Paul Sartre. " +
            "His books have been translated into more than 170 languages.";

    private static final String BIO4 = BIO3 + "\n" +
            "Dostoevsky's parents were part of a multi-ethnic and multi-denominational noble family, " +
            "its branches including Russian Orthodox Christians, Polish Roman Catholics and Ukrainian Eastern Catholics. " +
            "The family traced its roots back to a Tatar, Aslan Chelebi-Murza, who in 1389 defected from the Golden Horde and joined the forces of Dmitry Donskoy, " +
            "the first prince of Muscovy to openly challenge the Mongol authority in the region, and whose descendant, " +
            "Danilo Irtishch, was ennobled and given lands in the Pinsk region (for centuries part of the Grand Duchy of " +
            "Lithuania, now in modern-day Belarus) in 1509 for his services under a local prince, his progeny then taking " +
            "the name 'Dostoevsky' based on a village there called Dostoïevo.";

    //positive
    private Author dostoevsky;
    private CarModel crownVictoria;

    @BeforeClass
    public void setUpBeforeClass() {
        dostoevsky = dataBaseSteps.select(oneOf(Author.class,
                byJDOQuery(QAuthor.class).addWhere(qAuthor -> qAuthor
                        .firstName.eq("Fyodor")
                        .and(qAuthor.lastName.eq("Dostoevsky")))));

        crownVictoria = dataBaseSteps.select(oneOf(CarModel.class, byJDOQuery(QCarModel.class)
                .addWhere(qCarModel -> qCarModel.carModelName.eq("Crown Victoria"))));
    }

    @Test(groups = "positive update")
    public void positiveUpdateTest1() {
        var dostoevsky = dataBaseSteps.select(oneOf(Author.class,
                byJDOQuery(QAuthor.class).addWhere(qAuthor -> qAuthor
                        .firstName.eq("Fyodor")
                        .and(qAuthor.lastName.eq("Dostoevsky")))));

        var crownVictoria = dataBaseSteps.select(oneOf(CarModel.class, byJDOQuery(QCarModel.class)
                .addWhere(qCarModel -> qCarModel.carModelName.eq("Crown Victoria"))));

        var dateToChange = new Date();
        var updatedObjects = dataBaseSteps.update(of(dostoevsky, crownVictoria), change("Add biography details to Author",
                persistableObject -> {
                    if (Author.class.isAssignableFrom(persistableObject.getClass())) {
                        ((Author) persistableObject).setBiography(BIO1);
                    }
                }),
                change("Change date of the 'Produced to' to current ",
                        persistableObject -> {
                            if (CarModel.class.isAssignableFrom(persistableObject.getClass())) {
                                ((CarModel) persistableObject).setProducedTo(dateToChange);
                            }
                        }));

        var updatedDostoevsky = dataBaseSteps.select(oneOf(Author.class,
                id(dostoevsky.getId()))
                .criteria("Biography is changed",
                        author -> BIO1.equals(author.getBiography())));

        var updatedCrownVictoria = dataBaseSteps.select(oneOf(CarModel.class,
                id(crownVictoria.getId()))
                .criteria("'Produced to' is changed",
                        carModel -> carModel.getProducedTo().equals(dateToChange)));

        assertThat(updatedObjects, containsInAnyOrder(updatedDostoevsky,
                updatedCrownVictoria));
    }

    @Test(groups = "positive update")
    public void positiveUpdateTest2() {
        var updatedAuthor = dataBaseSteps.update(oneOf(Author.class,
                byJDOQuery(QAuthor.class).addWhere(qAuthor -> qAuthor
                        .firstName.eq("Fyodor")
                        .and(qAuthor.lastName.eq("Dostoevsky")))),
                change("Add biography details to Author", author ->
                        author.setBiography(BIO2)));

        var dateToChange = new Date();
        var updatedCarModel = dataBaseSteps.update(oneOf(CarModel.class, byJDOQuery(QCarModel.class)
                        .addWhere(qCarModel -> qCarModel.carModelName.eq("Crown Victoria"))),
                change("Change date of the 'Produced to' to current", carModel ->
                        carModel.setProducedTo(dateToChange)));

        var updatedDostoevsky = dataBaseSteps.select(oneOf(Author.class,
                id(dostoevsky.getId()))
                .criteria("Biography is changed",
                        author -> BIO2.equals(author.getBiography())));

        var updatedCrownVictoria = dataBaseSteps.select(oneOf(CarModel.class,
                id(crownVictoria.getId()))
                .criteria("'Produced to' is changed",
                        carModel -> carModel.getProducedTo().equals(dateToChange)));

        assertEquals(updatedAuthor, updatedDostoevsky);
        assertEquals(updatedCarModel, updatedCrownVictoria);
    }

    @Test(groups = "positive update")
    public void positiveUpdateTest3() {
        var updatedAuthors = dataBaseSteps.update(listOf(Author.class,
                byJDOQuery(QAuthor.class).addWhere(qAuthor -> qAuthor
                        .firstName.eq("Fyodor")
                        .and(qAuthor.lastName.eq("Dostoevsky")))),
                change("Add biography details to Author", author ->
                        author.setBiography(BIO3)));

        var dateToChange = new Date();
        var updatedCarModels = dataBaseSteps.update(listOf(CarModel.class, byJDOQuery(QCarModel.class)
                        .addWhere(qCarModel -> qCarModel.carModelName.eq("Crown Victoria"))),
                change("Change date of the 'Produced to' to current", carModel ->
                        carModel.setProducedTo(dateToChange)));

        var updatedDostoevsky = dataBaseSteps.select(oneOf(Author.class,
                id(dostoevsky.getId()))
                .criteria("Biography is changed",
                        author -> BIO3.equals(author.getBiography())));

        var updatedCrownVictoria = dataBaseSteps.select(oneOf(CarModel.class,
                id(crownVictoria.getId()))
                .criteria("'Produced to' is changed",
                        carModel -> carModel.getProducedTo().equals(dateToChange)));

        assertThat(updatedAuthors, contains(updatedDostoevsky));
        assertThat(updatedCarModels, contains(updatedCrownVictoria));
    }

    @Test(dependsOnGroups = "positive update")
    public void negativeUpdateTest1() {
        var dostoevsky = dataBaseSteps.select(oneOf(Author.class,
                byJDOQuery(QAuthor.class).addWhere(qAuthor -> qAuthor
                        .firstName.eq("Fyodor")
                        .and(qAuthor.lastName.eq("Dostoevsky")))));

        var corollaE170 = dataBaseSteps.select(oneOf(CarModel.class, byJDOQuery(QCarModel.class)
                .addWhere(qCarModel -> qCarModel.carModelName.eq("Corolla E170"))));

        try {
            dataBaseSteps.update(of(dostoevsky, crownVictoria), change("Add biography details to Author",
                    persistableObject -> { //this updating is supposed to be successful
                        if (Author.class.isAssignableFrom(persistableObject.getClass())) {
                            ((Author) persistableObject).setBiography(BIO4);
                        }
                    }),
                    change("Change date of the 'Produced from' to null ",
                            persistableObject -> { //this updating is supposed to be failed
                                if (CarModel.class.isAssignableFrom(persistableObject.getClass())) {
                                    ((CarModel) persistableObject).setProducedFrom(null);
                                }
                            }));
        } catch (Exception e) {
            var updatedDostoevsky = dataBaseSteps.select(oneOf(Author.class,
                    id(dostoevsky.getId())));

            var updatedCorollaE170 = dataBaseSteps.select(oneOf(CarModel.class,
                    id(corollaE170.getId())));

            assertNotEquals(updatedDostoevsky.getBiography(), BIO4);
            assertNotNull(updatedCorollaE170.getProducedFrom());

            return;
        }

        fail("Exception was expected");
    }
}

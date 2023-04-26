package ru.tinkoff.qa.neptune.spring.data;

import org.hamcrest.Matchers;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.qa.neptune.spring.data.model.TestEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.database.abstractions.UpdateAction.change;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byIds;

public class SaveTest extends BaseSpringDataPreparing {

    private final TestEntity updatedEntity = new TestEntity().setId(10L)
            .setName("Test Name 1")
            .setArrayData(new String[]{"A1", "B1", "C1", "D1"})
            .setListData(new ArrayList<>(List.of("A1", "B1", "C1", "D1")));

    private final List<TestEntity> updatedEntities = new LinkedList<>(List.of(updatedEntity, updatedEntity));
    @Mock
    Mono<TestEntity> mockMonoLocal;
    @Mock
    Flux<TestEntity> mockFluxLocal;
    @Mock
    Mono<List<TestEntity>> listMonoLocal;
    @Mock
    io.reactivex.rxjava3.core.Flowable<TestEntity> mockFlowableLocal2;
    @Mock
    io.reactivex.rxjava3.core.Single<List<TestEntity>> mockSingleLocalListUpd2;
    private List<TestEntity> original;
    @Mock
    private io.reactivex.rxjava3.core.Single<TestEntity> mockSingleLocal2;
    @Mock
    private io.reactivex.rxjava3.core.Single<TestEntity> mockSingleLocalUpd2;

    private static List<TestEntity> cloneOriginalEntities() {
        return TEST_ENTITIES.stream().map(TestEntity::clone).collect(toList());
    }

    @BeforeMethod
    @BeforeClass
    public void prepareClass() {
        super.prepareClass();
        original = cloneOriginalEntities();

        when(testRepository.findById(1L)).thenReturn(ofNullable(original.get(0)));
        when(testRepository.findAllById(of(1L, 2L))).thenReturn(original);

        when(reactiveCrudRepository.findById(1L)).thenReturn(mockMono);
        when(mockMono.block()).thenReturn(original.get(0));
        when(reactiveCrudRepository.findAllById(of(1L, 2L))).thenReturn(mockFlux);
        when(mockFlux.collectList()).thenReturn(listMono);
        when(listMono.block()).thenReturn(original);

        when(testRxJava3SortingRepository.findById(1L)).thenReturn(mockMaybe2);
        when(mockMaybe2.blockingGet()).thenReturn(original.get(0));
        when(testRxJava3SortingRepository.findAllById(of(1L, 2L))).thenReturn(mockFlowable2);
        when(mockFlowable2.toList()).thenReturn(mockSingle2);
        when(mockSingle2.blockingGet()).thenReturn(original);

        when(testRepository.save(TEST_ENTITIES.get(0))).thenReturn(TEST_ENTITIES.get(0));
        when(reactiveCrudRepository.save(TEST_ENTITIES.get(0))).thenReturn(mockMono);
        when(testRxJava3SortingRepository.save(TEST_ENTITIES.get(0))).thenReturn(mockSingleLocal2);

        when(testRepository.saveAll(TEST_ENTITIES)).thenReturn(TEST_ENTITIES);
        when(reactiveCrudRepository.saveAll(TEST_ENTITIES)).thenReturn(mockFlux);
        when(testRxJava3SortingRepository.saveAll(TEST_ENTITIES)).thenReturn(mockFlowable2);

        when(testRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(reactiveCrudRepository.save(updatedEntity)).thenReturn(mockMonoLocal);
        when(testRxJava3SortingRepository.save(updatedEntity)).thenReturn(mockSingleLocalUpd2);

        when(testRepository.saveAll(updatedEntities)).thenReturn(updatedEntities);
        when(reactiveCrudRepository.saveAll(updatedEntities)).thenReturn(mockFluxLocal);
        when(testRxJava3SortingRepository.saveAll(updatedEntities)).thenReturn(mockFlowableLocal2);

        when(mockSingleLocal2.blockingGet()).thenReturn(TEST_ENTITIES.get(0));

        when(mockMonoLocal.block()).thenReturn(updatedEntity);
        when(mockSingleLocalUpd2.blockingGet()).thenReturn(updatedEntity);


        when(mockFluxLocal.collectList()).thenReturn(listMonoLocal);
        when(listMonoLocal.block()).thenReturn(updatedEntities);

        when(mockFlowableLocal2.toList()).thenReturn(mockSingleLocalListUpd2);
        when(mockSingleLocalListUpd2.blockingGet()).thenReturn(updatedEntities);
    }

    @Test
    public void saveOne() {
        var entity = springData().save("Test entity",
                testRepository,
                TEST_ENTITIES.get(0));

        assertThat(entity, Matchers.is(TEST_ENTITIES.get(0)));
        verify(testRepository, times(1)).save(TEST_ENTITIES.get(0));
    }

    @Test
    public void saveOne2() {
        var entity = springData().save("Test entity",
                reactiveCrudRepository,
                TEST_ENTITIES.get(0));

        assertThat(entity, Matchers.is(TEST_ENTITIES.get(0)));
        verify(reactiveCrudRepository, times(1)).save(TEST_ENTITIES.get(0));
        verify(mockMono, times(1)).block();
    }

    @Test
    public void saveOne4() {
        var entity = springData().save("Test entity",
                testRxJava3SortingRepository,
                TEST_ENTITIES.get(0));

        assertThat(entity, Matchers.is(TEST_ENTITIES.get(0)));
        verify(testRxJava3SortingRepository, times(1)).save(TEST_ENTITIES.get(0));
        verify(mockSingleLocal2, times(1)).blockingGet();
    }

    @Test
    public void saveVarArg() {
        var entity = springData().save("Test entities",
                testRepository,
                TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));

        assertThat(entity, Matchers.is(TEST_ENTITIES));
        verify(testRepository, times(1)).saveAll(TEST_ENTITIES);
    }

    @Test
    public void saveVarArg2() {
        var entity = springData().save("Test entities",
                reactiveCrudRepository,
                TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));

        assertThat(entity, Matchers.is(TEST_ENTITIES));
        verify(reactiveCrudRepository, times(1)).saveAll(TEST_ENTITIES);
        verify(mockFlux, times(1)).collectList();
        verify(listMono, times(1)).block();
    }

    @Test
    public void saveVarArg4() {
        var entity = springData().save("Test entities",
                testRxJava3SortingRepository,
                TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));

        assertThat(entity, Matchers.is(TEST_ENTITIES));
        verify(testRxJava3SortingRepository, times(1)).saveAll(TEST_ENTITIES);
        verify(mockFlowable2, times(1)).toList();
        verify(mockSingle2, times(1)).blockingGet();
    }

    @Test
    public void saveIterable() {
        var entity = springData().save("Test entities",
                testRepository,
                TEST_ENTITIES);

        assertThat(entity, Matchers.is(TEST_ENTITIES));
        verify(testRepository, times(1)).saveAll(TEST_ENTITIES);
    }

    @Test
    public void saveIterable2() {
        var entity = springData().save("Test entities",
                reactiveCrudRepository,
                TEST_ENTITIES);

        assertThat(entity, Matchers.is(TEST_ENTITIES));
        verify(reactiveCrudRepository, times(1)).saveAll(TEST_ENTITIES);
        verify(mockFlux, times(1)).collectList();
        verify(listMono, times(1)).block();
    }

    @Test
    public void saveIterable4() {
        var entity = springData().save("Test entities",
                testRxJava3SortingRepository,
                TEST_ENTITIES);

        assertThat(entity, Matchers.is(TEST_ENTITIES));
        verify(testRxJava3SortingRepository, times(1)).saveAll(TEST_ENTITIES);
        verify(mockFlowable2, times(1)).toList();
        verify(mockSingle2, times(1)).blockingGet();
    }

    @Test
    public void updateOne() {
        var entity = springData().save("Test entity",
                testRepository,
                original.get(0),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entity, Matchers.is(updatedEntity));
        verify(testRepository, times(1)).save(updatedEntity);
    }

    @Test
    public void updateOne2() {
        var entity = springData().save("Test entity",
                reactiveCrudRepository,
                original.get(0),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entity, Matchers.is(updatedEntity));
        verify(reactiveCrudRepository, times(1)).save(updatedEntity);
        verify(mockMonoLocal, times(1)).block();
    }

    @Test
    public void updateOne4() {
        var entity = springData().save("Test entity",
                testRxJava3SortingRepository,
                original.get(0),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entity, Matchers.is(updatedEntity));
        verify(testRxJava3SortingRepository, times(1)).save(updatedEntity);
        verify(mockSingleLocalUpd2, times(1)).blockingGet();
    }


    @Test
    public void updateIterable() {
        var entities = springData().save("Test entities",
                testRepository,
                original,
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entities, Matchers.is(updatedEntities));
        verify(testRepository, times(1)).saveAll(updatedEntities);
    }

    @Test
    public void updateIterable2() {
        var entities = springData().save("Test entities",
                reactiveCrudRepository,
                original,
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entities, Matchers.is(entities));
        verify(reactiveCrudRepository, times(1)).saveAll(updatedEntities);
        verify(mockFluxLocal, times(1)).collectList();
        verify(listMonoLocal, times(1)).block();
    }

    @Test
    public void updateIterable4() {
        var entities = springData().save("Test entities",
                testRxJava3SortingRepository,
                original,
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entities, Matchers.is(updatedEntities));
        verify(testRxJava3SortingRepository, times(1)).saveAll(updatedEntities);
        verify(mockFlowableLocal2, times(1)).toList();
        verify(mockSingleLocalListUpd2, times(1)).blockingGet();
    }


    @Test
    public void updateOneByQuery() {
        var entity = springData().save("Test entity",
                byId(testRepository, 1L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entity, Matchers.is(updatedEntity));
        verify(testRepository, times(1)).save(updatedEntity);
    }

    @Test
    public void updateOneByQuery2() {
        var entity = springData().save("Test entity",
                byId(reactiveCrudRepository, 1L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entity, Matchers.is(updatedEntity));
        verify(reactiveCrudRepository, times(1)).save(updatedEntity);
        verify(mockMonoLocal, times(1)).block();
    }

    @Test
    public void updateOneByQuery4() {
        var entity = springData().save("Test entity",
                byId(testRxJava3SortingRepository, 1L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entity, Matchers.is(updatedEntity));
        verify(testRxJava3SortingRepository, times(1)).save(updatedEntity);
        verify(mockSingleLocalUpd2, times(1)).blockingGet();
    }

    @Test
    public void updateIterableByQuery() {
        var entities = springData().save("Test entities",
                byIds(testRepository, 1L, 2L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entities, Matchers.is(updatedEntities));
        verify(testRepository, times(1)).saveAll(updatedEntities);
    }

    @Test
    public void updateIterableByQuery2() {
        var entities = springData().save("Test entities",
                byIds(reactiveCrudRepository, 1L, 2L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entities, Matchers.is(entities));
        verify(reactiveCrudRepository, times(1)).saveAll(updatedEntities);
        verify(mockFluxLocal, times(1)).collectList();
        verify(listMonoLocal, times(1)).block();
    }


    @Test
    public void updateIterableByQuery4() {
        var entities = springData().save("Test entities",
                byIds(testRxJava3SortingRepository, 1L, 2L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entities, Matchers.is(updatedEntities));
        verify(testRxJava3SortingRepository, times(1)).saveAll(updatedEntities);
        verify(mockFlowableLocal2, times(1)).toList();
        verify(mockSingleLocalListUpd2, times(1)).blockingGet();
    }
}

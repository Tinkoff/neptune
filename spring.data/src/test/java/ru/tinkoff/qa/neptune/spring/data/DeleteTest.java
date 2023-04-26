package ru.tinkoff.qa.neptune.spring.data;

import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import static java.util.List.of;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byIds;

public class DeleteTest extends BaseSpringDataPreparing {

    @Mock
    private Mono<Void> mockOneVoidMono;

    @Mock
    private io.reactivex.rxjava3.core.Completable mockOneCompletable2;

    @Mock
    private Mono<Void> mockAllVoidMono;

    @Mock
    private io.reactivex.rxjava3.core.Completable mockAllCompletable2;

    @BeforeMethod
    @BeforeClass
    public void prepareClass() {
        super.prepareClass();

        when(reactiveCrudRepository.delete(TEST_ENTITIES.get(0))).thenReturn(mockOneVoidMono);
        when(testRxJava3SortingRepository.delete(TEST_ENTITIES.get(0))).thenReturn(mockOneCompletable2);

        when(reactiveCrudRepository.deleteAll(TEST_ENTITIES)).thenReturn(mockAllVoidMono);
        when(testRxJava3SortingRepository.deleteAll(TEST_ENTITIES)).thenReturn(mockAllCompletable2);

        when(reactiveCrudRepository.deleteById(1L)).thenReturn(mockOneVoidMono);
        when(testRxJava3SortingRepository.deleteById(1L)).thenReturn(mockOneCompletable2);

        when(reactiveCrudRepository.deleteAllById(of(1L, 2L))).thenReturn(mockAllVoidMono);
        when(testRxJava3SortingRepository.deleteAllById(of(1L, 2L))).thenReturn(mockAllCompletable2);

        when(reactiveCrudRepository.deleteAll()).thenReturn(mockAllVoidMono);
        when(testRxJava3SortingRepository.deleteAll()).thenReturn(mockAllCompletable2);
    }

    @Test
    public void deleteOneByQueryTest() {
        springData().delete("Test entity", byId(testRepository, 1L));
        verify(testRepository, times(1)).delete(TEST_ENTITIES.get(0));
    }

    @Test
    public void deleteOneByQueryTest2() {
        springData().delete("Test entity", byId(reactiveCrudRepository, 1L));
        verify(reactiveCrudRepository, times(1)).delete(TEST_ENTITIES.get(0));
        verify(mockOneVoidMono, times(1)).block();
    }

    @Test
    public void deleteOneByQueryTest4() {
        springData().delete("Test entity", byId(testRxJava3SortingRepository, 1L));
        verify(testRxJava3SortingRepository, times(1)).delete(TEST_ENTITIES.get(0));
        verify(mockOneCompletable2, times(1)).blockingAwait();
    }

    @Test
    public void deleteAllByQueryTest() {
        springData().delete("Test entities", byIds(testRepository, 1L, 2L));
        verify(testRepository, times(1)).deleteAll(TEST_ENTITIES);
    }

    @Test
    public void deleteAllByQueryTest2() {
        springData().delete("Test entities", byIds(reactiveCrudRepository, 1L, 2L));
        verify(reactiveCrudRepository, times(1)).deleteAll(TEST_ENTITIES);
        verify(mockAllVoidMono, times(1)).block();
    }

    @Test
    public void deleteAllByQueryTest4() {
        springData().delete("Test entities", byIds(testRxJava3SortingRepository, 1L, 2L));
        verify(testRxJava3SortingRepository, times(1)).deleteAll(TEST_ENTITIES);
        verify(mockAllCompletable2, times(1)).blockingAwait();
    }

    @Test
    public void deleteOneTest() {
        springData().delete("Test entity",
                testRepository,
                TEST_ENTITIES.get(0));
        verify(testRepository, times(1)).delete(TEST_ENTITIES.get(0));
    }

    @Test
    public void deleteOneTest2() {
        springData().delete("Test entity",
                reactiveCrudRepository,
                TEST_ENTITIES.get(0));
        verify(reactiveCrudRepository, times(1)).delete(TEST_ENTITIES.get(0));
        verify(mockOneVoidMono, times(1)).block();
    }

    @Test
    public void deleteOneTest4() {
        springData().delete("Test entity",
                testRxJava3SortingRepository,
                TEST_ENTITIES.get(0));
        verify(testRxJava3SortingRepository, times(1)).delete(TEST_ENTITIES.get(0));
        verify(mockOneCompletable2, times(1)).blockingAwait();
    }

    @Test
    public void deleteVarArgTest() {
        springData().delete("Test entities",
                testRepository,
                TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));
        verify(testRepository, times(1)).deleteAll(TEST_ENTITIES);
    }

    @Test
    public void deleteVarArgTest2() {
        springData().delete("Test entities",
                reactiveCrudRepository,
                TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));
        verify(reactiveCrudRepository, times(1)).deleteAll(TEST_ENTITIES);
        verify(mockAllVoidMono, times(1)).block();
    }

    @Test
    public void deleteVarArgTest4() {
        springData().delete("Test entities",
                testRxJava3SortingRepository,
                TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));
        verify(testRxJava3SortingRepository, times(1)).deleteAll(TEST_ENTITIES);
        verify(mockAllCompletable2, times(1)).blockingAwait();
    }

    @Test
    public void deleteIterableTest() {
        springData().delete("Test entities",
                testRepository,
                TEST_ENTITIES);
        verify(testRepository, times(1)).deleteAll(TEST_ENTITIES);
    }

    @Test
    public void deleteIterableTest2() {
        springData().delete("Test entities",
                reactiveCrudRepository,
                TEST_ENTITIES);
        verify(reactiveCrudRepository, times(1)).deleteAll(TEST_ENTITIES);
        verify(mockAllVoidMono, times(1)).block();
    }

    @Test
    public void deleteIterableTest4() {
        springData().delete("Test entities",
                testRxJava3SortingRepository,
                TEST_ENTITIES);
        verify(testRxJava3SortingRepository, times(1)).deleteAll(TEST_ENTITIES);
        verify(mockAllCompletable2, times(1)).blockingAwait();
    }

    @Test
    public void deleteOneByIdsTest() {
        springData().deleteByIds("Test entity",
                testRepository,
                1L);
        verify(testRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteOneByIdsTest2() {
        springData().deleteByIds("Test entity",
                reactiveCrudRepository,
                1L);
        verify(reactiveCrudRepository, times(1)).deleteById(1L);
        verify(mockOneVoidMono, times(1)).block();
    }

    @Test
    public void deleteOneByIdsTest4() {
        springData().deleteByIds("Test entity",
                testRxJava3SortingRepository,
                1L);
        verify(testRxJava3SortingRepository, times(1)).deleteById(1L);
        verify(mockOneCompletable2, times(1)).blockingAwait();
    }

    @Test
    public void deleteAllByIdsTest() {
        springData().deleteByIds("Test entities",
                testRepository,
                1L, 2L);
        verify(testRepository, times(1)).deleteAllById(of(1L, 2L));
    }

    @Test
    public void deleteAllByIdsTest2() {
        springData().deleteByIds("Test entities",
                reactiveCrudRepository,
                1L, 2L);
        verify(reactiveCrudRepository, times(1)).deleteAllById(of(1L, 2L));
        verify(mockAllVoidMono, times(1)).block();
    }

    @Test
    public void deleteAllByIdsTest4() {
        springData().deleteByIds("Test entities",
                testRxJava3SortingRepository,
                1L, 2L);
        verify(testRxJava3SortingRepository, times(1)).deleteAllById(of(1L, 2L));
        verify(mockAllCompletable2, times(1)).blockingAwait();
    }

    @Test
    public void deleteAllTest() {
        springData().deleteAllFrom(testRepository);
        verify(testRepository, times(1)).deleteAll();
    }

    @Test
    public void deleteAllTest2() {
        springData().deleteAllFrom(reactiveCrudRepository);
        verify(reactiveCrudRepository, times(1)).deleteAll();
        verify(mockAllVoidMono, times(1)).block();
    }

    @Test
    public void deleteAllTest4() {
        springData().deleteAllFrom(testRxJava3SortingRepository);
        verify(testRxJava3SortingRepository, times(1)).deleteAll();
        verify(mockAllCompletable2, times(1)).blockingAwait();
    }
}

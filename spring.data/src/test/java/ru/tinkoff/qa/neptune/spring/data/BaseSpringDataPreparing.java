package ru.tinkoff.qa.neptune.spring.data;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.mockito.Mock;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.testng.annotations.BeforeClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.qa.neptune.spring.data.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SuppressWarnings("unchecked")
public class BaseSpringDataPreparing {
    static final List<TestEntity> TEST_ENTITIES = new ArrayList<>(of(
            new TestEntity().setId(1L)
                    .setName("Test Name 1")
                    .setArrayData(new String[]{"A", "B", "C", "D"})
                    .setListData(new ArrayList<>(of("A", "B", "C", "D"))),

            new TestEntity().setId(2L)
                    .setName("Test Name 2")
                    .setArrayData(new String[]{"E", "F", "G", "H"})
                    .setListData(new ArrayList<>(of("E", "F", "G", "H")))
    ));
    @Mock
    TestRepository testRepository;
    @Mock
    TestReactiveRepository reactiveCrudRepository;
    @Mock
    Mono<TestEntity> mockMono;
    @Mock
    Flux<TestEntity> mockFlux;
    @Mock
    Mono<List<TestEntity>> listMono;

    @Mock
    TestRxJava3SortingRepository testRxJava3SortingRepository;
    @Mock
    io.reactivex.rxjava3.core.Maybe<TestEntity> mockMaybe2;
    @Mock
    io.reactivex.rxjava3.core.Flowable<TestEntity> mockFlowable2;
    @Mock
    io.reactivex.rxjava3.core.Single<List<TestEntity>> mockSingle2;

    @Mock
    TestQuerydslPredicateExecutorRepository querydslRepository;

    @Mock
    TestReactiveQuerydslPredicateExecutorRepository reactiveQuerydslRepository;

    @Mock
    Page<TestEntity> mockPage;

    @BeforeClass
    public void prepareClass() {
        openMocks(this);
        when(testRepository.findById(1L)).thenReturn(ofNullable(TEST_ENTITIES.get(0)));
        when(testRepository.findAllById(of(1L, 2L))).thenReturn(TEST_ENTITIES);

        when(reactiveCrudRepository.findById(1L)).thenReturn(mockMono);
        when(mockMono.block()).thenReturn(TEST_ENTITIES.get(0));
        when(reactiveCrudRepository.findAllById(of(1L, 2L))).thenReturn(mockFlux);
        when(mockFlux.collectList()).thenReturn(listMono);
        when(listMono.block()).thenReturn(TEST_ENTITIES);

        when(testRxJava3SortingRepository.findById(1L)).thenReturn(mockMaybe2);
        when(mockMaybe2.blockingGet()).thenReturn(TEST_ENTITIES.get(0));
        when(testRxJava3SortingRepository.findAllById(of(1L, 2L))).thenReturn(mockFlowable2);
        when(mockFlowable2.toList()).thenReturn(mockSingle2);
        when(mockSingle2.blockingGet()).thenReturn(TEST_ENTITIES);

        when(testRepository.findAll(any(Sort.class))).thenReturn(TEST_ENTITIES);
        when(reactiveCrudRepository.findAll(any(Sort.class))).thenReturn(mockFlux);
        when(testRxJava3SortingRepository.findAll(any(Sort.class))).thenReturn(mockFlowable2);

        when(testRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);
        when(mockPage.getContent()).thenReturn(TEST_ENTITIES);

        when(testRepository.findOne(any(Example.class))).thenReturn(ofNullable(TEST_ENTITIES.get(0)));
        when(reactiveCrudRepository.findOne(any(Example.class))).thenReturn(mockMono);

        when(testRepository.findAll(any(Example.class))).thenReturn(TEST_ENTITIES);
        when(testRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(TEST_ENTITIES);

        when(reactiveCrudRepository.findAll(any(Example.class))).thenReturn(mockFlux);
        when(reactiveCrudRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(mockFlux);

        when(testRepository.findSomething(any(boolean.class), any(String.class), any(int.class))).thenReturn(TEST_ENTITIES.get(0));
        when(testRepository.findEntities(any(boolean.class), any(String.class), any(int.class))).thenReturn(TEST_ENTITIES);

        when(querydslRepository.findOne(any(Predicate.class))).thenReturn(ofNullable(TEST_ENTITIES.get(0)));
        when(querydslRepository.findAll(any(Predicate.class))).thenReturn(TEST_ENTITIES);

        when(reactiveQuerydslRepository.findOne(any(Predicate.class))).thenReturn(mockMono);
        when(reactiveQuerydslRepository.findAll(any(Predicate.class))).thenReturn(mockFlux);

        when(querydslRepository.findAll(any(Predicate.class), any(Sort.class))).thenReturn(TEST_ENTITIES);
        when(reactiveQuerydslRepository.findAll(any(Predicate.class), any(Sort.class))).thenReturn(mockFlux);

        when(querydslRepository.findAll(any(Predicate.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class)))
                .thenReturn(TEST_ENTITIES);

        when(querydslRepository.findAll(any(OrderSpecifier.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class)))
                .thenReturn(TEST_ENTITIES);

        when(reactiveQuerydslRepository.findAll(any(Predicate.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class))).thenReturn(mockFlux);
        when(reactiveQuerydslRepository.findAll(
                any(OrderSpecifier.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class))).thenReturn(mockFlux);

        when(querydslRepository.findAll(any(Predicate.class),
                any(QPageRequest.class)))
                .thenReturn(mockPage);

        when(testRepository.findAll()).thenReturn(TEST_ENTITIES);
        when(reactiveCrudRepository.findAll()).thenReturn(mockFlux);
        when(testRxJava3SortingRepository.findAll()).thenReturn(mockFlowable2);
    }
}

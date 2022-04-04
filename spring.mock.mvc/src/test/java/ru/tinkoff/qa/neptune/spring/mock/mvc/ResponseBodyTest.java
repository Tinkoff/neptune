package ru.tinkoff.qa.neptune.spring.mock.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer.SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER;

public class ResponseBodyTest {

    private static final String RESPONSE_BODY = "{\n" +
            "  \"stringValue\": \"ABCD\",\n" +
            "  \"arrayValue1\": [\n" +
            "    \"a\",\n" +
            "    \"b\",\n" +
            "    \"c\",\n" +
            "    \"d\"\n" +
            "  ],\n" +
            "  \"arrayValue2\": [\n" +
            "    1,\n" +
            "    2,\n" +
            "    3,\n" +
            "    4\n" +
            "  ]\n" +
            "}";

    private static final String RESPONSE_BODY2 =
            "[\n" +
                    "    1,\n" +
                    "    2,\n" +
                    "    3,\n" +
                    "    6\n" +
                    "]";

    private final RequestBuilder builder1 =
            post("/api/request/1")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}");

    private final RequestBuilder builder2 =
            post("/api/request/2")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}");

    private final MockHttpServletRequest request1 = new MockHttpServletRequest();
    private final MockHttpServletResponse response1 = new MockHttpServletResponse() {
        @Override
        public String getContentAsString() {
            return RESPONSE_BODY;
        }
    };

    private final MockHttpServletResponse response2 = new MockHttpServletResponse() {
        @Override
        public String getContentAsString() {
            return RESPONSE_BODY2;
        }
    };

    private final MvcResult result1 = new MvcResult() {
        @Override
        public MockHttpServletRequest getRequest() {
            return request1;
        }

        @Override
        public MockHttpServletResponse getResponse() {
            return response1;
        }

        @Override
        public Object getHandler() {
            return null;
        }

        @Override
        public HandlerInterceptor[] getInterceptors() {
            return new HandlerInterceptor[0];
        }

        @Override
        public ModelAndView getModelAndView() {
            return null;
        }

        @Override
        public Exception getResolvedException() {
            return null;
        }

        @Override
        public FlashMap getFlashMap() {
            return null;
        }

        @Override
        public Object getAsyncResult() {
            return null;
        }

        @Override
        public Object getAsyncResult(long timeToWait) {
            return null;
        }
    };

    private final MvcResult result2 = new MvcResult() {
        @Override
        public MockHttpServletRequest getRequest() {
            return request1;
        }

        @Override
        public MockHttpServletResponse getResponse() {
            return response2;
        }

        @Override
        public Object getHandler() {
            return null;
        }

        @Override
        public HandlerInterceptor[] getInterceptors() {
            return new HandlerInterceptor[0];
        }

        @Override
        public ModelAndView getModelAndView() {
            return null;
        }

        @Override
        public Exception getResolvedException() {
            return null;
        }

        @Override
        public FlashMap getFlashMap() {
            return null;
        }

        @Override
        public Object getAsyncResult() {
            return null;
        }

        @Override
        public Object getAsyncResult(long timeToWait) {
            return null;
        }
    };

    @Mock
    private MockMvc mockMvc;

    @BeforeClass
    public void prepare() throws Exception {
        SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.accept(TestDataTransformer.class);

        openMocks(this);
        request1.setRequestURI("https://google.com/api/request/1");
        request1.addHeader("someHeader1", 1);
        request1.addHeader("someHeader1", 2);
        request1.addHeader("someHeader1", "String 1");
        request1.addHeader("someHeader1", true);
        request1.setContentType(APPLICATION_JSON.toString());
        request1.setContent("{\"someString 1\"}".getBytes());
        request1.setCharacterEncoding("UTF-8");
        request1.setMethod("POST");

        response1.setStatus(200);
        response1.setForwardedUrl("https://google.com/api/request/1");
        response1.addHeader("someHeader1", "1");
        response1.addHeader("someHeader1", "2");
        response1.addHeader("someHeader1", "String 1");
        response1.addHeader("someHeader1", "true");
        response1.setCharacterEncoding("UTF-8");
        response1.setContentType(APPLICATION_JSON.toString());

        response2.setStatus(200);
        response2.setForwardedUrl("https://google.com/api/request/1");
        response2.addHeader("someHeader1", "1");
        response2.addHeader("someHeader1", "2");
        response2.addHeader("someHeader1", "String 1");
        response2.addHeader("someHeader1", "true");
        response2.setCharacterEncoding("UTF-8");
        response2.setContentType(APPLICATION_JSON.toString());

        when(mockMvc.perform(builder1)).thenReturn(new ResultActions() {
            @Override
            public ResultActions andExpect(ResultMatcher matcher) throws Exception {
                matcher.match(result1);
                return this;
            }

            @Override
            public ResultActions andDo(ResultHandler handler) throws Exception {
                handler.handle(result1);
                return this;
            }

            @Override
            public MvcResult andReturn() {
                return result1;
            }
        });

        when(mockMvc.perform(builder2)).thenReturn(new ResultActions() {
            @Override
            public ResultActions andExpect(ResultMatcher matcher) throws Exception {
                matcher.match(result2);
                return this;
            }

            @Override
            public ResultActions andDo(ResultHandler handler) throws Exception {
                handler.handle(result2);
                return this;
            }

            @Override
            public MvcResult andReturn() {
                return result2;
            }
        });
    }

    @AfterClass
    public void clear() {
        SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.accept(null);
    }

    @Test
    public void stringBodyTest() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetStringContent()
                .criteria("Contains String 'ABC'", s -> s.contains("ABC")));

        assertThat(body, equalTo(RESPONSE_BODY));
    }

    @Test
    public void stringBodyTest2() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetStringContent()
                .criteria("Contains String 'DEF'", s -> s.contains("DEF")));

        assertThat(body, nullValue());
    }


    @Test
    public void bodyTest() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetBody(BoundedDto.class)
                .criteria("stringValue = 'ABCD'", dto -> dto.getStringValue().equals("ABCD"))
                .criteria("arrayValue1 size greater then 1", dto -> dto.getArrayValue1().size() > 1)
                .criteria("arrayValue2 length greater then 1", dto -> dto.getArrayValue2().length > 1)
        );

        assertThat(body, not(nullValue()));
    }

    @Test
    public void bodyTest2() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetBody(new TypeReference<GenericDto<String[], List<Integer>>>() {
                })
                .criteria("stringValue = 'ABCD'", dto -> dto.getStringValue().equals("ABCD"))
                .criteria("arrayValue1 length greater then 1", dto -> dto.getArrayValue1().length > 1)
                .criteria("arrayValue2 size greater then 1", dto -> dto.getArrayValue2().size() > 1)
        );

        assertThat(body, not(nullValue()));
    }

    @Test
    public void bodyTest3() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetBody(BoundedDto.class)
                .criteria("stringValue = 'ABCD'", dto -> dto.getStringValue().equals("ABCD"))
                .criteria("arrayValue1 size greater then 5", dto -> dto.getArrayValue1().size() > 5)
                .criteria("arrayValue2 length greater then 5", dto -> dto.getArrayValue2().length > 5)
        );

        assertThat(body, nullValue());
    }

    @Test
    public void bodyTest4() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetBody(new TypeReference<GenericDto<String[], List<Integer>>>() {
                })
                .criteria("stringValue = 'ABCD'", dto -> dto.getStringValue().equals("ABCD"))
                .criteria("arrayValue1 length greater then 5", dto -> dto.getArrayValue1().length > 5)
                .criteria("arrayValue2 size greater then 5", dto -> dto.getArrayValue2().size() > 5)
        );

        assertThat(body, nullValue());
    }

    @Test
    public void valueTest() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValue("Value of the field 'stringValue'",
                        BoundedDto.class,
                        GenericDto::getStringValue)
                .criteria("contains 'A'", s -> s.contains("A"))
        );

        assertThat(body, not(nullValue()));
    }

    @Test
    public void valueTest2() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValue("Value of the field 'stringValue'",
                        new TypeReference<GenericDto<String[], List<Integer>>>() {
                        },
                        GenericDto::getStringValue)
                .criteria("contains 'A'", s -> s.contains("A"))
        );

        assertThat(body, not(nullValue()));
    }

    @Test
    public void valueTest3() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder("1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValue("Value of the field 'stringValue'",
                        BoundedDto.class,
                        GenericDto::getStringValue)
                .criteria("contains 'E'", s -> s.contains("E"))
        );

        assertThat(body, nullValue());
    }

    @Test
    public void valueTest4() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValue("Value of the field 'stringValue'",
                        new TypeReference<GenericDto<String[], List<Integer>>>() {
                        },
                        GenericDto::getStringValue)
                .criteria("contains 'E'", s -> s.contains("E"))
        );

        assertThat(body, nullValue());
    }


    @Test
    public void iterableTest() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetList("List of int", IntList.class)
                .criteria("lesser than 5", i -> i < 5)
        );

        assertThat(body, hasSize(3));
    }

    @Test
    public void iterableTest2() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetList("List of int", new TypeReference<List<Integer>>() {
                })
                .criteria("lesser than 5", i -> i < 5)
        );

        assertThat(body, hasSize(3));
    }

    @Test
    public void iterableTest3() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetList("List of int", IntList.class)
                .criteria("greater than 6", i -> i > 6)
        );

        assertThat(body, emptyIterable());
    }

    @Test
    public void iterableTest4() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetList("List of int", new TypeReference<List<Integer>>() {
                })
                .criteria("greater than 6", i -> i > 6)
        );

        assertThat(body, emptyIterable());
    }

    @Test
    public void iterableValueTest() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetList("Value of the field 'arrayValue1'",
                        BoundedDto.class,
                        GenericDto::getArrayValue1)
                .criteria("contains 'a'", s -> s.contains("a"))
        );

        assertThat(body, hasSize(1));
    }

    @Test
    public void iterableValueTest2() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetList("Value of the field 'arrayValue2'",
                        new TypeReference<GenericDto<String[], List<Integer>>>() {
                        },
                        GenericDto::getArrayValue2)
                .criteria("Lesser than 5'", i -> i < 5)
        );

        assertThat(body, hasSize(4));
    }

    @Test
    public void iterableValueTest3() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetList("Value of the field 'arrayValue1'",
                        BoundedDto.class,
                        GenericDto::getArrayValue1)
                .criteria("contains 'j'", s -> s.contains("j"))
        );

        assertThat(body, emptyIterable());
    }

    @Test
    public void iterableValueTest4() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetList("Value of the field 'arrayValue2'",
                        new TypeReference<GenericDto<String[], List<Integer>>>() {
                        },
                        GenericDto::getArrayValue2)
                .criteria("Greater than 5'", i -> i > 5)
        );

        assertThat(body, emptyIterable());
    }

    @Test
    public void iterableItemTest() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromIterable("int", IntList.class)
                .criteria("lesser than 5", i -> i < 5)
        );

        assertThat(body, equalTo(1));
    }

    @Test
    public void iterableItemTest2() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromIterable("int", new TypeReference<List<Integer>>() {
                })
                .criteria("lesser than 5", i -> i < 5)
        );

        assertThat(body, equalTo(1));
    }

    @Test
    public void iterableItemTest3() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromIterable("int", IntList.class)
                .criteria("greater than 6", i -> i > 6)
        );

        assertThat(body, nullValue());
    }

    @Test
    public void iterableItemTest4() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromIterable("int", new TypeReference<List<Integer>>() {
                })
                .criteria("greater than 6", i -> i > 6)
        );

        assertThat(body, nullValue());
    }

    @Test
    public void iterableItemValueTest() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromIterable("Value from the field 'arrayValue1'",
                        BoundedDto.class,
                        GenericDto::getArrayValue1)
                .criteria("contains 'a'", s -> s.contains("a"))
        );

        assertThat(body, notNullValue());
    }

    @Test
    public void iterableItemValueTest2() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromIterable("Value from the field 'arrayValue2'",
                        new TypeReference<GenericDto<String[], List<Integer>>>() {
                        },
                        GenericDto::getArrayValue2)
                .criteria("Lesser than 5'", i -> i < 5)
        );

        assertThat(body, notNullValue());
    }

    @Test
    public void iterableItemValueTest3() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromIterable("Value from the field 'arrayValue1'",
                        BoundedDto.class,
                        GenericDto::getArrayValue1)
                .criteria("contains 'j'", s -> s.contains("j"))
        );

        assertThat(body, nullValue());
    }

    @Test
    public void iterableItemValueTest4() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromIterable("Value from the field 'arrayValue2'",
                        new TypeReference<GenericDto<String[], List<Integer>>>() {
                        },
                        GenericDto::getArrayValue2)
                .criteria("Greater than 5'", i -> i > 5)
        );

        assertThat(body, nullValue());
    }


    @Test
    public void arrayTest() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetArray("Array of int", Integer[].class)
                .criteria("lesser than 5", i -> i < 5)
        );

        assertThat(body, arrayWithSize(3));
    }

    @Test
    public void arrayTest2() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetArray("Array of int", Integer[].class)
                .criteria("greater than 6", i -> i > 6)
        );

        assertThat(body, emptyArray());
    }

    @Test
    public void arrayValueTest() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetArray("Value of the field 'arrayValue2'",
                        BoundedDto.class,
                        GenericDto::getArrayValue2)
                .criteria("> 1", i -> i > 1)
        );

        assertThat(body, arrayWithSize(3));
    }

    @Test
    public void arrayValueTest2() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetArray("Value of the field 'arrayValue2'",
                        new TypeReference<GenericDto<String[], Integer[]>>() {
                        },
                        GenericDto::getArrayValue2)
                .criteria("> 1", i -> i > 1)
        );

        assertThat(body, arrayWithSize(3));
    }

    @Test
    public void arrayValueTest3() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetArray("Value of the field 'arrayValue2'",
                        BoundedDto.class,
                        GenericDto::getArrayValue2)
                .criteria("< 1", i -> i < 1)
        );

        assertThat(body, emptyArray());
    }

    @Test
    public void arrayValueTest4() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetArray("Value of the field 'arrayValue2'",
                        new TypeReference<GenericDto<String[], Integer[]>>() {
                        },
                        GenericDto::getArrayValue2)
                .criteria("< 1", i -> i < 1)
        );

        assertThat(body, emptyArray());
    }

    @Test
    public void arrayItemTest() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromArray("int", Integer[].class)
                .criteria("lesser than 5", i -> i < 5)
        );

        assertThat(body, equalTo(1));
    }

    @Test
    public void arrayItemTest2() {
        var body = mockMvcGet(response(mockMvc, builder2)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromArray("int", Integer[].class)
                .criteria("greater than 6", i -> i > 6)
        );

        assertThat(body, nullValue());
    }

    @Test
    public void arrayItemValueTest() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromArray("Value from the field 'arrayValue1'",
                        BoundedDto.class,
                        GenericDto::getArrayValue2)
                .criteria("> 1", i -> i > 1)
        );

        assertThat(body, notNullValue());
    }

    @Test
    public void arrayItemValueTest2() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromArray("Value from the field 'arrayValue2'",
                        new TypeReference<GenericDto<String[], Integer[]>>() {
                        },
                        GenericDto::getArrayValue2)
                .criteria("> 1", i -> i > 1)
        );

        assertThat(body, notNullValue());
    }

    @Test
    public void arrayItemValueTest3() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromArray("Value from the field 'arrayValue1'",
                        BoundedDto.class,
                        GenericDto::getArrayValue2)
                .criteria("< 1", i -> i < 1)
        );

        assertThat(body, nullValue());
    }

    @Test
    public void arrayItemValueTest4() {
        var body = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        ))
                .expectForward("https://google.com/api/request/1")
                .thenGetValueFromArray("Value from the field 'arrayValue2'",
                        new TypeReference<GenericDto<String[], Integer[]>>() {
                        },
                        GenericDto::getArrayValue2)
                .criteria("< 1", i -> i < 1)
        );

        assertThat(body, nullValue());
    }
}

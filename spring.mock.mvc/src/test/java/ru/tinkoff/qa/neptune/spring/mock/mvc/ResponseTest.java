package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.hamcrest.Matcher;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testng.FileAssert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.TestStringInjector.getMessages;

public class ResponseTest {

    private final RequestBuilder builder1 =
            post("/api/request/1")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}");
    private final RequestBuilder builder2 =
            post("/api/request/2")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 2\"}");
    private final MockHttpServletRequest request1 = new MockHttpServletRequest();
    private final MockHttpServletRequest request2 = new MockHttpServletRequest();
    private final MockHttpServletResponse response1 = new MockHttpServletResponse() {
        @Override
        public String getContentAsString() {
            return "SUCCESS";
        }
    };
    private final MockHttpServletResponse response2 = new MockHttpServletResponse() {
        @Override
        public String getContentAsString() {
            return "FAILURE";
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
            return request2;
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

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {SUCCESS, mapOf(
                        mapEntry("Body of the request", notOf(emptyOrNullString())),
                        mapEntry("Body of the response", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                )},
                {FAILURE, anEmptyMap()},
                {SUCCESS_AND_FAILURE, mapOf(
                        mapEntry("Body of the request", notOf(emptyOrNullString())),
                        mapEntry("Body of the response", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                )},
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {SUCCESS, anEmptyMap()},
                {FAILURE, mapOf(
                        mapEntry("Body of the request", notOf(emptyOrNullString())),
                        mapEntry("Body of the response", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                )},
                {SUCCESS_AND_FAILURE, mapOf(
                        mapEntry("Body of the request", notOf(emptyOrNullString())),
                        mapEntry("Body of the response", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                )},
        };
    }

    @BeforeClass
    public void setUpBeforeClass() throws Throwable {
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

        request2.setRequestURI("https://google.com/api/request/2");
        request2.addHeader("someHeader2", -1);
        request2.addHeader("someHeader2", -2);
        request2.addHeader("someHeader2", "String 2");
        request2.addHeader("someHeader2", false);
        request2.setContentType(APPLICATION_JSON.toString());
        request2.setContent("{\"someString 2\"}".getBytes());
        request2.setCharacterEncoding("UTF-8");
        request2.setMethod("POST");

        response1.setStatus(200);
        response1.setForwardedUrl("https://google.com/api/request/1");
        response1.addHeader("someHeader1", "1");
        response1.addHeader("someHeader1", "2");
        response1.addHeader("someHeader1", "String 1");
        response1.addHeader("someHeader1", "true");
        response1.setCharacterEncoding("UTF-8");
        response1.setContentType(APPLICATION_JSON.toString());

        response2.setStatus(413);
        response2.setForwardedUrl("https://google.com/api/request/2");
        response2.addHeader("someHeader1", "-1");
        response2.addHeader("someHeader1", "-2");
        response2.addHeader("someHeader1", "String 2");
        response2.addHeader("someHeader1", "false");
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

    @AfterMethod
    @BeforeMethod
    public void prepare() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
        getMessages().clear();
    }

    @Test(description = "successful")
    public void successfulTest() {
        var r = mockMvcGet(response(mockMvc, builder1)
                .expect(status().isOk())
                .expect(header().stringValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        )))
                .expect(content().string(containsString("SUCCESS")))
                .expect(forwardedUrl("https://google.com/api/request/1")));

        assertThat(r, equalTo(response1));
    }

    @Test(description = "successful")
    public void failedTest() {
        try {
            mockMvcGet(response(mockMvc, builder2)
                    .expect(status().is2xxSuccessful())
                    .expect(header().stringValues("someHeader1",
                            iterableInOrder("1",
                                    "2",
                                    "String 1",
                                    "true"
                            )))
                    .expect(content().string(containsString("SUCCESS")))
                    .expect(forwardedUrl("https://google.com/api/request/1")));
        } catch (AssertionError e) {
            assertThat(e.getMessage(), all(
                    containsString("Mismatches: "),
                    containsString("Range for response status value "),
                    containsString("Response header"),
                    containsString("Response content"),
                    containsString("Forwarded URL expected")));
            return;
        }

        fail("Exception was expected");
    }

    @Test(dependsOnMethods = "successfulTest", dataProvider = "data")
    public void capturesOfSuccessful(CapturedEvents eventType, Matcher<Map<String, String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(eventType);

        mockMvcGet(response(mockMvc, builder1)
                .expect(status().isOk())
                .expect(header().stringValues("someHeader1",
                        iterableInOrder("1",
                                "2",
                                "String 1",
                                "true"
                        )))
                .expect(content().string(containsString("SUCCESS")))
                .expect(forwardedUrl("https://google.com/api/request/1")));

        assertThat(getMessages(), matcher);
    }

    @Test(dependsOnMethods = "failedTest", dataProvider = "data2")
    public void capturesOfFailed(CapturedEvents eventType, Matcher<Map<String, String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(eventType);

        try {
            mockMvcGet(response(mockMvc, builder2)
                    .expect(status().isOk())
                    .expect(header().stringValues("someHeader1",
                            iterableInOrder("1",
                                    "2",
                                    "String 1",
                                    "true"
                            )))
                    .expect(content().string(containsString("SUCCESS")))
                    .expect(forwardedUrl("https://google.com/api/request/1")));
        } catch (Throwable ignored) {
        }

        assertThat(getMessages(), matcher);
    }
}

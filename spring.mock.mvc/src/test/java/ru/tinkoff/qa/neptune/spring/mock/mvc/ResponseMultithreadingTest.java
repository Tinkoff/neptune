package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.*;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.TestStringInjector.getMessages;

public class ResponseMultithreadingTest {

    private final RequestBuilder builder1 =
            post("/api/request/1")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}");
    private final MockHttpServletRequest request1 = new MockHttpServletRequest();
    private final MockHttpServletResponse response1 = new MockHttpServletResponse() {
        @Override
        public String getContentAsString() {
            return "SUCCESS";
        }

        @Override
        public byte[] getContentAsByteArray() {
            return getContentAsString().getBytes(StandardCharsets.UTF_8);
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
    @Mock
    private MockMvc mockMvc;

    @BeforeClass
    public void prepareGlobal() throws Exception {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

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
    }

    @AfterClass
    public void clear() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @Test(threadPoolSize = 10, invocationCount = 10)
    public void test() {
        var r = mockMvcGet(response(mockMvc, builder1)
                .expectStatusIs2xxSuccessful()
                .expectHeaderValues("someHeader1", iterableInOrder(
                        "1",
                        "2",
                        "String 1",
                        "true"
                ))
                .expectContent("SUCCESS")
                .expectForward("https://google.com/api/request/1"));

        assertThat(r, equalTo(response1));

        assertThat(getMessages(),
                mapOf(
                        mapEntry("Body of the request", notOf(emptyOrNullString())),
                        mapEntry("Body of the response", notOf(emptyOrNullString())),
                        mapEntry("Response", notOf(emptyOrNullString()))
                ));
    }

    @AfterMethod
    @BeforeMethod
    public void prepare() {
        getMessages().clear();
    }
}

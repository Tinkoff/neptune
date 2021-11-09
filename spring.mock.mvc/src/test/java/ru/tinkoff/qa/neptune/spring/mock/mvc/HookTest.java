package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.testng.Assert.fail;

public class HookTest {

    private static final SpringMockMvcExecutionHook HOOK = new SpringMockMvcExecutionHook();
    private static final PseudoTest PSEUDO_TEST = new PseudoTest();
    private static final RequestBuilder BUILDER = post("/");
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final MvcResult result = new MvcResult() {
        @Override
        public MockHttpServletRequest getRequest() {
            return request;
        }

        @Override
        public MockHttpServletResponse getResponse() {
            return response;
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
    private MockMvc mockMVC;

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {true},
                {false}
        };
    }

    @BeforeClass
    public void prepareGlobal() throws Exception {
        openMocks(this);

        when(mockMVC.perform(BUILDER)).thenReturn(new ResultActions() {
            @Override
            public ResultActions andExpect(ResultMatcher matcher) throws Exception {
                matcher.match(result);
                return this;
            }

            @Override
            public ResultActions andDo(ResultHandler handler) throws Exception {
                handler.handle(result);
                return this;
            }

            @Override
            public MvcResult andReturn() {
                return result;
            }
        });
    }

    @Test(dataProvider = "data", expectedExceptions = IllegalStateException.class)
    public void test1(boolean isTest) throws Exception {
        PSEUDO_TEST.setMockMvc(null);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        PSEUDO_TEST.test1();
        fail("Exception was expected");
    }

    @Test(dataProvider = "data")
    public void test2(boolean isTest) throws Exception {
        PSEUDO_TEST.setMockMvc(mockMVC);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        assertThat(PSEUDO_TEST.test1(), is(mockMVC));
    }

    @Test(dataProvider = "data", dependsOnMethods = "test2")
    public void test3(boolean isTest) throws Exception {
        PSEUDO_TEST.setMockMvc(mockMVC);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        assertThat(PSEUDO_TEST.test2(BUILDER), is(response));
    }
}

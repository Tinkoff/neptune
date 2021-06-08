package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.Body;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter;
import ru.tinkoff.qa.neptune.http.api.test.BaseHttpTest;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryStringMatcher.uriHasQuery;
import static ru.tinkoff.qa.neptune.http.api.properties.date.format.ApiDateFormatProperty.API_DATE_FORMAT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.GET;

public class DateFormatTest extends BaseHttpTest {

    private final DateService dateService = createAPI(DateService.class, REQUEST_URI);
    private String formattedDate;
    private Date date;

    @BeforeClass
    public void beforeClass() {
        API_DATE_FORMAT_PROPERTY.accept("yyyy-MM-dd");
        date = new Date();
        formattedDate = API_DATE_FORMAT_PROPERTY.get().format(date);
    }

    @AfterClass
    public void afterClass() {
        API_DATE_FORMAT_PROPERTY.accept(null);
    }

    @Test
    public void test1() {
        var request = dateService.someQuery(date, date, date, date).build();
        var uri = request.uri();

        assertThat(uri, uriHasPath("/" + formattedDate));
        assertThat(uri, uriHasQuery("dateQueryParam=" + formattedDate));

        assertThat(((NeptuneHttpRequestImpl) request).body().toString(), equalTo(formattedDate));
        assertThat(request.headers().map(), hasEntry(equalTo("dateHeader"), contains(formattedDate)));
    }

    @Test
    public void test2() {
        var request = dateService.someQuery2(date).build();
        assertThat(((NeptuneHttpRequestImpl) request).body().toString(), equalTo("dateParameter=" + formattedDate));
    }

    @Test
    public void test3() {
        var request = dateService.someQuery3(date).build();
        assertThat(((NeptuneHttpRequestImpl) request).body().toString(), containsString(formattedDate));
    }

    @Test
    public void test4() {
        assertThat(new SomeDTO().setSomeDate(date).toMap(), hasEntry("someDate", formattedDate));
    }

    public interface DateService extends HttpAPI<DateService> {

        @HttpMethod(httpMethod = GET)
        @URIPath("/{dateParam}")
        RequestBuilder someQuery(@PathParameter(name = "dateParam") Date dateParam,
                                 @HeaderParameter(headerName = "dateHeader") Date dateHeader,
                                 @QueryParameter(name = "dateQueryParam") Date dateQueryParam,
                                 @Body Date dateBody);

        @HttpMethod(httpMethod = GET)
        RequestBuilder someQuery2(@URLEncodedParameter(name = "dateParameter") Date dateBody);

        @HttpMethod(httpMethod = GET)
        RequestBuilder someQuery3(@MultiPartBody(name = "datePart") Date dateBody);
    }

    public static class SomeDTO extends MappedObject {

        private Date someDate;

        public Date getSomeDate() {
            return someDate;
        }

        public SomeDTO setSomeDate(Date someDate) {
            this.someDate = someDate;
            return this;
        }
    }
}

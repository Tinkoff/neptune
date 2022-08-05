Для URI/URL
=======================

.. code-block:: java
   :caption: Примеры использования матчеров для URI/URL

   package org.mypack;
   
   import java.net.URI;
   
   import static java.net.URI.create;
   import static org.hamcrest.MatcherAssert.assertThat;
   import static org.hamcrest.Matchers.containsString;
   import static org.hamcrest.Matchers.greaterThan;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.*;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasHostMatcher.uriHasHost;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasHostMatcher.urlHasHost;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasPathMatcher.uriHasPath;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasPathMatcher.urlHasPath;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasPortMatcher.uriHasPort;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasPortMatcher.urlHasPort;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasProtocolMatcher.urlHasProtocol;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasQueryParameters.uriHasQueryParameter;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasQueryParameters.urlHasQueryParameter;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasQueryStringMatcher.uriHasQueryString;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasQueryStringMatcher.urlHasQueryString;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasReferenceMatcher.urlHasReference;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasSchemeMatcher.uriHasScheme;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasUserInfoMatcher.uriHasUserInfo;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator
                .HasUserInfoMatcher.urlHasUserInfo;
   
   public class MyTest {
   
     private static final URI testURI = create("https://user:password@www.google.com:666/search?q=somequery#test");
   
     @Test(description = "Хост")
     public void test1() throws Exception {
       assertThat(testURI, uriHasHost("www.google.com"));
       assertThat(testURI, uriHasHost(containsString("google")));
       assertThat(testURI.toURL(), urlHasHost("www.google.com"));
           assertThat(testURI.toURL(), urlHasHost(containsString("google")));
       }
   
       @Test(description = "Path")
       public void test2() throws Exception {
           assertThat(testURI, uriHasPath("/search"));
           assertThat(testURI, uriHasPath(containsString("search")));
           assertThat(testURI.toURL(), urlHasPath("/search"));
           assertThat(testURI.toURL(), urlHasPath(containsString("search")));
       }
   
       @Test(description = "Порт")
       public void test3() throws Exception {
           assertThat(testURI, uriHasPort(666));
           assertThat(testURI, uriHasPort(greaterThan(665)));
           assertThat(testURI.toURL(), urlHasPort(666));
           assertThat(testURI.toURL(), urlHasPort(greaterThan(665)));
       }
   
       @Test(description = "Схема/Протокол")
       public void test4() throws Exception {
           assertThat(testURI, uriHasScheme("https"));
           assertThat(testURI.toURL(), urlHasProtocol("https"));
       }
   
       @Test(description = "Query")
       public void test5() throws Exception {
           assertThat(testURI, uriHasQueryString("q=somequery"));
           assertThat(testURI.toURL(), urlHasQueryString("q=somequery"));
   
           assertThat(testURI, uriHasQueryParameter("q", iterableInOrder("somequery")));
           assertThat(testURI.toURL(), urlHasQueryParameter("q", iterableInOrder("somequery")));
       }
   
       @Test(description = "Reference")
       public void test6() throws Exception {
           assertThat(testURI.toURL(), urlHasReference("test"));
       }
   
       @Test(description = "User info")
       public void test7() throws Exception {
           assertThat(testURI, uriHasUserInfo("user:password"));
           assertThat(testURI.toURL(), urlHasUserInfo("user:password"));
       }
   }
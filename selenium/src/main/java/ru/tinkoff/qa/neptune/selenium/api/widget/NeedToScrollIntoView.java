package ru.tinkoff.qa.neptune.selenium.api.widget;

import com.google.common.annotations.Beta;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This a mark of a method implemented by a subclass of the {@link Widget}.
 * It means that it is necessary to scroll a widget into view before any method marked by this annotation.
 * <p>NOTE!</p>
 * <p>The scrolling is performed implicitly when:</p>
 * <p>- the subclass of the {@link Widget} implements {@link ScrollsIntoView}</p>
 * <p>- an instance of the {@link Widget} is received with {@link SearchSupplier}</p>
 */
@Beta
@Retention(RUNTIME) @Target({METHOD})
public @interface NeedToScrollIntoView {
}

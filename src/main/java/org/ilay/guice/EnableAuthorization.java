package org.ilay.guice;

import com.vaadin.guice.annotation.Import;
import com.vaadin.guice.annotation.PackagesToScan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables authorization when attached to a {@link com.vaadin.guice.server.GuiceVaadinServlet}.
 * All {@link org.ilay.api.Authorizer}s in the {@link PackagesToScan#value()} will be
 * managed and created by Guice and made available for {@link org.ilay.Authorization}.
 *
 * <code>
 *     {@literal @}EnableAuthorization
 *     {@literal @}Configuration(basePackages = "org.foo")
 *     {@literal @}WebServlet(urlPatterns = "/*", name = "MyServlet", asyncSupported = true)
 *     public class MyServlet extends GuiceVaadinServlet {
 *     }
 * </code>
 *
 * @author Bernd Hopp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(value = AuthorizationModule.class, packagesToScan = "org.ilay.guice")
public @interface EnableAuthorization {
}

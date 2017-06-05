package org.ilay.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

import com.vaadin.guice.server.NeedsInjector;
import com.vaadin.guice.server.NeedsReflections;
import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinServiceInitListener;

import org.ilay.Authorization;
import org.ilay.api.Authorizer;
import org.reflections.Reflections;

import java.util.Set;
import java.util.logging.Logger;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.lang.String.format;

class AuthorizationModule extends AbstractModule implements VaadinServiceInitListener, NeedsInjector, NeedsReflections {

    private Provider<Injector> injectorProvider;
    private Reflections reflections;

    public void setInjectorProvider(Provider<Injector> injectorProvider) {
        this.injectorProvider = injectorProvider;
    }

    public void setReflections(Reflections reflections) {
        this.reflections = reflections;
    }

    protected void configure() {

        final Logger logger = Logger.getGlobal();

        Multibinder<Authorizer> multibinder = newSetBinder(binder(), Authorizer.class);

        final Set<Class<? extends Authorizer>> authorizerClasses = reflections.getSubTypesOf(Authorizer.class);

        if (authorizerClasses.isEmpty()) {
            logger.severe("no authorizers found in given basePackages, authentication will throw runtime-errors!");
            return;
        }

        logger.info("authorizers discovered from package-scan:");

        for (Class<? extends Authorizer> authorizerClass : authorizerClasses) {

            logger.info("\t" + authorizerClass);

            multibinder.addBinding().to(authorizerClass);
        }

        logger.info("\n");
    }

    public void serviceInit(ServiceInitEvent event) {
        final Key<Set<Authorizer>> authorizerSetKey = Key.get(new TypeLiteral<Set<Authorizer>>(){});
        final Injector injector = injectorProvider.get();

        Authorization.start(() -> injector.getInstance(authorizerSetKey));
    }
}

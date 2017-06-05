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

import static com.google.inject.multibindings.Multibinder.newSetBinder;

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
        Multibinder<Authorizer> multibinder = newSetBinder(binder(), Authorizer.class);

        reflections.getSubTypesOf(Authorizer.class).forEach(multibinder.addBinding()::to);
    }

    public void serviceInit(ServiceInitEvent event) {
        final Key<Set<Authorizer>> authorizerSetKey = Key.get(new TypeLiteral<Set<Authorizer>>(){});
        final Injector injector = injectorProvider.get();

        Authorization.start(() -> injector.getInstance(authorizerSetKey));
    }
}

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

public class AuthorizationModule extends AbstractModule implements VaadinServiceInitListener, NeedsInjector, NeedsReflections {

    private Provider<Injector> injectorProvider;
    private Reflections reflections;

    protected void configure() {
        Multibinder<Authorizer> multibinder = Multibinder.newSetBinder(binder(), Authorizer.class);

        for (Class<? extends Authorizer> authorizerClass : reflections.getSubTypesOf(Authorizer.class)) {
            multibinder.addBinding().to(authorizerClass);
        }
    }

    public void serviceInit(ServiceInitEvent event) {
        final Key<Set<Authorizer>> authorizerSetKey = Key.get(new TypeLiteral<Set<Authorizer>>(){});

        Authorization.start(() -> injectorProvider.get().getInstance(authorizerSetKey));
    }

    public void setInjectorProvider(Provider<Injector> injectorProvider) {
        this.injectorProvider = injectorProvider;
    }

    public void setReflections(Reflections reflections) {
        this.reflections = reflections;
    }
}

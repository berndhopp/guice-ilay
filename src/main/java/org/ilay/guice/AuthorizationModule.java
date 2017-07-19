package org.ilay.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.ilay.api.Authorizer;
import org.reflections.Reflections;

class AuthorizationModule extends AbstractModule {

    private final Reflections reflections;

    AuthorizationModule(Reflections reflections){
        this.reflections = reflections;

        reflections.merge(new Reflections("org.ilay.guice"));
    }

    protected void configure() {
        Multibinder<Authorizer> multibinder = Multibinder.newSetBinder(binder(), Authorizer.class);

        for (Class<? extends Authorizer> authorizerClass : reflections.getSubTypesOf(Authorizer.class)) {
            multibinder.addBinding().to(authorizerClass);
        }
    }
}

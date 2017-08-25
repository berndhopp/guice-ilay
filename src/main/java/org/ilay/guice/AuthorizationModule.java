package org.ilay.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import org.ilay.api.Authorizer;
import org.reflections.Reflections;

import java.util.Set;
import java.util.logging.Logger;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.lang.reflect.Modifier.isAbstract;
import static java.util.stream.Collectors.toSet;

class AuthorizationModule extends AbstractModule {

    private final Reflections reflections;

    AuthorizationModule(Reflections reflections) {
        this.reflections = reflections;
    }

    protected void configure() {

        final Logger logger = Logger.getGlobal();

        final Set<Class<? extends Authorizer>> authorizerClasses = reflections
                .getSubTypesOf(Authorizer.class)
                .stream()
                .filter(authorizerClass -> !isAbstract(authorizerClass.getModifiers()))
                .collect(toSet());

        if (authorizerClasses.isEmpty()) {
            logger.severe("no authorizers found in given basePackages, authentication will throw runtime-errors!");
            return;
        }

        Multibinder<Authorizer> multibinder = newSetBinder(binder(), Authorizer.class);

        for (Class<? extends Authorizer> authorizerClass : authorizerClasses) {

            logger.info("installing authorizer " + authorizerClass);

            multibinder.addBinding().to(authorizerClass);
        }
    }
}

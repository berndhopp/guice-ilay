package org.ilay.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.ilay.api.Authorizer;
import org.reflections.Reflections;

import java.util.Set;
import java.util.logging.Logger;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

class AuthorizationModule extends AbstractModule {

    private final Reflections reflections;

    AuthorizationModule(Reflections reflections){
        this.reflections = reflections;

        reflections.merge(new Reflections("org.ilay.guice"));
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
}

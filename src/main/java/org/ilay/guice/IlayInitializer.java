package org.ilay.guice;

import com.google.inject.*;
import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinServiceInitListener;
import org.ilay.Authorization;
import org.ilay.api.Authorizer;

import java.util.Set;

class IlayInitializer implements VaadinServiceInitListener{

    @Inject
    private Provider<Set<Authorizer>> authorizerSetProvider;

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        Authorization.start(authorizerSetProvider::get);
    }
}

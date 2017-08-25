package org.ilay.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinServiceInitListener;

import org.ilay.Authorization;
import org.ilay.api.Authorizer;

import java.util.Set;

class IlayInitializer implements VaadinServiceInitListener {

    @Inject
    private Provider<Set<Authorizer>> authorizerProvider;

    @Override
    public void serviceInit(ServiceInitEvent event) {
        Authorization.start(authorizerProvider::get);
    }
}

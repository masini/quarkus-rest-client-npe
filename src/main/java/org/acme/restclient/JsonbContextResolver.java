package org.acme.restclient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class JsonbContextResolver implements ContextResolver<Jsonb> {

    @Inject
    JsonbObjectMapper serializerControl;

    @Override
    public Jsonb getContext(Class<?> type) {

        return serializerControl.jsonbFor(type);
    }

}

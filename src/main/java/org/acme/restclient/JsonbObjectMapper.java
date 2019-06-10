package org.acme.restclient;

import org.eclipse.yasson.FieldAccessStrategy;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.JsonbException;
import javax.json.bind.serializer.JsonbSerializer;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static java.util.Locale.ITALIAN;
import static javax.json.bind.annotation.JsonbDateFormat.TIME_IN_MILLIS;

@ApplicationScoped
public class JsonbObjectMapper {
    private Map<Type, Jsonb> configurations = Collections.emptyMap();

    Jsonb defaultSerializer;

    @PostConstruct
    void creaSerializers() {
        defaultSerializer = createJsonb();
    }

    private Jsonb createJsonb(JsonbSerializer...serializers) {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(false)
                .withPropertyVisibilityStrategy(new FieldAccessStrategy())
                .withDateFormat(TIME_IN_MILLIS, ITALIAN);

        for (JsonbSerializer serializer : serializers) {
            config.withSerializers(serializer);
        }

        return JsonbBuilder.create(config);
    }

    Jsonb jsonbFor(Type type) {

        Objects.requireNonNull(type, "Non esiste il serializer del type null!");

        Jsonb jsonb = configurations.get(type);

        if( jsonb==null) {
            jsonb = defaultSerializer;
        }

        return jsonb;
    }

    public <T> T fromJson(String jsonString, Type runtimeType) throws JsonbException {
        Objects.requireNonNull(jsonString, "Non si può deserializzare null!");

        Jsonb jsonb = jsonbFor(runtimeType);

        return jsonb.fromJson(jsonString, runtimeType);
    }

    public <T> T fromJson(InputStream stream, Type runtimeType) throws JsonbException {
        Objects.requireNonNull(stream, "Non si può deserializzare null!");

        Jsonb jsonb = jsonbFor(runtimeType);

        return jsonb.fromJson(stream, runtimeType);
    }


    public String toJson(Object object) {

        Objects.requireNonNull(object, "Non si può serializzare null!");

        Jsonb jsonb = jsonbFor(object.getClass());

        return jsonb.toJson(object);
    }

}

package com.chatapp.backend.util.json;

import com.chatapp.backend.model.Channel;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class SimpleChannelSerializer extends StdSerializer<Channel> {

    public SimpleChannelSerializer() {
        this(null);
    }

    public SimpleChannelSerializer(Class<Channel> t) {
        super(t);
    }

    @Override
    public void serialize(Channel value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeEndObject();
    }
}

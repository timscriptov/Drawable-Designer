package com.mcal.drawabledesigner.state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class StatePersist {
    public static void save(Object obj, Writer writer) throws IOException {
        writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(obj));
    }

    public static Shape load(Reader reader) {
        return new Gson().fromJson(reader, Shape.class);
    }
}
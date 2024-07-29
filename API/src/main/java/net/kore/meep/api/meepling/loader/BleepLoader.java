package net.kore.meep.api.meepling.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class BleepLoader implements Loader {
    @Override
    public void loadFile(File parent) {
        try {
            File metadata = new File(parent, "meepling.json");
            JsonObject jo = new JsonParser().parse(Loader.getString(new FileInputStream(metadata))).getAsJsonObject();
            executeString(new String(Files.readAllBytes(parent.toPath())), jo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeString(String str, JsonObject jo) {
        //TODO: Add Bleep as a dep to actually be able to use it
    }
}

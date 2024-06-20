/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.meepling.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kore.meep.api.logger.Logger;
import org.graalvm.polyglot.Context;

import java.io.*;
import java.nio.file.Files;

public class JSLoader implements Loader {
    @Override
    public void loadFile(File parent) {
        try {
            File metadata = new File(parent, "meepling.json");
            JsonObject jo = new JsonParser().parse(getString(new FileInputStream(metadata))).getAsJsonObject();
            executeString(new String(Files.readAllBytes(parent.toPath())), jo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeString(String str, JsonObject jo) {
        str = "function JImport(t){let e=t.split(\".\");if(void 0!==globalThis[e[e.length-1]])throw Error(e[e.length-1]+\"is already defined, you cannot redefine it using JImport.\");globalThis[e[e.length-1]]=Java.type(t)}" + str;
        try (Context context = Context.newBuilder("js").allowAllAccess(true).build()) {
            context.getBindings("js").putMember("console", new Logger(jo.get("name").getAsString()));
            context.getBindings("js").putMember("Metadata", jo);
            context.eval("js", str);
        }
    }
}

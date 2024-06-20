/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.meepling.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kore.meep.api.Meep;
import net.kore.meep.api.meepling.Meepling;
import net.kore.meep.api.meepling.Memepling;
import net.kore.meep.api.meepling.MeeplingManager;
import net.kore.meep.api.utils.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JavaLoader implements Loader {
    @Override
    public void loadFile(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            JarEntry meta = jarFile.getJarEntry("meepling.json");
            InputStream is = jarFile.getInputStream(meta);
            JsonObject jo = new JsonParser().parse(getString(is)).getAsJsonObject();
            if (jo.get("name") == null || jo.get("main") == null || jo.get("version") == null) {
                handleMeeplingErrorLoad("Could not load " + file.getName() + ", it is missing one or more of the required properties, 'name', 'main' or 'version'.");
                return;
            }
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {new URI("jar:file:" + file.getAbsolutePath() + "!/").toURL()};

            try (URLClassLoader cl = URLClassLoader.newInstance(urls, Thread.currentThread().getContextClassLoader())) {
                Class<?> mainClass = null;
                while (e.hasMoreElements()) {
                    JarEntry je = e.nextElement();
                    if (!je.getName().equals("meepling.json")) {
                        if (je.isDirectory() || !je.getName().endsWith(".class")) continue;
                        String className = je.getName().substring(0, je.getName().length() - 6);
                        className = className.replace('/', '.');
                        Class<?> c = cl.loadClass(className);
                        if (className.equals(jo.get("main").getAsString())) {
                            mainClass = c;
                        }
                    }
                }

                if (mainClass == null) {
                    handleMeeplingErrorLoad("Could not load " + jo.get("name").getAsString() + ", the main class could not be found.");
                    return;
                }

                if (!mainClass.isAssignableFrom(Meepling.class)) {
                    handleMeeplingErrorLoad("Could not load " + jo.get("name").getAsString() + ", the main class doesn't extend Meepling.");
                    return;
                }

                @SuppressWarnings("unchecked") // Checked above
                Constructor<Meepling> pluginConstructer = (Constructor<Meepling>) mainClass.getDeclaredConstructor();
                Meepling pluginInstance = pluginConstructer.newInstance();
                pluginInstance.setMetadata(jo);
                if (pluginInstance.shouldUseDefaultConfig()) {
                    pluginInstance.handleDefaultReload();
                }
                pluginInstance.init();
                if (Meep.get().getConfig().node("memeps.enabled").getBoolean(true) && pluginInstance instanceof Memepling memepling) {
                    Meep.memeps.addAll(memepling.memeps());
                }
                MeeplingManager.get().registerMeepling(pluginInstance, jo.get("name").getAsString());
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | IOException | ClassNotFoundException | URISyntaxException e) {
            handleMeeplingErrorLoad(e);
        }
    }

    private void handleMeeplingErrorLoad(String message) {
        if (!Meep.get().getConfig().node("error-handling", "meepling-fail-loading-should-crash").getBoolean(true)) {
            Meep.getMasterLogger().error(message);
        } else {
            Meep.get().executeCrash(null, message);
        }
    }

    private void handleMeeplingErrorLoad(Throwable issue) {
        if (!Meep.get().getConfig().node("error-handling", "meepling-fail-loading-should-crash").getBoolean(true)) {
            Meep.getMasterLogger().error(ExceptionUtils.throwableToString(issue));
        } else {
            Meep.get().executeCrash(issue, "An error was thrown while loading a Meepling.");
        }
    }
}

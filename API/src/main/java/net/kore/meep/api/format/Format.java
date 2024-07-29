package net.kore.meep.api.format;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kore.meep.api.Meep;
import net.kore.meep.api.entity.Entity;
import net.kore.meep.api.meepling.Meepling;
import net.kore.meep.api.meepling.loader.Loader;
import net.kore.meep.api.utils.ExceptionUtils;
import net.kore.meep.api.world.World;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format {
    private static final Map<String, Formatter> FORMATTERS = new HashMap<>();
    private static final Pattern pattern = Pattern.compile("\\^.+\\^");

    public static void register(Formatter formatter) {
        FORMATTERS.put(formatter.id(), formatter);
    }

    public static void reload() {
        FORMATTERS.forEach((str, formatter) -> {
            if (!formatter.persistOnReload()) {
                FORMATTERS.remove(str);
                Meep.getMasterLogger().info("Unloaded " + PlainTextComponentSerializer.plainText().serialize(formatter.displayName()) + "...");
            }
        });

        File meeplingDir = new File(Meep.getServerDirectory(), "meeplings");

        File[] files = meeplingDir.listFiles();
        if (files != null) {
            for (File file : files) {
                try (JarFile jarFile = new JarFile(file)) {
                    JarEntry formatterData = jarFile.getJarEntry("formatter.json");
                    if (formatterData != null) {
                        InputStream is = jarFile.getInputStream(formatterData);
                        JsonObject jo = new JsonParser().parse(Loader.getString(is)).getAsJsonObject();
                        if (jo.get("main") == null) {
                            Meep.getMasterLogger().error("Could not load " + file.getName() + ", it is missing the required property 'main'. It is a Formatter JAR");
                            continue;
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
                                Meep.getMasterLogger().error("Could not load " + jo.get("name").getAsString() + ", the main class could not be found. It is a Formatter JAR");
                                continue;
                            }

                            if (!mainClass.isAssignableFrom(Formatter.class)) {
                                Meep.getMasterLogger().error("Could not load " + jo.get("name").getAsString() + ", the main class doesn't extend Formatter.");
                                continue;
                            }

                            @SuppressWarnings("unchecked") // Checked above
                            Constructor<Formatter> formatterConstructor = (Constructor<Formatter>) mainClass.getDeclaredConstructor();
                            Formatter formatter = formatterConstructor.newInstance();

                            Format.register(formatter);
                        }
                    }
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                         IOException | ClassNotFoundException | URISyntaxException e) {
                    Meep.getMasterLogger().error(ExceptionUtils.throwableToString(e));
                }
            }
        }
    }

    public static String parse(String message) {
        return parse(message, null, null);
    }

    public static String parse(String message, Entity entity) {
        return parse(message, entity, entity.getPosition().getWorld());
    }

    public static String parse(String message, World world) {
        return parse(message, null, world);
    }

    public static String parse(String message, Entity entity, World world) {
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String toReplace = matcher.group();
            String id = toReplace.substring(1, toReplace.length() - 1);
            if (Meep.get().getConfig().node("debug", "format-debug").getBoolean(false)) {
                Meep.getMasterLogger().info("----------FORMAT DEBUGGING----------");
                Meep.getMasterLogger().info("Before Formatting:");
                Meep.getMasterLogger().info("ID -> " + id);
                Meep.getMasterLogger().info("Entity ID -> " + (entity == null ? "null" : entity.getUUID()));
                Meep.getMasterLogger().info("World -> " + (world == null ? "null" : world.getKey().getFormatted()));
                Meep.getMasterLogger().info("------------------------------------");
            }
            for (Formatter formatter : FORMATTERS.values()) {
                String result = formatter.parse(id, entity, world);
                if (Meep.get().getConfig().node("debug", "format-debug").getBoolean(false)) {
                    Meep.getMasterLogger().info("Formatted with "+formatter.id()+":");
                    Meep.getMasterLogger().info("ID -> " + id);
                    Meep.getMasterLogger().info("Entity ID -> " + (entity == null ? "null" : entity.getUUID()));
                    Meep.getMasterLogger().info("World -> " + (world == null ? "null" : world.getKey().getFormatted()));
                    Meep.getMasterLogger().info("------------------------------------");
                }
                if (result != null) {
                    message = message.replace(toReplace, result);
                }
            }
            if (Meep.get().getConfig().node("debug", "format-debug").getBoolean(false)) {
                Meep.getMasterLogger().info("After Formatting:");
                Meep.getMasterLogger().info("ID -> " + id);
                Meep.getMasterLogger().info("Entity ID -> " + (entity == null ? "null" : entity.getUUID()));
                Meep.getMasterLogger().info("World -> " + (world == null ? "null" : world.getKey().getFormatted()));
                Meep.getMasterLogger().info("----------FORMAT DEBUGGING----------");
            }
        }

        return message;
    }
}

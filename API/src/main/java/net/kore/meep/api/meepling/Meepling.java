/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.meepling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kore.meep.api.Meep;
import net.kore.meep.api.logger.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.*;

public abstract class Meepling {
    private Logger logger;

    /**
     * Get the logger for the Meepling
     * @return {@link Logger}
     */
    public Logger getLogger() {
        if (logger == null) logger = new Logger(name);
        return logger;
    }

    private String name;

    /**
     * Get the name of the Meepling
     * @return {@link String}
     */
    public @NotNull String getName() {
        return name == null ? "" : name;
    }

    private String version;

    /**
     * Get the version of the Meepling
     * @return {@link String}
     */
    public @NotNull String getVersion() {
        return version == null ? "" : version;
    }

    private String description;

    /**
     * Get the description of the Meepling
     * @return {@link String}
     */
    public @NotNull String getDescription() {
        return description == null ? "" : description;
    }

    private CommentedConfigurationNode config;
    @ApiStatus.Internal
    public void setConfig(CommentedConfigurationNode config) {
        this.config = config;
    }

    /**
     * Get the config of the Meepling
     * @return {@link CommentedConfigurationNode}
     */
    public CommentedConfigurationNode getConfig() {
        return config;
    }

    /**
     * Handle a reload using the default config file
     */
    public void handleDefaultReload() {
        try {
            handleDefaultReloadElseThrow();
        } catch (ConfigurateException ex) {
            if (!Meep.get().getConfig().node("error-handling", "meepling-fail-loading-config-should-crash").getBoolean(false)) {
                throw new RuntimeException(ex);
            } else {
                Meep.get().executeCrash(ex, "Could not load config for "+name);
            }
        }
    }

    /**
     * Handle a reload using the default config file, can throw
     * @throws ConfigurateException When loading a config file which has incorrect syntax
     */
    public void handleDefaultReloadElseThrow() throws ConfigurateException {
        File configFile = new File(getConfigDir(), "config.conf");
        if (!configFile.exists()) {
            try {
                OutputStream os = new FileOutputStream(configFile);
                InputStream is = getClass().getResourceAsStream("config.conf");
                if (is == null) throw new RuntimeException("Unable to read default config");
                os.write(is.readAllBytes());
                os.close();
                is.close();
            } catch (IOException ex) {
                throw new RuntimeException("Unable to write config.", ex);
            }
        }

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .file(configFile)
                .build();

        setConfig(loader.load());
    }

    @ApiStatus.Internal
    public void setMetadata(JsonObject data) {
        name = data.get("name").getAsString().replaceAll("[^a-zA-Z0-9]", "");
        version = data.get("version").getAsString();
        JsonElement des = data.get("description");
        description = des == null ? null : des.getAsString();
        logger = new Logger(name);
    }

    /**
     * Get the config directory of the Meepling
     * @return {@link File}
     */
    public File getConfigDir() {
        return getConfigDir(true);
    }
    /**
     * Get the config directory of the Meepling or server in general
     * @return {@link File}
     */
    public File getConfigDir(boolean personal) {
        return personal ? new File(getConfigDir(false), name) : new File(Meep.getServerDirectory(), "meeplingConfig");
    }

    /**
     * Where the magic happens.
     */
    public abstract void init();

    /**
     * Should use default config
     * @return boolean
     */
    public boolean shouldUseDefaultConfig() { // You SHOULD set this, but there's a default of no
        return false;
    }
}

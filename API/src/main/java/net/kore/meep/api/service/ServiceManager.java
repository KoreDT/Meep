/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.service;

import net.kore.meep.api.Meep;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {
    private ServiceManager INSTANCE;
    public ServiceManager get() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceManager();
        }
        return INSTANCE;
    }
    private ServiceManager() {}

    private final Map<Class<?>, Object> SERVICES = new HashMap<>();

    private boolean shouldAllowOverwrites = false;
    public boolean shouldAllowOverwrites() {
        return shouldAllowOverwrites;
    }

    public void setShouldAllowOverwrites(boolean shouldAllowOverwrites) {
        this.shouldAllowOverwrites = shouldAllowOverwrites;
    }

    /**
     * Register a new service
     * @param clazz The class of the service
     * @param thing The implementation of the service
     * @param <T> The class of the thing
     */
    public <T> void register(Class<T> clazz, T thing) {
        if (thing.getClass().isAssignableFrom(clazz)) { // Genertics are shifty
            if (SERVICES.containsKey(clazz) && !shouldAllowOverwrites) {
                Meep.getMasterLogger().warn("Service using class `%s` was attempted to be registered but the service already existed.".formatted(clazz.getSimpleName()));
                return;
            }
            SERVICES.put(clazz, thing);
        }
    }

    /**
     * Get the service
     * @param clazz The class of the service
     * @return {@link T}
     * @param <T> The class of the thing
     */
    @SuppressWarnings("unchecked") // We know, method above
    public <T> T get(Class<T> clazz) {
        Object object = SERVICES.get(clazz);
        if (object == null) return null;
        return (T) object;
    }
}

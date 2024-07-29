/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.meepling;

import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MeeplingManager {
    private static MeeplingManager INSTANCE;
    public static MeeplingManager get() {
        if (INSTANCE == null) {
            INSTANCE = new MeeplingManager();
        }
        return INSTANCE;
    }
    private MeeplingManager() {}

    private final Map<String, Meepling> meeplingMap = new HashMap<>();
    private final Map<Class<? extends Meepling>, String> clazzMap = new HashMap<>();

    public List<Meepling> getMeeplings() {
        return meeplingMap.values().stream().toList();
    }

    @ApiStatus.Internal
    public void registerMeepling(Meepling meepling, String name) {
        meeplingMap.put(name, meepling);
        clazzMap.put(meepling.getClass(), name);
    }

    /**
     * Check if a Meepling is registered
     * @param name The name of the Meepling
     * @return boolean
     */
    public boolean isMeeplingRegistered(String name) {
        return meeplingMap.get(name) != null;
    }

    /**
     * Check if a Meepling is registered
     * @param clazz The class of the Meepling
     * @return boolean
     */
    public boolean isMeeplingRegistered(Class<? extends Meepling> clazz) {
        return clazzMap.get(clazz) != null;
    }

    /**
     * Get the Meepling by name
     * @param name The name of the Meepling
     * @return {@link Meepling}
     */
    public Optional<Meepling> getMeepling(String name) {
        return Optional.ofNullable(meeplingMap.get(name));
    }

    /**
     * Get the Meepling by class
     * @param clazz The class of the Meepling
     * @return {@link Meepling}
     */
    public Optional<Meepling> getMeepling(Class<? extends Meepling> clazz) {
        return getMeepling(clazzMap.get(clazz));
    }
}

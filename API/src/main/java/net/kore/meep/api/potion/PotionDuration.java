/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.potion;

public class PotionDuration {
    private final long duration;
    public long getDuration() {
        return duration;
    }

    public PotionDuration(long duration) {
        this.duration = duration;
    }

    public static class Infinite extends PotionDuration {
        public Infinite() {
            super(-1);
        }
    }
}

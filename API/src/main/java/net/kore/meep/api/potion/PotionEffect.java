/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.potion;

import net.kore.meep.api.NamespaceKey;

public class PotionEffect {
    private final NamespaceKey type;
    private final PotionDuration duration;
    private final int amplifier;
    private final boolean particles;
    private final boolean ambient;
    private final boolean icon;

    public NamespaceKey getType() {
        return type;
    }
    public PotionDuration getDuration() {
        return duration;
    }
    public int getAmplifier() {
        return amplifier;
    }
    public boolean isParticles() {
        return particles;
    }
    public boolean isAmbient() {
        return ambient;
    }
    public boolean isIcon() {
        return icon;
    }

    public boolean isInfinite() {
        return duration.getDuration() == -1;
    }

    public PotionEffect(NamespaceKey type, PotionDuration duration, int amplifier, boolean particles, boolean ambient, boolean icon) {
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
        this.particles = particles;
        this.ambient = ambient;
        this.icon = icon;
    }

    public PotionEffect(NamespaceKey type, PotionDuration duration, int amplifier, boolean visual) {
        this(type, duration, amplifier, visual, visual, visual);
    }

    public PotionEffect(NamespaceKey type, PotionDuration duration, int amplifier) {
        this(type, duration, amplifier, true);
    }
}

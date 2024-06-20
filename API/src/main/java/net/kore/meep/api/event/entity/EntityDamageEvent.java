/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.entity;

import net.kore.meep.api.entity.Entity;
import net.kore.meep.api.event.CancellableEvent;

public class EntityDamageEvent extends EntityEvent implements CancellableEvent {
    private boolean cancelled = false;
    @Override
    public void cancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    @Override
    public boolean cancelled() {
        return cancelled;
    }

    public EntityDamageEvent(Entity entity) {
        super(entity);
    }
}

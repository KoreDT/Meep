/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.entity;

import net.kore.meep.api.entity.Entity;
import net.kore.meep.api.event.Event;

public abstract class EntityEvent extends Event {
    private final Entity entity;
    public Entity getEntity() {
        return entity;
    }

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }
}

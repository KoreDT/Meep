/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event;

public interface CancellableEvent {
    /**
     * Set the cancelled state of the event
     * @param cancelled Should the event be cancelled
     */
    void cancelled(boolean cancelled);

    /**
     * Get if the event is cancelled
     * @return boolean
     */
    boolean cancelled();
}

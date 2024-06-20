/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

public interface Neutral extends Agressible {
    /**
     * Check to see if the current entity will attack in packs (like wolfs do)
     * @return boolean
     */
    boolean othersTargetAgressee();
}

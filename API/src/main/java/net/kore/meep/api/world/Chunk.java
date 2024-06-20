/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.world;

import net.kore.meep.api.PersistantStorer;
import net.kore.meep.api.block.Block;
import org.jetbrains.annotations.Range;

public interface Chunk extends PersistantStorer {
    int getX();
    int getZ();

    World getWorld();
    Block getBlock(@Range(from = 0, to = 15) int x, @Range(from = -64, to = 319) int y, @Range(from = 0, to = 15) int z);
}

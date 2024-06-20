/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.entity.types.Breedable;
import net.kore.meep.api.entity.types.Bucketable;
import net.kore.meep.api.entity.types.Leashable;

public interface Axolotl extends Entity, Breedable, Bucketable, Leashable {
    @Override
    default NamespaceKey getBucketMaterialKey() {
        return NamespaceKey.MINECRAFT("axolotl_bucket", NamespaceKey.KeyType.ITEM);
    }

    boolean isPlayingDead();
}

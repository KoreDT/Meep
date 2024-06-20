/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.attribute;

import net.kore.meep.api.inventory.EquipmentSlot;

import java.util.UUID;

public class AttributeMod {
    private final UUID uuid;
    private final double amount;
    private final Operation operation;
    private final EquipmentSlot slot;

    /**
     * Get the UUID with this modifier
     * @return {@link UUID}
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * The amount of value to change
     * @return double
     */
    public double getAmount() {
        return amount;
    }

    /**
     * The operation to apply to the modifier
     * @return {@link Operation}
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Get the equipment slot that the attribute applies to
     * @return {@link EquipmentSlot}
     */
    public EquipmentSlot getSlot() {
        return slot;
    }

    public AttributeMod(double amount, Operation operation, EquipmentSlot slot) {
        this(UUID.randomUUID(), amount, operation, slot);
    }

    public AttributeMod(UUID uuid, double amount, Operation operation, EquipmentSlot slot) {
        this.uuid = uuid;
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
    }

    /**
     * The operation to apply to the modifier
     */
    public enum Operation {
        ADD_NUMBER,
        ADD_SCALAR,
        MULTIPLY_SCALAR;

        Operation() {}
    }
}

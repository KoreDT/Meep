/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.task;

import net.kore.meep.api.Meep;

public abstract class MeepTask {
    private int tickCount = 0;
    public int getTickCount() {
        return tickCount;
    }
    public void executeTick() {
        if (tickCount == 0) {
            run();
            if (repeatAfter() != null) {
                tickCount = repeatAfter();
            } else {
                remove();
            }
        } else {
            tickCount--;
        }
    }
    public void init() {
        tickCount = delay();
    }

    public abstract int delay();
    public abstract Integer repeatAfter();
    public abstract void run();

    public void remove() {
        Meep.get().getSchedular().destroyTask(this);
    }
}

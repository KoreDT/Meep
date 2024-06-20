/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.task;

import net.kore.meep.api.task.MeepTask;
import net.kore.meep.api.task.TaskSchedular;

import java.util.HashSet;
import java.util.Set;

public class PaperAsyncTaskSchedular implements TaskSchedular {
    private final Set<MeepTask> tasks = new HashSet<>();

    public void onTick() {
        for (MeepTask task : tasks) {
            task.executeTick();
        }
    }

    @Override
    public void registerTask(MeepTask task) {
        task.init();
        tasks.add(task);
    }

    @Override
    public void destroyTask(MeepTask task) {
        tasks.remove(task);
    }
}

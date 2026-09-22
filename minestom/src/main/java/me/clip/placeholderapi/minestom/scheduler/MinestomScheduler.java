/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2026 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlaceholderAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.clip.placeholderapi.minestom.scheduler;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

public final class MinestomScheduler {

    private MinestomScheduler() {
    }

    @NotNull
    public static Task runTask(@NotNull final Runnable task) {
        return MinecraftServer.getSchedulerManager().submitTask(() -> {
            task.run();
            return TaskSchedule.stop();
        }, ExecutionType.TICK_END);
    }

    @NotNull
    public static Task runTaskLater(@NotNull final Runnable task, @NotNull final Duration delay) {
        final AtomicBoolean first = new AtomicBoolean(true);

        return MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (first.getAndSet(false))
                return TaskSchedule.duration(delay);

            task.run();
            return TaskSchedule.stop();
        }, ExecutionType.TICK_END);
    }

    @NotNull
    public static Task runTaskTimer(@NotNull final Runnable task, @NotNull final Duration delay,
                                    @NotNull final Duration period) {
        final AtomicBoolean first = new AtomicBoolean(true);

        return MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (first.getAndSet(false))
                return TaskSchedule.duration(delay);

            task.run();
            return TaskSchedule.duration(period);
        }, ExecutionType.TICK_END);
    }
}

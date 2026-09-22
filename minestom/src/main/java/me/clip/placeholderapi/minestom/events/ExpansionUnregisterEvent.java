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

package me.clip.placeholderapi.minestom.events;

import me.clip.placeholderapi.minestom.PlaceholderExpansion;
import net.minestom.server.event.Event;
import org.jetbrains.annotations.NotNull;

public final class ExpansionUnregisterEvent implements Event {

    @NotNull
    private final PlaceholderExpansion expansion;

    public ExpansionUnregisterEvent(@NotNull final PlaceholderExpansion expansion) {
        this.expansion = expansion;
    }

    @NotNull
    public PlaceholderExpansion getExpansion() {
        return expansion;
    }
}

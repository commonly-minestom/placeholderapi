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

import java.util.Collections;
import java.util.List;

import me.clip.placeholderapi.minestom.PlaceholderExpansion;
import net.minestom.server.event.Event;
import org.jetbrains.annotations.NotNull;

public final class ExpansionsLoadedEvent implements Event {

    @NotNull
    private final List<PlaceholderExpansion> expansions;

    public ExpansionsLoadedEvent(@NotNull final List<PlaceholderExpansion> expansions) {
        this.expansions = Collections.unmodifiableList(expansions);
    }

    @NotNull
    public List<PlaceholderExpansion> getExpansions() {
        return expansions;
    }
}

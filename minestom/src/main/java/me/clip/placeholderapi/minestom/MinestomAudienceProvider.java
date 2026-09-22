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

package me.clip.placeholderapi.minestom;

import java.util.UUID;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

final class MinestomAudienceProvider implements AudienceProvider {

    @NotNull
    private static final MinestomAudienceProvider instance = new MinestomAudienceProvider();

    @NotNull
    static MinestomAudienceProvider get() {
        return instance;
    }

    private MinestomAudienceProvider() {
    }

    @NotNull
    @Override
    public Audience all() {
        return Audiences.all();
    }

    @NotNull
    @Override
    public Audience console() {
        return Audiences.console();
    }

    @NotNull
    @Override
    public Audience players() {
        return Audiences.players();
    }

    @NotNull
    @Override
    public Audience player(@NotNull final UUID playerId) {
        final Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(playerId);

        if (player == null)
            return Audience.empty();

        return player;
    }

    @NotNull
    @Override
    public Audience permission(@NotNull final String permission) {
        return Audience.empty();
    }

    @NotNull
    @Override
    public Audience world(@NotNull final Key world) {
        return Audience.empty();
    }

    @NotNull
    @Override
    public Audience server(@NotNull final String serverName) {
        return Audience.empty();
    }

    @NotNull
    @Override
    public ComponentFlattener flattener() {
        return ComponentFlattener.basic();
    }

    @Override
    public void close() {
    }
}

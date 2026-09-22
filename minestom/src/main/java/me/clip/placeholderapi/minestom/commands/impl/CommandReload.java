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

package me.clip.placeholderapi.minestom.commands.impl;

import me.clip.placeholderapi.minestom.PlaceholderAPIPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import me.clip.placeholderapi.minestom.commands.PlaceholderCommand;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;

public final class CommandReload extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public CommandReload(@NotNull final PlaceholderAPIPlugin plugin) {
        super("reload");

        this.plugin = plugin;

        setDefaultExecutor((sender, context) -> {
            if (!PlaceholderCommand.requireAdmin(sender))
                return;

            run(sender);
        });
    }

    public void run(@NotNull final CommandSender sender) {
        plugin.getLocalExpansionManager().kill();
        plugin.getLocalExpansionManager().load();

        sender.sendMessage(Component.text("PlaceholderAPI reloaded.", NamedTextColor.GREEN));
    }
}

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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import me.clip.placeholderapi.minestom.commands.PlaceholderCommand;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;

public final class CommandHelp extends Command {

    public CommandHelp() {
        super("help");

        setDefaultExecutor((sender, context) -> {
            if (!PlaceholderCommand.requireAdmin(sender))
                return;

            run(sender);
        });
    }

    public void run(@NotNull final CommandSender sender) {
        sender.sendMessage(Component.text("PlaceholderAPI commands:", NamedTextColor.GOLD));

        sender.sendMessage(Component.text("/papi help - Shows this page", NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/papi info <identifier> - Shows expansion info",
                NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/papi list - Lists registered expansions",
                NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/papi parse <player> <text...> - Parses placeholders",
                NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/papi register <identifier> - Registers an expansion",
                NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/papi reload - Reloads expansions",
                NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/papi unregister <identifier> - Unregisters an expansion",
                NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/papi version - Shows version info",
                NamedTextColor.GRAY));
    }
}

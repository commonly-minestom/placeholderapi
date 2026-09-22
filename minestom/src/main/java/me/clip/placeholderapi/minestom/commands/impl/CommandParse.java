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

import java.util.Arrays;

import me.clip.placeholderapi.minestom.PlaceholderAPI;
import me.clip.placeholderapi.minestom.commands.PlaceholderCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CommandParse extends Command {

    public CommandParse() {
        super("parse");

        setDefaultExecutor((sender, context) -> {
            if (!PlaceholderCommand.requireAdmin(sender))
                return;

            final String[] args = PlaceholderCommand.arguments(context);

            if (args.length == 0) {
                sender.sendMessage(Component.text("Usage: /papi parse <player> <text...>",
                        NamedTextColor.RED));
                return;
            }

            final Player target = MinecraftServer.getConnectionManager()
                    .getOnlinePlayerByUsername(args[0]);

            final String text;

            if (target == null)
                text = String.join(" ", args);
            else if (args.length == 1) {
                sender.sendMessage(Component.text("Usage: /papi parse <player> <text...>",
                        NamedTextColor.RED));
                return;
            } else {
                text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            }

            run(sender, target, text);
        });
    }

    public void run(@NotNull final CommandSender sender, @Nullable final Player target,
                    @NotNull final String text) {
        sender.sendMessage(Component.text(PlaceholderAPI.setPlaceholders(target, text),
                NamedTextColor.GRAY));
    }
}

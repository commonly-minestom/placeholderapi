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
import me.clip.placeholderapi.minestom.commands.PlaceholderCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;

public final class CommandRegister extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public CommandRegister(@NotNull final PlaceholderAPIPlugin plugin) {
        super("register");

        this.plugin = plugin;

        setDefaultExecutor((sender, context) -> {
            if (!PlaceholderCommand.requireAdmin(sender))
                return;

            final String[] args = PlaceholderCommand.arguments(context);

            if (args.length == 0) {
                sender.sendMessage(Component.text("Usage: /papi register <identifier>",
                        NamedTextColor.RED));
                return;
            }

            run(sender, args[0]);
        });
    }

    public void run(@NotNull final CommandSender sender, @NotNull final String identifier) {
        if (plugin.getLocalExpansionManager().registerFromFile(identifier)) {
            sender.sendMessage(Component.text("Registered expansion " + identifier,
                    NamedTextColor.GREEN));
            return;
        }

        sender.sendMessage(Component.text("Failed to register expansion " + identifier,
                NamedTextColor.RED));
    }
}

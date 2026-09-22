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

import java.util.Optional;

import me.clip.placeholderapi.minestom.PlaceholderAPIPlugin;
import me.clip.placeholderapi.minestom.PlaceholderExpansion;
import me.clip.placeholderapi.minestom.commands.PlaceholderCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;

public final class CommandInfo extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public CommandInfo(@NotNull final PlaceholderAPIPlugin plugin) {
        super("info");

        this.plugin = plugin;

        setDefaultExecutor((sender, context) -> {
            if (!PlaceholderCommand.requireAdmin(sender))
                return;

            final String[] args = PlaceholderCommand.arguments(context);

            if (args.length == 0) {
                sender.sendMessage(Component.text("Usage: /papi info <identifier>",
                        NamedTextColor.RED));
                return;
            }

            run(sender, args[0]);
        });
    }

    public void run(@NotNull final CommandSender sender, @NotNull final String identifier) {
        final Optional<PlaceholderExpansion> found = plugin.getLocalExpansionManager()
                .findExpansionByIdentifier(identifier);

        if (found.isEmpty()) {
            sender.sendMessage(Component.text("No expansion with identifier " + identifier,
                    NamedTextColor.RED));
            return;
        }

        final PlaceholderExpansion expansion = found.get();

        sender.sendMessage(Component.text("Expansion: " + expansion.getName(),
                NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Identifier: " + expansion.getIdentifier(),
                NamedTextColor.GRAY));
        sender.sendMessage(Component.text("Author: " + expansion.getAuthor(),
                NamedTextColor.GRAY));
        sender.sendMessage(Component.text("Version: " + expansion.getVersion(),
                NamedTextColor.GRAY));
    }
}

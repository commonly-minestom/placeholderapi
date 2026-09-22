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

package me.clip.placeholderapi.minestom.commands;

import java.util.Arrays;

import me.clip.placeholderapi.minestom.PlaceholderAPIPlugin;
import me.clip.placeholderapi.minestom.commands.impl.CommandHelp;
import me.clip.placeholderapi.minestom.commands.impl.CommandInfo;
import me.clip.placeholderapi.minestom.commands.impl.CommandList;
import me.clip.placeholderapi.minestom.commands.impl.CommandParse;
import me.clip.placeholderapi.minestom.commands.impl.CommandRegister;
import me.clip.placeholderapi.minestom.commands.impl.CommandReload;
import me.clip.placeholderapi.minestom.commands.impl.CommandUnregister;
import me.clip.placeholderapi.minestom.commands.impl.CommandVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderCommand extends Command {

    public PlaceholderCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("papi");

        addSubcommand(new CommandHelp());
        addSubcommand(new CommandInfo(plugin));
        addSubcommand(new CommandList(plugin));
        addSubcommand(new CommandParse());
        addSubcommand(new CommandRegister(plugin));
        addSubcommand(new CommandReload(plugin));
        addSubcommand(new CommandUnregister(plugin));
        addSubcommand(new CommandVersion(plugin));

        setDefaultExecutor((sender, context) -> new CommandVersion(plugin).run(sender));
    }

    @NotNull
    public static String[] arguments(@NotNull final CommandContext context) {
        final String[] parts = context.getInput().split(" ");

        if (parts.length <= 2)
            return new String[0];

        return Arrays.copyOfRange(parts, 2, parts.length);
    }

    public static boolean requireAdmin(@NotNull final CommandSender sender) {
        if (sender instanceof Player && ((Player) sender).getPermissionLevel() < 4) {
            sender.sendMessage(Component.text("You do not have permission to use this command.",
                    NamedTextColor.RED));
            return false;
        }

        return true;
    }
}

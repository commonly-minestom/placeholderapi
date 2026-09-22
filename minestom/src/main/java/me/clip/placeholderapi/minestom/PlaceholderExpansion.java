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

import java.util.Collections;
import java.util.List;

import me.clip.placeholderapi.configuration.YamlConfiguration;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PlaceholderExpansion {

    @NotNull
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NotNull
    public abstract String getIdentifier();

    @NotNull
    public abstract String getAuthor();

    @NotNull
    public abstract String getVersion();

    @Nullable
    public abstract String onRequest(@Nullable final Player player, @NotNull final String params);

    @NotNull
    public String getName() {
        return getIdentifier();
    }

    @Nullable
    public String getRequiredPlugin() {
        return null;
    }

    public boolean persist() {
        return false;
    }

    public boolean canRegister() {
        return true;
    }

    public final boolean isRegistered() {
        return PlaceholderAPI.findExpansion(getIdentifier())
                .map(found -> found == this)
                .orElse(false);
    }

    public boolean register() {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();

        if (plugin == null)
            return PlaceholderAPI.register(this);

        return plugin.getLocalExpansionManager().register(this);
    }

    public final boolean unregister() {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();

        if (plugin == null)
            return PlaceholderAPI.unregister(getIdentifier());

        return plugin.getLocalExpansionManager().unregister(this);
    }

    @Nullable
    public final YamlConfiguration getConfigSection() {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();

        if (plugin == null)
            return null;

        return plugin.getConfiguration().getConfigurationSection("expansions." + getIdentifier());
    }

    @Nullable
    public final YamlConfiguration getConfigSection(@NotNull final String path) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return null;

        return section.getConfigurationSection(path);
    }

    @Nullable
    public final Object get(@NotNull final String path, @Nullable final Object def) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return def;

        return section.get(path, def);
    }

    public final int getInt(@NotNull final String path, final int def) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return def;

        return section.getInt(path, def);
    }

    public final long getLong(@NotNull final String path, final long def) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return def;

        return section.getLong(path, def);
    }

    public final double getDouble(@NotNull final String path, final double def) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return def;

        return section.getDouble(path, def);
    }

    @Nullable
    public final String getString(@NotNull final String path, @Nullable final String def) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return def;

        return section.getString(path, def);
    }

    @NotNull
    public final List<String> getStringList(@NotNull final String path) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return Collections.emptyList();

        return section.getStringList(path);
    }

    public final boolean getBoolean(@NotNull final String path, final boolean def) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return def;

        return section.getBoolean(path, def);
    }

    public final boolean configurationContains(@NotNull final String path) {
        final YamlConfiguration section = getConfigSection();

        if (section == null)
            return false;

        return section.contains(path);
    }

    public void info(@NotNull final String message) {
        logger.info("[{}] {}", getName(), message);
    }

    public void warning(@NotNull final String message) {
        logger.warn("[{}] {}", getName(), message);
    }

    public void severe(@NotNull final String message) {
        logger.error("[{}] {}", getName(), message);
    }

    public void severe(@NotNull final String message, @NotNull final Throwable throwable) {
        logger.error("[{}] {}", getName(), message, throwable);
    }
}

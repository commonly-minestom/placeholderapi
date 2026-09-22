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

package me.clip.placeholderapi.configuration;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BukkitYamlConfiguration implements YamlConfiguration {

    @NotNull
    private final ConfigurationSection handle;

    public BukkitYamlConfiguration(@NotNull final ConfigurationSection handle) {
        this.handle = handle;
    }

    @Nullable
    @Override
    public Object get(@NotNull final String path, @Nullable final Object def) {
        return handle.get(path, def);
    }

    @Nullable
    @Override
    public String getString(@NotNull final String path) {
        return handle.getString(path);
    }

    @Nullable
    @Override
    public String getString(@NotNull final String path, @Nullable final String def) {
        return handle.getString(path, def);
    }

    @Override
    public int getInt(@NotNull final String path, final int def) {
        return handle.getInt(path, def);
    }

    @Override
    public long getLong(@NotNull final String path, final long def) {
        return handle.getLong(path, def);
    }

    @Override
    public double getDouble(@NotNull final String path, final double def) {
        return handle.getDouble(path, def);
    }

    @Override
    public boolean getBoolean(@NotNull final String path) {
        return handle.getBoolean(path);
    }

    @Override
    public boolean getBoolean(@NotNull final String path, final boolean def) {
        return handle.getBoolean(path, def);
    }

    @NotNull
    @Override
    public List<String> getStringList(@NotNull final String path) {
        return handle.getStringList(path);
    }

    @Override
    public boolean contains(@NotNull final String path) {
        return handle.contains(path);
    }

    @Override
    public void set(@NotNull final String path, @Nullable final Object value) {
        handle.set(path, value);
    }

    @Nullable
    @Override
    public YamlConfiguration getConfigurationSection(@NotNull final String path) {
        final ConfigurationSection section = handle.getConfigurationSection(path);

        if (section == null)
            return null;

        return new BukkitYamlConfiguration(section);
    }
}

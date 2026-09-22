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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface YamlConfiguration {

    @Nullable
    Object get(@NotNull final String path, @Nullable final Object def);

    @Nullable
    String getString(@NotNull final String path);

    @Nullable
    String getString(@NotNull final String path, @Nullable final String def);

    int getInt(@NotNull final String path, final int def);

    long getLong(@NotNull final String path, final long def);

    double getDouble(@NotNull final String path, final double def);

    boolean getBoolean(@NotNull final String path);

    boolean getBoolean(@NotNull final String path, final boolean def);

    @NotNull
    List<String> getStringList(@NotNull final String path);

    boolean contains(@NotNull final String path);

    void set(@NotNull final String path, @Nullable final Object value);

    @Nullable
    YamlConfiguration getConfigurationSection(@NotNull final String path);
}

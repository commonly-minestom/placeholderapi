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

package me.clip.placeholderapi.minestom.configuration;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.clip.placeholderapi.configuration.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

public final class MinestomYamlConfiguration implements YamlConfiguration {

    @NotNull
    private final Map<String, Object> root;

    public MinestomYamlConfiguration() {
        this(new LinkedHashMap<>());
    }

    public MinestomYamlConfiguration(@NotNull final Map<String, Object> root) {
        this.root = root;
    }

    @NotNull
    public static MinestomYamlConfiguration load(@NotNull final Path file) throws IOException {
        if (!Files.exists(file))
            return new MinestomYamlConfiguration();

        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            final Object loaded = new Yaml().load(reader);

            if (loaded instanceof Map)
                return new MinestomYamlConfiguration(cast(loaded));

            return new MinestomYamlConfiguration();
        }
    }

    public void save(@NotNull final Path file) throws IOException {
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            new Yaml().dump(root, writer);
        }
    }

    @Nullable
    @Override
    public Object get(@NotNull final String path, @Nullable final Object def) {
        final Object value = navigate(path);

        if (value == null)
            return def;

        return value;
    }

    @Nullable
    @Override
    public String getString(@NotNull final String path) {
        return getString(path, null);
    }

    @Nullable
    @Override
    public String getString(@NotNull final String path, @Nullable final String def) {
        final Object value = navigate(path);

        if (value == null)
            return def;

        return String.valueOf(value);
    }

    @Override
    public int getInt(@NotNull final String path, final int def) {
        final Object value = navigate(path);

        if (value instanceof Number)
            return ((Number) value).intValue();

        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (final NumberFormatException ignored) {
                return def;
            }
        }

        return def;
    }

    @Override
    public long getLong(@NotNull final String path, final long def) {
        final Object value = navigate(path);

        if (value instanceof Number)
            return ((Number) value).longValue();

        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (final NumberFormatException ignored) {
                return def;
            }
        }

        return def;
    }

    @Override
    public double getDouble(@NotNull final String path, final double def) {
        final Object value = navigate(path);

        if (value instanceof Number)
            return ((Number) value).doubleValue();

        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (final NumberFormatException ignored) {
                return def;
            }
        }

        return def;
    }

    @Override
    public boolean getBoolean(@NotNull final String path) {
        return getBoolean(path, false);
    }

    @Override
    public boolean getBoolean(@NotNull final String path, final boolean def) {
        final Object value = navigate(path);

        if (value instanceof Boolean)
            return (Boolean) value;

        if (value instanceof String)
            return Boolean.parseBoolean((String) value);

        return def;
    }

    @NotNull
    @Override
    public List<String> getStringList(@NotNull final String path) {
        final Object value = navigate(path);

        if (!(value instanceof List))
            return new ArrayList<>();

        final List<String> result = new ArrayList<>();

        for (final Object entry : (List<?>) value) {
            if (entry != null)
                result.add(String.valueOf(entry));
        }

        return result;
    }

    @Override
    public boolean contains(@NotNull final String path) {
        return navigate(path) != null;
    }

    @Override
    public void set(@NotNull final String path, @Nullable final Object value) {
        final String[] parts = path.split("\\.");

        Map<String, Object> current = root;

        for (int i = 0; i < parts.length - 1; i++) {
            final Object child = current.get(parts[i]);

            if (!(child instanceof Map)) {
                final Map<String, Object> created = new LinkedHashMap<>();
                current.put(parts[i], created);

                current = created;
                continue;
            }

            current = cast(child);
        }

        if (value == null)
            current.remove(parts[parts.length - 1]);
        else
            current.put(parts[parts.length - 1], value);
    }

    @Nullable
    @Override
    public YamlConfiguration getConfigurationSection(@NotNull final String path) {
        final Object value = navigate(path);

        if (!(value instanceof Map))
            return null;

        return new MinestomYamlConfiguration(cast(value));
    }

    @Nullable
    private Object navigate(@NotNull final String path) {
        Map<String, Object> current = root;

        final String[] parts = path.split("\\.");

        for (int i = 0; i < parts.length - 1; i++) {
            final Object child = current.get(parts[i]);

            if (!(child instanceof Map))
                return null;

            current = cast(child);
        }

        return current.get(parts[parts.length - 1]);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private static Map<String, Object> cast(@NotNull final Object value) {
        return (Map<String, Object>) value;
    }
}

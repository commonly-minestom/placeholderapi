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

package me.clip.placeholderapi.minestom.expansion.manager;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.clip.placeholderapi.configuration.YamlConfiguration;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.Taskable;
import me.clip.placeholderapi.minestom.PlaceholderAPIPlugin;
import me.clip.placeholderapi.minestom.PlaceholderAPI;
import me.clip.placeholderapi.minestom.PlaceholderExpansion;
import me.clip.placeholderapi.minestom.events.ExpansionRegisterEvent;
import me.clip.placeholderapi.minestom.events.ExpansionUnregisterEvent;
import me.clip.placeholderapi.minestom.events.ExpansionsLoadedEvent;
import me.clip.placeholderapi.minestom.expansion.Cleanable;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LocalExpansionManager {

    @NotNull
    private final Logger logger = LoggerFactory.getLogger(LocalExpansionManager.class);

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    @NotNull
    private final File folder;

    private boolean listenersRegistered;

    public LocalExpansionManager(@NotNull final PlaceholderAPIPlugin plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "expansions");
    }

    public void load() {
        if (!listenersRegistered) {
            MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
                for (final PlaceholderExpansion expansion : getExpansions()) {
                    if (expansion instanceof Cleanable)
                        ((Cleanable) expansion).cleanup(event.getPlayer());
                }
            });

            listenersRegistered = true;
        }

        if (!folder.exists() && !folder.mkdirs()) {
            logger.warn("Failed to create expansions folder: {}", folder);
            return;
        }

        final File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));

        if (files == null)
            return;

        final List<PlaceholderExpansion> registered = new ArrayList<>();

        for (final File file : files) {
            try {
                registered.addAll(registerAll(findExpansionsInFile(file)));
            } catch (final Exception ex) {
                logger.error("Failed to load expansion file: {}", file, ex);
            }
        }

        MinecraftServer.getGlobalEventHandler().call(new ExpansionsLoadedEvent(registered));
    }

    public void kill() {
        for (final PlaceholderExpansion expansion : getExpansions()) {
            if (expansion.persist())
                continue;

            unregister(expansion);
        }
    }

    @NotNull
    public File getExpansionsFolder() {
        return folder;
    }

    @NotNull
    public Set<String> getIdentifiers() {
        return PlaceholderAPI.getIdentifiers();
    }

    @NotNull
    public Collection<PlaceholderExpansion> getExpansions() {
        final List<PlaceholderExpansion> result = new ArrayList<>();

        for (final String identifier : getIdentifiers())
            PlaceholderAPI.findExpansion(identifier).ifPresent(result::add);

        return result;
    }

    @Nullable
    public PlaceholderExpansion getExpansion(@NotNull final String identifier) {
        return PlaceholderAPI.findExpansion(identifier).orElse(null);
    }

    @NotNull
    public Optional<PlaceholderExpansion> findExpansionByIdentifier(@NotNull final String identifier) {
        return PlaceholderAPI.findExpansion(identifier);
    }

    @NotNull
    public Optional<PlaceholderExpansion> findExpansionByName(@NotNull final String name) {
        for (final String identifier : getIdentifiers()) {
            final Optional<PlaceholderExpansion> found = PlaceholderAPI.findExpansion(identifier);

            if (found.isPresent() && found.get().getName().equalsIgnoreCase(name))
                return found;
        }

        return Optional.empty();
    }

    public boolean register(@NotNull final PlaceholderExpansion expansion) {
        final String identifier = expansion.getIdentifier().toLowerCase(Locale.ROOT);

        if (!expansion.canRegister())
            return false;

        if (findExpansionByIdentifier(identifier).isPresent()) {
            logger.warn("Failed to load expansion {}. Identifier is already in use.",
                    expansion.getIdentifier());
            return false;
        }

        if (expansion instanceof Configurable)
            applyDefaults(identifier, (Configurable) expansion);

        final ExpansionRegisterEvent event = new ExpansionRegisterEvent(expansion);
        MinecraftServer.getGlobalEventHandler().call(event);

        if (event.isCancelled())
            return false;

        if (!PlaceholderAPI.register(expansion))
            return false;

        if (expansion instanceof Taskable)
            ((Taskable) expansion).start();

        return true;
    }

    public boolean unregister(@NotNull final PlaceholderExpansion expansion) {
        return unregister(expansion.getIdentifier());
    }
    public boolean unregister(@NotNull final String identifier) {
        final PlaceholderExpansion expansion = getExpansion(identifier);

        if (expansion == null)
            return false;

        if (!PlaceholderAPI.unregister(identifier))
            return false;

        if (expansion instanceof Taskable)
            ((Taskable) expansion).stop();

        if (expansion instanceof Cacheable)
            ((Cacheable) expansion).clear();

        MinecraftServer.getGlobalEventHandler().call(new ExpansionUnregisterEvent(expansion));

        return true;
    }

    public boolean registerFromFile(@NotNull final String identifier) {
        if (!folder.exists())
            return false;

        final File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));

        if (files == null)
            return false;

        for (final File file : files) {
            final List<Class<? extends PlaceholderExpansion>> classes;

            try {
                classes = findExpansionsInFile(file);
            } catch (final Exception ex) {
                logger.error("Failed to load expansion file: {}", file, ex);
                continue;
            }

            for (final Class<? extends PlaceholderExpansion> clazz : classes) {
                final PlaceholderExpansion expansion;

                try {
                    expansion = clazz.getDeclaredConstructor().newInstance();
                } catch (final Exception ex) {
                    logger.error("Failed to create expansion instance for class {}",
                            clazz.getName(), ex);
                    continue;
                }

                if (!expansion.getIdentifier().equalsIgnoreCase(identifier))
                    continue;

                if (register(expansion))
                    return true;
            }
        }

        return false;
    }

    @NotNull
    private List<Class<? extends PlaceholderExpansion>> findExpansionsInFile(@NotNull final File file)
            throws Exception {
        final List<Class<? extends PlaceholderExpansion>> result = new ArrayList<>();

        final URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()},
                getClass().getClassLoader());

        try (JarFile jar = new JarFile(file)) {
            final Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();

                if (entry.isDirectory() || !entry.getName().endsWith(".class"))
                    continue;

                final String className = entry.getName()
                        .substring(0, entry.getName().length() - 6)
                        .replace('/', '.');

                final Class<?> clazz;

                try {
                    clazz = Class.forName(className, false, loader);
                } catch (final VerifyError | NoClassDefFoundError ex) {
                    logger.error("Failed to load expansion {} (is a dependency missing?)",
                            file.getName(), ex);
                    continue;
                }

                if (!PlaceholderExpansion.class.isAssignableFrom(clazz))
                    continue;

                if (Modifier.isAbstract(clazz.getModifiers()))
                    continue;

                try {
                    clazz.getDeclaredConstructor();
                } catch (final NoSuchMethodException ex) {
                    logger.error("Failed to load expansion {}: no public no-arg constructor in {}",
                            file.getName(), className);
                    continue;
                }

                result.add(clazz.asSubclass(PlaceholderExpansion.class));
            }
        }

        return result;
    }

    @NotNull
    private List<PlaceholderExpansion> registerAll(
            @NotNull final List<Class<? extends PlaceholderExpansion>> classes) {
        final List<PlaceholderExpansion> registered = new ArrayList<>();

        for (final Class<? extends PlaceholderExpansion> clazz : classes) {
            final PlaceholderExpansion expansion;

            try {
                expansion = clazz.getDeclaredConstructor().newInstance();
            } catch (final Exception ex) {
                logger.error("Failed to create expansion instance for class {}", clazz.getName(), ex);
                continue;
            }

            if (register(expansion))
                registered.add(expansion);
        }

        return registered;
    }

    private void applyDefaults(@NotNull final String identifier,
                               @NotNull final Configurable expansion) {
        final Map<String, Object> defaults = expansion.getDefaults();

        if (defaults == null)
            return;

        final YamlConfiguration config = plugin.getConfiguration();
        final String prefix = "expansions." + identifier + ".";
        boolean save = false;

        for (final Map.Entry<String, Object> entry : defaults.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isEmpty())
                continue;

            if (entry.getValue() == null) {
                if (config.contains(prefix + entry.getKey())) {
                    config.set(prefix + entry.getKey(), null);
                    save = true;
                }
            } else if (!config.contains(prefix + entry.getKey())) {
                config.set(prefix + entry.getKey(), entry.getValue());
                save = true;
            }
        }

        if (save)
            plugin.saveConfig();
    }
}

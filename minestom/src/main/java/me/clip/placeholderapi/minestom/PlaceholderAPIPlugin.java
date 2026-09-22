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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import me.clip.placeholderapi.PlatformPlugin;
import me.clip.placeholderapi.configuration.YamlConfiguration;
import me.clip.placeholderapi.expansion.Version;
import me.clip.placeholderapi.minestom.commands.PlaceholderCommand;
import me.clip.placeholderapi.minestom.configuration.MinestomYamlConfiguration;
import me.clip.placeholderapi.minestom.expansion.manager.LocalExpansionManager;
import net.kyori.adventure.platform.AudienceProvider;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PlaceholderAPIPlugin implements PlatformPlugin {

    @Nullable
    private static PlaceholderAPIPlugin instance;

    @Nullable
    public static PlaceholderAPIPlugin getInstance() {
        return instance;
    }

    @NotNull
    private final Path dataFolder;

    @NotNull
    private final Path configFile;

    @NotNull
    private final LocalExpansionManager localExpansionManager;

    @NotNull
    private final PlaceholderCommand command;

    @NotNull
    private MinestomYamlConfiguration config;

    private boolean enabled;

    public PlaceholderAPIPlugin(@NotNull final Path dataFolder) {
        this.dataFolder = dataFolder;
        this.configFile = dataFolder.resolve("config.yml");
        this.config = new MinestomYamlConfiguration();
        this.localExpansionManager = new LocalExpansionManager(this);
        this.command = new PlaceholderCommand(this);

        instance = this;
    }

    @Override
    public void enable() {
        if (enabled)
            throw new IllegalStateException("PlaceholderAPI is already enabled");

        try {
            Files.createDirectories(dataFolder);

            if (!Files.exists(configFile)) {
                try (final InputStream stream = getClass().getResourceAsStream("/config.yml")) {
                    if (stream != null)
                        Files.copy(stream, configFile);
                }
            }
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }

        loadConfig();
        PlaceholderAPI.init();

        MinecraftServer.getCommandManager().register(command);
        localExpansionManager.load();

        enabled = true;
    }

    @Override
    public void disable() {
        localExpansionManager.kill();

        MinecraftServer.getCommandManager().unregister(command);

        PlaceholderAPI.shutdown();

        instance = null;
        enabled = false;
    }

    @Override
    public <S> void reloadConf(final S sender) {
        loadConfig();
    }

    @NotNull
    @Override
    public Version getRunningServerVersion() {
        return new Version(MinecraftServer.VERSION_NAME, false);
    }

    @NotNull
    @Override
    public File getDataFolder() {
        return dataFolder.toFile();
    }

    @Override
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @NotNull
    @Override
    public YamlConfiguration getConfiguration() {
        return config;
    }

    @NotNull
    @Override
    public AudienceProvider getAdventure() {
        return MinestomAudienceProvider.get();
    }

    @NotNull
    public LocalExpansionManager getLocalExpansionManager() {
        return localExpansionManager;
    }

    private void loadConfig() {
        try {
            config = MinestomYamlConfiguration.load(configFile);
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}

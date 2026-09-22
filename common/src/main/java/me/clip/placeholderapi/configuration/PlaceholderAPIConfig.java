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

import me.clip.placeholderapi.PlatformPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class PlaceholderAPIConfig {

    @NotNull
    private final PlatformPlugin plugin;

    public PlaceholderAPIConfig(@NotNull final PlatformPlugin plugin) {
        this.plugin = plugin;
    }


    public boolean checkUpdates() {
        return plugin.getConfiguration().getBoolean("check_updates");
    }


    public boolean isCloudEnabled() {
        return plugin.getConfiguration().getBoolean("cloud_enabled");
    }

    public void setCloudEnabled(boolean state) {
        plugin.getConfiguration().set("cloud_enabled", state);
        plugin.saveConfig();
    }


    public boolean isDebugMode() {
        return plugin.getConfiguration().getBoolean("debug", false);
    }

    public boolean useAdventureReplacer() {
        return plugin.getConfiguration().getBoolean("use_adventure_provided_replacer", false);
    }


    public Optional<ExpansionSort> getExpansionSort() {
        final String option = plugin.getConfiguration()
                .getString("cloud_sorting", ExpansionSort.LATEST.name());

        try {
            //noinspection ConstantConditions (bad spigot annotation)
            return Optional.of(ExpansionSort.valueOf(option.toUpperCase()));
        } catch (final IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }


    @NotNull
    public String dateFormat() {
        //noinspection ConstantConditions (bad spigot annotation)
        return plugin.getConfiguration().getString("date_format", "MM/dd/yy HH:mm:ss");
    }


    @NotNull
    public String booleanTrue() {
        //noinspection ConstantConditions (bad spigot annotation)
        return plugin.getConfiguration().getString("boolean.true", "true");
    }

    @NotNull
    public String booleanFalse() {
        //noinspection ConstantConditions (bad spigot annotation)
        return plugin.getConfiguration().getString("boolean.false", "false");
    }

    public boolean useAdventureProvidedReplacer() {
        return plugin.getConfiguration().getBoolean("use_adventure_provided_replacer", false);
    }

    public boolean detectMaliciousExpansions() {
        return plugin.getConfiguration().getBoolean("detect_malicious_expansions", true);
    }
}

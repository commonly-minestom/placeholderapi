package me.clip.placeholderapi.minestom.expansion.manager;

import me.clip.placeholderapi.minestom.PlaceholderExpansion;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ManagerTestExpansion extends PlaceholderExpansion {

    @NotNull
    private final String identifier;

    private final boolean persist;

    ManagerTestExpansion(@NotNull final String identifier, final boolean persist) {
        this.identifier = identifier;
        this.persist = persist;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return identifier;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "papi";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Nullable
    @Override
    public String onRequest(@Nullable final Player player, @NotNull final String params) {
        return null;
    }

    @Override
    public boolean persist() {
        return persist;
    }
}

package me.clip.placeholderapi.minestom;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PlayerNameTestExpansion extends PlaceholderExpansion {

    @NotNull
    @Override
    public String getIdentifier() {
        return "nametest";
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
        if (params.equals("name") && player != null)
            return player.getUsername();

        return null;
    }
}

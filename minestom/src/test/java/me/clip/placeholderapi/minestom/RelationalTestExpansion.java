package me.clip.placeholderapi.minestom;

import me.clip.placeholderapi.minestom.expansion.Relational;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class RelationalTestExpansion extends PlaceholderExpansion implements Relational {

    @NotNull
    @Override
    public String getIdentifier() {
        return "reltest";
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

    @Nullable
    @Override
    public String onPlaceholderRequest(@Nullable final Player one, @Nullable final Player two,
                                        @NotNull final String params) {
        if (params.equals("status"))
            return "friends";

        return null;
    }
}

package me.clip.placeholderapi.minestom;

import java.util.concurrent.atomic.AtomicBoolean;

import me.clip.placeholderapi.minestom.expansion.Cleanable;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class CleanableTestExpansion extends PlaceholderExpansion implements Cleanable {

    @NotNull
    final AtomicBoolean cleaned = new AtomicBoolean(false);

    @NotNull
    @Override
    public String getIdentifier() {
        return "cleantest";
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
    public void cleanup(@NotNull final Player player) {
        cleaned.set(true);
    }
}

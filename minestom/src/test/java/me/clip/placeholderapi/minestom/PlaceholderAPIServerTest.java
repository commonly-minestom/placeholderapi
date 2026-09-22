package me.clip.placeholderapi.minestom;

import java.nio.file.Files;
import java.nio.file.Path;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnvTest
class PlaceholderAPIServerTest {

    @Test
    void bootsServerAndParsesWithRealPlayer(final Env env, @TempDir final Path dataFolder) {
        final PlaceholderAPIPlugin plugin = new PlaceholderAPIPlugin(dataFolder);
        plugin.enable();

        try {
            assertTrue(Files.exists(dataFolder.resolve("config.yml")));
            assertEquals("MM/dd/yy HH:mm:ss",
                    plugin.getConfiguration().getString("date_format", null));
            assertNotNull(MinecraftServer.getCommandManager().getCommand("papi"));

            assertTrue(new PlayerNameTestExpansion().register());

            final CleanableTestExpansion cleanable = new CleanableTestExpansion();
            assertTrue(cleanable.register());

            final Instance instance = env.createFlatInstance();
            final Player player = env.createPlayer(instance, new Pos(0, 42, 0));

            assertEquals("hello " + player.getUsername(),
                    PlaceholderAPI.setPlaceholders(player, "hello %nametest_name%"));
            assertEquals("hello " + player.getUsername(),
                    PlaceholderAPI.setBracketPlaceholders(player, "hello {nametest_name}"));

            player.remove();
            env.tickWhile(() -> !player.isRemoved(), java.time.Duration.ofSeconds(5));

            assertTrue(cleanable.cleaned.get());

            env.destroyInstance(instance);
        } finally {
            plugin.disable();
        }
    }
}

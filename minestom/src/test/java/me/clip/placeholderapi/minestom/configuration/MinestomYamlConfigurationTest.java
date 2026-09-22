package me.clip.placeholderapi.minestom.configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinestomYamlConfigurationTest {

    @Test
    void readsAndWritesPaths() {
        final MinestomYamlConfiguration config = new MinestomYamlConfiguration();

        config.set("boolean.true", "yes");
        config.set("expansions.test.value", 42);

        assertEquals("yes", config.getString("boolean.true", "no"));
        assertEquals(42, config.getInt("expansions.test.value", 0));
        assertEquals(0, config.getInt("expansions.test.missing", 0));
        assertTrue(config.contains("boolean.true"));
        assertFalse(config.contains("boolean.missing"));
    }

    @Test
    void exposesSections() {
        final MinestomYamlConfiguration config = new MinestomYamlConfiguration(
                Map.of("expansions", Map.of("test", Map.of("key", "value"))));

        assertEquals("value", config.getConfigurationSection("expansions.test")
                .getString("key", null));
        assertNull(config.getConfigurationSection("expansions.missing"));
        assertEquals(List.of("a", "b"), new MinestomYamlConfiguration(Map.of("list", List.of("a", "b")))
                .getStringList("list"));
    }

    @Test
    void roundTripsFiles(@TempDir final Path dir) throws IOException {
        final Path file = dir.resolve("config.yml");

        final MinestomYamlConfiguration saved = new MinestomYamlConfiguration();
        saved.set("date_format", "MM/dd/yy HH:mm:ss");
        saved.set("debug", true);
        saved.save(file);

        final MinestomYamlConfiguration loaded = MinestomYamlConfiguration.load(file);

        assertEquals("MM/dd/yy HH:mm:ss", loaded.getString("date_format", null));
        assertTrue(loaded.getBoolean("debug", false));
    }
}

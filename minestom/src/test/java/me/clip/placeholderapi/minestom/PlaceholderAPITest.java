package me.clip.placeholderapi.minestom;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.minestom.server.entity.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceholderAPITest {

    private final PlaceholderExpansion expansion = new PlaceholderExpansion() {

        @NotNull
        @Override
        public String getIdentifier() {
            return "test";
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
            if (params.equals("name"))
                return "steve";

            return null;
        }
    };

    private final PlaceholderExpansion relational = new RelationalTestExpansion();

    @BeforeEach
    void setup() {
        PlaceholderAPI.init();

        assertTrue(PlaceholderAPI.register(expansion));
        assertTrue(PlaceholderAPI.register(relational));
    }

    @AfterEach
    void tearDown() {
        PlaceholderAPI.shutdown();

        assertFalse(PlaceholderAPI.isInitialized());
    }

    @Test
    void parsesRegisteredPlaceholder() {
        assertEquals("hello steve", PlaceholderAPI.setPlaceholders(null, "hello %test_name%"));
    }

    @Test
    void keepsUnknownPlaceholders() {
        assertEquals("hello %unknown_name%", PlaceholderAPI.setPlaceholders(null, "hello %unknown_name%"));
    }

    @Test
    void keepsNullResults() {
        assertEquals("%test_missing%", PlaceholderAPI.setPlaceholders(null, "%test_missing%"));
    }

    @Test
    void parsesBracketPlaceholders() {
        assertEquals("hello steve", PlaceholderAPI.setBracketPlaceholders(null, "hello {test_name}"));
    }

    @Test
    void parsesLists() {
        assertEquals(List.of("hello steve", "bye steve"),
                PlaceholderAPI.setPlaceholders(null, List.of("hello %test_name%", "bye %test_name%")));
    }

    @Test
    void parsesRelationalPlaceholders() {
        assertEquals("friends",
                PlaceholderAPI.setRelationalPlaceholders(null, null, "%rel_reltest_status%"));
    }

    @Test
    void tracksRegistration() {
        assertTrue(PlaceholderAPI.isRegistered("test"));
        assertTrue(PlaceholderAPI.getRegisteredIdentifiers().contains("test"));
        assertTrue(PlaceholderAPI.unregister("test"));
        assertFalse(PlaceholderAPI.isRegistered("test"));
        assertTrue(PlaceholderAPI.register(expansion));
    }
}

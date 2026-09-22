package me.clip.placeholderapi.minestom.expansion.manager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import me.clip.placeholderapi.minestom.PlaceholderAPI;
import me.clip.placeholderapi.minestom.PlaceholderAPIPlugin;
import me.clip.placeholderapi.minestom.events.ExpansionRegisterEvent;
import me.clip.placeholderapi.minestom.events.ExpansionUnregisterEvent;
import me.clip.placeholderapi.minestom.events.ExpansionsLoadedEvent;
import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalExpansionManagerTest {

    @TempDir
    static Path dataFolder;

    static PlaceholderAPIPlugin plugin;

    final List<Object> events = new ArrayList<>();

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @BeforeEach
    void setup() {
        plugin = new PlaceholderAPIPlugin(dataFolder);
        plugin.enable();

        MinecraftServer.getGlobalEventHandler().addListener(ExpansionRegisterEvent.class, events::add);
        MinecraftServer.getGlobalEventHandler().addListener(ExpansionUnregisterEvent.class, events::add);
        MinecraftServer.getGlobalEventHandler().addListener(ExpansionsLoadedEvent.class, events::add);
    }

    @AfterEach
    void tearDown() {
        plugin.disable();
        events.clear();
    }

    @Test
    void registersAndUnregisters() {
        assertTrue(plugin.getLocalExpansionManager()
                .register(new ManagerTestExpansion("managed", false)));
        assertTrue(PlaceholderAPI.isRegistered("managed"));

        assertTrue(plugin.getLocalExpansionManager().unregister("managed"));
        assertFalse(PlaceholderAPI.isRegistered("managed"));

        assertEquals(2, events.size());
        assertTrue(events.get(0) instanceof ExpansionRegisterEvent);
        assertTrue(events.get(1) instanceof ExpansionUnregisterEvent);
    }

    @Test
    void rejectsDuplicates() {
        assertTrue(plugin.getLocalExpansionManager()
                .register(new ManagerTestExpansion("dupe", false)));
        assertFalse(plugin.getLocalExpansionManager()
                .register(new ManagerTestExpansion("DUPE", false)));

        plugin.getLocalExpansionManager().unregister("dupe");
    }

    @Test
    void killKeepsPersistingExpansions() {
        plugin.getLocalExpansionManager().register(new ManagerTestExpansion("temp", false));
        plugin.getLocalExpansionManager().register(new ManagerTestExpansion("stays", true));

        plugin.getLocalExpansionManager().kill();

        assertFalse(PlaceholderAPI.isRegistered("temp"));
        assertTrue(PlaceholderAPI.isRegistered("stays"));

        plugin.getLocalExpansionManager().unregister("stays");
    }
}

# PlaceholderAPI for Minestom

Library module. There is no extension system in Minestom core, so the host
server wires the plugin class directly.

## Host setup

```java
PlaceholderAPIPlugin papi = new PlaceholderAPIPlugin(Path.of("extensions/placeholderapi"));

MinecraftServer.init();
papi.enable();
// ... start server
papi.disable();
```

`enable()` creates the data folder, copies the default `config.yml`, registers
the `/papi` command and loads expansions from the `expansions/` folder.
`disable()` unloads everything.

Drop expansion jars implementing
`me.clip.placeholderapi.minestom.PlaceholderExpansion` into `expansions/`.

## Writing an expansion

```java
public final class MyExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "myexpansion";
    }

    @Override
    public @NotNull String getAuthor() {
        return "me";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(@Nullable Player player, @NotNull String params) {
        if (params.equals("name"))
            return player == null ? null : player.getUsername();

        return null;
    }
}
```

Register from code with `new MyExpansion().register()`, parse with
`PlaceholderAPI.setPlaceholders(player, "%myexpansion_name%")`.
Bracket `{myexpansion_name}` and relational `%rel_<id>_<params>%`
(with the `Relational` interface) work the same way.

## Notes

- The expansion cloud serves Bukkit expansions only and is not supported here.
- `/papi` requires console or permission level 4.
- The published jar shades snakeyaml (relocated), Minestom itself is provided
  by the host.

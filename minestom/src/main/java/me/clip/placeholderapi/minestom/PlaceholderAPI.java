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

package me.clip.placeholderapi.minestom;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.clip.placeholderapi.minestom.expansion.Relational;
import me.clip.placeholderapi.replacer.PlaceholderPatterns;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PlaceholderAPI {

    private static final Map<String, PlaceholderExpansion> expansions = new ConcurrentHashMap<>();

    private static volatile boolean initialized;

    private PlaceholderAPI() {
    }

    public static void init() {
        initialized = true;
    }

    public static void shutdown() {
        expansions.clear();

        initialized = false;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static boolean register(@NotNull final PlaceholderExpansion expansion) {
        if (expansion.getIdentifier().isEmpty())
            return false;

        return expansions.putIfAbsent(normalize(expansion.getIdentifier()), expansion) == null;
    }

    public static boolean unregister(@NotNull final String identifier) {
        return expansions.remove(normalize(identifier)) != null;
    }

    @NotNull
    public static Optional<PlaceholderExpansion> findExpansion(@NotNull final String identifier) {
        return Optional.ofNullable(expansions.get(normalize(identifier)));
    }

    @NotNull
    public static Set<String> getIdentifiers() {
        return Set.copyOf(expansions.keySet());
    }

    @NotNull
    public static String setPlaceholders(@Nullable final Player player, @NotNull final String text) {
        return parse(player, text, '%', '%');
    }

    @NotNull
    public static List<String> setPlaceholders(@Nullable final Player player,
                                               @NotNull final List<String> text) {
        final List<String> result = new ArrayList<>(text.size());

        for (final String line : text)
            result.add(setPlaceholders(player, line));

        return result;
    }

    @NotNull
    public static String setBracketPlaceholders(@Nullable final Player player,
                                                @NotNull final String text) {
        return parse(player, text, '{', '}');
    }

    @NotNull
    public static List<String> setBracketPlaceholders(@Nullable final Player player,
                                                      @NotNull final List<String> text) {
        final List<String> result = new ArrayList<>(text.size());

        for (final String line : text)
            result.add(setBracketPlaceholders(player, line));

        return result;
    }

    @NotNull
    public static String setRelationalPlaceholders(@Nullable final Player one,
                                                   @Nullable final Player two,
                                                   @NotNull String text) {
        final Matcher matcher = PlaceholderPatterns.relationalPlaceholder().matcher(text);

        while (matcher.find()) {
            final String format = matcher.group(2);
            final int index = format.indexOf('_');

            if (index <= 0)
                continue;

            final String identifier = format.substring(0, index).toLowerCase(Locale.ROOT);
            final String params = format.substring(index + 1);

            final PlaceholderExpansion expansion = expansions.get(identifier);

            if (!(expansion instanceof Relational))
                continue;

            final String value = ((Relational) expansion).onPlaceholderRequest(one, two, params);

            if (value != null)
                text = text.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));
        }

        return text;
    }

    @NotNull
    public static List<String> setRelationalPlaceholders(@Nullable final Player one,
                                                         @Nullable final Player two,
                                                         @NotNull final List<String> text) {
        final List<String> result = new ArrayList<>(text.size());

        for (final String line : text)
            result.add(setRelationalPlaceholders(one, two, line));

        return result;
    }

    public static boolean isRegistered(@NotNull final String identifier) {
        return findExpansion(identifier).isPresent();
    }

    @NotNull
    public static Set<String> getRegisteredIdentifiers() {
        return getIdentifiers();
    }

    @NotNull
    public static Pattern getPlaceholderPattern() {
        return PlaceholderPatterns.placeholder();
    }

    @NotNull
    public static Pattern getBracketPlaceholderPattern() {
        return PlaceholderPatterns.bracketPlaceholder();
    }

    @NotNull
    public static Pattern getRelationalPlaceholderPattern() {
        return PlaceholderPatterns.relationalPlaceholder();
    }

    public static boolean containsPlaceholders(@Nullable final String text) {
        return PlaceholderPatterns.containsPlaceholders(text);
    }

    public static boolean containsBracketPlaceholders(@Nullable final String text) {
        return PlaceholderPatterns.containsBracketPlaceholders(text);
    }

    @NotNull
    private static String parse(@Nullable final Player player, @NotNull final String text,
                                final char head, final char tail) {
        if (!initialized || text.indexOf(head) == -1)
            return text;

        final StringBuilder result = new StringBuilder(text.length());

        int index = 0;

        while (index < text.length()) {
            final int start = text.indexOf(head, index);

            if (start == -1) {
                result.append(text, index, text.length());
                break;
            }

            final int end = text.indexOf(tail, start + 1);

            if (end == -1) {
                result.append(text, index, text.length());
                break;
            }

            result.append(text, index, start);
            result.append(resolve(player, text.substring(start + 1, end)));

            index = end + 1;
        }

        return result.toString();
    }

    @NotNull
    private static String resolve(@Nullable final Player player, @NotNull final String content) {
        final int separator = content.indexOf('_');

        if (separator == -1)
            return wrap(content);

        final PlaceholderExpansion expansion = expansions.get(normalize(content.substring(0, separator)));

        if (expansion == null)
            return wrap(content);

        final String value = expansion.onRequest(player, content.substring(separator + 1));

        if (value == null)
            return wrap(content);

        return value;
    }

    @NotNull
    private static String wrap(@NotNull final String content) {
        return '%' + content + '%';
    }

    @NotNull
    private static String normalize(@NotNull final String identifier) {
        return identifier.toLowerCase(Locale.ROOT);
    }
}

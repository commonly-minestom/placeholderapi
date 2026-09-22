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

package me.clip.placeholderapi.replacer;

import java.util.regex.Pattern;

public final class PlaceholderPatterns {

    private static final Pattern PLACEHOLDER = Pattern.compile("[%]([^%]+)[%]");
    private static final Pattern BRACKET_PLACEHOLDER = Pattern.compile("[{]([^{}]+)[}]");
    private static final Pattern RELATIONAL_PLACEHOLDER = Pattern.compile("[%](rel_)([^%]+)[%]");

    private PlaceholderPatterns() {
    }

    public static Pattern placeholder() {
        return PLACEHOLDER;
    }

    public static Pattern bracketPlaceholder() {
        return BRACKET_PLACEHOLDER;
    }

    public static Pattern relationalPlaceholder() {
        return RELATIONAL_PLACEHOLDER;
    }

    public static boolean containsPlaceholders(final String text) {
        if (text == null)
            return false;

        final int firstPercent = text.indexOf('%');

        if (firstPercent == -1)
            return false;

        return text.indexOf('%', firstPercent + 1) != -1;
    }

    public static boolean containsBracketPlaceholders(final String text) {
        if (text == null)
            return false;

        final int openBracket = text.indexOf('{');

        if (openBracket == -1)
            return false;

        return text.indexOf('}', openBracket + 1) != -1;
    }
}

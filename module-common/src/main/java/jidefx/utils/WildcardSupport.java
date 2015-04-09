/*
 * Copyright (c) 2002-2015, JIDE Software Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package jidefx.utils;

import java.io.Serializable;

/**
 * An interface to support the wildcard. All methods in this interface have the default implementations. It uses the
 * following three chars as the wildcards. <ul> <li> '?' The question mark indicates there is exact one of missing
 * element. For example, colo?r matches "colour" but not "color" or "colouur". <li>'*' The asterisk indicates there are
 * zero or more of the missing elements. For example, ab*c matches "abc", "abbc", "abdbc", and so on. <li>'+' The plus
 * sign indicates there are at least one of the missing elements. For example, ab+c matches "abbc", "abdbc", but not
 * "abc". </ul>
 */
public interface WildcardSupport extends Serializable {
    /**
     * Gets the quantifier that indicates there is zero or one of the preceding element. Usually '?', the question mark
     * is used for this quantifier. For example, colou?r matches both "color" and "colour".
     *
     * @return the quantifier that indicates there is zero or one of the preceding element.
     */
    default char getZeroOrOneQuantifier() {
        return '?';
    }

    /**
     * Gets the quantifier that indicates there is zero or more of the preceding element. Usually '*', the asterisk is
     * used for this quantifier. For example, ab*c matches "ac", "abc", "abbc", "abbbc", and so on.
     *
     * @return the quantifier that indicates there is zero or more of the preceding element.
     */
    default char getZeroOrMoreQuantifier() {
        return '*';
    }

    /**
     * Gets the quantifier that indicates there is one or more of the preceding element. Usually '+', the plus sign is
     * used for this quantifier. For example, ab+c matches "abc", "abbc", "abbbc", and so on, but not "ac".
     *
     * @return the quantifier that indicates there is one or more of the preceding element.
     */
    default char getOneOrMoreQuantifier() {
        return '+';
    }

    /**
     * Converts a string with wildcards to a regular express that is compatible with {@link java.util.regex.Pattern}. If
     * the string has no wildcard, the same string will be returned.
     *
     * @param s a string with wildcards.
     * @return a regular express that is compatible with {@link java.util.regex.Pattern}.
     */
    default String convert(String s) {
        // if it doesn't have the two special characters we support, we don't need to use regular expression.
        char zeroOrMoreQuantifier = getZeroOrMoreQuantifier();
        int posAny = zeroOrMoreQuantifier == 0 ? -1 : s.indexOf(zeroOrMoreQuantifier);
        char zeroOrOneQuantifier = getZeroOrOneQuantifier();
        int posOne = zeroOrOneQuantifier == 0 ? -1 : s.indexOf(zeroOrOneQuantifier);
        char oneOrMoreQuantifier = getOneOrMoreQuantifier();
        int posOneOrMore = oneOrMoreQuantifier == 0 ? -1 : s.indexOf(oneOrMoreQuantifier);
        //
        if (posAny == -1 && posOne == -1 && posOneOrMore == -1) {
            return s;
        }

        StringBuilder buffer = new StringBuilder();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if (zeroOrOneQuantifier != 0 && c == zeroOrOneQuantifier) {
                buffer.append(".");
            }
            else if (zeroOrMoreQuantifier != 0 && c == zeroOrMoreQuantifier) {
                buffer.append(".*");
            }
            else if (oneOrMoreQuantifier != 0 && c == oneOrMoreQuantifier) {
                buffer.append("..*");
            }
            else if ("(){}[].^$\\".indexOf(c) != -1) { // escape all other special characters
                buffer.append('\\');
                buffer.append(c);
            }
            else {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }
}

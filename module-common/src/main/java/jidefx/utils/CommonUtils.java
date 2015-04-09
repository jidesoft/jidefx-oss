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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Utils that contains methods that are not depending on JavaFX classes.
 */
public class CommonUtils {
    /**
     * Checks if the two objects equal. If both are null, they are equal. If o1 and o2 both are Comparable, we will use
     * compareTo method to see if it equals 0. At last, we will use {@code o1.equals(o2)} to compare. If none of
     * the above conditions match, we return false.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @return true if the two objects are equal. Otherwise false.
     */
    public static boolean equals(Object o1, Object o2) {
        return equals(o1, o2, false);
    }

    /**
     * Checks if the two objects equal. If both are the same instance, they are equal. If both are null, they are equal.
     * If o1 and o2 both are Comparable, we will use compareTo method to see if it equals 0. If considerArrayOrList is
     * true and o1 and o2 are both array, we will compare each element in the array. At last, we will use
     * {@code o1.equals(o2)} to compare. If none of the above conditions match, we return false.
     *
     * @param o1                  the first object to compare
     * @param o2                  the second object to compare
     * @param considerArrayOrList If true, and if o1 and o2 are both array, we will compare each element in the array
     *                            instead of just compare the two array objects.
     * @return true if the two objects are equal. Otherwise false.
     */
    public static boolean equals(Object o1, Object o2, boolean considerArrayOrList) {
        if (o1 == o2) {
            return true;
        }
        else if (o1 != null && o2 == null) {
            return false;
        }
        else if (o1 == null) {
            return false;
        }
        else if (o1 instanceof CharSequence && o2 instanceof CharSequence) {
            return equals((CharSequence) o1, (CharSequence) o2, true);
        }
        else if (o1 instanceof Comparable && o2 instanceof Comparable && o1.getClass().isAssignableFrom(o2.getClass())) {
            return ((Comparable) o1).compareTo(o2) == 0;
        }
        else if (o1 instanceof Comparable && o2 instanceof Comparable && o2.getClass().isAssignableFrom(o1.getClass())) {
            return ((Comparable) o2).compareTo(o1) == 0;
        }
        else if (considerArrayOrList && o1 instanceof List && o2 instanceof List) {
            int length1 = ((List) o1).size();
            int length2 = ((List) o2).size();
            if (length1 != length2) {
                return false;
            }
            for (int i = 0; i < length1; i++) {
                if (!equals(((List) o1).get(i), ((List) o2).get(i), true)) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (considerArrayOrList && o1.getClass().isArray() && o2.getClass().isArray()) {
                int length1 = Array.getLength(o1);
                int length2 = Array.getLength(o2);
                if (length1 != length2) {
                    return false;
                }
                for (int i = 0; i < length1; i++) {
                    if (!equals(Array.get(o1, i), Array.get(o2, i), true)) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return o1.equals(o2);
            }
        }
    }

    public static boolean equals(CharSequence s1, CharSequence s2, boolean caseSensitive) {
        if (s1 == s2) return true;
        if (s1 == null || s2 == null) return false;

        // Algorithm from String.regionMatches()

        if (s1.length() != s2.length()) return false;
        int to = 0;
        int po = 0;
        int len = s1.length();

        while (len-- > 0) {
            char c1 = s1.charAt(to++);
            char c2 = s2.charAt(po++);
            if (c1 == c2) {
                continue;
            }
            if (!caseSensitive && charsEqualIgnoreCase(c1, c2)) continue;
            return false;
        }

        return true;
    }

    public static boolean charsEqualIgnoreCase(char a, char b) {
        return a == b || toUpperCase(a) == toUpperCase(b) || toLowerCase(a) == toLowerCase(b);
    }

    /**
     * A toUpperCase routine which is faster to process the ASCII lowercase letters than Character.toUpperCase.
     *
     * @param a the character to be converted.
     * @return the uppercase equivalent of the character, if any; otherwise, the character itself.
     */
    public static char toUpperCase(char a) {
        if (a < 'a') {
            return a;
        }
        if (a >= 'a' && a <= 'z') {
            return (char) (a + ('A' - 'a'));
        }
        return Character.toUpperCase(a);
    }

    /**
     * A toLowerCase routine which is faster to process the ASCII lowercase letters then Character.toLowerCase.
     *
     * @param a the character to be converted.
     * @return the lowercase equivalent of the character, if any; otherwise, the character itself.
     */
    public static char toLowerCase(final char a) {
        if (a < 'A' || a >= 'a' && a <= 'z') {
            return a;
        }

        if (a >= 'A' && a <= 'Z') {
            return (char) (a + ('a' - 'A'));
        }

        return Character.toLowerCase(a);
    }

    /**
     * Ignore the exception. This method does nothing. However it's a good practice to use this method so that we can
     * easily find out the place that ignoring exception. In development phase, we can log a message in this method so
     * that we can verify if it makes sense to ignore.
     *
     * @param e the exception
     */
    @SuppressWarnings("UnusedParameters")
    public static void ignoreException(Exception e) {
//        e.printStackTrace();
    }

    /**
     * Prints out the message of the exception.
     *
     * @param e the exception
     */
    public static void printException(Exception e) {
        System.err.println(e.getLocalizedMessage());
    }

    /**
     * Throws the exception. If the exception is RuntimeException, just throw it. Otherwise, wrap it in RuntimeException
     * and throw it.
     *
     * @param e the exception
     */
    public static void throwException(Exception e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        else {
            throw new RuntimeException(e);
        }
    }

    /**
     * Throws the InvocationTargetException. Usually InvocationTargetException has a nested exception as target
     * exception. If the target exception is a RuntimeException or Error, we will throw it. Otherwise, we will wrap it
     * inside RuntimeException and throw it.
     *
     * @param e the exception
     */
    public static void throwInvocationTargetException(InvocationTargetException e) {
        // in most cases, target exception will be RuntimeException
        // but to be on safer side (it may be Error) we explicitly check it
        if (e.getTargetException() instanceof RuntimeException) {
            throw (RuntimeException) e.getTargetException();
        }
        else if (e.getTargetException() instanceof Error) {
            throw (Error) e.getTargetException();
        }
        else {
            throw new RuntimeException(e.getTargetException());
        }
    }

    /**
     * Perform a binary search over a sorted list for the given key.
     *
     * @param a   the array to search
     * @param key the key to search for
     * @return the index of the given key if it exists in the list, otherwise -1 times the index value at the insertion
     *         point that would be used if the key were added to the list.
     */
    @SuppressWarnings("unchecked")
    public static int binarySearch(List<Object> a, Object key) {
        int x1 = 0;
        int x2 = a.size();
        int i = x2 / 2, c;
        while (x1 < x2) {
            if (!(a.get(i) instanceof Comparable)) {
                return i;
            }
            c = ((Comparable) a.get(i)).compareTo(key);
            if (c == 0) {
                return i;
            }
            else if (c < 0) {
                x1 = i + 1;
            }
            else {
                x2 = i;
            }
            i = x1 + (x2 - x1) / 2;
        }
        return -1 * i;
    }

    /**
     * Perform a binary search over a sorted array for the given key.
     *
     * @param a   the array to search
     * @param key the key to search for
     * @return the index of the given key if it exists in the array, otherwise -1 times the index value at the insertion
     *         point that would be used if the key were added to the array.
     */
    @SuppressWarnings("unchecked")
    public static int binarySearch(Object[] a, Object key) {
        int x1 = 0;
        int x2 = a.length;
        int i = x2 / 2, c;
        while (x1 < x2) {
            if (!(a[i] instanceof Comparable)) {
                return i;
            }
            c = ((Comparable) a[i]).compareTo(key);
            if (c == 0) {
                return i;
            }
            else if (c < 0) {
                x1 = i + 1;
            }
            else {
                x2 = i;
            }
            i = x1 + (x2 - x1) / 2;
        }
        return -1 * i;
    }

    /**
     * Perform a binary search over a sorted array for the given key.
     *
     * @param a   the array to search
     * @param key the key to search for
     * @return the index of the given key if it exists in the array, otherwise -1 times the index value at the insertion
     *         point that would be used if the key were added to the array.
     */
    public static int binarySearch(int[] a, int key) {
        return binarySearch(a, key, 0, a.length);
    }

    /**
     * Perform a binary search over a sorted array for the given key.
     *
     * @param a     the array to search
     * @param key   the key to search for
     * @param start the start index to search inclusive
     * @param end   the end index to search exclusive
     * @return the index of the given key if it exists in the array, otherwise -1 times the index value at the insertion
     *         point that would be used if the key were added to the array.
     */
    public static int binarySearch(int[] a, int key, int start, int end) {
        int x1 = start;
        int x2 = end;
        int i = x2 / 2;
        while (x1 < x2) {
            if (a[i] == key) {
                return i;
            }
            else if (a[i] < key) {
                x1 = i + 1;
            }
            else {
                x2 = i;
            }
            i = x1 + (x2 - x1) / 2;
        }
        return -1 * i;
    }

    /**
     * Accepts a function that extracts an object of a type {@code U} as sort key from a type {@code T}, and returns the
     * object that compares by the specified comparator.  For example, if a class {@code Element} has a {@code value}
     * which has a getter, but we only have a Comparator for the value, not for the {@code Element}. We can use this
     * method to call {@code Person} objects to get the value, then use the existing Comparator for the value.
     *
     * @param <T>          the original element type
     * @param <U>          the actual type for comparison
     * @param comparator   a comparator that can sort the value.
     * @param keyExtractor the function used to extract the {@code value} sort key for the Comparator
     */
    public static <T, U> Comparator<T> comparing(Comparator<? super U> comparator, Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(keyExtractor);
        return new Comparator<T>() {
            @Override
            public int compare(T c1, T c2) {
                return comparator.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
            }
        };
    }
}

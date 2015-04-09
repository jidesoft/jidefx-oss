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
package jidefx.utils.comparator;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A Comparator that compares Comparable objects. Throws ClassCastExceptions if the objects are not Comparable, or if
 * they are null. Throws ClassCastException if the compareTo of both objects do not provide an inverse result of each
 * other as per the Comparable javadoc. <br>If both objects are null, they will be treated as equal. If one is null and
 * the other is not, the null value will be treated as smaller than non-null value.
 *
 * @author bayard@generationjava.com
 * @author JIDE Software
 */
public class ComparableComparator implements Comparator<Object>, Serializable {
    private static final long serialVersionUID = -291439688585137865L;

    private static final ComparableComparator instance =
            new ComparableComparator();

    /**
     * Return a shared instance of a ComparableComparator.  Developers are encouraged to use the comparator returned
     * from this method instead of constructing a new instance to reduce allocation and GC overhead when multiple
     * comparable comparators may be used in the same VM.
     *
     * @return an instance of ComparableComparator.
     */
    public static ComparableComparator getInstance() {
        return instance;
    }

    /**
     * Constructs a ComparableComparator.
     */
    public ComparableComparator() {
    }

    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        else if (o1 == null) {
            return -1;
        }
        else if (o2 == null) {
            return 1;
        }

        if (o1 instanceof Comparable) {
            if (o2 instanceof Comparable) {
                int result1;
                int result2;
                try {
                    result1 = ((Comparable<Object>) o1).compareTo(o2);
                    result2 = ((Comparable<Object>) o2).compareTo(o1);
                }
                catch (ClassCastException e) {
                    return o1.getClass().getName().compareTo(o2.getClass().getName()); // fall back to compare the string
                }

                // enforce comparable contract
                if (result1 == 0 && result2 == 0) {
                    return 0;
                }
                else if (result1 < 0 && result2 > 0) { // to make sure the two results are consistent
                    return result1;
                }
                else if (result1 > 0 && result2 < 0) { // to make sure the two results are consistent
                    return result1;
                }
                else {
                    // results inconsistent
                    throw new ClassCastException("The two compareTo methods of o1 and o2 returned two inconsistent results. Please make sure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y.");
                }
            }
            else {
                // o2 wasn't comparable
                throw new ClassCastException("The second argument of this method was not a Comparable: " + o2.getClass().getName());
            }
        }
        else if (o2 instanceof Comparable) {
            // o1 wasn't comparable
            throw new ClassCastException("The first argument of this method was not a Comparable: " + o1.getClass().getName());
        }
        else {
            // neither were comparable
            throw new ClassCastException("Both arguments of this method were not Comparables: " + o1.getClass().getName() + " and " + o2.getClass().getName());
        }
    }
}

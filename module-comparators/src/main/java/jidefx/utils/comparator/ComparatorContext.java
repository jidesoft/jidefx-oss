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

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The context object used by {@code ObjectComparatorManager}. For the same type, we may need different way to
 * compare them. This context is used so that user can register different comparators for the same type.
 */
public class ComparatorContext implements Serializable {
    private static final long serialVersionUID = -4846614433415551998L;

    private String _name;

    /**
     * Default ComparatorContext with empty name and no user object.
     */
    public static final ComparatorContext DEFAULT_CONTEXT = new ComparatorContext("");

    /**
     * Creates a ComparatorContext with a name.
     *
     * @param name the name of the ComparatorContext.
     */
    public ComparatorContext(String name) {
        _name = name;
    }

    /**
     * Gets the name of the ComparatorContext.
     *
     * @return the name of the ComparatorContext
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the name of the ComparatorContext.
     *
     * @param name the name of the ComparatorContext
     */
    public void setName(String name) {
        _name = name;
    }

    // A map containing a set of properties for this node
    private ObservableMap<Object, Object> properties;

    /**
     * Returns an observable map of properties on this node for use primarily by application developers.
     *
     * @return an observable map of properties on this node for use primarily by application developers
     */
    public final ObservableMap<Object, Object> getProperties() {
        if (properties == null) {
            properties = FXCollections.observableMap(new HashMap<>());
        }
        return properties;
    }

    /**
     * Tests if Node has properties.
     *
     * @return true if node has properties.
     */
    public boolean hasProperties() {
        return properties != null && !properties.isEmpty();
    }

    /**
     * Override equals. Two ComparatorContexts are equal as long as the name is the same.
     *
     * @param o object to compare.
     * @return if two objects equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComparatorContext)) return false;

        final ComparatorContext context = (ComparatorContext) o;

        return !(_name != null ? !_name.equals(context._name) : context._name != null);
    }

    @Override
    public int hashCode() {
        return (_name != null ? _name.hashCode() : 0);
    }

    @Override
    public String toString() {
        return getName();
    }
}

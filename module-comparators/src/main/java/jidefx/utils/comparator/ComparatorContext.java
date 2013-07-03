/*
 * @(#)ComparatorContext.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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

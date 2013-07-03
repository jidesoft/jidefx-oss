/*
 * @(#)ConverterContext.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The {@code ConverterContext} is used by {@code ObjectConverterManager} so that for the same type, different
 * converters can be registered since the {@code ConverterContext} is different.
 */
public class ConverterContext implements Serializable {
    private static final long serialVersionUID = 8015351559541303641L;

    private String _name;

    /**
     * Default ConverterContext with empty name.
     */
    public final static ConverterContext CONTEXT_DEFAULT = new ConverterContext("");

    /**
     * Property for the ObjectConverterManager. If this property is set to an ObjectConverterManager instance, this
     * instance will be used to do any string/object conversion inside the toString or fromString method. Use it along
     * with {@link RequiringConverterManager} interface which can be implemented by the ObjectConverter.
     */
    public final static String PROPERTY_OBJECT_CONVERTER_MANAGER = "ObjectConverterManager"; //NON-NLS

    /**
     * Creates a ConverterContext with a name.
     *
     * @param name the name of the ConverterContext
     */
    public ConverterContext(String name) {
        _name = name;
    }

    /**
     * Checks if the context is for an array. By conversion, we put "[]" at the end of the ConverterContext's name if
     * the context is for an array data type. Please note, this is a conversion only. If developer chooses to not put
     * "[]" at the end for their own customized context, this method will fail.
     *
     * @param context the context.
     * @return true or false.
     */
    public static boolean isArrayConverterContext(ConverterContext context) {
        return context != null && context.getName() != null && context.getName().endsWith("[]");
    }

    /**
     * Gets the ConverterContext which removes the trailing "[]" from the context name.
     *
     * @param context the context for an array type.
     * @return the ConverterContext for the element type of an array.
     */
    public static ConverterContext getElementConverterContext(ConverterContext context) {
        if (isArrayConverterContext(context)) {
            return new ConverterContext(context.getName().substring(0, context.getName().length() - 2));
        }
        else {
            return context;
        }
    }

    /**
     * Gets the ConverterContext which add a trailing "[]" to the context name.
     *
     * @param context the context for the element type of an array.
     * @return the ConverterContext the array of the element type.
     */
    public static ConverterContext getArrayConverterContext(ConverterContext context) {
        if (!isArrayConverterContext(context)) {
            return new ConverterContext(context.getName() + "[]");
        }
        else {
            return context;
        }
    }

    /**
     * Gets the name of the ConverterContext.
     *
     * @return the name of the ConverterContext
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the name of the ConverterContext.
     *
     * @param name the name of the ConverterContext
     */
    public void setName(String name) {
        _name = name;
    }

    // A map containing a set of properties for this node
    private ObservableMap<Object, Object> _properties;

    /**
     * Returns an observable map of properties on this node for use primarily by application developers.
     *
     * @return an observable map of properties on this node for use primarily by application developers
     */
    public final ObservableMap<Object, Object> getProperties() {
        if (_properties == null) {
            _properties = FXCollections.observableMap(new HashMap<>());
        }
        return _properties;
    }

    /**
     * Tests if Node has properties.
     *
     * @return true if node has properties.
     */
    public boolean hasProperties() {
        return _properties != null && !_properties.isEmpty();
    }

    /**
     * Override equals. Two ConverterContexts equals as long as the name is the same.
     *
     * @param o object to compare.
     * @return if two objects equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConverterContext)) return false;

        final ConverterContext context = (ConverterContext) o;

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


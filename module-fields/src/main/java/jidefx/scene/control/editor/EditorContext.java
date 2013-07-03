/*
 * @(#)EditorContext.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.scene.control.editor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The editor context is used by EditorManager and CellEditorManager so that for the same type, different editors can be
 * registered since the EditorContext is different.
 */
public class EditorContext implements Serializable {
    private static final long serialVersionUID = -3390819455707408573L;
    private String _name;

    /**
     * A property for the EditorContext. A {@link jidefx.utils.Customizer} can be set on this property.
     */
    public static final String PROPERTY_EDITOR_NODE_CUSTOMIZER = "EditorNodeCustomizer";
    /**
     * A property for the EditorContext. An ObservableList can be set on this property for editors using ObservableList, such as combobox, choicebox etc.
     */
    public static final String PROPERTY_OBSERVABLE_LIST = "ObservableList";
    /**
     * A property for the EditorContext. A boolean can be set on this property to indicate if the editor is editable or not for a comboBox-based editor.
     */
    public static final String PROPERTY_EDITABLE = "Editable";
    /**
     * A property for the EditorContext. A value of the specified data type can be set on this property to indicate the min value allowed by this editor.
     */
    public static final String PROPERTY_MIN = "Min";
    /**
     * A property for the EditorContext. A value of the specified data type can be set on this property to indicate the max value allowed by this editor.
     */
    public static final String PROPERTY_MAX = "Max";

    /**
     * Default EditorContext with empty name and no user object.
     */
    public static EditorContext CONTEXT_DEFAULT = new EditorContext("");

    /**
     * Creates an EditorContext with a name.
     *
     * @param name the name of the EditorContext
     */
    public EditorContext(String name) {
        _name = name;
    }

    /**
     * Gets the name of the abstract context.
     *
     * @return the name of the abstract context
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the name of the EditorContext.
     *
     * @param name the name of the EditorContext
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
     * Override equals. Two EditorContexts are equal as long as the name is the same.
     *
     * @param o object to compare.
     * @return if two objects equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EditorContext)) return false;

        final EditorContext context = (EditorContext) o;

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

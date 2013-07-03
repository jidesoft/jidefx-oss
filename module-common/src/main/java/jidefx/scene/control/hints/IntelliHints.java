/*
 * @(#)IntelliHints.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.scene.control.hints;

import javafx.scene.Node;

/**
 * {@code IntelliHints} is an interface that defines all necessary methods to implement showing a hint popup depending
 * on a context and allows user to pick from a list of hints. {@link #createHintsNode()} will create a node that
 * contains the hints. It will be shown in a popup window. After the hint popup is created, {@link #updateHints(Object)}
 * will update the content of hints based on the context. Once user picks a hint from the hint popup, {@link
 * #getSelectedHint()} will be called to find the hint that user selected and call {@link #acceptHint(Object)} to accept
 * it.
 *
 * @param <T> the data type of the hints
 */
public interface IntelliHints<T> {
    /**
     * The key of a client property. If a node has IntelliHints registered, you can use this client property to get the
     * IntelliHints instance.
     */
    String PROPERTY_INTELLI_HINTS = "IntelliHints"; //NOI18N NON-NLS

    /**
     * Creates the node which contains hints. At this moment, the content should be empty. Following call {@link
     * #updateHints(Object)} will update the content.
     *
     * @return the node which will be used to display the hints.
     */
    Node createHintsNode();

    /**
     * Update hints depending on the context. This method will be triggered for every key typed event in the text
     * control. Subclass can override it to provide your own list of hints and call {@link
     * AbstractListIntelliHints#setAvailableHints(javafx.collections.ObservableList)} to set it and returns true after
     * that.
     *
     * @param context the current context
     * @return true or false. If it is false, hint popup will not be shown.
     */
    boolean updateHints(Object context);

    /**
     * Gets the selected hint. This hint will be used to complete the text control using {@link #acceptHint(Object)}.
     *
     * @return the selected hint.
     */
    T getSelectedHint();

    /**
     * Accepts the selected hint. Subclass can implements to decide how the new hint be set to the text control.
     *
     * @param hint the hint to be accepted.
     */
    void acceptHint(T hint);
}

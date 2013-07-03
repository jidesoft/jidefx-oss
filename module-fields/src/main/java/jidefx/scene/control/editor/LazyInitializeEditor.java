/*
 * @(#)LazyInitializeEditor.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.editor;

/**
 * A {@code LazyInitializeEditor} is a markup interface to indicate the editor will initialize lazily, either because of
 * the performance consideration, or because the information needed for the initialization of the editor is not
 * available in the construction time (i.e. need the actual class or the editorContext).
 *
 * @param <T> the value type of the editor.
 */
@FunctionalInterface
public interface LazyInitializeEditor<T> {
    void initialize(Class<T> clazz, EditorContext context);
}

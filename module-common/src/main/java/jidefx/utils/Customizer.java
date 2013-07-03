/*
 * @(#)CellEditorCustomizer.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils;

/**
 * An interface that can be used to customize any object.
 */
@FunctionalInterface
public interface Customizer<T> {
    void customize(T t);
}

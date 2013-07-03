/*
 * @(#)ListDataIntelliHints.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.scene.control.hints;

import javafx.collections.FXCollections;
import javafx.scene.control.TextInputControl;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code ListDataIntelliHints} is a concrete implementation of {@link IntelliHints}. It provides hints from a
 * known list of data. It is similar to auto complete text field except the list will be filtered depending on what user
 * types in so far.
 */
public class ListDataIntelliHints<T> extends AbstractListIntelliHints<T> {

    private boolean _caseSensitive = false;
    private List<T> _completionList;

    public ListDataIntelliHints(TextInputControl comp, List<T> completionList) {
        super(comp);
        setCompletionList(completionList);
    }

    public ListDataIntelliHints(TextInputControl comp, T[] completionList) {
        super(comp);
        setCompletionList(completionList);
    }

    /**
     * Gets the list of hints.
     *
     * @return the list of hints.
     */
    public List<T> getCompletionList() {
        return _completionList;
    }

    /**
     * Sets a new list of hints.
     *
     * @param completionList a new list of hints.
     */
    public void setCompletionList(List<T> completionList) {
        _completionList = completionList;
    }

    /**
     * Sets a new list of hints.
     *
     * @param completionList a new array of hints.
     */
    public void setCompletionList(T[] completionList) {
        final T[] list = completionList;
        _completionList = new AbstractList<T>() {
            @Override
            public T get(int index) {
                return list[index];
            }

            @Override
            public int size() {
                return list.length;
            }
        };
    }

    public boolean updateHints(Object context) {
        if (context == null) {
            return false;
        }
        List<T> possibleHints = new ArrayList<>();
        for (T o : getCompletionList()) {
            if (compare(context, o)) possibleHints.add(o);
        }

        setAvailableHints(FXCollections.observableArrayList(possibleHints));
        return possibleHints.size() > 0;
    }

    /**
     * Compares the context with the object in the completion list.
     *
     * @param context the context returned from {@link #getContext()} method.
     * @param o       the object in the completion list.
     * @return true if the context matches with the object. Otherwise false.
     */
    protected boolean compare(Object context, T o) {
        boolean match = false;
        String listEntry = o == null ? "" : o.toString();
        String s = context.toString();
        int substringLen = s.length();
        if (substringLen <= listEntry.length()) {
            if (!isCaseSensitive()) {
                if (s.equalsIgnoreCase(listEntry.substring(0, substringLen)))
                    match = true;
            }
            else if (listEntry.startsWith(s))
                match = true;
        }
        return match;
    }

    /**
     * Checks if it used case sensitive search. By default it's false.
     *
     * @return if it's case sensitive.
     */
    public boolean isCaseSensitive() {
        return _caseSensitive;
    }

    /**
     * Sets the case sensitive flag. By default, it's false meaning it's a case insensitive search.
     *
     * @param caseSensitive true or false.
     */
    public void setCaseSensitive(boolean caseSensitive) {
        _caseSensitive = caseSensitive;
    }

}
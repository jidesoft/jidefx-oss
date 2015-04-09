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
package jidefx.scene.control.searchable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * {@code TextComponentSearchable} is an concrete implementation of {@link Searchable} that enables the search
 * function in TextInputControl. <p>It's very simple to use it. Assuming you have a TextArea, all you need to do is to
 * call
 * <pre>{@code
 * TextArea textArea = ....;
 * TextInputControlSearchable searchable = new TextInputControlSearchable(textComponent);
 * }</pre>
 * Now the TextInputControl will have the search function.
 * <p>
 * There is very little customization you need to do to ListSearchable. The only thing you might need is when the
 * element in the TextInputControl needs a special conversion to convert to string. If so, you can override
 * convertElementToString() to provide you own algorithm to do the conversion.
 * <pre>{@code
 * TextArea textArea = ....;
 * TextInputControlSearchable searchable = new TextInputControlSearchable(textArea) {
 *      protected String convertElementToString(Object object) {
 *          ...
 *      }
 *
 *      protected boolean isActivateKey(KeyEvent e) { // change to a different activation key
 *          return ...;
 *      }
 * };
 * }</pre>
 * <p>
 * Additional customization can be done on the base Searchable class such as background and foreground color,
 * keystrokes, case sensitivity.
 * <p>
 * Due to the special case of TextInputControl, the searching doesn't support wild card '*' or '?' as in other
 * Searchables.
 */
@SuppressWarnings({"Convert2Lambda", "unchecked"})
public class TextInputControlSearchable extends Searchable<String> {
    private int _selectedIndex = -1;
    private ChangeListener<String> _textChangeListener;

    public TextInputControlSearchable(TextInputControl textInputControl) {
        super(textInputControl);
    }

    @Override
    public void installListeners() {
        super.installListeners();
        if (_node instanceof TextInputControl) {
            _textChangeListener = new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    hidePopup();
                }
            };
            ((TextInputControl) _node).textProperty().addListener(_textChangeListener);
        }
    }

    @Override
    public void uninstallListeners() {
        super.uninstallListeners();
        if (_node instanceof TextInputControl) {
            if (_textChangeListener != null) {
                ((TextInputControl) _node).textProperty().removeListener(_textChangeListener);
            }
        }
    }

    @Override
    protected void keyTypedOrPressed(KeyEvent e) {
        if (isActivateKey(e)) {
            switchToNonEditable();
        }
        super.keyTypedOrPressed(e);
    }

    @Override
    protected void setSelectedIndex(int index, boolean incremental) {
        if (_node instanceof TextInputControl) {
            if (index == -1) {
                removeAllHighlights();
                _selectedIndex = -1;
                return;
            }

            if (!incremental) {
                removeAllHighlights();
            }

            String text = getSearchingText();
            addHighlight(index, text, incremental);
        }
    }

    /**
     * Adds highlight to text component at specified index and text.
     *
     * @param index       the index of the text to be highlighted
     * @param text        the text to be highlighted
     * @param incremental if this is an incremental adding highlight
     */
    @SuppressWarnings("UnusedParameters")
    protected void addHighlight(final int index, final String text, boolean incremental) {
        if (_node instanceof TextInputControl) {
            final TextInputControl textComponent = ((TextInputControl) _node);
            textComponent.selectRange(index, index + text.length());
            _selectedIndex = index;
        }
    }

    /**
     * Removes all highlights from the text component.
     */
    protected void removeAllHighlights() {
        if (_node instanceof TextInputControl) {
            ((TextInputControl) _node).deselect();
        }
    }

    @Override
    protected int getSelectedIndex() {
        if (_node instanceof TextInputControl) {
            return _selectedIndex;
        }
        return 0;
    }

    @Override
    protected String getElementAt(int index) {
        String text = getSearchingText();
        if (text != null) {
            if (_node instanceof TextInputControl) {
                int endIndex = index + text.length();
                int elementCount = getElementCount();
                if (endIndex > elementCount) {
                    endIndex = getElementCount();
                }

                if (endIndex < getElementCount() && index < getElementCount()) {
                    return ((TextInputControl) _node).getText().substring(index, endIndex);
                }
            }
        }
        return "";
    }

    @Override
    protected int getElementCount() {
        if (_node instanceof TextInputControl) {
            return ((TextInputControl) _node).getLength();
        }
        return 0;
    }

    /**
     * Converts the element in TextInputControl to string. The returned value will be the {@code toString()} of
     * whatever element that returned from {@code list.getModel().getElementAt(i)}.
     *
     * @param element the element to be converted
     * @return the string representing the element in the TextInputControl.
     */
    @Override
    protected String convertElementToString(String element) {
        if (element != null) {
            return element;
        }
        else {
            return "";
        }
    }

    @Override
    protected boolean isActivateKey(KeyEvent e) {
        if (_node instanceof TextInputControl && ((TextInputControl) _node).isEditable()) {
            return KeyCombination.keyCombination("Ctrl+F").match(e); //NON-NLS
        }
        else {
            return super.isActivateKey(e);
        }
    }

    @Override
    public int findLast(String s) {
        if (_node instanceof TextInputControl) {
            String text = getDocumentText();
            if (isCaseSensitive()) {
                return text.lastIndexOf(s);
            }
            else {
                return lastIndexOf(text, s, text.length());
            }
        }
        else {
            return super.findLast(s);
        }
    }

    private transient String _text = null;

    /**
     * Gets the text from Document.
     *
     * @return the text of this TextInputControl. It used Document to get the text.
     */
    private String getDocumentText() {
        if (_text == null && _node instanceof TextInputControl) {
            _text = ((TextInputControl) _node).getText();
        }
        return _text;
    }

    @Override
    public int findFirst(String s) {
        if (_node instanceof TextInputControl) {
            String text = getDocumentText();
            if (isCaseSensitive()) {
                return text.indexOf(s);
            }
            else {
                return indexOf(text, s, 0);
            }
        }
        else {
            return super.findFirst(s);
        }
    }

    static int lastIndexOf(String source, String target, int fromIndex) {
        int sourceCount = source.length();
        int targetCount = target.length();
        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0) {
            return -1;
        }
        if (fromIndex > rightIndex) {
            fromIndex = rightIndex;
        }
        /* Empty string always matches. */
        if (targetCount == 0) {
            return fromIndex;
        }
        char[] lowerTarget = target.toLowerCase().toCharArray();
        char[] upperTarget = target.toUpperCase().toCharArray();

        int strLastIndex = targetCount - 1;
        int min = targetCount - 1;
        int i = min + fromIndex;

        while (i >= min) {
            while (i >= min && source.charAt(i) != lowerTarget[strLastIndex] && source.charAt(i) != upperTarget[strLastIndex]) {
                i--;
            }
            if (i < min) {
                break;
            }
            int j = i - 1;
            int start = j - (targetCount - 1);
            int k = strLastIndex - 1;

            while (j > start) {
                char ch = source.charAt(j);
                if (ch != lowerTarget[k] && ch != upperTarget[k]) {
                    i--;
                    break;
                }
                j--;
                k--;
            }
            if (j <= start) {
                return start + 1;
            }
        }
        return -1;
    }

    private static int indexOf(String source, String target, int fromIndex) {
        int sourceCount = source.length();
        int targetCount = target.length();
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }
        char[] lowerTarget = target.toLowerCase().toCharArray();
        char[] upperTarget = target.toUpperCase().toCharArray();

        int max = sourceCount - targetCount;

        for (int i = fromIndex; i <= max; i++) {
            /* Look for first character. */
            char c = source.charAt(i);
            if (c != lowerTarget[0] && c != upperTarget[0]) {
                i++;
                while (i <= max && source.charAt(i) != lowerTarget[0] && source.charAt(i) != upperTarget[0]) {
                    i++;
                }
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = 1; j < end; j++) {
                    char ch = source.charAt(j);
                    if (ch != lowerTarget[k] && ch != upperTarget[k]) {
                        break;
                    }
                    k++;
                }

                if (j == end) {
                    /* Found whole string. */
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int findFromCursor(String s) {
        if (isReverseOrder()) {
            return reverseFindFromCursor(s);
        }

        if (_node instanceof TextInputControl) {
            String text = getDocumentText();
            int selectedIndex = (getCursor() != -1 ? getCursor() : getSelectedIndex());
            if (selectedIndex < 0)
                selectedIndex = 0;
            int count = getElementCount();
            if (count == 0)
                return s.length() > 0 ? -1 : 0;

            // find from cursor
            int found = isCaseSensitive() ? text.indexOf(s, selectedIndex) : indexOf(text, s, selectedIndex);

            // if not found, start over from the beginning
            if (found == -1) {
                found = isCaseSensitive() ? text.indexOf(s, 0) : indexOf(text, s, 0);
                if (found >= selectedIndex) {
                    found = -1;
                }
            }

            return found;
        }
        else {
            return super.findFromCursor(s);
        }
    }

    @Override
    public int reverseFindFromCursor(String s) {
        if (!isReverseOrder()) {
            return findFromCursor(s);
        }

        if (_node instanceof TextInputControl) {
            String text = getDocumentText();
            int selectedIndex = (getCursor() != -1 ? getCursor() : getSelectedIndex());
            if (selectedIndex < 0)
                selectedIndex = 0;
            int count = getElementCount();
            if (count == 0)
                return s.length() > 0 ? -1 : 0;

            // find from cursor
            int found = isCaseSensitive() ? text.lastIndexOf(s, selectedIndex) : lastIndexOf(text, s, selectedIndex);

            // if not found, start over from the end
            if (found == -1) {
                found = isCaseSensitive() ? text.lastIndexOf(s, text.length() - 1) : lastIndexOf(text, s, text.length() - 1);
                if (found <= selectedIndex) {
                    found = -1;
                }
            }

            return found;
        }
        else {
            return super.findFromCursor(s);
        }
    }

    @Override
    public int findNext(String s) {
        if (_node instanceof TextInputControl) {
            String text = getDocumentText();
            int selectedIndex = (getCursor() != -1 ? getCursor() : getSelectedIndex());
            if (selectedIndex < 0)
                selectedIndex = 0;
            int count = getElementCount();
            if (count == 0)
                return s.length() > 0 ? -1 : 0;

            // find from cursor
            int found = isCaseSensitive() ? text.indexOf(s, selectedIndex + 1) : indexOf(text, s, selectedIndex + 1);

            // if not found, start over from the beginning
            if (found == -1 && isRepeats()) {
                found = isCaseSensitive() ? text.indexOf(s, 0) : indexOf(text, s, 0);
                if (found > selectedIndex) {
                    found = -1;
                }
            }

            return found;
        }
        else {
            return super.findNext(s);
        }
    }

    @Override
    public int findPrevious(String s) {
        if (_node instanceof TextInputControl) {
            String text = getDocumentText();
            int selectedIndex = (getCursor() != -1 ? getCursor() : getSelectedIndex());
            if (selectedIndex < 0)
                selectedIndex = 0;
            int count = getElementCount();
            if (count == 0)
                return s.length() > 0 ? -1 : 0;

            // find from cursor
            int found = isCaseSensitive() ? text.lastIndexOf(s, selectedIndex - 1) : lastIndexOf(text, s, selectedIndex - 1);

            // if not found, start over from the beginning
            if (found == -1 && isRepeats()) {
                found = isCaseSensitive() ? text.lastIndexOf(s, count - 1) : lastIndexOf(text, s, count - 1);
                if (found < selectedIndex) {
                    found = -1;
                }
            }

            return found;
        }
        else {
            return super.findPrevious(s);
        }
    }

    private transient boolean _oldEditable = false;

    private void switchToNonEditable() {
        if (_node instanceof TextInputControl && !_oldEditable) {
            _oldEditable = ((TextInputControl) _node).isEditable();
            if (_oldEditable) {
                ((TextInputControl) _node).setEditable(false);
            }

        }
    }

    private void switchToEditable() {
        if (_node instanceof TextInputControl && _oldEditable) {
            ((TextInputControl) _node).setEditable(_oldEditable);
            _oldEditable = false;
        }
    }

    @Override
    public void showPopup(String searchingText) {
        switchToNonEditable();
        super.showPopup(searchingText);
    }

    @Override
    public void hidePopup() {
        super.hidePopup();
        _selectedIndex = -1;
        _text = null;
        switchToEditable();
    }
}

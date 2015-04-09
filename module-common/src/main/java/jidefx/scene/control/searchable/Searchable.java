/*
 * @(#)Searchable.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.searchable;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;
import javafx.util.Duration;
import jidefx.utils.WildcardSupport;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * In JavaFX, ListView, TableView, TreeView, ComboBox, ChoiceBox, TextArea are six data-rich controls. They can be used
 * to display a huge amount of data so a convenient searching feature is essential in those controls.
 * <p>
 * The Searchable feature is to that user can type a character, the control will find the first entry that matches with
 * the character. Searchable is such a class that makes it possible. An end user can simply type any string they want to
 * search for and use arrow keys to navigate to the next or previous occurrence. See below for the list of controls that
 * support searchable.
 * <p>
 * The main purpose of searchable is to make the searching for a particular string easier in a control having a lot of
 * information. All features are related to how to make it quicker and easier to identify the matching text.
 * <p>
 * Navigation feature - After user types in a text and presses the up or down arrow keys, only items that match with the
 * typed text will be selected. User can press the up and down keys to quickly look at what those items are. In
 * addition, end users can use the home key in order to navigate to the first occurrence. Likewise, the end key will
 * navigate to the last occurrence. The navigation keys are fully customizable. The next section will explain how to
 * customize them.
 * <p>
 * Multiple selection feature - If you press and hold CTRL key while pressing up and down arrow, it will find
 * next/previous occurrence while keeping existing selections. See the screenshot below. This way one can easily find
 * several occurrences and apply an action to all of them later.
 * <p>
 * Select all feature ? Further extending the multiple selections feature, you can even select all. If you type in a
 * searching text and press CTRL+A, all the occurrences matching the searching text will be selected. This is a very
 * handy feature. For example, you want to delete all rows in a table whose "name" column begins with "old". You can
 * type in "old" and press CTRL+A, now all rows beginning with "old" will be selected. If you hook up delete key with
 * the table, pressing delete key will delete all selected rows. Imagine without this searchable feature, users will
 * have to hold CTRL key, look through each row, and click on the row they want to delete. In case they forgot to hold
 * tight the CTRL key while clicking, they have to start over again.
 * <p>
 * Basic regular expression support - It allows '?' to match any character and '*' to match any number of characters.
 * For example "a*c" will match "ac", "abc", "abbbc", or even "a b c" etc. "a?c" will only match "abc" or "a c".
 * <p>
 * Recursive search (only in TreeViewSearchable) ? In the case of TreeSearchable, there is an option called recursive.
 * You can call TreeViewSearchable#setRecursive(true/false) to change it. If TreeViewSearchable is recursive, it will
 * search all tree nodes including those, which are not visible to find the matching node. Obviously, if your tree has
 * unlimited number of tree nodes or a potential huge number of tree nodes (such as a tree to represent file system),
 * the recursive attribute should be false. To avoid this potential problem in this case, we default it to false.
 * <p>
 * Popup position ? the search popup position can be customized using setPopupPosition method using the JavaFX Pos. We
 * currently only support TOP_XXX and BOTTOM_XXX total six positions. Furthermore, you can use
 * setPopupPositionRelativeTo method to specify a Node which will be used to determine which Node the Pos is relative
 * to.
 * <p>
 * Please refer to the JideFX Common Layer Developer Guide for more information.
 *
 * @param <T> the element type in the control.
 */
@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
public abstract class Searchable<T> {
    protected final Node _node;
    private SearchPopup _popup;

    private transient Pattern _pattern;
    private transient String _searchText;

    // listeners
    protected ChangeListener<Boolean> _visibleListener;
    protected ChangeListener<Bounds> _boundsListener;
    protected EventHandler<KeyEvent> _keyListener;

    private int _cursor = -1;

    private ObjectProperty<Pos> _popupPositionProperty;
    private ObjectProperty<Node> _popupPositionRelativeToProperty;

    private Duration _hidePopupDelay = Duration.INDEFINITE;
    private Timeline _hidePopupTimer;

    private boolean _reverseOrder = false;

    // searching status update
    private BooleanProperty _searchingProperty;
    private StringProperty _searchingTextProperty;
    private StringProperty _typedTextProperty;
    private ObjectProperty<T> _matchingElementProperty;
    private IntegerProperty _matchingIndexProperty;

    // matching options
    private BooleanProperty _caseSensitiveProperty;
    private BooleanProperty _fromStartProperty;
    private BooleanProperty _wildcardEnabledProperty;
    private WildcardSupport _wildcardSupport = null;

    // searching behavior
    private ObjectProperty<Duration> _searchingDelayProperty;
    private BooleanProperty _repeatsProperty;

    // UI options
    private StringProperty _searchingLabelProperty;


    /**
     * The client property for Searchable instance. When Searchable is installed on a control, this client property has
     * the Searchable.
     */
    public static final String PROPERTY_SEARCHABLE = "Searchable"; //NON-NLS

    private Set<Integer> _selection;

    private ChangeListener<Number> _windowPositionChangeListener;

    /**
     * Creates a Searchable.
     *
     * @param node node where the Searchable will be installed.
     */
    public Searchable(Node node) {
        _node = node;
        _selection = new HashSet<>();
        updateProperty(_node, this);
        installListeners();
    }

    /**
     * Gets the selected index in the control. The concrete implementation should call methods on the control to
     * retrieve the current selected index. If the control supports multiple selection, it's OK just return the index of
     * the first selection. <p>Here are some examples. In the case of ListView, the index is the row index. In the case
     * of TreeView, the index is the row index too. In the case of TableView, depending on the selection mode, the index
     * could be row index (in row selection mode), or could be the cell index (in cell selection mode).
     *
     * @return the selected index.
     */
    protected abstract int getSelectedIndex();

    /**
     * Sets the selected index. The concrete implementation should call methods on the control to select the element at
     * the specified index. The incremental flag is used to do multiple select. If the flag is true, the element at the
     * index should be added to current selection. If false, you should clear previous selection and then select the
     * element.
     *
     * @param index       the index to be selected
     * @param incremental a flag to enable multiple selection. If the flag is true, the element at the index should be
     *                    added to current selection. If false, you should clear previous selection and then select the
     *                    element.
     */
    protected abstract void setSelectedIndex(int index, boolean incremental);

    /**
     * Sets the selected index. The reason we have this method is just for back compatibility. All the method do is just
     * to invoke {@link #setSelectedIndex(int, boolean)}.
     * <p>
     * Please do NOT try to override this method. Always override {@link #setSelectedIndex(int, boolean)} instead.
     *
     * @param index       the index to be selected
     * @param incremental a flag to enable multiple selection. If the flag is true, the element at the index should be
     *                    added to current selection. If false, you should clear previous selection and then select the
     *                    element.
     */
    public void adjustSelectedIndex(int index, boolean incremental) {
        setSelectedIndex(index, incremental);
    }

    /**
     * Gets the total element count in the control. Different concrete implementation could have different
     * interpretation of the count. This is totally OK as long as it's consistent in all the methods. For example, the
     * index parameter in other methods should be always a valid value within the total count.
     *
     * @return the total element count.
     */
    protected abstract int getElementCount();

    /**
     * Gets the element at the specified index. The element could be any data structure that internally used in the
     * control. The convertElementToString method will give you a chance to convert the element to string which is used
     * to compare with the string that user types in.
     *
     * @param index the index
     * @return the element at the specified index.
     */
    protected abstract T getElementAt(int index);

    /**
     * Converts the element that returns from getElementAt() to string.
     *
     * @param element the element to be converted
     * @return the string representing the element in the control.
     */
    protected abstract String convertElementToString(T element);

    private void select(int index, KeyEvent e) {
        if (index != -1) {
            boolean incremental = e != null && isIncrementalSelectKey(e);
            setSelectedIndex(index, incremental);
            Searchable.this.setCursor(index, incremental);
            updateText(getTypedText());
        }
        else {
            updateText(getTypedText() + " " + getResourceString("Searchable.noMatch"));
        }
        if (index != -1) {
            setMatchingElement(getElementAt(index));
            setMatchingIndex(index);
        }
        else {
            setMatchingElement(null);
            setMatchingIndex(-1);
        }
    }

    /**
     * Hides the popup.
     */
    public void hidePopup() {
        if (_popup != null) {
            _popup.hide();
            _popup = null;
            stopHidePopupTimer();
            setSearching(false);

            Window window = _node.getScene().getWindow();
            if (window != null) {
                if (_windowPositionChangeListener != null) {
                    window.xProperty().removeListener(_windowPositionChangeListener);
                    window.yProperty().removeListener(_windowPositionChangeListener);
                    _windowPositionChangeListener = null;
                }
            }
        }
        setCursor(-1);
    }

    /**
     * Installs necessary listeners to the control. This method will be called automatically when Searchable is
     * created.
     */
    public void installListeners() {
        if (_visibleListener == null) {
            _visibleListener = new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> property, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        hidePopup();
                    }
                }
            };
        }
        _node.focusedProperty().addListener(_visibleListener);
        _node.visibleProperty().addListener(_visibleListener);

        if (_boundsListener == null) {
            _boundsListener = new ChangeListener<Bounds>() {
                @Override
                public void changed(ObservableValue<? extends Bounds> property, Bounds oldValue, Bounds newValue) {
                    updatePopupPosition();
                }
            };
        }
        _node.layoutBoundsProperty().addListener(_boundsListener);

        if (_keyListener == null) {
            _keyListener = new EventHandler<KeyEvent>() {
                @Override
                public void handle(javafx.scene.input.KeyEvent event) {
                    keyTypedOrPressed(event);
                }
            };
        }
        _node.addEventHandler(KeyEvent.ANY, _keyListener);
    }

    /**
     * Uninstall the listeners that installed before. You can call this method if you decide to make the existing
     * searchable control not searchable.
     */
    public void uninstallListeners() {
        if (_visibleListener != null) {
            _node.visibleProperty().removeListener(_visibleListener);
            _node.focusedProperty().removeListener(_visibleListener);
            _visibleListener = null;
        }

        if (_boundsListener != null) {
            _node.layoutBoundsProperty().removeListener(_boundsListener);
            _boundsListener = null;
        }

        if (_keyListener != null) {
            _node.removeEventHandler(KeyEvent.ANY, _keyListener);
            _keyListener = null;
        }
    }

    /**
     * Checks if the element matches the searching text.
     *
     * @param element       the element to be checked
     * @param searchingText the searching text
     * @return true if matches.
     */
    protected boolean compare(T element, String searchingText) {
        String text = convertElementToString(element);
        return text != null && compareAsString(isCaseSensitive() ? text : text.toLowerCase(), searchingText);
    }

    /**
     * Checks if the element string matches the searching text. Different from {@link #compare(Object, String)}, this
     * method is after the element has been converted to string using {@link #convertElementToString(Object)}.
     *
     * @param text          the text to be checked
     * @param searchingText the searching text
     * @return true if matches.
     */
    protected boolean compareAsString(String text, String searchingText) {
        if (searchingText == null || searchingText.trim().length() == 0) {
            return true;
        }

        if (!isWildcardEnabled()) {
            return (searchingText.equals(text) || searchingText.length() > 0 && (isFromStart() ? text.startsWith(searchingText) : text.contains(searchingText)));
        }
        else {
            // use the previous pattern since nothing changed.
            if (_searchText != null && _searchText.equals(searchingText) && _pattern != null) {
                return _pattern.matcher(text).find();
            }

            WildcardSupport wildcardSupport = getWildcardSupport();
            String s = wildcardSupport.convert(searchingText);
            if (searchingText.equals(s)) {
                return isFromStart() ? text.startsWith(searchingText) : text.contains(searchingText);
            }
            _searchText = searchingText;

            try {
                _pattern = Pattern.compile(isFromStart() ? "^" + s : s, isCaseSensitive() ? 0 : Pattern.CASE_INSENSITIVE);
                return _pattern.matcher(text).find();
            }
            catch (PatternSyntaxException e) {
                return false;
            }
        }
    }


    /**
     * Gets the cursor which is the index of current location when searching. The value will be used in findNext and
     * findPrevious.
     *
     * @return the current position of the cursor.
     */
    public int getCursor() {
        return _cursor;
    }

    /**
     * Sets the cursor which is the index of current location when searching. The value will be used in findNext and
     * findPrevious.
     *
     * @param cursor the new position of the cursor.
     */
    public void setCursor(int cursor) {
        setCursor(cursor, false);
    }

    /**
     * Sets the cursor which is the index of current location when searching. The value will be used in findNext and
     * findPrevious. We will call this method automatically inside this class. However, if you ever call {@link
     * #setSelectedIndex(int, boolean)} method from your code, you should call this method with the same parameters.
     *
     * @param cursor      the new position of the cursor.
     * @param incremental a flag to enable multiple selection. If the flag is true, the element at the index should be
     *                    added to current selection. If false, you should clear previous selection and then select the
     *                    element.
     */
    public void setCursor(int cursor, boolean incremental) {
        if (!incremental || _cursor < 0) _selection.clear();
        if (_cursor >= 0) _selection.add(cursor);
        _cursor = cursor;
    }

    /**
     * Highlight all matching cases in the target.
     * <p>
     * In default implementation, it will just search all texts in the target to highlight all. If you have a really
     * huge text to search, you may want to override this method to have a lazy behavior on visible areas only.
     */
    protected void highlightAll() {
        int firstIndex = -1;
        int index = getSelectedIndex();
        String text = getTypedText();

        while (index != -1) {
            int newIndex = findNext(text);
            if (index == newIndex) {
                index = -1;
            }
            else {
                index = newIndex;
            }
            if (index != -1) {
                if (firstIndex == -1) {
                    firstIndex = index;
                }
                select(index);
            }
        }
        // now select the first one
        if (firstIndex != -1) {
            select(firstIndex);
        }
    }

    /**
     * Select the index for the searching text.
     *
     * @param index the start offset
     */
    protected void select(int index) {
        if (index != -1) {
            setSelectedIndex(index, true);
            setCursor(index, true);
            setMatchingElement(getElementAt(index));
            setMatchingIndex(index);
        }
        else {
            setSelectedIndex(-1, false);
            setMatchingElement(null);
            setMatchingIndex(-1);
        }
    }

    /**
     * Finds the next matching index from the cursor.
     *
     * @param s the searching text
     * @return the next index that the element matches the searching text.
     */
    public int findNext(String s) {
        String str = isCaseSensitive() ? s : s.toLowerCase();
        int count = getElementCount();
        if (count == 0)
            return s.length() > 0 ? -1 : 0;
        int selectedIndex = getCurrentIndex();
        for (int i = selectedIndex + 1; i < count; i++) {
            T element = getElementAt(i);
            if (compare(element, str))
                return i;
        }

        if (isRepeats()) {
            for (int i = 0; i < selectedIndex; i++) {
                T element = getElementAt(i);
                if (compare(element, str))
                    return i;
            }
        }

        return selectedIndex == -1 ? -1 : (compare(getElementAt(selectedIndex), str) ? selectedIndex : -1);
    }

    protected int getCurrentIndex() {
        if (_selection.contains(getSelectedIndex())) {
            return _cursor != -1 ? _cursor : getSelectedIndex();
        }
        else {
            _selection.clear();
            return getSelectedIndex();
        }
    }

    /**
     * Finds the previous matching index from the cursor.
     *
     * @param s the searching text
     * @return the previous index that the element matches the searching text.
     */
    public int findPrevious(String s) {
        String str = isCaseSensitive() ? s : s.toLowerCase();
        int count = getElementCount();
        if (count == 0)
            return s.length() > 0 ? -1 : 0;
        int selectedIndex = getCurrentIndex();
        for (int i = selectedIndex - 1; i >= 0; i--) {
            T element = getElementAt(i);
            if (compare(element, str))
                return i;
        }

        if (isRepeats()) {
            for (int i = count - 1; i >= selectedIndex; i--) {
                T element = getElementAt(i);
                if (compare(element, str))
                    return i;
            }
        }
        return selectedIndex == -1 ? -1 : (compare(getElementAt(selectedIndex), str) ? selectedIndex : -1);
    }

    /**
     * Finds the next matching index from the cursor. If it reaches the end, it will restart from the beginning. However
     * is the reverseOrder flag is true, it will finds the previous matching index from the cursor. If it reaches the
     * beginning, it will restart from the end.
     *
     * @param s the searching text
     * @return the next index that the element matches the searching text.
     */
    public int findFromCursor(String s) {
        if (isReverseOrder()) {
            return reverseFindFromCursor(s);
        }

        String str = isCaseSensitive() ? s : s.toLowerCase();
        int selectedIndex = getCurrentIndex();
        if (selectedIndex < 0)
            selectedIndex = 0;
        int count = getElementCount();
        if (count == 0)
            return -1; // no match

        // find from cursor
        for (int i = selectedIndex; i < count; i++) {
            T element = getElementAt(i);
            if (compare(element, str))
                return i;
        }

        // if not found, start over from the beginning
        for (int i = 0; i < selectedIndex; i++) {
            T element = getElementAt(i);
            if (compare(element, str))
                return i;
        }

        return -1;
    }

    /**
     * Finds the previous matching index from the cursor. If it reaches the beginning, it will restart from the end.
     *
     * @param s the searching text
     * @return the next index that the element matches the searching text.
     */
    public int reverseFindFromCursor(String s) {
        if (!isReverseOrder()) {
            return findFromCursor(s);
        }

        String str = isCaseSensitive() ? s : s.toLowerCase();
        int selectedIndex = getCurrentIndex();
        if (selectedIndex < 0)
            selectedIndex = 0;
        int count = getElementCount();
        if (count == 0)
            return -1; // no match

        // find from cursor to beginning
        for (int i = selectedIndex; i >= 0; i--) {
            T element = getElementAt(i);
            if (compare(element, str))
                return i;
        }

        // if not found, start over from the end
        for (int i = count - 1; i >= selectedIndex; i--) {
            T element = getElementAt(i);
            if (compare(element, str))
                return i;
        }

        return -1;
    }

    /**
     * Finds the first element that matches the searching text.
     *
     * @param s the searching text
     * @return the first element that matches with the searching text.
     */
    public int findFirst(String s) {
        String str = isCaseSensitive() ? s : s.toLowerCase();
        int count = getElementCount();
        if (count == 0)
            return s.length() > 0 ? -1 : 0;

        for (int i = 0; i < count; i++) {
            int index = getIndex(count, i);
            T element = getElementAt(index);
            if (compare(element, str))
                return index;
        }

        return -1;
    }

    /**
     * Finds the last element that matches the searching text.
     *
     * @param s the searching text
     * @return the last element that matches the searching text.
     */
    public int findLast(String s) {
        String str = isCaseSensitive() ? s : s.toLowerCase();
        int count = getElementCount();
        if (count == 0)
            return s.length() > 0 ? -1 : 0;
        for (int i = count - 1; i >= 0; i--) {
            T element = getElementAt(i);
            if (compare(element, str))
                return i;
        }
        return -1;
    }

    /**
     * This method is called when a key is typed or pressed.
     *
     * @param e the KeyEvent.
     */
    protected void keyTypedOrPressed(KeyEvent e) {
        if (isActivateKey(e)) {
            String searchingText = "";
            if (e.getEventType() == KeyEvent.KEY_PRESSED && !e.isControlDown() && !e.isAltDown() && !e.isMetaDown()) {
                searchingText = e.getText();
            }
            showPopup(searchingText);
            if (e.getCode() != KeyCode.ENTER) {
                e.consume();
            }
        }
        else if (isDeactivateKey(e)) {
            hidePopup();
        }
    }

    private int getIndex(int count, int index) {
        return isReverseOrder() ? count - index - 1 : index;
    }

    /**
     * Shows the search popup. By default, the search popup will be visible automatically when user types in the first
     * key (in the case of ListView, TreeView, TableView, ComboBox, ChoiceBox) or types in designated keystroke (in the
     * case of editable TextInputControl). So this method is only used when you want to show the popup manually.
     *
     * @param searchingText the searching text
     */
    public void showPopup(String searchingText) {
        if (_popup == null) {
            _popup = createSearchPopup();
            setSearching(true);
            setTypedText(searchingText);
            showPopup();
            startHidePopupTimer();
        }
    }

    /**
     * Creates the popup to hold the searching text.
     *
     * @return the searching popup.
     */
    protected SearchPopup createSearchPopup() {
        return new SearchPopup();
    }

    private void showPopup() {
        Node node = getPopupPositionRelativeTo() != null ? getPopupPositionRelativeTo() : _node;
        Window window = node.getScene().getWindow();

        if (window != null) {
            _popup.show(window);
            _popup.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    updatePopupPosition();
                }
            });

            if (_windowPositionChangeListener == null) {
                _windowPositionChangeListener = new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        updatePopupPosition();
                    }
                };
            }
            window.xProperty().addListener(_windowPositionChangeListener);
            window.yProperty().addListener(_windowPositionChangeListener);

            updatePopupPosition();
        }
    }

    private void updatePopupPosition() {
        Node node = getPopupPositionRelativeTo() != null ? getPopupPositionRelativeTo() : _node;
        if (_popup == null || node == null) return;

        double w = _popup.prefWidth(-1); // just a random number
        double h = _popup.prefHeight(-1); // just a random number
        Bounds bounds = node.getBoundsInLocal();
        Point2D p = node.localToScreen(bounds.getMinX(), bounds.getMinY());
        Pos pos = getPopupPosition();
        double x = 0, y = 0;
        switch (pos) {
            case TOP_LEFT:
                x = p.getX();
                y = p.getY() - h;
                break;
            case TOP_CENTER:
                x = p.getX() + bounds.getWidth() / 2 - w / 2;
                y = p.getY() - h;
                break;
            case TOP_RIGHT:
                x = p.getX() - w + bounds.getWidth();
                y = p.getY() - h;
                break;
            case BOTTOM_LEFT:
                x = p.getX();
                y = p.getY() + bounds.getHeight();
                break;
            case BOTTOM_CENTER:
                x = p.getX() + bounds.getWidth() / 2 - w / 2;
                y = p.getY() + bounds.getHeight();
                break;
            case BOTTOM_RIGHT:
                x = p.getX() - w + bounds.getWidth();
                y = p.getY() + bounds.getHeight();
                break;
        }

        _popup.setX(x);
        _popup.setY(y);
    }

    /**
     * Checks if the key is used as a key to find the first occurrence.
     *
     * @param e the key event
     * @return true if the key in KeyEvent is a key to find the first occurrence. By default, home key is used.
     */
    protected boolean isFindFirstKey(KeyEvent e) {
        return e.getCode() == KeyCode.HOME;
    }

    /**
     * Checks if the key is used as a key to find the last occurrence.
     *
     * @param e the key event
     * @return true if the key in KeyEvent is a key to find the last occurrence. By default, end key is used.
     */
    protected boolean isFindLastKey(KeyEvent e) {
        return e.getCode() == KeyCode.END;
    }

    /**
     * Checks if the key is used as a key to find the previous occurrence.
     *
     * @param e the key event
     * @return true if the key in KeyEvent is a key to find the previous occurrence. By default, up arrow key is used.
     */
    protected boolean isFindPreviousKey(KeyEvent e) {
        return e.getCode() == KeyCode.UP;
    }

    /**
     * Checks if the key is used as a key to find the next occurrence.
     *
     * @param e the key event
     * @return true if the key in KeyEvent is a key to find the next occurrence. By default, down arrow key is used.
     */
    protected boolean isFindNextKey(KeyEvent e) {
        return e.getCode() == KeyCode.DOWN;
    }

    /**
     * Checks if the key is used as a navigation key. Navigation keys are keys which are used to navigate to other
     * occurrences of the searching string.
     *
     * @param e the key event
     * @return true if the key in KeyEvent is a navigation key.
     */
    protected boolean isNavigationKey(KeyEvent e) {
        return isFindFirstKey(e) || isFindLastKey(e) || isFindNextKey(e) || isFindPreviousKey(e);
    }

    /**
     * Checks if the key in KeyEvent should activate the search popup.
     *
     * @param e the key event
     * @return true if the KeyEvent is a KEY_PRESSED event and the key code is isLetterKey or isDigitKey.
     */
    protected boolean isActivateKey(KeyEvent e) {
        return e.getEventType() == KeyEvent.KEY_PRESSED && (e.getCode().isLetterKey() || e.getCode().isDigitKey());
    }

    /**
     * Checks if the key in KeyEvent should hide the search popup. If this method return true and the key is not used
     * for navigation purpose ({@link #isNavigationKey(KeyEvent)} return false), the popup will be hidden.
     *
     * @param e the key event
     * @return true if the keyCode in the KeyEvent is escape key, enter key, or any of the arrow keys such as page up,
     *         page down, home, end, left, right, up and down.
     */
    protected boolean isDeactivateKey(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        return keyCode == KeyCode.ENTER || keyCode == KeyCode.ESCAPE
                || keyCode == KeyCode.PAGE_UP || keyCode == KeyCode.PAGE_DOWN
                || keyCode == KeyCode.HOME || keyCode == KeyCode.END;
    }

    /**
     * Checks if the key will trigger selecting all.
     *
     * @param e the key event
     * @return true if the key in KeyEvent is a key to trigger selecting all.
     */
    protected boolean isSelectAllKey(KeyEvent e) {
        return e.isShortcutDown() && e.getCode() == KeyCode.A; // Control+A on Win, Command+A on Mac.
    }

    /**
     * Checks if the key will trigger incremental selection.
     *
     * @param e the key event
     * @return true if the key in KeyEvent is a key to trigger incremental selection. By default, ctrl down key is
     *         used.
     */
    protected boolean isIncrementalSelectKey(KeyEvent e) {
        return e.isControlDown();
    }

    public BooleanProperty caeSensitiveProperty() {
        if (_caseSensitiveProperty == null) {
            _caseSensitiveProperty = new SimpleBooleanProperty();
        }
        return _caseSensitiveProperty;
    }

    /**
     * Checks if the case is sensitive during searching.
     *
     * @return true if the searching is case sensitive.
     */
    public boolean isCaseSensitive() {
        return caeSensitiveProperty().get();
    }

    /**
     * Sets the case sensitive flag. By default, it's false meaning it's a case insensitive search.
     *
     * @param caseSensitive the flag if searching is case sensitive
     */
    public void setCaseSensitive(boolean caseSensitive) {
        caeSensitiveProperty().set(caseSensitive);
    }

    public ObjectProperty<Duration> searchingDelayProperty() {
        if (_searchingDelayProperty == null) {
            _searchingDelayProperty = new SimpleObjectProperty<>(Duration.millis(200));
        }
        return _searchingDelayProperty;
    }

    /**
     * If it returns a positive number, it will wait for that many ms before doing the search. When the searching is
     * complex, this flag will be useful to make the searching efficient. In the other words, if user types in several
     * keys very quickly, there will be only one search. If it returns Duration.ZERO, each key will generate a search.
     *
     * @return the delay before searching starts.
     */
    public Duration getSearchingDelay() {
        return searchingDelayProperty().get();
    }

    /**
     * If this flag is set to Duration, it will wait for that many ms before doing the search. When the searching is
     * complex, this flag will be useful to make the searching efficient. In the other words, if user types in several
     * keys very quickly, there will be only one search. If this flag is set to Duration.ZERO, each key will generate a
     * search with no delay.
     *
     * @param searchingDelay the delay before searching start.
     */
    public void setSearchingDelay(Duration searchingDelay) {
        searchingDelayProperty().set(searchingDelay);
    }


    public BooleanProperty repeatsProperty() {
        if (_repeatsProperty == null) {
            _repeatsProperty = new SimpleBooleanProperty(true);
        }
        return _repeatsProperty;
    }

    /**
     * Checks if restart from the beginning when searching reaches the end or restart from the end when reaches
     * beginning. Default is false.
     *
     * @return true or false.
     */
    public boolean isRepeats() {
        return repeatsProperty().get();
    }

    /**
     * Sets the repeat flag. By default, it's false meaning it will stop searching when reaching the end or reaching the
     * beginning.
     *
     * @param repeats the repeat flag
     */
    public void setRepeats(boolean repeats) {
        repeatsProperty().set(repeats);
    }

    public BooleanProperty wildcardEnabledProperty() {
        if (_wildcardEnabledProperty == null) {
            _wildcardEnabledProperty = new SimpleBooleanProperty();
        }
        return _wildcardEnabledProperty;
    }

    /**
     * Checks if it supports wildcard in searching text. By default it is true which means user can type in "*" or "?"
     * to match with any characters or any character. If it's false, it will treat "*" or "?" as a regular character.
     *
     * @return true if it supports wildcard.
     */
    public boolean isWildcardEnabled() {
        return wildcardEnabledProperty().get();
    }

    /**
     * Enable or disable the usage of wildcard.
     *
     * @param wildcardEnabled the flag if wildcard is enabled
     * @see #isWildcardEnabled()
     */
    public void setWildcardEnabled(Boolean wildcardEnabled) {
        wildcardEnabledProperty().set(wildcardEnabled);
    }

    /**
     * Gets the WildcardSupport. If user never sets it, {@link WildcardSupport} will be used.
     *
     * @return the WildcardSupport.
     */
    public WildcardSupport getWildcardSupport() {
        if (_wildcardSupport == null) {
            _wildcardSupport = new WildcardSupport() {};
        }
        return _wildcardSupport;
    }

    /**
     * Sets the WildcardSupport. This class allows you to define what wildcards to use and how to convert the wildcard
     * strings to a regular expression string which is eventually used to search.
     *
     * @param wildcardSupport the new WildCardSupport.
     */
    public void setWildcardSupport(WildcardSupport wildcardSupport) {
        _wildcardSupport = wildcardSupport;
    }

    public StringProperty searchingLabelProperty() {
        if (_searchingLabelProperty == null) {
            _searchingLabelProperty = new SimpleStringProperty(getResourceString("Searchable.searchFor"));
        }
        return _searchingLabelProperty;
    }

    /**
     * Gets the current text that appears in the search popup. By default it is "Search for: ".
     *
     * @return the text that appears in the search popup.
     */
    public String getSearchLabel() {
        return searchingLabelProperty().get();
    }

    /**
     * Sets the text that appears in the search popup.
     *
     * @param searchLabel the search label
     */
    public void setSearchLabel(String searchLabel) {
        searchingLabelProperty().set(searchLabel);
    }

    /**
     * Gets the actual node which installed this Searchable.
     *
     * @return the actual node which installed this Searchable.
     */
    public Node getNode() {
        return _node;
    }

    public ObjectProperty<Pos> popupPositionProperty() {
        if (_popupPositionProperty == null) {
            _popupPositionProperty = new SimpleObjectProperty<Pos>(this, "popupPosition", Pos.TOP_LEFT) { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    if (isPopupVisible()) {
                        updatePopupPosition();
                    }
                }
            };
        }
        return _popupPositionProperty;
    }

    /**
     * Gets the popup position. The valid values are defined in {@link Pos}. We currently only support TOP_XXX and
     * BOTTOM_XXX total six positions.
     *
     * @return the popup position.
     */
    public Pos getPopupPosition() {
        return popupPositionProperty().get();
    }

    /**
     * Sets the popup position.
     *
     * @param popupPosition the popup location. The valid values are defined in {@link Pos}. We currently only support
     *                      TOP_XXX and BOTTOM_XXX total six positions.
     */
    public void setPopupPosition(Pos popupPosition) {
        popupPositionProperty().set(popupPosition);
    }

    public ObjectProperty<Node> popupPositionRelativeToProperty() {
        if (_popupPositionRelativeToProperty == null) {
            _popupPositionRelativeToProperty = new SimpleObjectProperty<>();
        }
        return _popupPositionRelativeToProperty;
    }

    /**
     * Gets the node that the position of the popup relative to.
     *
     * @return the control that the position of the popup relative to.
     */
    public Node getPopupPositionRelativeTo() {
        return popupPositionRelativeToProperty().get();
    }

    /**
     * Sets the position of the popup relative to the specified node. Then based on the value of {@link
     * #getPopupPosition()}. If you never set, we will use the searchable node or its scroll pane (if exists) as the
     * popupPositionRelativeTo Node.
     *
     * @param popupPositionRelativeTo the relative node
     */
    public void setPopupPositionRelativeTo(Node popupPositionRelativeTo) {
        popupPositionRelativeToProperty().set(popupPositionRelativeTo);
    }


    private void updateText(String newValue) {
        if (_popup != null) {
            _popup.setText(newValue);
        }
    }

    public StringProperty searchingTextProperty() {
        if (_searchingTextProperty == null) {
            _searchingTextProperty = new SimpleStringProperty();
        }
        return _searchingTextProperty;
    }

    public String getSearchingText() {
        return searchingTextProperty().get();
    }

    public void setSearchingText(String searchingText) {
        searchingTextProperty().set(searchingText);
    }

    public StringProperty typedTextProperty() {
        if (_typedTextProperty == null) {
            _typedTextProperty = new SimpleStringProperty(this, "typedText", "") { //NON-NLS
                private Timeline timer = new Timeline(new KeyFrame(getSearchingDelay(), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        applyText();
                    }
                }));

                @Override
                protected void invalidated() {
                    super.invalidated();
                    if (isPopupVisible()) {
                        updateText(get());
                        startHidePopupTimer();
                        startDelayTimer();
                    }
                }

                protected void applyText() {
                    String text = getTypedText().trim();
                    setSearchingText(text);
                    if (text.length() != 0) {
                        int found = findFromCursor(text);
                        select(found, null);
                    }
                    else {
                        hidePopup();
                    }
                }

                void startDelayTimer() {
                    timer.setDelay(getSearchingDelay());
                    if (!Duration.ZERO.equals(getSearchingDelay())) {
                        if (timer.getStatus() == Animation.Status.RUNNING) {
                            timer.stop();
                            timer.play();
                        }
                        else {
                            timer.setCycleCount(1);
                            timer.play();
                        }
                    }
                    else {
                        applyText();
                    }
                }
            };
        }
        return _typedTextProperty;
    }

    private String getTypedText() {
        return typedTextProperty().get();
    }

    private void setTypedText(String typedText) {
        typedTextProperty().set(typedText);
    }

    public BooleanProperty searchingProperty() {
        if (_searchingProperty == null) {
            _searchingProperty = new SimpleBooleanProperty();
        }
        return _searchingProperty;
    }

    public boolean isSearching() {
        return searchingProperty().get();
    }

    public void setSearching(boolean searching) {
        searchingProperty().set(searching);
    }


    public IntegerProperty matchingIndexProperty() {
        if (_matchingIndexProperty == null) {
            _matchingIndexProperty = new SimpleIntegerProperty();
        }
        return _matchingIndexProperty;
    }

    public int getMatchingIndex() {
        return matchingIndexProperty().get();
    }

    public void setMatchingIndex(int matchingIndex) {
        matchingIndexProperty().set(matchingIndex);
    }


    public ObjectProperty<T> matchingElementProperty() {
        if (_matchingElementProperty == null) {
            _matchingElementProperty = new SimpleObjectProperty<>();
        }
        return _matchingElementProperty;
    }

    public T getMatchingElement() {
        return matchingElementProperty().get();
    }

    public void setMatchingElement(T matchingElement) {
        matchingElementProperty().set(matchingElement);
    }

    protected class SearchPopup extends Tooltip implements EventHandler<KeyEvent> {

        public SearchPopup() {
            getStyleClass().add("searchable-popup");
            setEventHandler(KeyEvent.ANY, this);
            Label label = new Label(getSearchLabel());
            label.getStyleClass().add("searchable-popup-label");
            setGraphic(label);
        }

        @Override
        public void handle(KeyEvent e) {
            String text = getTypedText();
            if (e.getEventType() == KeyEvent.KEY_PRESSED) {
                if (isSelectAllKey(e)) {
                    int count = selectAll(e, text);
                    updateText(text + " " + MessageFormat.format(getResourceString("Searchable.found"), count));
                    e.consume();
                    return;
                }

                int found;
                if (isFindPreviousKey(e)) {
                    found = findPrevious(text);
                    select(found, e);
                    e.consume();
                    return;
                }
                else if (isFindNextKey(e)) {
                    found = findNext(text);
                    select(found, e);
                    e.consume();
                    return;
                }
                else if (isFindFirstKey(e)) {
                    found = findFirst(text);
                    select(found, e);
                    e.consume();
                    return;
                }
                else if (isFindLastKey(e)) {
                    found = findLast(text);
                    select(found, e);
                    e.consume();
                    return;
                }
            }

            String newText;
            if (e.getEventType() == KeyEvent.KEY_PRESSED) {
                if (e.getCode() == KeyCode.DELETE) {
                    newText = text.substring(1);
                }
                else if (e.getCode() == KeyCode.BACK_SPACE) {
                    newText = text.substring(0, Math.max(0,text.length() - 1));
                }
                else if (!e.isAltDown() && !e.isControlDown() && !e.isMetaDown()) {
                    newText = text.concat(e.getText());
                }
                else {
                    newText = text;
                }
                setTypedText(newText);
            }
        }

        private int selectAll(KeyEvent e, String text) {
            boolean oldReverseOrder = isReverseOrder(); // keep the old reverse order and we will set it back.
            if (oldReverseOrder) {
                setReverseOrder(false);
            }

            int index = findFirst(text);
            if (index != -1) {
                setSelectedIndex(index, false); // clear side effect of ctrl-a will select all items
                Searchable.this.setCursor(index); // as setSelectedIndex is used directly, we have to manually set the cursor value.
            }


            boolean oldRepeats = isRepeats(); // set repeats to false and set it back later.
            if (oldRepeats) {
                setRepeats(false);
            }

            while (index != -1) {
                int newIndex = findNext(text);
                if (index == newIndex) {
                    index = -1;
                }
                else {
                    index = newIndex;
                }
                if (index == -1) {
                    break;
                }
                select(index, e);
            }

            if (oldRepeats) {
                setRepeats(oldRepeats);
            }

            if (oldReverseOrder) {
                setReverseOrder(oldReverseOrder);
            }

            return _selection.size();
        }
    }

    /**
     * Checks the searching order. By default the searchable starts searching from top to bottom. If this flag is false,
     * it searches from bottom to top.
     *
     * @return the reverseOrder flag.
     */
    public boolean isReverseOrder() {
        return _reverseOrder;
    }

    /**
     * Sets the searching order. By default the searchable starts searching from top to bottom. If this flag is false,
     * it searches from bottom to top.
     *
     * @param reverseOrder the flag if searching from top to bottom or from bottom to top
     */
    public void setReverseOrder(boolean reverseOrder) {
        _reverseOrder = reverseOrder;
    }

    /**
     * Gets the localized string from resource bundle. Subclass can override it to provide its own string. Available
     * keys are defined in swing.properties that begin with "Searchable.".
     *
     * @param key the resource string key
     * @return the localized string.
     */
    protected String getResourceString(String key) {
        return Resource.getResourceBundle(Locale.getDefault()).getString(key);
    }

    /**
     * Check if the searchable popup is visible.
     *
     * @return true if visible. Otherwise, false.
     */
    public boolean isPopupVisible() {
        return _popup != null;
    }

    public BooleanProperty fromStartProperty() {
        if (_fromStartProperty == null) {
            _fromStartProperty = new SimpleBooleanProperty(this, "fromStart") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    hidePopup();
                }
            };
        }
        return _fromStartProperty;
    }

    /**
     * This is a property of how to compare searching text with the data. If it is true, it will use {@link
     * String#startsWith(String)} to do the comparison. Otherwise, it will use {@link String#indexOf(String)} to do the
     * comparison.
     *
     * @return true or false.
     */
    public boolean isFromStart() {
        return fromStartProperty().get();
    }

    /**
     * Sets the fromStart property.
     *
     * @param fromStart true if the comparison matches from the start of the text only. Otherwise false. The difference
     *                  is if true, it will use String's {@code startWith} method to match. If false, it will use
     *                  {@code contains} method.
     */
    public void setFromStart(boolean fromStart) {
        fromStartProperty().set(fromStart);
    }

    /**
     * Gets the Searchable installed on the node. Null is no Searchable was installed.
     *
     * @param node the node
     * @return the Searchable installed. Null is no Searchable was installed.
     */
    public static Searchable getSearchable(Node node) {
        Object property = node.getProperties().get(PROPERTY_SEARCHABLE);
        if (property instanceof Searchable) {
            return ((Searchable) property);
        }
        else {
            return null;
        }
    }

    private void updateProperty(Node node, Searchable searchable) {
        if (node != null) {
            Object clientProperty = _node.getProperties().get(PROPERTY_SEARCHABLE);
            if (clientProperty instanceof Searchable) {
                ((Searchable) clientProperty).uninstallListeners();
            }
            node.getProperties().put(PROPERTY_SEARCHABLE, searchable);
        }
    }

    /**
     * Gets the duration before hiding the popup.
     *
     * @return the popup timeout.
     * @see #setHidePopupDelay(Duration)
     */
    public Duration getHidePopupDelay() {
        return _hidePopupDelay;
    }

    /**
     * Sets the delay before hiding the popup.
     * <p>
     * By default, the delay value is Duration.INDEFINITE, which means the hide popup will not be hidden unless users
     * press ESCAPE key. You could set it to a positive value to automatically hide the search popup after an idle
     * time.
     *
     * @param hidePopupDelay the delay in Duration
     */
    public void setHidePopupDelay(Duration hidePopupDelay) {
        _hidePopupDelay = hidePopupDelay;
    }

    private void startHidePopupTimer() {
        if (!Duration.INDEFINITE.equals(getHidePopupDelay()) && _hidePopupTimer == null) {
            _hidePopupTimer = new Timeline(new KeyFrame(getHidePopupDelay(), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (isPopupVisible()) {
                        hidePopup();
                    }
                }
            }));
            _hidePopupTimer.setCycleCount(1);
            _hidePopupTimer.play();
        }
        else if (_hidePopupTimer != null && _hidePopupTimer.getStatus() == Animation.Status.RUNNING) {
            _hidePopupTimer.stop();
            _hidePopupTimer.play();
        }
    }

    private void stopHidePopupTimer() {
        if (_hidePopupTimer != null) {
            _hidePopupTimer.stop();
            _hidePopupTimer = null;
        }
    }

    /**
     * {@code findAll} uses the Searchable to find all the element indices that match the searching string.
     *
     * @param s the searching string.
     * @return the list of indices.
     */
    public java.util.List<Integer> findAll(String s) {
        String str = isCaseSensitive() ? s : s.toLowerCase();
        java.util.List<Integer> list = new ArrayList<>();
        for (int i = 0, count = getElementCount(); i < count; i++) {
            T element = getElementAt(i);
            if (compare(element, str)) {
                list.add(i);
            }
        }
        return list;
    }

    /**
     * Gets the element at the specified index as string using {@link #convertElementToString(Object)} method.
     *
     * @param index the index.
     * @return the element at the index converted to string.
     */
    public String getElementAtAsString(int index) {
        return convertElementToString(getElementAt(index));
    }
}

/*
 * @(#)AbstractIntelliHints.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.scene.control.hints;

import com.sun.javafx.event.EventUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


/**
 * {@code AbstractIntelliHints} is an abstract implementation of {@link IntelliHints}. It covers functions such as
 * showing the hint popup at the correct position, delegating keystrokes, updating and selecting hint. The only thing
 * that is left out to subclasses is the creation of the hint popup.
 */
@SuppressWarnings("Convert2Lambda")
public abstract class AbstractIntelliHints<T> implements IntelliHints<T> {

    private Popup _popup;
    private TextInputControl _textInputControl;
    private Node _hintsNode;

//    private BooleanProperty _followCaretProperty;

    // we use this flag to workaround the bug that setText() will trigger the hint popup.
    private boolean _keyTyped = false;

    // Specifies whether the hints popup should be displayed automatically.
    // Default is true for backward compatibility.
    private BooleanProperty _autoPopupProperty;
    private ObjectProperty<Duration> _showHintsDelayProperty;
    private List<KeyCombination> _showHintsKeyStrokes;

    /**
     * Creates an IntelliHints object for a given TextInputControl such as TextField or TextArea.
     *
     * @param textInputControl the text input control.
     */
    public AbstractIntelliHints(TextInputControl textInputControl) {
        _textInputControl = textInputControl;
        getTextInputControl().getProperties().put(PROPERTY_INTELLI_HINTS, this);

        getTextInputControl().textProperty().addListener(_textChangeListener);
        getTextInputControl().addEventFilter(KeyEvent.ANY, _keyEventHandler);
        getTextInputControl().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    hideHintsPopup();
                }
            }
        });

        addShowHintsKeyCombination(getShowHintsKeyCombination());
    }

    protected Popup createPopup() {
        return new Popup();
    }

    public TextInputControl getTextInputControl() {
        return _textInputControl;
    }


    /**
     * After user has selected a item in the hints popup, this method will update TextInputControl accordingly to accept
     * the hint.
     * <p/>
     * For TextArea, the default implementation will insert the hint into current caret position. For TextField, by
     * default it will replace the whole content with the item user selected. Subclass can always choose to override it
     * to accept the hint in a different way. For example, {@link jidefx.scene.control.hints.FileIntelliHints} will
     * append the selected item at the end of the existing text in order to complete a full file path.
     *
     * @param selected the selected hint
     */
    public void acceptHint(T selected) {
        if (selected == null)
            return;

        String newText;
        int pos = getTextInputControl().getCaretPosition();
        if (isMultiline()) {
            String text = getTextInputControl().getText();
            int start = text.lastIndexOf('\n', pos - 1);
            String remain = pos == -1 ? "" : text.substring(pos);
            text = text.substring(0, start + 1);
            text += selected;
            text += remain;
            newText = text;
        }
        else {
            newText = selected.toString();
        }

        getTextInputControl().setText(newText);

        String actualText = getTextInputControl().getText();
        int separatorIndex = actualText.indexOf('\n', pos);
        int anchor = separatorIndex == -1 ? actualText.length() : separatorIndex;
        getTextInputControl().selectRange(anchor, anchor);
    }

    /**
     * Returns whether this IntelliHints' {@code TextInputControl} supports single-line text or multi-line text.
     *
     * @return {@code true} if the node supports multiple text lines, {@code false} otherwise.
     */
    protected boolean isMultiline() {
        return getTextInputControl() instanceof TextArea;
    }

    /**
     * This method will call {@link #showHints()} if and only if the text control is enabled and has focus.
     */
    protected void showHintsPopup() {
        TextInputControl control = getTextInputControl();
        if (control.isDisabled() || !control.isEditable() || !control.isFocused()) {
            return;
        }
        showHints();
    }

    /**
     * Shows the hints popup which contains the hints. It will call {@link #updateHints(Object)}. Only if it returns
     * true, the popup will be shown. You can call this method to fore the hints to be displayed.
     */
    public void showHints() {
        if (_popup == null) {
            _popup = createPopup();
        }

        if (_hintsNode == null) {
            _hintsNode = createHintsNode();
            _popup.getContent().add(_hintsNode);
            getDelegateNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    hideHintsPopup();
                    setHintsEnabled(false);
                    acceptHint(getSelectedHint());
                    setHintsEnabled(true);
                }
            });
        }

        if (updateHints(getContext())) {
            TextInputControl control = getTextInputControl();
            Bounds bounds = control.getBoundsInLocal();
            Point2D d = control.localToScreen(bounds.getMinX(), bounds.getMaxY());
            if (!_popup.isShowing()) {
                _popup.show(control, d.getX(), d.getY());
            }
        }
        else {
            hideHintsPopup();
        }
    }

    /**
     * Gets the context for hints. The context is the information that IntelliHints needs in order to generate a list of
     * hints. For example, for code-completion, the context is current word the cursor is on. for file completion, the
     * context is the full string starting from the file system root. <p>We provide a default context in
     * AbstractIntelliHints. If it's a TextArea, the context will be the string at the caret line from line beginning to
     * the caret position. If it's a TextField, the context will be whatever string in the text field. Subclass can
     * always override it to return the context that is appropriate.
     *
     * @return the context.
     */
    protected Object getContext() {
        TextInputControl control = getTextInputControl();
        if (isMultiline()) {
            int pos = control.getCaretPosition();
            if (pos == 0) {
                return "";
            }
            else {
                String text = control.getText();
                int start = text.lastIndexOf('\n', pos - 1);
                return text.substring(start + 1, pos);
            }
        }
        else {
            return control.getText();
        }
    }

    /**
     * Hides the hints popup.
     */
    protected void hideHintsPopup() {
        if (_popup != null) {
            _popup.hide();
            _popup = null;
            _hintsNode = null;
        }
        setKeyTyped(false);
    }

    /**
     * Enables or disables the hints popup.
     *
     * @param enabled true to enable the hints popup. Otherwise false.
     */
    public void setHintsEnabled(boolean enabled) {
        if (!enabled) {
            // disable show hint temporarily
            getTextInputControl().textProperty().removeListener(_textChangeListener);
            getTextInputControl().removeEventFilter(KeyEvent.ANY, _keyEventHandler);
        }
        else {
            // enable show hint again
            getTextInputControl().textProperty().addListener(_textChangeListener);
            getTextInputControl().addEventFilter(KeyEvent.ANY, _keyEventHandler);
        }
    }

    /**
     * Checks if the hints popup is visible.
     *
     * @return true if it's visible. Otherwise, false.
     */
    public boolean isHintsPopupVisible() {
        return _popup != null && _popup.isShowing();
    }

//    public BooleanProperty followCaretProperty() {
//        if (_followCaretProperty == null) {
//            _followCaretProperty = new SimpleBooleanProperty(false);
//        }
//        return _followCaretProperty;
//    }
//
//    /**
//     * Should the hints popup follows the caret.
//     *
//     * @return true if the popup shows up right below the caret. False if the popup always shows at the bottom-left
//     *         corner (or top-left if there isn't enough on the bottom of the screen) of the TextInputControl.
//     */
//    public boolean isFollowCaret() {
//        return followCaretProperty().get();
//    }
//
//    /**
//     * Sets the position of the hints popup. If followCaret is true, the popup shows up right below the caret.
//     * Otherwise, it will stay at the bottom-left corner (or top-left if there isn't enough on the bottom of the screen)
//     * of TextInputControl.
//     *
//     * @param followCaret true or false.
//     */
//    public void setFollowCaret(boolean followCaret) {
//        followCaretProperty().set(followCaret);
//    }

    public BooleanProperty autoPopupProperty() {
        if (_autoPopupProperty == null) {
            _autoPopupProperty = new SimpleBooleanProperty(true);
        }
        return _autoPopupProperty;
    }

    /**
     * Returns whether the hints popup is automatically displayed. Default is true
     *
     * @return true if the popup should be automatically displayed. False will never show it automatically and then need
     *         the user to manually activate it via the getShowHintsKeyStroke() key binding.
     * @see #setAutoPopup(boolean)
     */
    public boolean isAutoPopup() {
        return autoPopupProperty().get();
    }

    /**
     * Sets whether the popup should be displayed automatically. If autoPopup is true then is the popup automatically
     * displayed whenever updateHints() return true. If autoPopup is false it's not automatically displayed and will
     * need the user to activate the key binding defined by getShowHintsKeyStroke().
     *
     * @param autoPopup true or false
     */
    public void setAutoPopup(boolean autoPopup) {
        autoPopupProperty().set(autoPopup);
    }

    /**
     * Gets the delegate keystrokes.
     * <p/>
     * When hint popup is visible, the keyboard focus never leaves the text control. However the hint popup usually
     * contains a control that user will try to use navigation key to select an item. For example, use UP and DOWN key
     * to navigate the list. Those keystrokes, if the popup is visible, will be delegated to the the control that
     * returns from {@link #getDelegateNode()}.
     * <p/>
     * NOTE: Since this method would be invoked inside the constructor of AbstractIntelliHints, please do not try to
     * return a field because the field is not initiated yet at this time.
     *
     * @return an array of keystrokes that will be delegate to {@link #getDelegateNode()} when hint popup is shown.
     */
    abstract protected KeyCombination[] getDelegateKeyCombination();

    /**
     * Gets the delegate node in the hint popup.
     *
     * @return the node that will receive the keystrokes that are delegated to hint popup.
     */
    abstract protected Node getDelegateNode();

    /**
     * Gets the keystroke that will trigger the hint popup. Usually the hints popup will be shown automatically when
     * user types. Only when the hint popup is hidden accidentally, this keystroke will show the popup again.
     * <p/>
     * By default, it's the DOWN key for TextField and CTRL+SPACE for TextArea.
     *
     * @return the keystroke that will trigger the hint popup.
     */
    protected KeyCombination getShowHintsKeyCombination() {
        if (isMultiline()) {
            return KeyCombination.keyCombination("Ctrl+Space"); //NON-NLS
        }
        else {
            return KeyCombination.keyCombination("Down"); //NON-NLS
        }
    }

    private boolean acceptHint() {
        IntelliHints<T> hints = getIntelliHints(getTextInputControl());
        if (hints instanceof AbstractIntelliHints) {
            AbstractIntelliHints<T> aih = (AbstractIntelliHints<T>) hints;
            aih.hideHintsPopup();
            if (aih.getSelectedHint() != null) {
                aih.setHintsEnabled(false);
                aih.acceptHint(hints.getSelectedHint());
                aih.setHintsEnabled(true);
                return true;
            }
        }
        return false;
    }

    private void rejectHint() {
        hideHintsPopup();
    }

    EventHandler<KeyEvent> _keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                if (!isHintsPopupVisible() && getShowHintsKeyCombination().match(event)) {
                    showHintsPopup();
                    event.consume();
                    return;
                }
            }

            if (isHintsPopupVisible() && event.getEventType() == KeyEvent.KEY_RELEASED && KeyCombination.keyCombination("Enter").match(event)) { //NON-NLS
                acceptHint();
            }
            else if (isHintsPopupVisible() && event.getEventType() == KeyEvent.KEY_RELEASED && KeyCombination.keyCombination("Escape").match(event)) { //NON-NLS
                rejectHint();
            }
            else {
                setKeyTyped(true);
            }

            if (getDelegateNode() != null && isHintsPopupVisible()) {
                KeyCombination[] delegateKeyStrokes = getDelegateKeyCombination();
                if (delegateKeyStrokes != null) {
                    for (KeyCombination combination : delegateKeyStrokes) {
                        if (combination.match(event)) {
                            EventUtil.fireEvent(getDelegateNode(), event);
                            event.consume();
                            return;
                        }
                    }
                }
            }

            if (isHintsPopupVisible() && event.getEventType() == KeyEvent.KEY_PRESSED) {
                acceptHint();
            }
        }
    };

    private ChangeListener<String> _textChangeListener = new ChangeListener<String>() {
        private Timeline timer = new Timeline(new KeyFrame(getShowHintsDelay(), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                keyTyped();
            }
        }));

        private void keyTyped() {
            if (isKeyTyped()) {
                if (isHintsPopupVisible() || isAutoPopup()) {
                    showHintsPopup();
                }
                setKeyTyped(false);
            }
        }


        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (Duration.ZERO.equals(getShowHintsDelay())) {
                keyTyped();
            }
            else if (!Duration.INDEFINITE.equals(getShowHintsDelay())) {
                startTimer();
            }
        }

        void startTimer() {
            timer.setDelay(getShowHintsDelay());
            if (timer.getStatus() == Animation.Status.RUNNING) {
                timer.stop();
                timer.play();
            }
            else {
                timer.setCycleCount(1);
                timer.play();
            }
        }
    };

    private boolean isKeyTyped() {
        return _keyTyped;
    }

    private void setKeyTyped(boolean keyTyped) {
        _keyTyped = keyTyped;
    }

    public ObjectProperty<Duration> showHintsDelayProperty() {
        if (_showHintsDelayProperty == null) {
            _showHintsDelayProperty = new SimpleObjectProperty<>(Duration.millis(200));
        }
        return _showHintsDelayProperty;
    }

    /**
     * Sets the delay after the key is pressed to show hints. You can set it to Duration.ZERO which means no delay, or
     * Duration.INDEFINITE which means the hints popup will not show until user presses the down arrow key.
     * <p/>
     * By default, the delay time is 200ms Duration.
     *
     * @param showHintsDelay the delay time
     */
    public void setShowHintsDelay(Duration showHintsDelay) {
        showHintsDelayProperty().set(showHintsDelay);
    }

    /**
     * Gets the delay after the key is pressed to show hints.
     *
     * @return the delay duration.
     * @see #setShowHintsDelay(Duration)
     */
    public Duration getShowHintsDelay() {
        return showHintsDelayProperty().get();
    }

    /**
     * Adds a new key stroke to show hints popup.
     *
     * @param combination the key stroke
     * @see #removeShowHintKeyCombination(KeyCombination)
     * @see #getAllShowHintsKeyStrokes()
     */
    public void addShowHintsKeyCombination(KeyCombination combination) {
        if (_showHintsKeyStrokes == null) {
            _showHintsKeyStrokes = new ArrayList<>();
        }
        _showHintsKeyStrokes.add(combination);
    }

    /**
     * Removes a key stroke from the list to show hints popup.
     *
     * @param combination the key stroke
     */
    public void removeShowHintKeyCombination(KeyCombination combination) {
        if (_showHintsKeyStrokes != null) {
            _showHintsKeyStrokes.remove(combination);
        }
    }

    /**
     * Gets all key strokes that will show hints popup.
     *
     * @return the key stroke array.
     */
    public KeyCombination[] getAllShowHintsKeyStrokes() {
        if (_showHintsKeyStrokes == null) {
            return new KeyCombination[0];
        }
        return _showHintsKeyStrokes.toArray(new KeyCombination[_showHintsKeyStrokes.size()]);
    }

    /**
     * Gets the IntelliHints object if it was installed on the node before.
     *
     * @param node the node that has IntelliHints installed
     * @param <T> the data type of the hints
     * @return the IntelliHints.
     */
    public static <T> IntelliHints<T> getIntelliHints(Node node) {
        //noinspection unchecked
        return (IntelliHints<T>) node.getProperties().get(PROPERTY_INTELLI_HINTS);
    }
}

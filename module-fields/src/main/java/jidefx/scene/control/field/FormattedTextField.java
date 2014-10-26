/*
 * @(#)FormattedTextField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import com.sun.javafx.scene.text.HitInfo;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.shape.Shape;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jidefx.scene.control.decoration.*;
import jidefx.scene.control.editor.Editor;
import jidefx.scene.control.field.verifier.IntegerRangePatternVerifier;
import jidefx.scene.control.field.verifier.PatternVerifier;
import jidefx.utils.AutoRepeatButtonUtils;
import jidefx.utils.CommonUtils;
import jidefx.utils.PredefinedShapes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code FormattedTextField} is a {@code TextField} that can restrict the user input by applying a pattern to separate
 * the input string into groups, then define PatternVerifiers to verify/restrict the input for each group.
 * <p>
 * Let's start by looking an example for an IPv4 address field.
 * <pre>{@code
 * FormattedTextField&lt;String&gt; field = new FormattedTextField&lt;&gt;();
 * field.setPattern("h.h.h.h");
 * field.getPatternVerifiers().put('h', new IntegerRangeGroupVerifier(0, 255));
 * }</pre>
 * <p>
 * The pattern is "h.h.h.h", it means there are four groups, separated by dots. It doesn?t really matter which letter or
 * letters you use here because it is just a name for the group. In this case, four groups have the same name "h". Then
 * in the PatternVerifiers map, we added a verifier for the group named "h". The verifier will enforce the input string
 * for the group is a number and it must be between 0 and 255.
 *
 * @param <T> the data type of the value in the {@code FormattedTextField}
 */
@SuppressWarnings({"Convert2Lambda", "SpellCheckingInspection", "UnusedDeclaration"})
public class FormattedTextField<T> extends TextField implements DecorationSupport, Editor<T> {
    private static final String STYLE_CLASS_DEFAULT = "formatted-text-field"; //NON-NLS
    private static final String STYLE_CLASS_NO_BACKGROUND_BUTTON = "no-background-button"; //NON-NLS
    private static final String STYLE_CLASS_INCREASE_BUTTON_ = "increase-button"; //NON-NLS
    private static final String STYLE_CLASS_DECREASE_BUTTON = "decrease-button"; //NON-NLS
    private static final String PROPERTY_FORMATTED_TEXT_FIELD_ADJUSTMENT_MOUSE_HANDLER = "FormattedTextField.AdjustmentMouseHandler"; //NON-NLS
    private StringProperty _patternProperty;
    private StringProperty _regularExpressionProperty;
    private BooleanProperty _autoAdvanceProperty;
    private BooleanProperty _autoReformatProperty;
    private BooleanProperty _autoSelectAllProperty;
    private boolean _internalAutoSelectAll = true;
    private ObjectProperty<T> _valueProperty;
    private ObjectProperty<T> _defaultValueProperty;
    private ObjectProperty<StringConverter<T>> _stringConverterProperty;
    private BooleanProperty _clearbuttonVisibleProperty;
    private Decorator<Button> _clearButtonDecorator;
    private BooleanProperty _spinnersVisibleProperty;
    private ObservableMap<String, Callback<String, Boolean>> _patternVerifiers;
    private String _fixedText;
    private DecorationPane _decorationPane;
    private Decorator<Button> _increaseDecorator;
    private Decorator<Button> _decreaseDecorator;
    private Button _decreaseSpinnerButton;
    private Button _increaseSpinnerButton;

    /**
     * Creates a {@code FormattedTextField} with a null value. This constrcutor will call {@link #initializePattern()},
     * {@link #initializeTextField()}, {@link #initializeStyle()}, {@link #registerListeners()} in order, at last, the
     * setValue(null) method. Subclass can override those methods to do its own initialization.
     */
    public FormattedTextField() {
        this(null);
    }

    /**
     * Creates a {@code FormattedTextField} with an initial value. This constrcutor will call {@link
     * #initializeStyle()}, {@link #initializePattern()}, {@link #initializeTextField()}, {@link #registerListeners()}
     * in order, at last, the setValue method. Subclass can override those methods to do its own initialization.
     *
     * @param value the initial value.
     */
    public FormattedTextField(T value) {
        initializePattern();
        initializeTextField();
        initializeStyle();
        registerListeners();
        setValue(value);
    }

    /**
     * Adds or removes style from the getStyleClass. Subclass should call super if you want to keep the existing
     * styles.
     */
    protected void initializeStyle() {
        getStyleClass().addAll(STYLE_CLASS_DEFAULT, DecorationSupport.STYLE_CLASS_DECORATION_SUPPORT);
    }

    /**
     * Do some initialization of the text field, such as set the PopupContentFactory. Subclass should call supper
     * first.
     */
    protected void initializeTextField() {
//        setContentFilter(new Callback<ContentChange, ContentChange>() {
//            @Override
//            public ContentChange call(ContentChange param) {
//                return param;
//            }
//        });
    }

    /**
     * Subclass can override this method to initializes the pattern, pattern verifiers and the StringConverter. Subclass
     * should call supper first.
     */
    protected void initializePattern() {
        InvalidationListener verifierListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (getPattern() != null) {
                    _fixedText = null;
                    updateText(getValue());
                }
            }
        };
        getPatternVerifiers().addListener(verifierListener);
    }

    /**
     * Gets the verifiers for the group. The verifier is a callback which will verify the input String and return true
     * or false. True means the input String is valid for the group, false means invalid.
     *
     * @return the verifiers for the group.
     */
    public ObservableMap<String, Callback<String, Boolean>> getPatternVerifiers() {
        if (_patternVerifiers == null) {
            _patternVerifiers = FXCollections.observableHashMap();
        }
        return _patternVerifiers;
    }

    // TODO: not used yet. The original purpose is to enforce setText call so that it is formatted.
    private boolean enforcing = true;
    private String comboBoxStyleClass = "combo-box-field"; //NON-NLS
    private String textInputStyleClass = "text-input"; //NON-NLS

    protected void registerListeners() {
        addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (!isFocused()) {
                    requestFocus();
                }
                MouseEvent e = new MouseEvent(event.getSource(), event.getTarget(), MouseEvent.MOUSE_CLICKED,
                        event.getX(), event.getY(), event.getScreenX(), event.getScreenY(), MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, true, false, true,
                        null);
                // Note: if you are using JDK8u40 or earlier, please use the first line below and comment out the second line
                // HitInfo hitInfo = ((TextFieldSkin) getSkin()).getIndex(e);
                HitInfo hitInfo = ((TextFieldSkin) getSkin()).getIndex(e.getX(), e.getY());
                if (getCaretPosition() != hitInfo.getCharIndex()) {
                    ((TextFieldSkin) getSkin()).positionCaret(hitInfo, false);
                }
                int count = event.isShiftDown() ? 10 : 1;
                for (int i = 0; i < count; i++) {
                    if (event.getDeltaY() > 0) {
                        increaseValue();
                    }
                    else if (event.getDeltaY() < 0) {
                        decreaseValue();
                    }
                }
            }
        });
        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB && !event.isControlDown()) {
                    boolean forward = !event.isShiftDown();
                    if (forward && selectNextGroup()) {
                        event.consume();
                    }
                    if (!forward && selectPreviousGroup()) {
                        event.consume();
                    }
                }
                else if (event.getCode() == KeyCode.ENTER) {
                    if (commitEdit()) {
                        if (!(getParent() instanceof Cell)) {
                            event.consume();
                        }
                    }
                    else if (cancelEditing()) {
                        event.consume();
                    }
                }
                else if (event.getCode() == KeyCode.ESCAPE) {
                    if (cancelEditing()) {
                        event.consume();
                    }
                }
                if (!event.isConsumed()) {
                    if (processKeyCode(event)) {
                        event.consume();
                    }
                }
            }
        });

        editableProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (isComboBoxLike()) {
                    if (newValue) {
                        getStyleClass().remove(comboBoxStyleClass);
                        getStyleClass().add(textInputStyleClass);
                    }
                    else {
                        getStyleClass().add(comboBoxStyleClass);
                        getStyleClass().remove(textInputStyleClass);
                    }
                }
                configPseudoClassState(newValue);
            }
        });
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (_decorationPane != null && _decorationPane.getParent() != null && !isEditable()) {
                    if (newValue) {
                        pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), false);
                        _decorationPane.pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), true);
                    }
                    else {
                        _decorationPane.pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), false);
                    }
                }

                if (!newValue) {
                    if (!commitEdit()) {
                        cancelEditing();
                    }
                }
            }
        });

        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                configDecorationPane();
            }
        });

        spinnerStyleProperty().addListener(new ChangeListener<SpinnerStyle>() {
            @Override
            public void changed(ObservableValue<? extends SpinnerStyle> observable, SpinnerStyle oldValue, SpinnerStyle newValue) {
                hideSpinners();
                _increaseDecorator = null;
                _decreaseDecorator = null;

                configDecorationPane();

                if (isSpinnersVisible()) {
                    showSpinners();
                    requestLayout();
                }
            }
        });
    }

    private void configPseudoClassState(Boolean newValue) {
        if (_decorationPane != null) {
            pseudoClassStateChanged(PseudoClass.getPseudoClass("readonly"), !newValue); //NON-NLS
        }
        else if (newValue) {
            pseudoClassStateChanged(PseudoClass.getPseudoClass("readonly"), false); //NON-NLS
        }
    }

    private void configDecorationPane() {
        if (_decorationPane != null) {
            _decorationPane.getStyleClass().removeAll("combo-box-base"); //NON-NLS
            _decorationPane.getStyleClass().addAll("combo-box-base"); //NON-NLS

            double increaseWidth = _increaseSpinnerButton == null || _increaseSpinnerButton.getPrefWidth() == 0 ? spinnerButtonSize : _increaseSpinnerButton.getPrefWidth();
            double increaseHeight = _increaseSpinnerButton == null || _increaseSpinnerButton.getPrefHeight() == 0 ? spinnerButtonSize : _increaseSpinnerButton.getPrefHeight();
            double decreaseWidth = _decreaseSpinnerButton == null || _decreaseSpinnerButton.getPrefWidth() == 0 ? spinnerButtonSize : _decreaseSpinnerButton.getPrefWidth();
            double decreaseHeight = _decreaseSpinnerButton == null || _decreaseSpinnerButton.getPrefHeight() == 0 ? spinnerButtonSize : _decreaseSpinnerButton.getPrefHeight();
            double maxWidth = Math.max(increaseWidth, decreaseWidth);
            double maxHeight = Math.max(increaseHeight, decreaseHeight);

            double groupedVerticalPadding = maxHeight > getHeight() ? maxHeight / 2 - getHeight() / 2 : 0;
            double verticalPadding = maxHeight * 2 - getHeight() <= 0 ? 0 : maxHeight - getHeight() / 2;

            switch (getSpinnerStyle()) {
                case OUTSIDE_LEFT_HORIZONTAL:
                    _decorationPane.setPadding(new Insets(groupedVerticalPadding, 0, groupedVerticalPadding, maxWidth * horizontalTotalWidthRatio));
                    break;
                case OUTSIDE_LEFT_VERTICAL:
                    _decorationPane.setPadding(new Insets(verticalPadding, 0, verticalPadding, maxWidth * verticalTotalWidthRatio));
                    break;
                case OUTSIDE_CENTER_HORIZONTAL:
                    _decorationPane.setPadding(new Insets(groupedVerticalPadding, maxWidth * verticalTotalWidthRatio, groupedVerticalPadding, maxWidth * verticalTotalWidthRatio));
                    break;
                case OUTSIDE_CENTER_VERTICAL:
                    _decorationPane.setPadding(new Insets(maxHeight * 0.9, 0, maxHeight * 0.9, 0));
                    break;
                case OUTSIDE_RIGHT_HORIZONTAL:
                    _decorationPane.setPadding(new Insets(groupedVerticalPadding, maxWidth * horizontalTotalWidthRatio, groupedVerticalPadding, 0));
                    break;
                case OUTSIDE_RIGHT_VERTICAL:
                    _decorationPane.setPadding(new Insets(verticalPadding, maxWidth * verticalTotalWidthRatio, verticalPadding, 0));
                    break;
                default:
                    _decorationPane.setPadding(new Insets(0));
                    break;
            }
        }
    }

    /**
     * Set the value to the default value or null if the default value was not set.
     */
    @Override
    public void clear() {
        setValue(getDefaultValue());
    }

    private void setTextWithoutChecking(String text) {
        try {
            enforcing = false;
            setText(text);
        }
        finally {
            enforcing = true;
        }
    }

    /**
     * Cancels the edit to reset the text using the previous value.
     * <p>
     * This method was named cancelEdit and had to be renamed to cancelEditing since JDK8u40 because it introduced a
     * final cancelEdit() method in the TextInputControl.
     *
     * @return true if canceled. False if not canceled because the previous value is null or the current value is the
     * same as the previous cancel (or you can think it as the value was just canceled and now you cancel it again).
     */
    public boolean cancelEditing() {
        T value = getValue();
        if (value != null) {
            String groupName = getCurrentGroupName();
            enforcing = false;
            try {
                String oldText = getText();
                String newText = toString(value);
                if (!CommonUtils.equals(oldText, newText)) {
                    setTextWithoutChecking(newText);
                }
                else {
                    return false;
                }
            }
            finally {
                enforcing = false;
            }
            if (groupName != null) {
                IndexRange range = getGroupRangeAt(groupName);
                if (range != null) {
                    selectRange(range.getStart(), range.getEnd());
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Commits the edit and set the value.
     *
     * @return true if commited. False if not commited because the text format is incorrect.
     */
    public boolean commitEdit() {
        if (!supportFromString()) {
            return true;
        }

        T value = fromString(getText());
        if (value == null) {
            return false;
        }
        setValue(value);
        return true;
    }

    protected String toString(T value) {
        StringConverter<T> objectConverter = getStringConverter();
        if (objectConverter != null) {
            return objectConverter.toString(value);
        }
        return null;
    }

    protected T fromString(String text) {
        StringConverter<T> objectConverter = getStringConverter();
        if (objectConverter != null) {
            try {
                return objectConverter.fromString(text);
            }
            catch (Exception e) {
                CommonUtils.ignoreException(e);
            }
        }
        return null;
    }

    protected boolean supportFromString() {
        return getStringConverter() != null;
    }

    /**
     * Moves the caret to the beginning of the current group or the end of the previous group. This function also has
     * the effect of clearing the selection.
     */
    @Override
    public void previousWord() {
        IndexRange range = previousRange(true);
        if (range != null) {
            if (getCaretPosition() <= range.getEnd()) {
                positionCaret(range.getStart());
            }
            else {
                positionCaret(range.getEnd());
            }
        }
    }

    /**
     * Moves the caret to the end of the current group or beginning of next group. This function also has the effect of
     * clearing the selection.
     */
    @Override
    public void nextWord() {
        IndexRange range = nextRange(true);
        if (range != null) {
            if (getCaretPosition() >= range.getStart()) {
                positionCaret(range.getEnd());
            }
            else {
                positionCaret(range.getStart());
            }
        }
    }

    /**
     * Moves the caret to the end of the current group or beginning of next group. This function also has the effect of
     * clearing the selection. It is the same as {@link #nextWord()}.
     */
    @Override
    public void endOfNextWord() {
        nextWord();
    }

    /**
     * Moves the caret to the beginning of current group or the end of the previous group. This does not cause the
     * selection to be cleared. Rather, the anchor stays put and the caretPosition is moved.
     */
    @Override
    public void selectPreviousWord() {
        IndexRange range = previousRange(true);
        if (range != null) {
            if (getCaretPosition() <= range.getEnd()) {
                selectPositionCaret(range.getStart());
            }
            else {
                selectPositionCaret(range.getEnd());
            }
        }
    }

    /**
     * For the grouped-based field, it will move the caret to the end of current group or the beginning of the next
     * group. This does not cause the selection to be cleared. Rather, the anchor stays put and the caretPosition is
     * moved.
     * <p>
     * For position-based field, it will simply call super.
     */
    @Override
    public void selectNextWord() {
        IndexRange range = nextRange(true);
        if (range != null) {
            if (getCaretPosition() >= range.getStart()) {
                selectPositionCaret(range.getEnd());
            }
            else {
                selectPositionCaret(range.getStart());
            }
        }
    }

    /**
     * For the grouped-based field, it will move the caret to the end of current group or the beginning of the next
     * group. This does not cause the selection to be cleared. Rather, the anchor stays put and the caretPosition is
     * moved. It is the same as {@link #selectNextWord()}.
     * <p>
     * For position-based field, it will simply call super.
     */
    @Override
    public void selectEndOfNextWord() {
        selectNextWord();
    }

    private IndexRange nextRange(boolean checkEnd) {
        IndexRange range = getGroupRangeAt(getCaretPosition());
        if (range != null) {
            if (checkEnd && range.getEnd() != getCaretPosition()) { // not at the end of the range yet
                return range;
            }
            else if (!checkEnd && range.getStart() != getCaretPosition()) { // not at the start of the range yet
                return range;
            }
            else {
                IndexRange nextRange = findNextRange(range.getEnd());
                return nextRange == null ? new IndexRange(range.getEnd(), range.getEnd()) : nextRange;
            }
        }
        else return null;
    }

    private IndexRange findNextRange(int rangeEnd) {
        for (int i = rangeEnd + 1; i < getText().length(); i++) {
            IndexRange nextRange = getGroupRangeAt(i);
            if (nextRange != null && nextRange.getEnd() != rangeEnd) {
                return nextRange;
            }
        }
        return null;
    }

    private IndexRange previousRange(boolean checkStart) {
        IndexRange range = getGroupRangeAt(getCaretPosition());
        if (range != null) {
            if (checkStart && range.getStart() != getCaretPosition()) { // not at the start of the range yet
                return range;
            }
            else if (!checkStart && range.getEnd() != getCaretPosition()) { // not at the end of the range yet
                return range;
            }
            else {
                IndexRange previousRange = findPreviousRange(range.getStart());
                return previousRange == null ? new IndexRange(range.getStart(), range.getStart()) : previousRange;
            }
        }
        else return null;
    }

    private IndexRange findPreviousRange(int rangeStart) {
        for (int i = rangeStart - 1; i >= 0; i--) {
            IndexRange previousRange = getGroupRangeAt(i);
            if (previousRange != null && previousRange.getStart() != rangeStart) {
                return previousRange;
            }
        }
        return null;
    }

    /**
     * Selects the next group. Commits the edit before selecting.
     * <p>
     * Valid only for the group-based field.
     *
     * @return true if succeed, false if not selected.
     */
    public boolean selectPreviousGroup() {
        commitWithoutSelectAll();

        int caretPosition = getCaretPosition();
        IndexRange range = getGroupRangeAt(caretPosition);
        if (caretPosition > 0) {
            for (int i = caretPosition - 1; i >= 0; i--) {
                IndexRange newRange = getGroupRangeAt(i);
                if (newRange != null && !newRange.equals(range)) {
                    selectRange(newRange.getStart(), newRange.getEnd());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Selects the next group. Commits the edit before selecting.
     * <p>
     * Valid only for the group-based field.
     *
     * @return true if succeed, false if not selected.
     */
    public boolean selectNextGroup() {
        commitWithoutSelectAll();

        int caretPosition = getCaretPosition();
        IndexRange range = getGroupRangeAt(caretPosition);
        if (caretPosition < getText().length()) {
            for (int i = caretPosition + 1; i <= getText().length(); i++) {
                IndexRange newRange = getGroupRangeAt(i);
                if (newRange != null && !newRange.equals(range)) {
                    selectRange(newRange.getStart(), newRange.getEnd());
                    return true;
                }
            }
        }
        return false;
    }

    private void commitWithoutSelectAll() {
        _internalAutoSelectAll = false;
        try {
            if (!commitEdit()) {
                cancelEditing();
            }
        }
        finally {
            _internalAutoSelectAll = true;
        }
    }

    /**
     * Selects the group.
     *
     * @return true if succeed, false if not selected.
     */
    public boolean selectCurrentGroup() {
        IndexRange range = getGroupRangeAt(getCaretPosition());
        if (range != null) {
            selectRange(range.getStart(), range.getEnd());
            return true;
        }
        return false;
    }

    /**
     * Increases or decreases the value in the current group. The current group text will be selected.
     *
     * @param event the KeyEvent.
     * @return true if the value is adjusted, otherwise false. If the text in the current group is not number, it will
     * return false.
     */
    @SuppressWarnings("unchecked")
    public boolean processKeyCode(KeyEvent event) {
        if (!event.getCode().isNavigationKey()
                || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT
                || (!event.isShortcutDown() && (event.getCode() == KeyCode.HOME || event.getCode() == KeyCode.END)))
            return false;

        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            // find a group from the current caret position
            String name = getCurrentGroupName();
            if (name == null || name.trim().isEmpty()) {
                int caretPosition = getCaretPosition();
                int length = getText().length();
                for (int i = 0; i < length; i++) {
                    int caret = (i + caretPosition) % length;
                    positionCaret(caret);
                    name = getCurrentGroupName();
                    if (name != null && !name.trim().isEmpty()) break;
                }
            }

            String text = getCurrentGroupText();
            if (text != null) {
                try {
                    Callback<String, Boolean> verifier = getPatternVerifiers().get(name);
                    if (verifier != null) {
                        if (verifier instanceof PatternVerifier.Adjustable && verifier instanceof PatternVerifier.Formatter && verifier instanceof PatternVerifier.Parser) {

                            if (verifier instanceof PatternVerifier.Value) {
                                try {
                                    ((PatternVerifier.Value) verifier).setFieldValue(fromString(getText()));
                                }
                                catch (Exception e) {
                                    // setFieldValue will not accept any null value so we will explain to user why it happens.
                                    e.printStackTrace();
                                }
                            }
                            Object newValue = null;
                            Object value = ((PatternVerifier.Parser) verifier).parse(text);
                            switch (event.getCode()) {
                                case UP:
                                    newValue = ((PatternVerifier.Adjustable) verifier).getNextValue(value, !event.isShiftDown());
                                    break;
                                case DOWN:
                                    newValue = ((PatternVerifier.Adjustable) verifier).getPreviousValue(value, !event.isShiftDown());
                                    break;
                                case PAGE_UP:
                                    newValue = ((PatternVerifier.Adjustable) verifier).getNextPage(value, !event.isShiftDown());
                                    break;
                                case PAGE_DOWN:
                                    newValue = ((PatternVerifier.Adjustable) verifier).getPreviousPage(value, !event.isShiftDown());
                                    break;
                                case HOME:
                                    if (event.isShortcutDown()) {
                                        newValue = ((PatternVerifier.Adjustable) verifier).getHome(value);
                                    }
                                    break;
                                case END:
                                    if (event.isShortcutDown()) {
                                        newValue = ((PatternVerifier.Adjustable) verifier).getEnd(value);
                                    }
                                    break;
                            }
                            if (verifier instanceof PatternVerifier.Value && newValue == null) {
                                T newFieldValue = ((PatternVerifier.Value<T, ?>) verifier).getFieldValue();
                                String newWholeText = toString(newFieldValue);
                                if (newWholeText != null) {
                                    setValue(newFieldValue);
                                }
                                IndexRange range = getGroupRangeAt(name);
                                selectRange(range.getStart(), range.getEnd());
                                return true;
                            }
                            else if (newValue != null) {
                                String newText = ((PatternVerifier.Formatter) verifier).format(newValue);
                                int groupStart = getGroupStart(getCaretPosition());
                                if (verifier.call(newText)) {
                                    super.replaceText(groupStart, groupStart + text.length(), newText);
                                    commitWithoutSelectAll();
                                    selectRange(groupStart, groupStart + newText.length());
                                }
                                return true;
                            }
                        }
                    }
                }
                catch (NumberFormatException e) {
                    CommonUtils.ignoreException(e);
                }
            }
        }
        return false;
    }

    /**
     * Increases the value of the current group by a unit. Only valid for the group-based field.
     */
    public void increaseValue() {
        if (getCurrentGroupName() == null || getCurrentGroupName().trim().isEmpty()) {
            for (int i = 0; i < getText().length(); i++) {
                positionCaret(i);
                if (getCurrentGroupName() != null && !getCurrentGroupName().trim().isEmpty()) break;
            }
        }
        requestFocus();
        processKeyCode(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, true, false, false, false));
    }

    /**
     * Decreases the value of the current group by a unit. Only valid for the group-based field.
     */
    public void decreaseValue() {
        if (getCurrentGroupName() == null || getCurrentGroupName().trim().isEmpty()) {
            for (int i = 0; i < getText().length(); i++) {
                positionCaret(i);
                if (getCurrentGroupName() != null && !getCurrentGroupName().trim().isEmpty()) break;
            }
        }
        requestFocus();
        processKeyCode(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN, true, false, false, false));
    }

    private String getFixedText() {
        if (_fixedText == null) {
            _fixedText = getInitialTextFromMask();
        }
        return _fixedText;
    }

    private String getInitialTextFromMask() {
        String pattern = getPattern();
        ObservableMap<String, Callback<String, Boolean>> verifiers = getPatternVerifiers();
        for (String s : verifiers.keySet()) {
            pattern = pattern.replace(s, "");
        }
        _fixedText = pattern;
        return pattern;
    }

    public StringProperty patternProperty() {
        if (_patternProperty == null) {
            _patternProperty = new SimpleStringProperty(this, "pattern") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    _fixedText = null;
                    updateText(FormattedTextField.this.getValue());
                }
            };
        }
        return _patternProperty;
    }

    /**
     * Gets the pattern.
     *
     * @return the pattern.
     */
    public String getPattern() {
        return patternProperty().get();
    }

    /**
     * Sets the pattern.
     *
     * @param pattern a new pattern
     */
    public void setPattern(String pattern) {
        patternProperty().set(pattern);
    }

    public StringProperty regularExpressionProperty() {
        if (_regularExpressionProperty == null) {
            _regularExpressionProperty = new SimpleStringProperty();
        }
        return _regularExpressionProperty;
    }

    /**
     * Gets the regular expression pattern.
     *
     * @return the regex.
     */
    public String getRegularExpression() {
        String regex = regularExpressionProperty().get();
        if (regex == null) {
            regex = createRegexFromPattern(false);

            // verify the regex
            if (!verifyMatcher(regex, getPattern())) {
                regex = createRegexFromPattern(true);
                if (!verifyMatcher(regex, getPattern())) {
                    throw new IllegalStateException("You must call setRegularExpression method to set a regular expression for the pattern \"" + getPattern() + "\" as we couldn't figure out how to split it in to groups.");
                }
            }
        }
        return regex;
    }

    /**
     * Sets the regular expression pattern. This regular expression pattern will be used to parse a string and separate
     * it into groups.
     * <p>
     * In most cases, you don't need to call this method because we will automatically create the regular expression
     * using the pattern from the setPattern method. If the groups are separated by a non-group characters, we should
     * have no problem figuring out. Even if the groups are adjacent, we will check if the verifier implements {@link
     * PatternVerifier.Length}. If yes, we will use the min/maxLength as additional information to help us figuring it
     * out. If none of the efforts works, we will at last throw an IllegalStateException in the runtime. When you saw
     * this exception, you can then call this method to help us with the regular expression, or you can improve the
     * pattern or the verifier.
     *
     * @param regex a new regular expression pattern with the group information.
     */
    public void setRegularExpression(String regex) {
        regularExpressionProperty().set(regex);
    }

    private boolean verifyMatcher(String regex, String sampleText) {
        Matcher matcher = Pattern.compile(regex).matcher(sampleText);
        if (matcher != null && matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = matcher.group(i);
                if (group == null || group.trim().isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public ObjectProperty<StringConverter<T>> stringConverterProperty() {
        if (_stringConverterProperty == null) {
            _stringConverterProperty = new SimpleObjectProperty<>();
        }
        return _stringConverterProperty;
    }

    /**
     * Gets the StringConverter.
     *
     * @return the StringConverter.
     */
    public StringConverter<T> getStringConverter() {
        return stringConverterProperty().get();
    }

    /**
     * Sets the StringConverter that will convert the value from/to String.
     *
     * @param stringConverter the new StringConverter.
     */
    public void setStringConverter(StringConverter<T> stringConverter) {
        stringConverterProperty().set(stringConverter);
    }

    public ObjectProperty<T> valueProperty() {
        if (_valueProperty == null) {
            _valueProperty = new SimpleObjectProperty<T>(this, "value") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    T value = get();
                    if (value != null && !supportFromString()) {
                        throw new IllegalStateException("You must call setStringConverter before you can call setValue.");
                    }
                    updateText(value);
                }
            };
        }
        return _valueProperty;
    }

    private void updateText(T value) {
        String text = toString(value);
        if (text != null) {
            if (!text.equals(getText())) {
                setTextWithoutChecking(text);
            }
            if (isAutoSelectAll() && _internalAutoSelectAll) {
                selectAll();
            }
        }
        else {
            setTextWithoutChecking(getInitialTextFromMask());
            if (isAutoSelectAll() && _internalAutoSelectAll) {
                selectAll();
            }
        }
    }

    /**
     * Gets the value represented by this {@code FormattedTextField}. The value is only used by the group-based field.
     *
     * @return the value if it is the group-based field.
     */
    public T getValue() {
        return valueProperty().get();
    }

    /**
     * Sets the value represented by this {@code FormattedTextField}.  The value is only used by the group-based field.
     * Usually if you set a value, you also want to set a format. For the non-group-based field, setValue would behave
     * the same as the setText method.
     *
     * @param value the new value.
     */
    public void setValue(T value) {
        valueProperty().set(value);
    }

    @Override
    public ObservableValue<T> observableValue() {
        return valueProperty();
    }

    public ObjectProperty<T> defaultValueProperty() {
        if (_defaultValueProperty == null) {
            _defaultValueProperty = new SimpleObjectProperty<>(this, "defaultValue"); //NON-NLS
        }
        return _defaultValueProperty;
    }

    /**
     * Gets the default value. The default value is used when the clear method is called.
     *
     * @return the default value.
     */
    public T getDefaultValue() {
        return defaultValueProperty().get();
    }

    /**
     * Sets the default value. The default value is used when the clear method is called.
     *
     * @param value the new default value.
     */
    public void setDefaultValue(T value) {
        defaultValueProperty().set(value);
    }

    public BooleanProperty autoAdvanceProperty() {
        if (_autoAdvanceProperty == null) {
            _autoAdvanceProperty = new SimpleBooleanProperty(true);
        }
        return _autoAdvanceProperty;
    }

    /**
     * Checks if the auto-advance flag. If true, the caret will move to the next position if the typed letter is not
     * valid at the current position. We will check the next position to find out if the typed letter is valid. If not,
     * we will continue util finding a valid position for the letter, or reaching the end.
     *
     * @return the auto-advance flag. Default is true.
     */
    public boolean isAutoAdvance() {
        return autoAdvanceProperty().get();
    }

    /**
     * Sets the auto-advance flag.
     *
     * @param autoAdvance a new auto-advance flag
     */
    public void setAutoAdvance(boolean autoAdvance) {
        autoAdvanceProperty().set(autoAdvance);
    }

    public BooleanProperty autoReformatProperty() {
        if (_autoReformatProperty == null) {
            _autoReformatProperty = new SimpleBooleanProperty(false);
        }
        return _autoReformatProperty;
    }

    /**
     * Checks if the auto-reformat flag. If true, the text will be automatically reformat using the StringConverter for
     * every char typed. The value will be as well for each reformat.
     *
     * @return the auto-reformat flag. Default is false.
     */
    public boolean isAutoReformat() {
        return autoReformatProperty().get();
    }

    /**
     * Sets the auto-reformat flag.
     *
     * @param autoReformat a new auto-reformat flag
     */
    public void setAutoReformat(boolean autoReformat) {
        autoReformatProperty().set(autoReformat);
    }

    public BooleanProperty autoSelectAllProperty() {
        if (_autoSelectAllProperty == null) {
            _autoSelectAllProperty = new SimpleBooleanProperty(this, "autoSelectAll", true); //NON-NLS
        }
        return _autoSelectAllProperty;
    }

    /**
     * Checks whether the text will be all selected after setValue is called.
     *
     * @return true to select all after the setValue is called.
     */
    public boolean isAutoSelectAll() {
        return autoSelectAllProperty().get();
    }

    /**
     * Sets the flag whether  to select all after the setValue is called.
     *
     * @param autoSelectAll true to select all after the setValue is called. Otherwise false.
     */
    public void setAutoSelectAll(boolean autoSelectAll) {
        autoSelectAllProperty().set(autoSelectAll);
    }

    private String keepFixedText(String text) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String s = text.substring(i, i + 1);
            if (getFixedText().contains(s)) {
                buf.append(s);
            }
        }
        return buf.toString();
    }

//    private String removeFixedText(String text) {
//        StringBuilder buf = new StringBuilder();
//        for (int i = 0; i < text.length(); i++) {
//            String s = text.substring(i, i + 1);
//            if (!_fixedText.contains(s)) {
//                buf.append(s);
//            }
//        }
//        return buf.toString();
//    }

    protected Matcher getGroupMatcher(String newText) {
        String pattern = getPattern();
        if (pattern != null) {
            return Pattern.compile(getRegularExpression()).matcher(newText);
        }
        else {
            return null;
        }
    }

    private String createRegexFromPattern(boolean considerMinMaxLength) {
        String pattern = getPattern();
        String regex = pattern
                // escape all regex chars
                .replace(".", "\\.")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("+", "\\+")
                .replace("$", "\\$")
                .replace("#", "\\#")
                .replace("^", "\\^")
                .replace("&", "\\&")
                .replace("-", "\\-")
                .replace("*", "\\*");

        // TODO: escape all chars in the single quotes.

        // find the max length of the keys
        int maxLength = 0;
        for (String s : getPatternVerifiers().keySet()) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        for (int i = maxLength; i >= 0; i--) {
            for (String s : getPatternVerifiers().keySet()) {
                if (s.length() == i) {
                    Callback<String, Boolean> callback = getPatternVerifiers().get(s);
                    if (callback instanceof PatternVerifier.Length && considerMinMaxLength) {
                        int min = ((PatternVerifier.Length) callback).getMinLength();
                        int max = ((PatternVerifier.Length) callback).getMaxLength();
                        regex = regex.replace(s, "(.{" + min + "," + max + "}+)");
                    }
                    else {
                        regex = regex.replace(s, "(.*)");
                    }
                }
            }
        }
        regex = "^" + regex + "$";
        return regex;
    }

    /**
     * Gets the text of the group at the specified index.
     *
     * @param groupIndex the group index.
     * @return the text that belongs to the group.
     */
    public String getGroupText(int groupIndex) {
        String newText = getText();
        Matcher matcher = getGroupMatcher(newText);
        if (matcher.find() && groupIndex >= 1 && groupIndex <= matcher.groupCount()) {
            return matcher.group(groupIndex);
        }
        return null;
    }

    /**
     * Gets the text of the group with the specified name. You should only use this method if each group has a unique
     * name. Otherwise it will return the first group that matches with the name.
     *
     * @param groupName the group name.
     * @return the text that belongs to the group.
     */
    public String getGroupText(String groupName) {
        String text = getText();
        Matcher matcher = getGroupMatcher(text);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                Matcher m = getGroupMatcher(getPattern());
                if (m.find()) {
                    String name = m.group(i);
                    if (groupName.equals(name)) {
                        return matcher.group(i);
                    }
                }
                i++;
            }
        }
        return null;
    }

    private String getGroupTextAfterward(char c, int index, boolean insert) {
        String text = getText();
        String newText = text.substring(0, index) + c + text.substring(insert ? index : index + 1);
        Matcher matcher = getGroupMatcher(newText);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                if (index >= matcher.start(i) && index <= matcher.end(i)) {
                    String group = matcher.group(i);

//                    String groupName = getCurrentGroupName();
//                    if (groupName != null && groupName.trim().length() > 0) {
//                        Callback<String, Boolean> verifier = getPatternVerifiers().get(groupName);
//                        if (verifier != null && verifier instanceof PatternVerifier.Formatter && verifier instanceof PatternVerifier.Parser) {
//                            group = ((PatternVerifier.Formatter) verifier).format(((PatternVerifier.Parser) verifier).parse(group));
//                        }
//                    }

                    return group;
                }
                i++;
            }
        }
        return "" + c;
    }

    private String getGroupTextAfterward2(char c, int index, boolean insert) {
        String text = getText();
        String newText = text.substring(0, index) + c + text.substring(insert ? index : index + 1);
        Matcher matcher = getGroupMatcher(newText);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                if (index + 1 >= matcher.start(i) && index + 1 <= matcher.end(i)) {
                    return matcher.group(i);
                }
                i++;
            }
        }
        return "" + c;
    }

    private int getGroupStart(int index) {
        String text = getText();
        Matcher matcher = getGroupMatcher(text);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                if (index >= matcher.start(i) && index <= matcher.end(i)) {
                    return matcher.start(i);
                }
                i++;
            }
        }
        return index;
    }

    private String getGroupNameAfterward(char c, int index, boolean insert) {
        String text = getText();
        String newText = insert ? text.substring(0, index) + c + text.substring(index)
                : text.substring(0, index) + c + text.substring(index + 1);
        return getGroupNameAt(index, newText);
    }

    private String getGroupNameAfterward2(char c, int index, boolean insert) {
        String text = getText();
        String newText = insert ? text.substring(0, index) + c + text.substring(index)
                : text.substring(0, index) + c + text.substring(index + 1);
        return getGroupNameAt(index + 1, newText);
    }

    private String getCurrentGroupName() {
        int index = getCaretPosition();
        String text = getText();
        return getGroupNameAt(index, text);
    }

    private String getGroupNameAt(int index, String text) {
        Matcher matcher = getGroupMatcher(text);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                if (index >= matcher.start(i) && index <= matcher.end(i)) {
                    Matcher m = getGroupMatcher(getPattern());
                    if (m.find()) {
                        return m.group(i);
                    }
//                    int from = 0;
//                    int j;
//                    for (j = 0; j < getPattern().length(); j++) {
//                        if (getGroupVerifiers().containsKey(getPattern().charAt(j))) {
//                            from++;
//                            if (from == i) {
//                                break;
//                            }
//                        }
//                    }
//                    return getPattern().charAt(j);
                }
                i++;
            }
        }
        return "";
    }

    private String getCurrentGroupText() {
        int index = getCaretPosition();
        String text = getText();
        Matcher matcher = getGroupMatcher(text);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                if (index >= matcher.start(i) && index <= matcher.end(i)) {
                    return matcher.group(i);
                }
                i++;
            }
        }
        return null;
    }

    /**
     * Gets the current group index. Note it is 1-based. 0 means no group is found.
     */
    private int getCurrentGroupIndex() {
        int index = getCaretPosition();
        String text = getText();
        Matcher matcher = getGroupMatcher(text);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                if (index >= matcher.start(i) && index <= matcher.end(i)) {
                    return i;
                }
                i++;
            }
        }
        return 0;
    }

    private IndexRange getGroupRangeAt(int caretPosition) {
        String text = getText();
        Matcher matcher = getGroupMatcher(text);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                if (caretPosition >= matcher.start(i) && caretPosition <= matcher.end(i)) {
                    return new IndexRange(matcher.start(i), matcher.end(i));
                }
                i++;
            }
        }
        return null;
    }

    private IndexRange getGroupRange(int group) {
        String text = getText();
        Matcher matcher = getGroupMatcher(text);
        if (matcher.find() && group >= 0 && group <= matcher.groupCount()) {
            return new IndexRange(matcher.start(group), matcher.end(group));
        }
        return null;
    }

    private IndexRange getGroupRangeAt(String groupName) {
        String text = getText();
        Matcher matcher = getGroupMatcher(text);
        if (matcher != null && matcher.find()) {
            int i = 1;
            while (i <= matcher.groupCount()) {
                Matcher m = getGroupMatcher(getPattern());
                if (m.find()) {
                    String name = m.group(i);
                    if (groupName.equals(name)) {
                        return new IndexRange(matcher.start(i), matcher.end(i));
                    }
                }
                i++;
            }
        }
        return null;
    }

    enum Mode {Insert, Replace, ReplaceGroup}

    private boolean verifyChar(char c, int index, boolean insert) {
        String groupName = getGroupNameAfterward(c, index, insert);
        if (getValue() == null && (groupName == null || groupName.trim().isEmpty())) {
            return true;
        }

        Callback<String, Boolean> verifier = getPatternVerifiers().get(groupName);
        //noinspection SimplifiableConditionalExpression
        if (verifier != null) {
            T fieldValue = fromString(getText());
            if (fieldValue == null) {
                fieldValue = getValue();
            }
            if (verifier instanceof PatternVerifier.Value) {
                ((PatternVerifier.Value<T, ?>) verifier).setFieldValue(fieldValue);
            }
            String textAfterward = getGroupTextAfterward(c, index, insert);
            Boolean result = verifier.call(textAfterward);
            if (result) {
                // to get the next group and verify the next group is okay with the insersion too. only happen when the two groups are adjacent.
                String secondGroupName = getGroupNameAfterward2(c, index, insert);
                if (!groupName.equals(secondGroupName)) {
                    Callback<String, Boolean> secondVerifier = getPatternVerifiers().get(secondGroupName);
                    //noinspection SimplifiableConditionalExpression
                    if (secondVerifier != null) {
                        if (secondVerifier instanceof PatternVerifier.Value) {
                            ((PatternVerifier.Value<T, ?>) secondVerifier).setFieldValue(fieldValue);
                        }
                        String textAfterward2 = getGroupTextAfterward2(c, index, insert);
                        return secondVerifier.call(textAfterward2);
                    }
                }
            }
            return result;
        }
        else return false;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.isEmpty()) {
            String existingText = getText();
            int newEnd = Math.max(0, Math.min(end, existingText.length()));
            String deletedText = existingText.substring(start, newEnd);
            String newText = keepFixedText(deletedText);
            super.replaceText(start, newEnd, newText);
            reformat();
        }
        else if (text.length() >= 1) {

            // delete the existing text first
            String existingText = getText();
            int newEnd = Math.max(0, Math.min(end, existingText.length()));
            String deletedText = existingText.substring(start, newEnd);
            String newText = keepFixedText(deletedText);
            super.replaceText(start, newEnd, newText);
            reformat();

            int index = 0;
            while (index < text.length()) {
                char c = text.charAt(index);
                if (start != -1 && !getFixedText().contains("" + c) && verifyChar(c, start, true)) { // insert
                    super.replaceText(start, start, "" + c);
                    reformat();
                    index++;
                    start++;
                }
                else if (start != -1 && (start < getText().length() && !getFixedText().contains("" + getText().charAt(start))) && !getFixedText().contains("" + c) && verifyChar(c, start, false)) { // overwrite
                    super.replaceText(start, start + 1, "" + c);
                    reformat();
                    index++;
                    start++;
                }
                // TODO: add a check here if the inserted char will replace the whole group. For example, we have Apr, now we typed a J. J should replace Apr so that user can type in Jun or Jul
                else if (start < getText().length()) {
                    if (c == getText().charAt(start)) {
                        positionCaret(start + 1);
                        index++;
                    }
                    else {
                        if (isAutoAdvance()) {
                            replaceText(start + 1, end + 1, text.substring(index));
                            reformat();
                        }
                        break;
                    }
                }
                else {
                    break;
                }
            }
        }
    }

    private void reformat() {
        if (isAutoReformat()) {
            try {
                _internalAutoSelectAll = false;
                int position = getCaretPosition();
                String text = getText();
                commitEdit();
                String newText = getText();
                int newPosition = position + (newText.length() - text.length());
                positionCaret(Math.min(newPosition, newText.length()));
            }
            finally {
                _internalAutoSelectAll = true;
            }
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        IndexRange range = getSelection();
        if (getPattern() != null) {
            String newText = keepFixedText(getSelectedText());
            super.replaceText(range.getStart(), range.getEnd(), newText);
            positionCaret(range.getStart());
            for (int i = 0; i < replacement.length(); i++) {
                int start = getCaretPosition();
                replaceText(start, start, "" + replacement.charAt(i));
            }
        }
        else {
            super.replaceSelection(replacement);
        }
    }

    private void showSpinners() {
        if (_increaseDecorator == null) {
            _increaseDecorator = createIncreaseSpinnerDecorator();
            Button button = _increaseDecorator.getNode();
            enableAutoRepeat(button);
            button.disableProperty().bind(disabledProperty());
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!isDisabled())
                        increaseValue();
                }
            });
        }
        DecorationUtils.install(this, _increaseDecorator);

        if (_decreaseDecorator == null) {
            _decreaseDecorator = createDecreaseSpinnerDecorator();
            Button button = _decreaseDecorator.getNode();
            enableAutoRepeat(button);
            button.disableProperty().bind(disabledProperty());
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!isDisabled())
                        decreaseValue();
                }
            });
        }
        DecorationUtils.install(this, _decreaseDecorator);
    }

    /**
     * Installs the {@link AutoRepeatButtonUtils} to the spinner buttons. Subclass can override this method to disable
     * auto-repeat feature or install your own one with different initial delay and interval.
     *
     * @param button one of the two spinner buttons.
     */
    protected void enableAutoRepeat(Button button) {
        AutoRepeatButtonUtils.install(button);
    }

    /**
     * Creates the decorator for the increase button. Subclass can override it to create a different decorator.
     *
     * @return the decorator for the increase button
     */
    protected Decorator<Button> createIncreaseSpinnerDecorator() {
        Decorator<Button> increaseSpinnerDecorator = new PredefinedDecorators.AbstractButtonDecoratorSupplier() {
            @Override
            public Decorator<Button> get() {
                _increaseSpinnerButton = createIncreaseSpinnerButton();

                switch (getSpinnerStyle()) {
                    case OUTSIDE_LEFT_HORIZONTAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(-spinnerOffsetX, 0));
                    case OUTSIDE_CENTER_HORIZONTAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(spinnerOffsetX, 0));
                    case OUTSIDE_RIGHT_HORIZONTAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(spinnerOffsetX + 100, 0));
                    case OUTSIDE_LEFT_VERTICAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(-spinnerOffsetX, -spinnerOffsetY));
                    case OUTSIDE_CENTER_VERTICAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.TOP_CENTER, new Point2D(0, -spinnerOffsetY));
                    case OUTSIDE_RIGHT_VERTICAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(spinnerOffsetX, -spinnerOffsetY));
                    case INSIDE_LEFT_HORIZONTAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(spinnerOffsetX + 100, 0), new Insets(0, 0, 0, spinnerPadding));
                    case INSIDE_CENTER_HORIZONTAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(-spinnerOffsetX, 0), new Insets(0, spinnerPadding, 0, 0));
                    case INSIDE_RIGHT_HORIZONTAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(-spinnerOffsetX, 0), new Insets(0, spinnerPadding, 0, 0));
                    case INSIDE_LEFT_VERTICAL:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(spinnerOffsetX, -spinnerOffsetY), new Insets(0, 0, 0, spinnerPadding / 2));
//                    case INSIDE_CENTER_VERTICAL:
//                        return new DefaultDecorator<>(button, Pos.TOP_CENTER, new Point2D(0, spinnerOffsetX));
                    case INSIDE_RIGHT_VERTICAL:
                    default:
                        return new Decorator<>(_increaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(-spinnerOffsetX, -spinnerOffsetY), new Insets(0, spinnerPadding / 2, 0, 0));
                }
            }
        }.get();
        return increaseSpinnerDecorator;
    }

    protected Button createIncreaseSpinnerButton() {
        Shape arrowIcon = PredefinedShapes.getInstance().createArrowIcon(spinnerArrowIconSize);
        Button button = new Button();
        button.setId(STYLE_CLASS_INCREASE_BUTTON_);
        button.getStyleClass().addAll(STYLE_CLASS_NO_BACKGROUND_BUTTON);
        button.setPrefWidth(spinnerButtonSize);
        button.setPrefHeight(spinnerButtonSize);
        button.setFocusTraversable(false);
        button.setPickOnBounds(true);
        button.setCursor(Cursor.DEFAULT);

        arrowIcon.setFocusTraversable(false);
        button.setGraphic(arrowIcon);

        double arrowRotate = 0;
        switch (getSpinnerStyle()) {
//            case INSIDE_CENTER_VERTICAL:
            case INSIDE_LEFT_VERTICAL:
            case INSIDE_RIGHT_VERTICAL:
            case OUTSIDE_CENTER_VERTICAL:
            case OUTSIDE_LEFT_VERTICAL:
            case OUTSIDE_RIGHT_VERTICAL:
                arrowRotate = 180;
                break;
            case INSIDE_CENTER_HORIZONTAL:
            case INSIDE_LEFT_HORIZONTAL:
            case INSIDE_RIGHT_HORIZONTAL:
            case OUTSIDE_CENTER_HORIZONTAL:
            case OUTSIDE_LEFT_HORIZONTAL:
            case OUTSIDE_RIGHT_HORIZONTAL:
                arrowRotate = -90;
                break;
        }
        button.getGraphic().setRotate(arrowRotate);
        return button;
    }

    private double spinnerArrowIconSize = 10;
    private double spinnerButtonSize = 12;
    private double spinnerPadding = 100;
    private double spinnerOffsetX = 80;
    private double spinnerOffsetY = 40;
    private double horizontalTotalWidthRatio = 2.8; // (80 - 50) * 2 / 100 = 60%, that's how 0.6 in 2.6 come from
    private double verticalTotalWidthRatio = 1.8; // (80 - 50) * 2 / 100 = 60%, that's how 0.6 in 1.6 come from

    /**
     * Creates the decorator for the decrease button. Subclass can override it to create a different decorator.
     *
     * @return the decorator for the decrease button
     */
    protected Decorator<Button> createDecreaseSpinnerDecorator() {
        return new PredefinedDecorators.AbstractButtonDecoratorSupplier() {

            @Override
            public Decorator<Button> get() {
                _decreaseSpinnerButton = createDecreaseSpinnerButton();

                switch (getSpinnerStyle()) {
                    case OUTSIDE_LEFT_HORIZONTAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(-spinnerOffsetX - 100, 0));
                    case OUTSIDE_CENTER_HORIZONTAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(-spinnerOffsetX, 0));
                    case OUTSIDE_RIGHT_HORIZONTAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(spinnerOffsetX, 0));
                    case OUTSIDE_LEFT_VERTICAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(-(spinnerOffsetX), spinnerOffsetY));
                    case OUTSIDE_CENTER_VERTICAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.BOTTOM_CENTER, new Point2D(0, spinnerOffsetY));
                    case OUTSIDE_RIGHT_VERTICAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(spinnerOffsetX, spinnerOffsetY));
                    case INSIDE_LEFT_HORIZONTAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(spinnerOffsetX, 0), new Insets(0, 0, 0, spinnerPadding));
                    case INSIDE_CENTER_HORIZONTAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(spinnerOffsetX, 0), new Insets(0, 0, 0, spinnerPadding));
                    case INSIDE_RIGHT_HORIZONTAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(-spinnerOffsetX - 100, 0), new Insets(0, spinnerPadding, 0, 0));
                    case INSIDE_LEFT_VERTICAL:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_LEFT, new Point2D(spinnerOffsetX, spinnerOffsetY), new Insets(0, 0, 0, spinnerPadding / 2));
                    case INSIDE_RIGHT_VERTICAL:
                    default:
                        return new Decorator<>(_decreaseSpinnerButton, Pos.CENTER_RIGHT, new Point2D(-spinnerOffsetX, spinnerOffsetY), new Insets(0, spinnerPadding / 2, 0, 0));
                }
            }
        }.get();
    }

    protected Button createDecreaseSpinnerButton() {
        Shape arrowIcon = PredefinedShapes.getInstance().createArrowIcon(spinnerArrowIconSize);
        Button button = new Button();
        button.setId(STYLE_CLASS_DECREASE_BUTTON);
        button.getStyleClass().addAll(STYLE_CLASS_NO_BACKGROUND_BUTTON);
        button.setPrefWidth(spinnerButtonSize);
        button.setPrefHeight(spinnerButtonSize);
        button.setFocusTraversable(false);
        button.setPickOnBounds(true);
        button.setCursor(Cursor.DEFAULT);

        arrowIcon.setFocusTraversable(false);
        button.setGraphic(arrowIcon);

        double arrowRotate = 0;
        switch (getSpinnerStyle()) {
            case INSIDE_LEFT_HORIZONTAL:
            case INSIDE_CENTER_HORIZONTAL:
            case INSIDE_RIGHT_HORIZONTAL:
            case OUTSIDE_LEFT_HORIZONTAL:
            case OUTSIDE_CENTER_HORIZONTAL:
            case OUTSIDE_RIGHT_HORIZONTAL:
                arrowRotate = 90;
                break;
        }
        button.getGraphic().setRotate(arrowRotate);
        return button;
    }

    private void hideSpinners() {
        if (_increaseDecorator != null) {
            DecorationUtils.uninstall(this, _increaseDecorator);
        }
        if (_decreaseDecorator != null) {
            DecorationUtils.uninstall(this, _decreaseDecorator);
        }
    }

    private void showClearButton() {
        if (_clearButtonDecorator == null) {
            _clearButtonDecorator = PredefinedDecorators.getInstance().getClearButtonDecoratorSupplier().get();
            _clearButtonDecorator.getNode().disableProperty().bind(disabledProperty());
            _clearButtonDecorator.getNode().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!isDisabled())
                        clear();
                }
            });
        }
        DecorationUtils.install(this, _clearButtonDecorator);
    }

    private void hideClearButton() {
        if (_clearButtonDecorator != null) {
            DecorationUtils.uninstall(this, _clearButtonDecorator);
        }
    }

    public BooleanProperty spinnersVisibleProperty() {
        if (_spinnersVisibleProperty == null) {
            _spinnersVisibleProperty = new SimpleBooleanProperty(this, "spinnersVisible") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    boolean visible = get();
                    if (visible) {
                        showSpinners();
                    }
                    else {
                        hideSpinners();
                    }
                }
            };
        }
        return _spinnersVisibleProperty;
    }

    /**
     * Checks if the spinner buttons are visible.
     *
     * @return true or false.
     */
    public boolean isSpinnersVisible() {
        return spinnersVisibleProperty().get();
    }

    /**
     * Sets the spinner buttons visible. The spinner buttons are two buttons with up and down arrows. Clicking them will
     * increase and decrease the values in a group-based FormattedTextField. For position-based one, the spinners have
     * no effect.
     *
     * @param spinnersVisible true or false.
     */
    public void setSpinnersVisible(boolean spinnersVisible) {
        spinnersVisibleProperty().set(spinnersVisible);
    }

    public BooleanProperty clearButtonVisibleProperty() {
        if (_clearbuttonVisibleProperty == null) {
            _clearbuttonVisibleProperty = new SimpleBooleanProperty(this, "clearButtonVisible") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    boolean visible = get();
                    if (visible) {
                        showClearButton();
                    }
                    else {
                        hideClearButton();
                    }
                }
            };
        }
        return _clearbuttonVisibleProperty;
    }

    /**
     * Checsk if the clear button is visible. The clear button will clear the text on the field.
     *
     * @return true or false.
     */
    public boolean isClearButtonVisible() {
        return clearButtonVisibleProperty().get();
    }

    /**
     * Shows or hides the clear button.
     *
     * @param clearButtonVisible true or false.
     */
    public void setClearButtonVisible(boolean clearButtonVisible) {
        clearButtonVisibleProperty().set(clearButtonVisible);
    }

    private BooleanProperty _comboBoxLikeProperty;

    public BooleanProperty comboBoxLikeProperty() {
        if (_comboBoxLikeProperty == null) {
            _comboBoxLikeProperty = new SimpleBooleanProperty(this, "comboBoxLike", true) { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    getStyleClass().remove(comboBoxStyleClass);
                    getStyleClass().add(textInputStyleClass);
                }
            };
        }
        return _comboBoxLikeProperty;
    }

    public boolean isComboBoxLike() {
        return comboBoxLikeProperty().get();
    }

    public void setComboBoxLike(Boolean comboBoxLie) {
        comboBoxLikeProperty().set(comboBoxLie);
    }

    /**
     * Installs a mouse handler to the node so that when user drags the node, the value in the FormattedTextField will
     * be adjusted. Pressing on the adjustment node will make the field getting focus and select the current group.
     * Dragging the node will adjust the value. Also when the SHIFT key is pressed during dragging, we will adjust the
     * value 10 times for each mouse dragged event (the same as steps x 10). If the ALT key is pressed during dragging,
     * it will take a 10 times longer dragging distance in order to trigger the value adjustment (the same as threshold
     * x 10). Furthermore, if the field has multiple groups, A double-click will select the next group, A
     * shift-double-click will select the previous group.
     * <p>
     * Because the singleton nature of this feature, we only support one adjustment node per field. Installing the mouse
     * handler for another node will remove the previous mouse handler.
     *
     * @param node the node. It is usually the label node for the field. For example, you have a Label("Opacity") then a
     *             field for the opacity value. The node here would be the label.
     */
    public void installAdjustmentMouseHandler(Node node) {
        installAdjustmentMouseHandler(node, 1);
    }

    /**
     * Installs a mouse handler to the node so that when user drags the node, the value in the FormattedTextField will
     * be adjusted. Pressing on the adjustment node will make the field getting focus and select the current group.
     * Dragging the node will adjust the value. Also when the SHIFT key is pressed during dragging, we will adjust the
     * value 10 times for each mouse dragged event (the same as units x 10). If the ALT key is pressed during dragging,
     * it will take a 10 times longer dragging distance in order to trigger the value adjustment (the same as threshold
     * x 10). Furthermore, if the field has multiple groups, A double-click will select the next group, A
     * shift-double-click will select the previous group.
     * <p>
     * Because the singleton nature of this feature, we only support one adjustment node per field. Installing the mouse
     * handler for another node will remove the previous mouse handler.
     *
     * @param node  the node. It is usually the label node for the field. For example, you have a Label("Opacity") then
     *              a field for the opacity value. The node here would be the label.
     * @param units how many times the value will be adjusted when there is a mouse dragged event. It should be a value
     *              greater than or equal to 1.
     */
    public void installAdjustmentMouseHandler(Node node, int units) {
        installAdjustmentMouseHandler(node, units, 1);
    }

    /**
     * Installs a mouse handler to the node so that when user drags the node, the value in the FormattedTextField will
     * be adjusted. Pressing on the adjustment node will make the field getting focus and select the current group.
     * Dragging the node will adjust the value. Also when the SHIFT key is pressed during dragging, we will adjust the
     * value 10 times for each mouse dragged event (the same as units x 10). If the ALT key is pressed during dragging,
     * it will take a 10 times longer dragging distance in order to trigger the value adjustment (the same as threshold
     * x 10). Furthermore, if the field has multiple groups, A double-click will select the next group, A
     * shift-double-click will select the previous group.
     * <p>
     * Because the singleton nature of this feature, we only support one adjustment node per field. Installing the mouse
     * handler for another node will remove the previous mouse handler.
     *
     * @param node      the node. It is usually the label node for the field. For example, you have a Label("Opacity")
     *                  then a field for the opacity value. The node here would be the label.
     * @param units     how many units the value will be adjusted when there is a mouse dragged event. It should be a
     *                  value greater than or equal to 1.
     * @param threshold it will determine how far the mouse movement will trigger a value adjustment. The default value
     *                  is 1. Larger means longer distance. The larger the value, the longer distance the mouse has to
     *                  travel before a value adjustment is triggered.
     */
    public void installAdjustmentMouseHandler(final Node node, final int units, final double threshold) {
        uninstallAdjustmentMouseHandler(node);
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            double x = Double.NEGATIVE_INFINITY;

            @Override
            public void handle(MouseEvent event) {
                if (!isDisabled()) {
                    if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                        node.setCursor(Cursor.H_RESIZE);
                    }
                    else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                        if (!isFocused()) {
                            requestFocus();
                            selectCurrentGroup();
                        }
                    }
                    else if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED) && event.isStillSincePress()) {
                        if (event.getClickCount() >= 2) {
                            if (event.isShiftDown()) {
                                if (!selectPreviousGroup()) {
                                    positionCaret(getText().length());
                                    selectCurrentGroup();
                                }
                            }
                            else {
                                if (!selectNextGroup()) {
                                    positionCaret(0);
                                    selectCurrentGroup();
                                }
                            }
                        }
                    }
                    else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                        if (x == Double.NEGATIVE_INFINITY) {
                            x = event.getSceneX() - 1;
                        }

                        double value = event.getSceneX() - x;
                        int s = Math.max(1, units); // to make sure it is greater than 1
                        double t = threshold;
                        if (event.isAltDown()) {
                            t *= 10.0;
                        }
                        else if (event.isShiftDown()) {
                            s *= 10;
                        }
                        if (Math.abs(value) > t) {
                            for (int i = 0; i < s; i++) {
                                if (value > 0) {
                                    increaseValue();
                                }
                                else {
                                    decreaseValue();
                                }
                            }
                            x = event.getSceneX();
                        }
                    }
                    else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                        x = Double.NEGATIVE_INFINITY;
                    }
                    else if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                        if (x == Double.NEGATIVE_INFINITY) {
                            node.setCursor(Cursor.DEFAULT);
                        }
                    }
                }
            }
        };
        node.getProperties().put(PROPERTY_FORMATTED_TEXT_FIELD_ADJUSTMENT_MOUSE_HANDLER, eventHandler);
        node.addEventHandler(MouseEvent.ANY, eventHandler);
    }

    /**
     * Uninstalls the mouse handler added by {@link #installAdjustmentMouseHandler(Node)}.
     *
     * @param node the node where the mouse handler was added.
     */
    public void uninstallAdjustmentMouseHandler(Node node) {
        Object eventHandler = node.getProperties().get(PROPERTY_FORMATTED_TEXT_FIELD_ADJUSTMENT_MOUSE_HANDLER);
        if (eventHandler instanceof EventHandler) {
            node.removeEventHandler(MouseEvent.ANY, (EventHandler) eventHandler);
            node.getProperties().remove(PROPERTY_FORMATTED_TEXT_FIELD_ADJUSTMENT_MOUSE_HANDLER);
        }
    }

    /**
     * Returns a node which can be used as a spinner control. Instead of using the FormattedTextField, you will use the
     * returned node and add it to parent. In fact, you can display the spinner control inside the field by calling
     * {@link #setSpinnersVisible(boolean)} and set it to true. However, not all spinner styles are supported this way.
     * For example, all spinner styles that start with OUTSIDE_ are not supported very well because it doesn't have
     * outer border. If you use the node returned from this method, all spinner styles are supported.
     *
     * @return a node that can be used as a spinner control.
     */
    public Node asSpinner() {
        return asSpinner(SpinnerStyle.INSIDE_RIGHT_VERTICAL);
    }

    /**
     * Returns a node which can be used as a spinner control. Instead of using the FormattedTextField, you will use the
     * returned node and add it to parent. In fact, you can display the spinner control inside the field by calling
     * {@link #setSpinnersVisible(boolean)} and set it to true. However, not all spinner styles are supported this way.
     * For example, all spinner styles that start with OUTSIDE_ are not supported very well because it doesn't have
     * outer border. If you use the node returned from this method, all spinner styles are supported.
     *
     * @param style the SpinnerStyle
     * @return a node that can be used as a spinner control.
     */
    public Node asSpinner(SpinnerStyle style) {
        if (style != null) setSpinnerStyle(style);
        setSpinnersVisible(true);

        boolean isEditable = isEditable();

        if (_decorationPane == null) {
            _decorationPane = new DecorationPane(this);
            configDecorationPane();
        }

        // TODO figure out how to active edit status without reactivation
        setEditable(!isEditable);
        setEditable(isEditable);

        return _decorationPane;
    }

    /**
     * A static method which will create a FormattedTextField for IPv4 address. The InputMask for it is "G.G.G.G". The
     * Pattern "h.h.h.h". A GroupVerifier is defined for the group name 'h' to limit the number entered for the group is
     * from 0 to 255.
     *
     * @return a FormattedTextField for IPv4 address.
     */
    public static FormattedTextField<String> createIPv4Field() {
        FormattedTextField<String> field = new FormattedTextField<>();
        field.getPatternVerifiers().put("h", new IntegerRangePatternVerifier(0, 255));
        field.setPattern("h.h.h.h"); //NON-NLS
        return field;
    }

    private ObjectProperty<SpinnerStyle> _spinnerStyleProperty;

    public ObjectProperty<SpinnerStyle> spinnerStyleProperty() {
        if (_spinnerStyleProperty == null) {
            _spinnerStyleProperty = new SimpleObjectProperty<>(SpinnerStyle.INSIDE_RIGHT_VERTICAL);
        }
        return _spinnerStyleProperty;
    }

    /**
     * Gets the SpinnerStyle.
     *
     * @return the SpinnerStyle.
     */
    public SpinnerStyle getSpinnerStyle() {
        return spinnerStyleProperty().get();
    }

    /**
     * Sets the SpinnerStyle. It allows you to customize the location of the spinner buttons. If you are using any
     * SpinnerStyles that start with "OUTSIDE_", we recommand you add the field to its parent by adding
     * field.asSpinner() to a pane instead of the field itself.
     *
     * @param spinnerStyle the new SpinnerStyle.
     * @see #asSpinner()
     */
    public void setSpinnerStyle(SpinnerStyle spinnerStyle) {
        spinnerStyleProperty().set(spinnerStyle);
    }

    // For decoration
    @Override
    protected void layoutChildren() {
        prepareDecorations();
        super.layoutChildren();
        Platform.runLater(this::layoutDecorations);
    }

    private DecorationDelegate _operator;

    public void prepareDecorations() {
        if (_operator == null) {
            _operator = new DecorationDelegate(this);
        }
        _operator.prepareDecorations();
    }

    public void layoutDecorations() {
        _operator.layoutDecorations();
    }

    // For interface DecorationSupport
    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override
    public void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, HPos halignment, VPos valignment) {
        super.positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, halignment, valignment);
    }

    // end decoration
}


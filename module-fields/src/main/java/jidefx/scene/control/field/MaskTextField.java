/*
 * @(#)MaskTextField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.decoration.PredefinedDecorators;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * {@code MaskTextField} is a {@code TextField} that can restrict the user input by applying a mask.
 * <p>
 * <b >Position-based Mask</b>
 * <p>
 * Position-base mask works for all the editing cases when the input string has a fixed length and each character can be
 * restricted based on its position. To address this case, we allow you define the masks on a MaskTextField.
 * <p>
 * <b >InputMask</b>
 * <p>
 * MaskTextField has setInputMask(String mask) to set the mask we mentioned above. The pre-defined mask characters are:
 * <ul>
 * <li>A	INPUT_MASK_LETTER	ASCII alphabetic character required. A-Z, a-z.
 * <li>N	INPUT_MASK_DIGIT_OR_LETTER	ASCII alphanumeric character required. A-Z, a-z, 0-9.</li>
 * <li>X	INPUT_MASK_ANY_NON_SPACE	Any character required except spaces.
 * <li>H	INPUT_MASK_HAX	Hexadecimal character required. A-F, a-f, 0-9.
 * <li>D	INPUT_MASK_DIGIT_NON_ZERO	ASCII digit required. 1-9.
 * <li>9	INPUT_MASK_DIGIT	ASCII digit required. 0-9.
 * <li>8	INPUT_MASK_DIGIT_0_TO_8	ASCII digit required. 0-8.
 * <li>7, 6, 5, 4, 3, 2 and so on which means only allows from 0 to that number
 * <li>2	INPUT_MASK_DIGIT_0_TO_2	ASCII digit required. 0-2.
 * <li>1	INPUT_MASK_DIGIT_0_TO_1	ASCII digit required. 0-1, for example, a binary number
 * <li>0	INPUT_MASK_DIGIT_ZERO	0 required
 * <li>G	INPUT_MASK_DIGIT_GROUP	Indicates a Group. This is the special one, we will discuss it later
 * </ul>
 * If the setInitialText is not called, the InputMask will be used to create the initial text for the field. We create
 * it by removing all masks, replace them with spaces or the specified placeholder character, and leave the non-mask
 * characters where they are. The placeholder character can be set using setPlaceholderCharacter(char). See below. In
 * both cases, the InputMask is 999-99-9999.
 * <p>
 * <b>ConversionMask</b>
 * <p>
 * To make the grammar easy to understand, we strictly enforced that there is only one mask character per position for
 * the InputMask. But clearly, one character is not enough in some cases. So we also allow you to define several other
 * masks for other purposes. In addition to the Input Mask, we also have a separate Conversion Mask which will
 * automatically convert the entered character to another character. This mask is optional. It can be set using
 * setConversionMask(String mask). If not set, there will be no conversion. If you ever set the mask, please make sure
 * they have the exact same length as the InputMask, and have a valid conversion mask character at the exact position
 * where there is an InputMask character.
 * <ul>
 * <li>U	CONVERSION_MASK_UPPER_CASE	Uppercase required. If user enters a lowercase letter, it will be automatically
 * converted to the corresponding uppercase letter.
 * <li>L	CONVERSION_MASK_LOWER_CASE	Lowercase required. If user enters an uppercase letter, it will be automatically
 * converted to the corresponding lowercase letter.
 * <li>Any other undefined chars	CONVERSION_MASK_IGNORE	No conversion
 * </ul>
 * <p>
 * {@code RequiredMask}
 * <p>
 * The RequiredMask is to indicate whether the character on a particular position is required. It is again optional. It
 * can be set using setRequiredMask(String mask). If not set, a valid non-space character is required on all the
 * positions. If you ever set the mask, please make sure they have the same length as the InputMask, and have a valid
 * required mask character at the exact position where there is an InputMask character.
 * <ul>
 * <li>R	REQUIRED_MASK_REQUIRED	Required. Users must enter a valid character that matches with the mask on this
 * position.
 * <li>Any other undefined chars	REQUIRED_MASK_NOT_REQUIRED	Not required. User can enter a space at this position.
 * </ul>
 */
@SuppressWarnings({"Convert2Lambda", "SpellCheckingInspection"})
public class MaskTextField extends TextField /*implements DecorationSupport */ {
    private static final String STYLE_CLASS_DEFAULT = "mask-combo-box"; //NON-NLS

    public static final char INPUT_MASK_LETTER = 'A';
    public static final char INPUT_MASK_DIGIT_OR_LETTER = 'N';
    public static final char INPUT_MASK_ANY_NON_SPACE = 'X';
    public static final char INPUT_MASK_HAX = 'H';
    public static final char INPUT_MASK_DIGIT_NON_ZERO = 'D';
    public static final char INPUT_MASK_DIGIT = '9';
    public static final char INPUT_MASK_DIGIT_0_TO_8 = '8';
    public static final char INPUT_MASK_DIGIT_0_TO_7 = '7';
    public static final char INPUT_MASK_DIGIT_0_TO_6 = '6';
    public static final char INPUT_MASK_DIGIT_0_TO_5 = '5';
    public static final char INPUT_MASK_DIGIT_0_TO_4 = '4';
    public static final char INPUT_MASK_DIGIT_0_TO_3 = '3';
    public static final char INPUT_MASK_DIGIT_0_TO_2 = '2';
    public static final char INPUT_MASK_DIGIT_0_TO_1 = '1';
    public static final char INPUT_MASK_DIGIT_ZERO = '0';

    public static final char CONVERSION_MASK_UPPER_CASE = 'U';
    public static final char CONVERSION_MASK_LOWER_CASE = 'L';
    public static final char CONVERSION_MASK_IGNORE = '_';

    public static final char REQUIRED_MASK_REQUIRED = 'R';
    public static final char REQUIRED_MASK_NOT_REQUIRED = '_';

    private StringProperty _inputMaskProperty;
    private StringProperty _requiredMaskProperty;
    private StringProperty _conversionMaskProperty;
    private StringProperty _validCharactersProperty;
    private StringProperty _invalidCharactersProperty;
    private ObjectProperty<Character> _placeholderCharacterProperty;
    private StringProperty _initialTextProperty;
    private BooleanProperty _autoAdvanceProperty;

    private BooleanProperty _clearbuttonVisibleProperty;

    private Decorator<Button> _clearButtonDecorator;

    private ObservableMap<Character, Callback<Character, Boolean>> _maskVerifiers;
    private ObservableMap<Character, Callback<Character, Character>> _conversions;
    private String _fixedText;

    public MaskTextField() {
        initializeTextField();
        initializeStyle();
        registerListeners();
    }

    public MaskTextField(String text) {
        super(text);
        initializeTextField();
        initializeStyle();
        registerListeners();
    }

    /**
     * Adds or removes style from the getStyleClass. Subclass should call super if you want to keep the existing
     * styles.
     */
    protected void initializeStyle() {
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    /**
     * Initializes the text field. We will initialize the inputMaskVerifiers and conversions in this method.
     */
    protected void initializeTextField() {
        initializeInputMaskVerifiers();
        initializeConversions();
//        setContentFilter(new Callback<ContentChange, ContentChange>() {
//            @Override
//            public ContentChange call(ContentChange param) {
//                if (getInputMask() != null) {
//                    int start = param.getStart();
//                    int caret = start;
//                    int end = param.getEnd(); // could be adjusted
//
//                    int totalLength = getInputMask().length();
//
//                    StringBuilder newText = new StringBuilder(); // buffer for new text. we will append one by one after verify and convert it
//
//                    String text = param.getText(); // input text in this ContentChange
//                    int index = 0;
//                    while (caret < totalLength) {
//                        if (index >= text.length())
//                            break;
//                        char c = text.charAt(index); // get the char from the text.
//                        if (verifyChar(c, caret) || c == getPlaceholderCharacter()) {
//                            newText.append(convert(c, caret));
//                            // TODO: replace by default unless it is at the end
//                            index++; // move to the next char in the text
//                            caret++;
//                        }
//                        else {
//                            // auto-advance
//                            if (isAutoAdvance() && (caret < getFixedText().length() && getFixedText().charAt(caret) == c)) { // typed a fixed text
//                                newText.append(c);
//                                caret++;
//                            }
//                            else if (isAutoAdvance() && (caret < getText().length() && getText().charAt(caret) != getPlaceholderCharacter() && getFixedText().contains("" + getText().charAt(caret)))) { // fixed text already there
//                                newText.append(getText().charAt(caret));
//                                caret++;
//                            }
//                            else if (isAutoAdvance() && (caret < getFixedText().length() && getFixedText().charAt(caret) != getPlaceholderCharacter())) { // next one is a fixed text so we automatically append the fixed text and continue
//                                newText.append(getFixedText().charAt(caret));
//                                caret++;
//                            }
//                            else {
//                                break;
//                            }
//                        }
//                    }
//                    // only break when there is no new text and we are done with the count that we are expected to do
//                    // if there are still new text, we will continue even when the count is done
//                    String s = newText.toString();
//                    if (s.length() > 0) {
//                        param.setText(s);
//                        param.setEnd(Math.min(caret, getText().length()));
//                        param.setNewAnchor(caret);
//                        param.setNewCaretPosition(caret);
//                    }
//                    else {
//                        String existingText = getText(start, end);
//                        param.setText(keepFixedText(existingText));
//                        param.setNewAnchor(end);
//                        param.setNewCaretPosition(end);
//                    }
//                }
//                return param;
//            }
//        });
    }

    @SuppressWarnings("Convert2MethodRef")
    protected void initializeInputMaskVerifiers() {
        Map<Character, Callback<Character, Boolean>> maskVerifiers = getInputMaskVerifiers();
        maskVerifiers.clear();
        maskVerifiers.put(INPUT_MASK_LETTER, c -> Character.isLetter(c));
        maskVerifiers.put(INPUT_MASK_DIGIT_OR_LETTER, c -> Character.isLetterOrDigit(c));
        maskVerifiers.put(INPUT_MASK_ANY_NON_SPACE, c -> !Character.isSpaceChar(c));
        maskVerifiers.put(INPUT_MASK_HAX, c -> ((c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || Character.isDigit(c)));
        maskVerifiers.put(INPUT_MASK_DIGIT_NON_ZERO, c -> Character.isDigit(c) && c != '0');
        maskVerifiers.put(INPUT_MASK_DIGIT, c -> Character.isDigit(c));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_8, c -> (c >= '0' && c <= '8'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_7, c -> (c >= '0' && c <= '7'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_6, c -> (c >= '0' && c <= '6'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_5, c -> (c >= '0' && c <= '5'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_4, c -> (c >= '0' && c <= '4'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_3, c -> (c >= '0' && c <= '3'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_2, c -> (c >= '0' && c <= '2'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_1, c -> (c >= '0' && c <= '1'));
        maskVerifiers.put(INPUT_MASK_DIGIT_ZERO, c -> (c == '0'));
    }

    /**
     * Gets the verifiers for the InputMask. The verifier is a callback which will verify the input char and return true
     * or false. True means the input char is valid at the position, false means invalid.
     *
     * @return the verifiers for the InputMask.
     */
    public ObservableMap<Character, Callback<Character, Boolean>> getInputMaskVerifiers() {
        if (_maskVerifiers == null) {
            _maskVerifiers = FXCollections.observableHashMap();
        }
        return _maskVerifiers;
    }

    @SuppressWarnings("Convert2MethodRef")
    protected void initializeConversions() {
        Map<Character, Callback<Character, Character>> conversions = getConversions();
        conversions.clear();
        conversions.put(CONVERSION_MASK_UPPER_CASE, c -> Character.toUpperCase(c));
        conversions.put(CONVERSION_MASK_LOWER_CASE, c -> Character.toLowerCase(c));
    }

    /**
     * Gets the conversions. The conversion is a callback which will convert an input char to another char.
     *
     * @return the conversions.
     */
    public ObservableMap<Character, Callback<Character, Character>> getConversions() {
        if (_conversions == null) {
            _conversions = FXCollections.observableHashMap();
        }
        return _conversions;
    }

    protected void registerListeners() {
        InvalidationListener verifierListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                clear();
            }
        };
        getInputMaskVerifiers().addListener(verifierListener);

        editableProperty().addListener(new ChangeListener<Boolean>() {
            private final String comboBoxStyleClass = "combo-box-field"; //NON-NLS
            private final String textInputStyleClass = "text-input"; //NON-NLS

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    getStyleClass().remove(comboBoxStyleClass);
                    getStyleClass().add(textInputStyleClass);
                }
                else {
                    getStyleClass().add(comboBoxStyleClass);
                    getStyleClass().remove(textInputStyleClass);
                }
            }
        });
    }

    /**
     * Clears the text if any.
     */
    @Override
    public void clear() {
        setTextWithoutChecking(getInitialText() != null ? getInitialText() : getInitialTextFromMask());
    }

    boolean enforcing = true;

    private void setTextWithoutChecking(String text) {
        try {
            enforcing = false;
            setText(text);
        }
        finally {
            enforcing = true;
        }
    }

    private String getFixedText() {
        if (_fixedText == null) {
            _fixedText = getInitialTextFromMask();
        }
        return _fixedText;
    }

    private String getInitialTextFromMask() {
        String mask = getInputMask();
        Map<Character, Callback<Character, Boolean>> maskVerifiers = getInputMaskVerifiers();
        for (Character c : maskVerifiers.keySet()) {
            mask = mask.replace(c, getPlaceholderCharacter());
        }
        _fixedText = mask;
        return mask;
    }

    public ObjectProperty<Character> placeholderCharacterProperty() {
        if (_placeholderCharacterProperty == null) {
            _placeholderCharacterProperty = new SimpleObjectProperty<Character>(this, "placeholderCharacter", ' ') { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    clear();
                }
            };
        }
        return _placeholderCharacterProperty;
    }

    /**
     * Gets the placeholder character. This is used fill in the spaces for the masks when the text is not entered yet.
     * By default, we will use whitespaces.
     *
     * @return the placeholder character.
     */
    public char getPlaceholderCharacter() {
        return placeholderCharacterProperty().get();
    }

    /**
     * Sets the placeholder character.
     *
     * @param placeholderCharacter a new placeholder character.
     */
    public void setPlaceholderCharacter(char placeholderCharacter) {
        placeholderCharacterProperty().set(placeholderCharacter);
    }

    public StringProperty inputMaskProperty() {
        if (_inputMaskProperty == null) {
            _inputMaskProperty = new SimpleStringProperty(this, "inputMask") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    clear();
                }
            };
        }
        return _inputMaskProperty;
    }

    /**
     * Gets the input mask.
     *
     * @return the input mask.
     */
    public String getInputMask() {
        return inputMaskProperty().get();
    }

    /**
     * Sets the input mask.
     *
     * @param inputMask a new input mask
     */
    public void setInputMask(String inputMask) {
        inputMaskProperty().set(inputMask);
    }

    public StringProperty requiredMaskProperty() {
        if (_requiredMaskProperty == null) {
            _requiredMaskProperty = new SimpleStringProperty(this, "requiredMask"); //NON-NLS
        }
        return _requiredMaskProperty;
    }

    /**
     * Gets the required mask.
     *
     * @return the required mask.
     */
    public String getRequiredMask() {
        return requiredMaskProperty().get();
    }

    /**
     * Sets the required mask.
     *
     * @param requiredMask a new required mask
     */
    public void setRequiredMask(String requiredMask) {
        requiredMaskProperty().set(requiredMask);
    }

    public StringProperty conversionMaskProperty() {
        if (_conversionMaskProperty == null) {
            _conversionMaskProperty = new SimpleStringProperty(this, "conversionMask"); //NON-NLS
        }
        return _conversionMaskProperty;
    }

    /**
     * Gets the conversion mask.
     *
     * @return the conversion mask.
     */
    public String getConversionMask() {
        return conversionMaskProperty().get();
    }

    /**
     * Sets the conversion mask.
     *
     * @param mask a new conversion mask
     */
    public void setConversionMask(String mask) {
        conversionMaskProperty().set(mask);
    }

    public StringProperty validCharactersProperty() {
        if (_validCharactersProperty == null) {
            _validCharactersProperty = new SimpleStringProperty(this, "validCharacters"); //NON-NLS
        }
        return _validCharactersProperty;
    }

    /**
     * Gets the valid characters.
     *
     * @return the valid characters.
     */
    public String getValidCharacters() {
        return validCharactersProperty().get();
    }

    /**
     * Sets the valid characters.
     *
     * @param validCharacters a new set of valid characters
     */
    public void setValidCharacters(String validCharacters) {
        validCharactersProperty().set(validCharacters);
    }

    public StringProperty invalidCharactersProperty() {
        if (_invalidCharactersProperty == null) {
            _invalidCharactersProperty = new SimpleStringProperty(this, "invalidCharacters"); //NON-NLS
        }
        return _invalidCharactersProperty;
    }

    /**
     * Gets the invalid characters.
     *
     * @return the invalid characters.
     */
    public String getInvalidCharacters() {
        return invalidCharactersProperty().get();
    }

    /**
     * Sets the invalid characters.
     *
     * @param invalidCharacters a new set of invalid characters
     */
    public void setInvalidCharacters(String invalidCharacters) {
        invalidCharactersProperty().set(invalidCharacters);
    }

    public StringProperty initialTextProperty() {
        if (_initialTextProperty == null) {
            _initialTextProperty = new SimpleStringProperty(this, "initialText") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    clear();
                }
            };
        }
        return _initialTextProperty;
    }

    /**
     * Gets the initial text. It will be used at the initial text on the field if setText was never called.
     *
     * @return the initial text.
     */
    public String getInitialText() {
        return initialTextProperty().get();
    }

    /**
     * Sets the initial text.
     *
     * @param mask the initial text.
     */
    public void setInitialText(String mask) {
        initialTextProperty().set(mask);
    }

    public BooleanProperty autoAdvanceProperty() {
        if (_autoAdvanceProperty == null) {
            _autoAdvanceProperty = new SimpleBooleanProperty(this, "autoAdvance", true); //NON-NLS
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

    private String keepFixedText(String text) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String s = text.substring(i, i + 1);
            if (getFixedText().contains(s)) {
                buf.append(s);
            }
            else {
                buf.append(getPlaceholderCharacter());
            }
        }
        return buf.toString();
    }

    private char convert(char c, int index) {
        String conversionMask = getConversionMask();
        if (Character.isSpaceChar(c) && getInputMaskVerifiers().containsKey(getInputMask().charAt(index)))
            return getPlaceholderCharacter();
        if (conversionMask != null && index < conversionMask.length()) {
            Callback<Character, Character> callback = getConversions().get(conversionMask.charAt(index));
            if (callback != null) {
                return callback.call(c);
            }
        }
        return c;
    }

    private boolean verifyChar(char c, int index) {
        String inputMask = getInputMask();
        if (inputMask != null && index < inputMask.length()) {
            // if it is a space, accept it if the requiredMask is defined and it said not required
            char maskChar = inputMask.charAt(index);
            if (maskChar == c) return true;
            if (Character.isSpaceChar(c) || c == getPlaceholderCharacter()) {
                String requiredMask = getRequiredMask();
                if (requiredMask != null && index < requiredMask.length()) {
                    return requiredMask.charAt(index) == REQUIRED_MASK_NOT_REQUIRED;
                }
            }
            Callback<Character, Boolean> callback = getInputMaskVerifiers().get(maskChar);
            return (getValidCharacters() == null || getValidCharacters().trim().isEmpty() || getValidCharacters().contains("" + convert(c, index)))
                    && (getInvalidCharacters() == null || getInvalidCharacters().trim().isEmpty() || !getInvalidCharacters().contains("" + convert(c, index)))
                    && (callback != null ? callback.call(convert(c, index)) : maskChar == convert(c, index));
        }
        else return false;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (getInputMask() != null) {
            int index = 0;
            int count = end - start;
            int caret = getCaretPosition();

            String existingText = getText();
            String deletedText = existingText.substring(start, end);
            String newText = keepFixedText(deletedText);
            super.replaceText(start, end, newText);

            while (start < getInputMask().length()) {
                if (index >= text.length())
                    break;
                char c = text.charAt(index); // get the char from the text.
                if (verifyChar(c, start)) {
                    if (getText().length() > start) { // in case it is at the end of the text
                        super.replaceText(start, start + 1, "" + convert(c, start));
                    }
                    else {
                        super.replaceText(start, start, "" + convert(c, start));
                    }
                    index++; // move to the next char in the text
                    start++; // move to the next caret position
                    count--; // counting how many char we replaced and we just did one char here.
                }
                else {
                    // auto-advance
                    if (isAutoAdvance() && (getFixedText().contains("" + c) ||
                            (caret < existingText.length() && existingText.charAt(caret) != getPlaceholderCharacter() && getFixedText().contains("" + existingText.charAt(caret))))) {
                        start++; // move to the next caret position
                        caret++;
                        selectRange(caret, caret);
                    }
                    else {
                        break;
                    }
                }
                // only break when there is no new text and we are done with the count that we are expected to do
                // if there are still new text, we will continue even when the count is done
                if (count <= 0 && index >= text.length()) {
                    break;
                }
            }
        }
        else {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        IndexRange range = getSelection();
        if (getInputMask() != null) {
            String newText = keepFixedText(getSelectedText());
            super.replaceText(range.getStart(), range.getEnd(), newText);
            selectRange(range.getStart(), range.getStart());
            for (int i = 0; i < replacement.length(); i++) {
                int start = getCaretPosition();
                replaceText(start, start, "" + replacement.charAt(i));
            }
        }
        else {
            super.replaceSelection(replacement);
        }
    }

// 5/10/2013 decided to remove this example as MaskFormatter is a Swing class. Don't want to introduce any unnecessary dependency on Swing

//    /**
//     * Configure the MaskTextField based on the information on the MaskFormatter. If you already have a MaskFormatter,
//     * you can use this method to automatically configure the MaskTextField. The items we will read from the
//     * MaskFormatter and set to the MaskTextField are InputMask, ConversionMask, Validcharacters, InvalidCharacters, and
//     * PlceholderCharacter.
//     * <p>
//     * Please note, in the current release, the escape char "'" that could be used by the MaskFormatter is not
//     * supported.
//     *
//     * @param formatter the MaskFormatter.
//     */
//    public void fromMaskFormatter(MaskFormatter formatter) {
//        String mask = formatter.getMask();
//        String inputMask = mask;
//        inputMask = inputMask.replace('#', '9')
//                .replace('A', 'N')
//                .replace('U', 'A')
//                .replace('L', 'A')
//                .replace('?', 'A')
//                .replace('*', 'X')
////                .replace('H', 'H') // same
//        ;
//        setInputMask(inputMask);
//        String conversionMask = mask;
//        conversionMask = conversionMask.replace('#', '_')
//                .replace('A', '_')
////                .replace('U', 'U') // same
////                .replace('L', 'L') // same
//                .replace('?', '_')
//                .replace('*', '_')
//                .replace('H', '_')
//        ;
//        setConversionMask(conversionMask);
//        setValidCharacters(formatter.getValidCharacters());
//        setInvalidCharacters(formatter.getInvalidCharacters());
//        setPlaceholderCharacter(formatter.getPlaceholderCharacter());
//    }

    /**
     * A static method which will create a MaskTextField for SSN (social security number, a 9-digit id number used in
     * United States for tax purposes). The InputMask for it is "999-99-9999". The ConversionMask and the RequiredMask
     * are not set which means no conversion and all positions are required.
     *
     * @return a MaskTextField for SSN.
     */
    public static MaskTextField createSSNField() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("999-99-9999");
        field.setPlaceholderCharacter('#');
        return field;
    }

    /**
     * A static method which will create a MaskTextField for Phone Number in US and Canada. The InputMask for it is
     * "(999) 999-9999". The ConversionMask and the RequiredMask are not set which means no conversion and all positions
     * are required.
     *
     * @return a MaskTextField for a Phone Number in US and Canada.
     */
    public static MaskTextField createPhoneNumberField() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("(999) 999-9999");
        return field;
    }

    /**
     * A static method which will create a MaskTextField for the Zip Code in US. The InputMask for it is "99999". The
     * ConversionMask and the RequiredMask are not set which means no conversion and all positions are required.
     *
     * @return a MaskTextField for a Phone Number in US and Canada.
     */
    public static MaskTextField createZipCodeField() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("99999");
        return field;
    }

    /**
     * A static method which will create a MaskTextField for the Zip Code + 4 in US. The InputMask for it is
     * "99999-9999". The ConversionMask and the RequiredMask are not set which means no conversion and all positions are
     * required.
     *
     * @return a MaskTextField for a Phone Number in US and Canada.
     */
    public static MaskTextField createZipCodePlus4Field() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("99999-9999");
        return field;
    }

    /**
     * A static method which will create a MaskTextField for the Zip Code + 4 in US. The InputMask for it is
     * "99999-9999". The ConversionMask and the RequiredMask are not set which means no conversion and all positions are
     * required.
     *
     * @return a MaskTextField for a Phone Number in US and Canada.
     */
    public static MaskTextField createCanadaPostalCodeField() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("A9A 9A9"); //NON-NLS
        field.setConversionMask("U_U _U_"); //NON-NLS
        field.setPlaceholderCharacter('_');
        field.setAutoAdvance(false);
        return field;
    }

    /**
     * A static method which will create a MaskTextField for serial number. This particular one is commonly used by
     * Microsoft's recent products. The InputMask for it is "NNNNN-NNNNN-NNNNN-NNNNN-NNNNN". The ConversionMask is
     * "UUUUU_UUUUU_UUUUU_UUUUU_UUUUU". The RequiredMask are not set which means all positions are required.
     *
     * @return a MaskTextField for a serial number.
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static MaskTextField createSerialNumberField() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("NNNNN-NNNNN-NNNNN-NNNNN-NNNNN"); //NON-NLS
        field.setConversionMask("UUUUU_UUUUU_UUUUU_UUUUU_UUUUU"); //NON-NLS
        return field;
    }

    /**
     * A static method which will create a MaskTextField for IPv6 address. The InputMask for it is
     * "HHHH:HHHH:HHHH:HHHH:HHHH:HHHH:HHHH:HHHH". The ConversionMask is "LLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL" so
     * that only lower case letters will be used, although according to the standard, the case shouldn't matter. The
     * RequiredMask are not set which means all positions are required.
     *
     * @return a MaskTextField for IPv6 address.
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static MaskTextField createIPv6Field() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("HHHH:HHHH:HHHH:HHHH:HHHH:HHHH:HHHH:HHHH"); //NON-NLS
        field.setConversionMask("LLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL"); //NON-NLS
        return field;
    }

    /**
     * A static method which will create a MaskTextField for MAC address. The InputMask for it is "HH:HH:HH:HH:HH:HH".
     * The ConversionMask is "LL:LL:LL:LL:LL:LL" so that only lower case letters will be used, although according to the
     * standard, the case shouldn't matter. The RequiredMask are not set which means all positions are required.
     *
     * @return a MaskTextField for MAC address.
     */
    public static MaskTextField createMacAddressField() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("HH:HH:HH:HH:HH:HH"); //NON-NLS
        field.setConversionMask("LL:LL:LL:LL:LL:LL"); //NON-NLS
        return field;
    }

    /**
     * A static method which will create a MaskTextField for the Date in US standard format (in the order of
     * month/day/year). The InputMask for it is "19/39/9999". The ConversionMask is "_x/_y/____" and we defined
     * conversion callbacks for both 'x' and 'y' to make sure the month is between 1 and 12, the day of the month is
     * between 1 and 31.
     * <p/>
     * The initial text was set to the today's date.
     *
     * @return a MaskTextField for Date.
     */
    public static MaskTextField createDateField() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("19/39/9999");

        field.setConversionMask("_x/_y/____"); //NON-NLS
        field.getConversions().put('x', new Callback<Character, Character>() {
            @Override
            public Character call(Character param) {
                char c = field.getText().charAt(0);
                if (c == '1' && param > '2') {
                    return '2';
                }
                return param;
            }
        });
        field.getConversions().put('y', new Callback<Character, Character>() {
            @Override
            public Character call(Character param) {
                char c = field.getText().charAt(3);
                if (c == '3' && param > '1') {
                    return '1';
                }
                return param;
            }
        });

        field.setInitialText(new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()));
        return field;
    }

    /**
     * A static method which will create a MaskTextField for the Time in the 12-hour format (in the order of
     * hour/minute/second AM/PM). The InputMask for it is "19:59:59 xm". The InputMask 'x' and 'm' were defined to make
     * sure the entered letter is 'A' or 'P', and 'M' respectively. The ConversionMask is "_x:__:__ UU" and we defined
     * conversion callbacks for the conversion mask 'x' to make sure the hour is between 1 and 12.
     * <p/>
     * The initial text was set to the current time.
     *
     * @return a MaskTextField for Date.
     */
    public static MaskTextField createTime12Field() {
        MaskTextField field = new MaskTextField();

        field.setInputMask("19:59:59 xm"); //NON-NLS
        field.getInputMaskVerifiers().put('x', new Callback<Character, Boolean>() {
            @Override
            public Boolean call(Character c) {
                return c == 'A' || c == 'P';
            }
        });
        field.getInputMaskVerifiers().put('m', new Callback<Character, Boolean>() {
            @Override
            public Boolean call(Character c) {
                return c == 'M';
            }
        });

        field.setConversionMask("_x:__:__ UU"); //NON-NLS
        field.getConversions().put('x', new Callback<Character, Character>() {
            @Override
            public Character call(Character c) {
                char firstChar = field.getText().charAt(0);
                if (firstChar == '1' && c > '2') {
                    return '2';
                }
                if (firstChar == '0' && c == '0') {
                    return '1';
                }
                return c;
            }
        });

        field.setInitialText(new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime()));

        return field;
    }

    /**
     * A static method which will create a MaskTextField for the Time in the 24-hour format (in the order of
     * hour/minute/second). The InputMask for it is "29/59/59". The ConversionMask is "_x/__/__" and we defined
     * conversion callbacks for the conversion mask named 'x' to make sure the hour is between 0 and 23.
     * <p/>
     * The initial text was set to the current time.
     *
     * @return a MaskTextField for Date.
     */
    public static MaskTextField createTime24Field() {
        MaskTextField field = new MaskTextField();
        field.setInputMask("29:59:59");

        field.setConversionMask("_x:__:__"); //NON-NLS
        field.getConversions().put('x', new Callback<Character, Character>() {
            @Override
            public Character call(Character c) {
                char firstChar = field.getText().charAt(0);
                if (firstChar == '2' && c > '3') {
                    return '3';
                }
                return c;
            }
        });

        field.setInitialText(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
        return field;
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

    public BooleanProperty clearButtonVisibleProperty() {
        if (_clearbuttonVisibleProperty == null) {
            _clearbuttonVisibleProperty = new SimpleBooleanProperty(this, "clearButtonVisible") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    if (get()) {
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

//    // For decoration
//    @Override
//    protected void layoutChildren() {
//        prepareDecorations();
//        super.layoutChildren();
//        Platform.runLater(this::layoutDecorations);
//    }
//
//    private DecorationDelegate _operator;
//
//    public void prepareDecorations() {
//        if (_operator == null) {
//            _operator = new DecorationDelegate(this);
//        }
//        _operator.prepareDecorations();
//    }
//
//    public void layoutDecorations() {
//        _operator.layoutDecorations();
//    }
//
//    // For interface DecorationSupport
//    @Override
//    public ObservableList<Node> getChildren() {
//        return super.getChildren();
//    }
//
//    @Override
//    public void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, HPos halignment, VPos valignment) {
//        super.positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, halignment, valignment);
//    }
//
//    // end decoration
}

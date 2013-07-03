/*
 * @(#)BaseDatePopupContent.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field.popup;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;

/**
 * The full content for a date picker or a date combobox. It was copied from DatePickerContent and modified to allow the
 * data type to be LocalDate, Date or Calendar as long as it can represent a date. Internally it uses the LocalDate to
 * draw the day grids. Two methods - {@link #toLocalDate(Object)} and {@link #fromLocalDate(LocalDate)} can be
 * implemented to provide conversions between the LocalDate and the actual data type.
 */
public abstract class AbstractDatePopupContent<T> extends VBox implements PopupContent<T> {
    private static final String STYLE_CLASS_DEFAULT = "popup-content"; //NON-NLS

    private Button backMonthButton;
    private Label monthLabel;
    private Label yearLabel;
    protected GridPane gridPane;

    private int daysPerWeek;
    private List<DateCell> dayNameCells = new ArrayList<>();
    private List<DateCell> weekNumberCells = new ArrayList<>();
    protected List<DateCell> dayCells = new ArrayList<>();
    private LocalDate[] dayCellDates;

    final DateTimeFormatter monthFormatter =
            DateTimeFormatter.ofPattern("MMMM"); //NON-NLS

    final DateTimeFormatter monthFormatterSO =
            DateTimeFormatter.ofPattern("LLLL"); // Standalone month name NON-NLS

    final DateTimeFormatter yearFormatter =
            DateTimeFormatter.ofPattern("y");

    final DateTimeFormatter yearWithEraFormatter =
            DateTimeFormatter.ofPattern("GGGGy"); // For Japanese. What to use for others?? NON-NLS

    final DateTimeFormatter weekNumberFormatter =
            DateTimeFormatter.ofPattern("w"); //NON-NLS

    final DateTimeFormatter weekDayNameFormatter =
            DateTimeFormatter.ofPattern("ccc"); // Standalone day name NON-NLS

    final DateTimeFormatter dayCellFormatter =
            DateTimeFormatter.ofPattern("d"); //NON-NLS

    final ContextMenu contextMenu = new ContextMenu();

    static String getString(String key) {
        return ControlResources.getString("DatePicker." + key); //NON-NLS
    }

    public AbstractDatePopupContent() {
        getStylesheets().add(PopupContent.class.getResource("PopupContent.css").toExternalForm()); //NON-NLS
        getStyleClass().add(STYLE_CLASS_DEFAULT);
        getStyleClass().add("date-picker-popup"); //NON-NLS
        setEffect(null);

        daysPerWeek = getDaysPerWeek();

        contextMenu.getItems().addAll(
                new MenuItem(getString("contextMenu.showToday")) {{ //NON-NLS
                    setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            displayedYearMonth.set(YearMonth.now());
                        }
                    });
                }},
                new SeparatorMenuItem(),
                new CheckMenuItem(getString("contextMenu.showWeekNumbers")) {{ //NON-NLS
                    selectedProperty().bindBidirectional(showWeekNumbersProperty());
                }}
        );

        setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent me) {
                contextMenu.show(AbstractDatePopupContent.this, me.getScreenX(), me.getScreenY());
                me.consume();
            }
        });

        {
            updateDisplayedYearMonth();
        }

        displayedYearMonth.addListener(new ChangeListener<YearMonth>() {
            @Override
            public void changed(ObservableValue<? extends YearMonth> observable,
                                YearMonth oldValue, YearMonth newValue) {
                updateValues();
            }
        });


        getChildren().add(createMonthYearPane());

        gridPane = new GridPane();
        gridPane.getStyleClass().add("calendar-grid"); //NON-NLS
        gridPane.setVgap(-1);
        gridPane.setHgap(-1);

        // get the weekday labels starting with the weekday that is the
        // first-day-of-the-week according to the locale in the
        // displayed LocalDate
        for (int i = 0; i < daysPerWeek; i++) {
            DateCell cell = new DateCell();
            cell.getStyleClass().add("day-name-cell"); //NON-NLS
            dayNameCells.add(cell);
        }

        // Week number column
        for (int i = 0; i < 6; i++) {
            DateCell cell = new DateCell();
            cell.getStyleClass().add("week-number-cell"); //NON-NLS
            weekNumberCells.add(cell);
        }

        createDayCells();
        updateGrid();
        getChildren().add(gridPane);

        refresh();
    }

    private ObjectProperty<YearMonth> displayedYearMonth =
            new SimpleObjectProperty<>(this, "displayedYearMonth"); //NON-NLS

    ObjectProperty<YearMonth> displayedYearMonthProperty() {
        return displayedYearMonth;
    }


    protected BorderPane createMonthYearPane() {
        BorderPane monthYearPane = new BorderPane();
        monthYearPane.getStyleClass().add("month-year-pane"); //NON-NLS

        // Month spinner

        HBox monthSpinner = new HBox();
        monthSpinner.getStyleClass().add("spinner"); //NON-NLS

        backMonthButton = new Button();
        backMonthButton.getStyleClass().add("left-button"); //NON-NLS

        Button forwardMonthButton = new Button();
        forwardMonthButton.getStyleClass().add("right-button"); //NON-NLS

        StackPane leftMonthArrow = new StackPane();
        leftMonthArrow.getStyleClass().add("left-arrow"); //NON-NLS
        leftMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        backMonthButton.setGraphic(leftMonthArrow);

        StackPane rightMonthArrow = new StackPane();
        rightMonthArrow.getStyleClass().add("right-arrow"); //NON-NLS
        rightMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        forwardMonthButton.setGraphic(rightMonthArrow);


        backMonthButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                displayedYearMonth.set(displayedYearMonth.get().minusMonths(1));
            }
        });

        monthLabel = new Label();
        monthLabel.getStyleClass().add("spinner-label"); //NON-NLS

        forwardMonthButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                displayedYearMonth.set(displayedYearMonth.get().plusMonths(1));
            }
        });

        monthSpinner.getChildren().addAll(backMonthButton, monthLabel, forwardMonthButton);
        monthYearPane.setLeft(monthSpinner);

        // Year spinner

        HBox yearSpinner = new HBox();
        yearSpinner.getStyleClass().add("spinner"); //NON-NLS

        Button backYearButton = new Button();
        backYearButton.getStyleClass().add("left-button"); //NON-NLS

        Button forwardYearButton = new Button();
        forwardYearButton.getStyleClass().add("right-button"); //NON-NLS

        StackPane leftYearArrow = new StackPane();
        leftYearArrow.getStyleClass().add("left-arrow"); //NON-NLS
        leftYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        backYearButton.setGraphic(leftYearArrow);

        StackPane rightYearArrow = new StackPane();
        rightYearArrow.getStyleClass().add("right-arrow"); //NON-NLS
        rightYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        forwardYearButton.setGraphic(rightYearArrow);


        backYearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                displayedYearMonth.set(displayedYearMonth.get().minusYears(1));
            }
        });

        yearLabel = new Label();
        yearLabel.getStyleClass().add("spinner-label"); //NON-NLS

        forwardYearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                displayedYearMonth.set(displayedYearMonth.get().plusYears(1));
            }
        });


        yearSpinner.getChildren().addAll(backYearButton, yearLabel, forwardYearButton);
        yearSpinner.setFillHeight(false);
        monthYearPane.setRight(yearSpinner);

        return monthYearPane;
    }

    private void refresh() {
        updateMonthLabelWidth();
        updateDayNameCells();
        updateValues();
    }

    void updateValues() {
        // Note: Preserve this order, as DatePickerHijrahContent needs
        // updateDayCells before updateMonthYearPane().
        updateWeeknumberDateCells();
        updateDayCells();
        updateMonthYearPane();
    }

    void updateGrid() {
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();

        int nCols = daysPerWeek + (isShowWeekNumbers() ? 1 : 0);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100); // Treated as weight
        for (int i = 0; i < nCols; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < daysPerWeek; i++) {
            gridPane.add(dayNameCells.get(i), i + nCols - daysPerWeek, 1);  // col, row
        }

        // Week number column
        if (isShowWeekNumbers()) {
            for (int i = 0; i < 6; i++) {
                gridPane.add(weekNumberCells.get(i), 0, i + 2);  // col, row
            }
        }

        // setup: 6 rows of daysPerWeek (which is the maximum number of cells required in the worst case layout)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < daysPerWeek; col++) {
                gridPane.add(dayCells.get(row * daysPerWeek + col), col + nCols - daysPerWeek, row + 2);
            }
        }
    }

    void updateDayNameCells() {
        // first day of week, 1 = monday, 7 = sunday
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();

        // july 13th 2009 is a Monday, so a firstDayOfWeek=1 must come out of the 13th
        LocalDate date = LocalDate.of(2009, 7, 12 + firstDayOfWeek);
        for (int i = 0; i < daysPerWeek; i++) {
            String name = weekDayNameFormatter.withLocale(getLocale()).format(date.plus(i, DAYS));
            dayNameCells.get(i).setText(titleCaseWord(name));
        }
    }

    void updateWeeknumberDateCells() {
        if (isShowWeekNumbers()) {
            Locale locale = getLocale();
            int maxWeeksPerMonth = 6; // TODO: Get this from chronology?

            LocalDate firstOfMonth = displayedYearMonth.get().atDay(1);
            for (int i = 0; i < maxWeeksPerMonth; i++) {
                LocalDate date = firstOfMonth.plus(i, WEEKS);
                // Use a formatter to ensure correct localization,
                // such as when Thai numerals are required.
                String cellText =
                        weekNumberFormatter.withLocale(locale)
//                                        .withSymbols(DateTimeFormatSymbols.of(locale))
                                .format(date);
                weekNumberCells.get(i).setText(cellText);
            }
        }
    }

    private void updateDisplayedYearMonth() {
        LocalDate date = toLocalDate(getValue());
        displayedYearMonth.set((date != null) ? YearMonth.from(date) : YearMonth.now());
    }

    void updateDayCells() {
        Locale locale = getLocale();
        Chronology chrono = getChronology();
        int firstOfMonthIdx = determineFirstOfMonthDayOfWeek();
        YearMonth curMonth = displayedYearMonth.get();
        YearMonth prevMonth = curMonth.minusMonths(1);
        YearMonth nextMonth = curMonth.plusMonths(1);
        int daysInCurMonth = determineDaysInMonth(curMonth);
        int daysInPrevMonth = determineDaysInMonth(prevMonth);
        int daysInNextMonth = determineDaysInMonth(nextMonth);

        for (int i = 0; i < 6 * daysPerWeek; i++) {
            DateCell dayCell = dayCells.get(i);
            dayCell.getStyleClass().setAll("cell", "day-cell"); //NON-NLS
            dayCell.setDisable(false);
            dayCell.setStyle(null);
            dayCell.setGraphic(null);
            dayCell.setTooltip(null);

            YearMonth month = curMonth;
            int day = i - firstOfMonthIdx + 1;
            //int index = firstOfMonthIdx + i - 1;
            if (i < firstOfMonthIdx) {
                month = prevMonth;
                day = i + daysInPrevMonth - firstOfMonthIdx + 1;
                dayCell.getStyleClass().add("previous-month"); //NON-NLS
            }
            else if (i >= firstOfMonthIdx + daysInCurMonth) {
                month = nextMonth;
                day = i - daysInCurMonth - firstOfMonthIdx + 1;
                dayCell.getStyleClass().add("next-month"); //NON-NLS
            }
            LocalDate date = month.atDay(day);
            dayCellDates[i] = date;
            ChronoLocalDate cDate = toChrono(date);

            if (isToday(date)) {
                dayCell.getStyleClass().add("today"); //NON-NLS
            }

            if (date.equals(toLocalDate(getValue()))) {
                dayCell.getStyleClass().add("selected"); //NON-NLS
            }

            String cellText =
                    dayCellFormatter.withLocale(locale)
                            .withChronology(chrono)
//                                 .withSymbols(DateTimeFormatSymbols.of(locale))
                            .format(cDate);
            dayCell.setText(cellText);

            dayCell.updateItem(date, false);
        }
    }

    private int getDaysPerWeek() {
        ValueRange range = getChronology().range(DAY_OF_WEEK);
        return (int) (range.getMaximum() - range.getMinimum() + 1);
    }

    private int getMonthsPerYear() {
        ValueRange range = getChronology().range(MONTH_OF_YEAR);
        return (int) (range.getMaximum() - range.getMinimum() + 1);
    }

    private void updateMonthLabelWidth() {
        if (monthLabel != null) {
            int monthsPerYear = getMonthsPerYear();
            double width = 0;
            for (int i = 0; i < monthsPerYear; i++) {
                YearMonth yearMonth = displayedYearMonth.get().withMonth(i + 1);
                String name = monthFormatterSO.withLocale(getLocale()).format(yearMonth);
                if (Character.isDigit(name.charAt(0))) {
                    // Fallback. The standalone format returned a number, so use standard format instead.
                    name = monthFormatter.withLocale(getLocale()).format(yearMonth);
                }
                width = Math.max(width, computeTextWidth(monthLabel.getFont(), name, 0));
            }
            monthLabel.setMinWidth(width);
        }
    }

    protected void updateMonthYearPane() {
        String str = formatMonth(displayedYearMonth.get());
        monthLabel.setText(str);

        str = formatYear(displayedYearMonth.get());
        yearLabel.setText(str);
        double width = computeTextWidth(yearLabel.getFont(), str, 0);
        if (width > yearLabel.getMinWidth()) {
            yearLabel.setMinWidth(width);
        }
    }

    private String formatMonth(YearMonth yearMonth) {
        Locale locale = getLocale();
        ChronoLocalDate cDate = toChrono(yearMonth.atDay(1));

        String str = monthFormatterSO.withLocale(getLocale())
                .withChronology(getChronology())
                .format(cDate);
        if (Character.isDigit(str.charAt(0))) {
            // Fallback. The standalone format returned a number, so use standard format instead.
            str = monthFormatter.withLocale(getLocale())
                    .withChronology(getChronology())
                    .format(cDate);
        }
        return titleCaseWord(str);
    }

    private String formatYear(YearMonth yearMonth) {
        Locale locale = getLocale();
        DateTimeFormatter formatter = yearFormatter;
        ChronoLocalDate cDate = toChrono(yearMonth.atDay(1));
        int era = cDate.getEra().getValue();
        int nEras = getChronology().eras().size();

        /*if (cDate.get(YEAR) < 0) {
            formatter = yearForNegYearFormatter;
        } else */
        if ((nEras == 2 && era == 0) || nEras > 2) {
            formatter = yearWithEraFormatter;
        }

        // Fixme: Format Japanese era names with Japanese text.
        String str = formatter.withLocale(getLocale())
                .withChronology(getChronology())
//                               .withSymbols(DateTimeFormatSymbols.of(getLocale()))
                .format(cDate);

        return str;
    }

    // Ensures that month and day names are titlecased (capitalized).
    private String titleCaseWord(String str) {
        if (!str.isEmpty()) {
            int firstChar = str.codePointAt(0);
            if (!Character.isTitleCase(firstChar)) {
                str = new String(new int[]{Character.toTitleCase(firstChar)}, 0, 1) +
                        str.substring(Character.offsetByCodePoints(str, 0, 1));
            }
        }
        return str;
    }


    /**
     * determine on which day of week idx the first of the months is
     */
    private int determineFirstOfMonthDayOfWeek() {
        // determine with which cell to start
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
        int firstOfMonthIdx = displayedYearMonth.get().atDay(1).getDayOfWeek().getValue() - firstDayOfWeek;
        if (firstOfMonthIdx < 0) {
            firstOfMonthIdx += daysPerWeek;
        }
        return firstOfMonthIdx;
    }

    private int determineDaysInMonth(YearMonth month) {
        return month.atDay(1).plusMonths(1).minusDays(1).getDayOfMonth();
    }

    private boolean isToday(LocalDate localDate) {
        return (localDate.equals(LocalDate.now()));
    }

    protected LocalDate dayCellDate(DateCell dateCell) {
        assert (dayCellDates != null);
        return dayCellDates[dayCells.indexOf(dateCell)];
    }

    private DateCell findDayCellForDate(LocalDate date) {
        for (int i = 0; i < dayCellDates.length; i++) {
            if (date.equals(dayCellDates[i])) {
                return dayCells.get(i);
            }
        }
        return dayCells.get(dayCells.size() / 2 + 1);
    }

    void clearFocus() {
        LocalDate focusDate = toLocalDate(getValue());
        if (focusDate == null) {
            focusDate = LocalDate.now();
        }
        if (YearMonth.from(focusDate).equals(displayedYearMonth.get())) {
            // focus date
            findDayCellForDate(focusDate).requestFocus();
        }
        else {
            // focus month spinner (should not happen)
            backMonthButton.requestFocus();
        }
    }

    protected void createDayCells() {
        EventHandler<MouseEvent> dayCellActionHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev) {
                DateCell dayCell = (DateCell) ev.getSource();
                LocalDate date = dayCellDate(dayCell);
                YearMonth yearMonth = YearMonth.from(date);

                if (yearMonth.equals(displayedYearMonth.get())) {
                    setValue(fromLocalDate(date));
                }
                else {
                    // previous or next month
                    displayedYearMonth.set(yearMonth);
                    findDayCellForDate(date).requestFocus();
                }
            }
        };

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < daysPerWeek; col++) {
                DateCell dayCell = createDayCell();
                dayCell.setOnMouseClicked(dayCellActionHandler);
                dayCells.add(dayCell);
            }
        }

        dayCellDates = new LocalDate[6 * daysPerWeek];
    }

    private DateCell createDayCell() {
        DateCell cell = null;
        if (getDayCellFactory() != null) {
            cell = getDayCellFactory().call(this);
        }
        if (cell == null) {
            cell = new DateCell();
        }

        return cell;
    }

    protected Locale getLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }

//    protected Chronology getChronology() {
//        return datePicker.getChronology();
//    }

    protected ChronoLocalDate toChrono(LocalDate date) {
        return getChronology().date(date);
    }

    // Added

    /**
     * A custom cell factory can be provided to customize individual day cells in the DatePicker popup. Refer to {@link
     * DateCell} and {@link Cell} for more information on cell factories.
     * Example:
     * <p>
     * <pre>{@code
     * final Callback&lt;DatePicker, DateCell&gt; dayCellFactory = new Callback&lt;DatePicker, DateCell&gt;() {
     *     public DateCell call(final DatePicker datePicker) {
     *         return new DateCell() {
     *             &#064;Override public void updateItem(LocalDate item, boolean empty) {
     *                 super.updateItem(item, empty);
     *                 if (MonthDay.from(item).equals(MonthDay.of(9, 25))) {
     *                     setTooltip(new Tooltip("Happy Birthday!"));
     *                     setStyle("-fx-background-color: #ff4444;");
     *                 }
     *                 if (item.equals(LocalDate.now().plusDays(1))) {
     *                     // Tomorrow is too soon.
     *                     setDisable(true);
     *                 }
     *             }
     *         };
     *     }
     * };
     * datePicker.setDayCellFactory(dayCellFactory);
     * }</pre>
     */
    private ObjectProperty<Callback<AbstractDatePopupContent, DateCell>> dayCellFactory;

    public final void setDayCellFactory(Callback<AbstractDatePopupContent, DateCell> value) {
        dayCellFactoryProperty().set(value);
    }

    public final Callback<AbstractDatePopupContent, DateCell> getDayCellFactory() {
        return (dayCellFactory != null) ? dayCellFactory.get() : null;
    }

    public final ObjectProperty<Callback<AbstractDatePopupContent, DateCell>> dayCellFactoryProperty() {
        if (dayCellFactory == null) {
            dayCellFactory = new SimpleObjectProperty<Callback<AbstractDatePopupContent, DateCell>>(this, "dayCellFactory") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    updateGrid();
                }
            };
        }
        return dayCellFactory;
    }

    /**
     * The calendar system used for parsing, displaying, and choosing dates in the DatePicker control.
     * <p>
     * <p>The default value is returned from a call to Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT)).
     * The default is usually {@link IsoChronology} unless provided explicitly in the {@link
     * Locale} by use of a Locale calendar extension.
     * <p>
     * Setting the value to {@code null} will restore the default chronology.
     */
    public final ObjectProperty<Chronology> chronologyProperty() {
        return chronology;
    }

    private ObjectProperty<Chronology> chronology =
            new SimpleObjectProperty<>(this, "chronology", null); //NON-NLS

    public final Chronology getChronology() {
        Chronology chrono = chronology.get();
        if (chrono == null) {
            try {
                chrono = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
            }
            catch (Exception ex) {
                System.err.println(ex);
            }
            if (chrono == null) {
                chrono = IsoChronology.INSTANCE;
            }
            //System.err.println(chrono);
        }
        return chrono;
    }

    public final void setChronology(Chronology value) {
        chronology.setValue(value);
    }


    /**
     * Whether the DatePicker popup should display a column showing week numbers.
     * <p>
     * <p>The default value is false unless otherwise defined in a resource bundle for the current locale.
     * <p>
     * <p>This property may be toggled by the end user by using a context menu in the DatePicker popup, so it is
     * recommended that applications save and restore the value between sessions.
     */
    public final BooleanProperty showWeekNumbersProperty() {
        if (showWeekNumbers == null) {
            boolean localizedDefault = "true".equals(ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers")); //NON-NLS
            showWeekNumbers = new SimpleBooleanProperty(localizedDefault) {
                @Override
                protected void invalidated() {
                    super.invalidated();
                    updateGrid();
                }
                //                @Override public CssMetaData getCssMetaData() {
//                    return StyleableProperties.SHOW_WEEK_NUMBERS;
//                }

                @Override
                public Object getBean() {
                    return AbstractDatePopupContent.this;
                }

                @Override
                public String getName() {
                    return "showWeekNumbers"; //NON-NLS
                }
            };
        }
        return showWeekNumbers;
    }

    private BooleanProperty showWeekNumbers;

    public final void setShowWeekNumbers(boolean value) {
        showWeekNumbersProperty().setValue(value);
    }

    public final boolean isShowWeekNumbers() {
        return showWeekNumbersProperty().getValue();
    }

// Copied from ComboBoxBase

    /**
     * The value of this ComboBox is defined as the selected item if the input is not editable, or if it is editable,
     * the most recent user action: either the value input by the user, or the last selected item.
     */
    @Override
    public final ObjectProperty<T> valueProperty() {
        return value;
    }

    private ObjectProperty<T> value = new SimpleObjectProperty<T>(this, "value") { //NON-NLS
        @Override
        protected void invalidated() {
            super.invalidated();
            updateDisplayedYearMonth();
            updateDayCells();
            clearFocus();
        }
    };

    @Override
    public final void setValue(T value) {
        valueProperty().set(value);
    }

    @Override
    public final T getValue() {
        return valueProperty().get();
    }

    /**
     * Subclass override this method to convert from the LocalDate to the value type that is being edited.
     */
    protected abstract T fromLocalDate(LocalDate localDate);

    /**
     * Subclass override this method to convert from the value type that is being edited to the LocalDate.
     */
    protected abstract LocalDate toLocalDate(T value);

    // Copied from Utils
    static final Text helper = new Text();
    static final double DEFAULT_WRAPPING_WIDTH = helper.getWrappingWidth();
    static final double DEFAULT_LINE_SPACING = helper.getLineSpacing();
    static final String DEFAULT_TEXT = helper.getText();

    static double computeTextWidth(Font font, String text, double wrappingWidth) {
        helper.setText(text);
        helper.setFont(font);
        // Note that the wrapping width needs to be set to zero before
        // getting the text's real preferred width.
        helper.setWrappingWidth(0);
        helper.setLineSpacing(0);
        double w = Math.min(helper.prefWidth(-1), wrappingWidth);
        helper.setWrappingWidth((int) Math.ceil(w));
        w = Math.ceil(helper.getLayoutBounds().getWidth());
        // RESTORE STATE
        helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
        helper.setLineSpacing(DEFAULT_LINE_SPACING);
        helper.setText(DEFAULT_TEXT);
        return w;
    }

}

/*
 * @(#)TableViewSearchable.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.searchable;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import jidefx.utils.CommonUtils;

/**
 * {@code TableSearchable} is an concrete implementation of {@link Searchable} that enables the search function in
 * TableView. <p>It's very simple to use it. Assuming you have a TableView, all you need to do is to call
 * <pre>{@code
 * TableView table = ....;
 * TableSearchable searchable = new TableSearchable(table);
 * }</pre>
 * Now the TableView will have the search function.
 * <p>
 * As TableView is a two dimension data, the search is a little different from ListView and TreeView which both have one
 * dimension data. So there is a little work you need to do in order to convert from two dimension data to one dimension
 * data. We use the selection mode to determine how to convert. There is a special property called mainIndex. You can
 * set it using setMainIndex(). If the TableView is in row selection mode, mainIndex will be the column that you want
 * search at. Please note you can change mainIndex at any time.
 * <p>
 * On the other hand, if the TableView is in column selection mode, mainIndex will be the row that you want search at.
 * There is one more case when cell selection is enabled. In this case, mainIndex will be ignore; all cells will be
 * searched.
 * <p>
 * In three cases above, the keys for find next and find previous are different too. In row selection mode, up/down
 * arrow are the keys. In column selection mode, left/right arrow are keys. In cell selection mode, both up and left
 * arrow are keys to find previous occurrence, both down and right arrow are keys to find next occurrence.
 * <p>
 * In addition, you might need to override convertElementToString() to provide you own algorithm to do the conversion.
 * <pre>{@code
 * TableView table = ....;
 * TableViewSearchable searchable = new TableViewSearchable(table) {
 *      protected String convertElementToString(Object object) {
 *          ...
 *      }
 * };
 * }</pre>
 * <p>
 * Additional customization can be done on the base Searchable class such as background and foreground color,
 * keystrokes, case sensitivity,
 */
@SuppressWarnings({"Convert2Lambda", "unchecked"})
public class TableViewSearchable<T> extends Searchable<Object> {

    private int[] _searchColumnIndices = {0};
    private ListChangeListener<T> _listChangeListener;
    private ChangeListener<ObservableList<T>> _itemsChangeListener;

    public TableViewSearchable(TableView<T> tableView) {
        super(tableView);
    }

    @Override
    public void installListeners() {
        super.installListeners();
        if (_node instanceof TableView) {
            if (_listChangeListener == null) {
                _listChangeListener = new ListChangeListener<T>() {
                    @Override
                    public void onChanged(Change<? extends T> c) {
                        hidePopup();
                    }
                };
            }
            ((TableView<T>) _node).getItems().addListener(_listChangeListener);
            if (_itemsChangeListener == null) {
                _itemsChangeListener = new ChangeListener<ObservableList<T>>() {
                    @Override
                    public void changed(ObservableValue<? extends ObservableList<T>> observable, ObservableList<T> oldValue, ObservableList<T> newValue) {
                        hidePopup();
                    }
                };
            }
            ((TableView<T>) _node).itemsProperty().addListener(_itemsChangeListener);
        }
    }

    @Override
    public void uninstallListeners() {
        if (_node instanceof TableView) {
            if (_listChangeListener != null) {
                ((TableView<T>) _node).getItems().removeListener(_listChangeListener);
                _listChangeListener = null;
            }
            if (_itemsChangeListener != null) {
                ((TableView<T>) _node).itemsProperty().removeListener(_itemsChangeListener);
                _itemsChangeListener = null;
            }
        }
        super.uninstallListeners();
    }

    @Override
    protected void setSelectedIndex(int index, boolean incremental) {
        int majorIndex, minorIndex;
        TableView<T> table = ((TableView<T>) _node);
        if (isColumnSelectionAllowed(table)) {
            minorIndex = index;
            majorIndex = getMainIndex();
            addTableSelection(table, majorIndex, minorIndex, incremental);
        }
        else if (isRowSelectionAllowed(table)) {
            majorIndex = index;
            minorIndex = getMainIndex();
            addTableSelection(table, majorIndex, minorIndex, incremental);
        }
        else { // cell selection allowed
            int columnCount = table.getColumns().size();
            if (columnCount == 0) {
                return;
            }
            majorIndex = index / columnCount;
            minorIndex = index % columnCount;
            addTableSelection(table, majorIndex, minorIndex, incremental);
        }
    }

    /**
     * Selects the cell at the specified row and column index. If incremental is true, the previous selection will not
     * be cleared.
     *
     * @param table       the table
     * @param rowIndex    the row index of the cell.
     * @param columnIndex the column index of the cell
     * @param incremental false to clear all previous selection. True to keep the previous selection.
     */
    protected void addTableSelection(TableView<T> table, int rowIndex, int columnIndex, boolean incremental) {
        if (!incremental)
            table.getSelectionModel().clearSelection();
        if (rowIndex >= 0 && columnIndex >= 0 && rowIndex < table.getItems().size() && columnIndex < table.getColumns().size()
                && !table.getSelectionModel().isSelected(rowIndex, table.getColumns().get(columnIndex))) {
            TableColumn<T, ?> column = table.getColumns().get(columnIndex);
            table.getSelectionModel().select(rowIndex, column);
            table.scrollTo(rowIndex);
            table.scrollToColumnIndex(columnIndex);
            table.getFocusModel().focus(rowIndex, column);
        }
    }

    /**
     * Is the column selection allowed?
     *
     * @param table the table.
     * @return true if the table is the column selection.
     */
    @SuppressWarnings("UnusedParameters")
    private boolean isColumnSelectionAllowed(TableView<T> table) {
        // NOTES: must sync with TableShrinkSearchableSupport#isColumnSelectionAllowed.
        return false;
    }

    /**
     * Is the row selection allowed?
     *
     * @param table the table.
     * @return true if the table is the row selection.
     */
    protected boolean isRowSelectionAllowed(TableView<T> table) {
        // NOTES: must sync with TableShrinkSearchableSupport#isRowSelectionAllowed.
        return getSearchColumnIndices().length == 1 && !table.getSelectionModel().isCellSelectionEnabled();
    }

    /**
     * Are we trying to search on multi-columns (but NOT all columns)?
     *
     * @return true if the search is set to look at multi-columns (but NOT all columns).
     */
    protected boolean isSearchSelectedRows() {
        return getSearchColumnIndices().length > 1;
    }

    /**
     * Gets the selected index.
     *
     * @return the selected index.
     */
    @Override
    protected int getSelectedIndex() {
        TableView<T> table = (TableView<T>) _node;
        if (isColumnSelectionAllowed(table)) {
            return getSelectedColumnIndex(table, table.getSelectionModel().getSelectedIndex());
        }
        else if (isRowSelectionAllowed(table)) {
            return table.getSelectionModel().getSelectedIndex();
        }
        else { // cell selection allowed
            return table.getSelectionModel().getSelectedIndex() * table.getColumns().size() + getSelectedColumnIndex(table, table.getSelectionModel().getSelectedIndex());
        }
    }

    private int getSelectedColumnIndex(TableView<T> table, int selectedIndex) {
        TablePosition cell = table.getFocusModel().getFocusedCell();
        if (cell != null) {
            return cell.getColumn();
        }
        for (int i = 0; i < table.getColumns().size(); i++) {
            TableColumn<T, ?> tableColumn = table.getColumns().get(i);
            boolean selected = table.getSelectionModel().isSelected(selectedIndex, tableColumn);
            if (selected) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected Object getElementAt(int index) {
        TableView<T> table = (TableView<T>) _node;
        if (isColumnSelectionAllowed(table)) { // column selection mode
            return getValueAt(table, getMainIndex(), index);
        }
        else if (isRowSelectionAllowed(table)) { // row selection mode
            return getValueAt(table, index, getMainIndex());
        }
        else if (isSearchSelectedRows()) { // search on multi columns
            int columnIndex = index % table.getColumns().size();
            boolean doNotSearch = true;
            for (int i : getSearchColumnIndices()) {
                if (i == columnIndex) {
                    doNotSearch = false;
                }
            }

            if (doNotSearch) {
                return null;
            }

            int rowIndex = index / table.getColumns().size();
            return getValueAt(table, rowIndex, columnIndex);
        }
        else { // cell selection allowed
            int columnIndex = index % table.getColumns().size();
            int rowIndex = index / table.getColumns().size();
            return getValueAt(table, rowIndex, columnIndex);
        }
    }

    /**
     * Get string value of the table.
     *
     * @param table       the TableView
     * @param rowIndex    the row index
     * @param columnIndex the column index
     * @return the string value of the cell in the table.
     */
    protected Object getValueAt(TableView table, int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < table.getItems().size() && columnIndex >= 0 && columnIndex < table.getColumns().size()) {
            return ((TableColumn) table.getColumns().get(columnIndex)).getCellObservableValue(rowIndex);
        }
        else {
            return null;
        }
    }

    @Override
    protected int getElementCount() {
        TableView<T> table = ((TableView<T>) _node);
        if (isColumnSelectionAllowed(table)) {
            return table.getColumns().size();
        }
        else if (isRowSelectionAllowed(table)) {
            return table.getItems().size();
        }
        else { // cell selection allowed
            return table.getColumns().size() * table.getItems().size();
        }
    }

    @Override
    protected String convertElementToString(Object item) {
        if (item instanceof Property) {
            Object value = ((Property) item).getValue();
            return value != null ? value.toString() : "";
        }
        else if (item != null) {
            return item.toString();
        }
        else {
            return "";
        }
    }

    /**
     * Gets the indexes of the column to be searched.
     *
     * @return the indexes of the column to be searched.
     */
    public int[] getSearchColumnIndices() {
        return _searchColumnIndices;
    }

    /**
     * Gets the index of the column to be searched.
     *
     * @return the index of the column to be searched.
     */
    public int getMainIndex() {
        if (_searchColumnIndices.length == 0) {
            return -1;
        }

        return _searchColumnIndices[0];
    }

    /**
     * Sets the main indexes. Main indexes are the columns index which you want to be searched.
     *
     * @param columnIndices the index of the columns to be searched. If empty, all columns will be searched.
     */
    public void setSearchColumnIndices(int[] columnIndices) {
        if (columnIndices == null) {
            columnIndices = new int[0];
        }

        int[] old = _searchColumnIndices;
        if (!CommonUtils.equals(old, columnIndices, true)) {
            _searchColumnIndices = columnIndices;
            hidePopup();
        }
    }

    /**
     * Sets the main index. Main index is the column index which you want to be searched.
     *
     * @param mainIndex the index of the column to be searched. If -1, all columns will be searched.
     */
    public void setMainIndex(int mainIndex) {
        int[] temp = {mainIndex};
        if (mainIndex < 0) {
            temp = new int[0];
        }
        int[] old = _searchColumnIndices;
        if (old != temp) {
            _searchColumnIndices = temp;
            hidePopup();
        }
    }

    @Override
    protected boolean isFindNextKey(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        TableView<T> table = ((TableView<T>) _node);
        if (isColumnSelectionAllowed(table)) {
            return keyCode == KeyCode.RIGHT;
        }
        else if (isRowSelectionAllowed(table)) {
            return keyCode == KeyCode.DOWN;
        }
        else { // cell selection allowed
            return keyCode == KeyCode.DOWN || keyCode == KeyCode.RIGHT;
        }
    }

    @Override
    protected boolean isFindPreviousKey(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        TableView<T> table = ((TableView<T>) _node);
        if (isColumnSelectionAllowed(table)) {
            return keyCode == KeyCode.LEFT;
        }
        else if (isRowSelectionAllowed(table)) {
            return keyCode == KeyCode.UP;
        }
        else { // cell selection allowed
            return keyCode == KeyCode.UP || keyCode == KeyCode.LEFT;
        }
    }

    @Override
    protected boolean isActivateKey(KeyEvent e) {
        boolean editable = isSelectedCellEditable();
        return !editable && super.isActivateKey(e);
    }

    /**
     * Checks if the selected cell is editable. If yes, we will not activate Searchable when key is typed.
     *
     * @return true if the selected cell is editable.
     */
    protected boolean isSelectedCellEditable() {
        return ((TableView<T>) _node).isEditable();
    }
}

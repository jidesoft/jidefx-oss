/*
 * @(#)FileIntelliHints.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.scene.control.hints;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputControl;
import javafx.util.Callback;

import java.io.File;
import java.io.FilenameFilter;

/**
 * {@code FileIntelliHints} is a concrete implementation of {@link IntelliHints}. It
 * allows user to type in a file patch quickly by providing them the hints based on what is existed on file system. You
 * can use {@link #setFolderOnly(boolean)} to control if the hints contain only the folders, or folders and files.
 */
@SuppressWarnings("Convert2Lambda")
public class FileIntelliHints extends AbstractListIntelliHints<String> {
    private boolean _folderOnly = false;
    private boolean _showFullPath = true;
    private FilenameFilter _filter;

    public FileIntelliHints(TextInputControl comp) {
        super(comp);
    }

    /**
     * If the hints contain the folder names only.
     *
     * @return true if the hints contain the folder names only.
     */
    public boolean isFolderOnly() {
        return _folderOnly;
    }

    /**
     * Sets the property of folder only. If true, the hints will only show the folder names. Otherwise, both folder and
     * file names will be shown in the hints.
     *
     * @param folderOnly only provide hints for the folders.
     */
    public void setFolderOnly(boolean folderOnly) {
        _folderOnly = folderOnly;
    }

    /**
     * If the hints contain the full path.
     *
     * @return true if the hints contain the full path.
     */
    public boolean isShowFullPath() {
        return _showFullPath;
    }

    /**
     * Sets the property of showing full path. If true, the hints will show the full path. Otherwise, it will only show
     * the path after user typed in so far.
     *
     * @param showFullPath whether show the full path.
     */
    public void setShowFullPath(boolean showFullPath) {
        _showFullPath = showFullPath;
    }

    public boolean updateHints(Object value) {
        if (value == null) {
            return false;
        }
        String s = value.toString();
        if (s.length() == 0) {
            return false;
        }
        int index1 = s.lastIndexOf('\\');
        int index2 = s.lastIndexOf('/');
        int index = Math.max(index1, index2);
        if (index == -1)
            return false;
        final String dir = s.substring(0, index + 1);
        final String prefix = index == s.length() - 1 ? null : s.substring(index + 1).toLowerCase();
        String[] files = new File(dir).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (isFolderOnly()) {
                    if (new File(dir.getAbsolutePath() + File.separator + name).isFile()) {
                        return false;
                    }
                }
                boolean result = prefix == null || name.toLowerCase().startsWith(prefix);
                if (result && getFilter() != null) {
                    return getFilter().accept(dir, name);
                }
                return result;
            }
        });

        if (files == null || files.length == 0 || (files.length == 1 && files[0].equalsIgnoreCase(prefix))) {
            setAvailableHints(null);
            return false;
        }
        else {
            if (isShowFullPath()) {
                getListView().setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        return new ListCell<String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                setText(item == null ? "" : dir + item);

                            }
                        };
                    }
                });
            }
            setAvailableHints(FXCollections.<String>observableArrayList(files));
            return true;
        }
    }

    @Override
    public void acceptHint(String selected) {
        if (selected == null)
            return;

        String selectedValue = "" + selected;

        String value = getTextInputControl().getText();
        int caretPosition = getTextInputControl().getCaretPosition();
        int index1 = value.lastIndexOf('\\', caretPosition);
        int index2 = value.lastIndexOf('/', caretPosition);
        int index = Math.max(index1, index2);
        if (index == -1) {
            return;
        }
        int prefixLength = caretPosition - index - 1;
        getTextInputControl().insertText(caretPosition, selectedValue.substring(prefixLength));
    }

    /**
     * Get FilenameFilter configured to this hints.
     * <p>
     * By default, it returns null. You could set this field to let the IntelliHints only show the files meet your
     * criteria.
     *
     * @return the FilenameFilter in use.
     */
    public FilenameFilter getFilter() {
        return _filter;
    }

    /**
     * Set FilenameFilter to this hints.
     *
     * @param filter the FilenameFilter in use.
     * @see #getFilter()
     */
    public void setFilter(FilenameFilter filter) {
        _filter = filter;
    }
}
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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code TreeViewSearchable} is an concrete implementation of {@link Searchable} that enables the search function
 * in TreeView. <p>It's very simple to use it. Assuming you have a TreeView, all you need to do is to call
 * <pre>{@code
 * TreeView tree = ....;
 * TreeViewSearchable searchable = new TreeViewSearchable(tree);
 * }</pre>
 * Now the TreeView will have the search function.
 * <p>
 * There is very little customization you need to do to TreeSearchable. The only thing you might need is when the
 * element in the TreeView needs a special conversion to convert to string. If so, you can override
 * convertElementToString() to provide you own algorithm to do the conversion.
 * <pre>{@code
 * TreeView tree = ....;
 * TreeViewSearchable searchable = new TreeViewSearchable(tree) {
 *      protected String convertElementToString(Object object) {
 *          ...
 *      }
 * };
 * }</pre>
 * The other customization you might want to is to call {@link #setRecursive} to true. It is false by default. If
 * setting it true, we will automatically expand the TreeItem to look for the string to be searched. If your TreeView
 * has a huge number or unlimited number of rows when fully expanded, please do not set it to true as it will for sure
 * run out of memory.
 * <p>
 * Additional customization can be done on the base Searchable class such as background and foreground color,
 * keystrokes, case sensitivity. <p>
 *
 * @param <T> the element type in the TreeView.
 */
@SuppressWarnings({"Convert2Lambda", "unchecked"})
public class TreeViewSearchable<T> extends Searchable<TreeItem<T>> {

    private BooleanProperty _recursiveProperty;

    private transient List<TreeItem> _treeItems;
    private ChangeListener _rootChangeListener;

    public TreeViewSearchable(TreeView<T> treeView) {
        super(treeView);
        recursiveProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                hidePopup();
                resetTreeItems();
            }
        });
    }

    @Override
    public void installListeners() {
        super.installListeners();

        if (_rootChangeListener == null) {
            _rootChangeListener = new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    hidePopup();
                    resetTreeItems();
                }
            };
        }
        ((TreeView<T>) _node).rootProperty().addListener(_rootChangeListener);
    }

    @Override
    public void uninstallListeners() {
        if (_rootChangeListener != null) {
            ((TreeView<T>) _node).rootProperty().removeListener(_rootChangeListener);
            _rootChangeListener = null;
        }
        super.uninstallListeners();
    }

    public BooleanProperty recursiveProperty() {
        if (_recursiveProperty == null) {
            _recursiveProperty = new SimpleBooleanProperty();
        }
        return _recursiveProperty;
    }

    /**
     * Checks if the searchable is recursive.
     *
     * @return true if searchable is recursive.
     */
    public boolean isRecursive() {
        return recursiveProperty().get();
    }

    /**
     * Sets the recursive attribute.
     * <p>
     * If TreeSearchable is recursive, it will all tree nodes including those which are not visible to find the matching
     * node. Obviously, if your tree has unlimited number of tree nodes or a potential huge number of tree nodes (such
     * as a tree to represent file system), the recursive attribute should be false. To avoid this potential problem in
     * this case, we default it to false.
     *
     * @param recursive the attribute
     */
    public void setRecursive(boolean recursive) {
        recursiveProperty().set(recursive);
    }

    @Override
    protected void setSelectedIndex(int index, boolean incremental) {
        TreeView<T> treeView = (TreeView<T>) _node;
        if (!isRecursive()) {
            if (incremental) {
                treeView.getSelectionModel().select(index);
            }
            else {
                treeView.getSelectionModel().clearAndSelect(index);
            }
            treeView.scrollTo(index);
        }
        else {
            TreeItem<T> item = getElementAt(index);
            if (item != null) { // else case should never happen
                if (incremental) {
                    treeView.getSelectionModel().select(item);
                }
                else {
                    treeView.getSelectionModel().clearSelection();
                    treeView.getSelectionModel().select(item);
                }
                treeView.scrollTo(treeView.getRow(item));
            }
        }
    }

    @Override
    protected int getSelectedIndex() {
        return ((TreeView<T>) _node).getSelectionModel().getSelectedIndex();
    }

    @Override
    protected TreeItem<T> getElementAt(int index) {
        if (index == -1) {
            return null;
        }
        if (!isRecursive()) {
            return ((TreeView<T>) _node).getTreeItem(index);
        }
        else {
            return getTreeItems().get(index);
        }
    }

    @Override
    protected int getElementCount() {
        if (!isRecursive()) {
            return ((TreeView<T>) _node).getExpandedItemCount();
        }
        else {
            return getTreeItems().size();
        }
    }

    /**
     * Recursively go through the tree to populate the tree paths into a list and cache them.
     * <p>
     * Tree paths list is only used when recursive attribute is true.
     */
    protected void populateTreePaths() {
        _treeItems = new ArrayList<>();
        TreeItem root = ((TreeView<T>) _node).getRoot();
        populateTreePaths0(root);
    }

    private void populateTreePaths0(TreeItem item) {
        _treeItems.add(item);
        ObservableList<TreeItem> children = item.getChildren();
        for (TreeItem child : children) {
            populateTreePaths0(child);
        }
    }

    /**
     * Reset the cached tree paths list.
     * <p>
     * Tree paths list is only used when recursive attributes true.
     */
    protected void resetTreeItems() {
        _treeItems = null;
    }

    /**
     * Gets the cached tree paths list. If it has never been cached before, this method will create the cache.
     * <p>
     * Tree paths list is only used when recursive attributes true.
     *
     * @return the tree paths list.
     */
    protected List<TreeItem> getTreeItems() {
        if (_treeItems == null) {
            populateTreePaths();
        }
        return _treeItems;
    }

    /**
     * Converts the element in TreeView to string. The element by default is TreePath. The returned value will be
     * {@code toString()} of the last path component in the TreePath.
     *
     * @param object the object to be converted
     * @return the string representing the TreePath in the TreeView.
     */
    @Override
    protected String convertElementToString(TreeItem<T> object) {
        if (object != null) {
            Object value = ((TreeItem) object).getValue();
            return value != null ? value.toString() : "";
        }
        else {
            return "";
        }
    }
}

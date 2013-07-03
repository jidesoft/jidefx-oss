/*
 * @(#)ObjectComparatorManager.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.comparator;

import javafx.scene.Node;

import java.text.Collator;
import java.util.Comparator;

/**
 * A global object that can register comparator with a type and a ComparatorContext.
 */
public class ObjectComparatorManager {

    public static final String PROPERTY_OBJECT_COMPARATOR_MANAGER = "ObjectComparatorManager"; //NON-NLS

    /**
     * Multi Manager Support
     */
    private static ObjectComparatorManager _instance = createInstance();

    /**
     * Creates a new instance of the ObjectComparatorManager.
     *
     * @return a new instance of the ObjectComparatorManager.
     */
    public static ObjectComparatorManager createInstance() {
        return new ObjectComparatorManager();
    }

    /**
     * Gets the default instance of the ObjectComparatorManager.
     *
     * @return the default instance of the ObjectComparatorManager.
     */
    public static ObjectComparatorManager getInstance() {
        return _instance;
    }

    /**
     * Gets the ObjectComparatorManager from the node if the node has an ObjectComparatorManager defined on the
     * Properties. If not there, return the default instance.
     *
     * @param node the node
     * @return an ObjectComparatorManager.
     */
    public static ObjectComparatorManager getInstance(Node node) {
        if (node != null && node.getProperties().get(PROPERTY_OBJECT_COMPARATOR_MANAGER) instanceof ObjectComparatorManager) {
            return (ObjectComparatorManager) node.getProperties().get(PROPERTY_OBJECT_COMPARATOR_MANAGER);
        }
        else {
            return getInstance();
        }
    }

    /**
     * Instance individual
     */
    private boolean _inited = false;
    private boolean _initing = false;
    private boolean _autoInit = true;

    private final CacheMap<Comparator<?>, ComparatorContext> _cache = new CacheMap<>(new ComparatorContext(""));

    public Comparator<Object> getDefaultComparator() {
        return new NaturalOrderComparator();
    }

    public void registerComparator(Class<?> clazz, Comparator comparator) {
        registerComparator(clazz, comparator, ComparatorContext.DEFAULT_CONTEXT);
    }

    /**
     * Registers a comparator with the type specified as class and a comparator context specified as context.
     *
     * @param clazz      type.
     * @param comparator the comparator to be registered.
     * @param context    the comparator context.
     */
    public void registerComparator(Class<?> clazz, Comparator comparator, ComparatorContext context) {
        if (clazz == null) {
            throw new IllegalArgumentException("Parameter clazz cannot be null");
        }
        if (context == null) {
            context = ComparatorContext.DEFAULT_CONTEXT;
        }

        if (isAutoInit() && !_inited && !_initing) {
            initDefaultComparators();
        }

        _cache.register(clazz, comparator, context);
    }

    /**
     * Unregisters comparator associated with the class and context.
     *
     * @param clazz the data type.
     */
    public void unregisterComparator(Class<?> clazz) {
        unregisterComparator(clazz, ComparatorContext.DEFAULT_CONTEXT);
    }

    /**
     * Unregisters comparator associated with the class and context.
     *
     * @param clazz   the data type.
     * @param context the comparator context.
     */
    public void unregisterComparator(Class<?> clazz, ComparatorContext context) {
        if (context == null) {
            context = ComparatorContext.DEFAULT_CONTEXT;
        }

        if (isAutoInit() && !_inited && !_initing) {
            initDefaultComparators();
        }

        _cache.unregister(clazz, context);
    }

    /**
     * Unregisters all comparators associated with the class.
     *
     * @param clazz the data type.
     */
    public void unregisterAllComparators(Class<?> clazz) {
        _cache.remove(clazz);
    }

    /**
     * Unregisters all the comparators which registered before.
     */
    public void unregisterAllComparators() {
        _cache.clear();
    }

    /**
     * Gets the registered comparator associated with class and default context.
     *
     * @param clazz the data type.
     * @return the registered comparator.
     */
    public Comparator getComparator(Class<?> clazz) {
        return getComparator(clazz, ComparatorContext.DEFAULT_CONTEXT);
    }

    /**
     * Gets the comparator.
     *
     * @param clazz   the data type.
     * @param context the comparator context.
     * @return the comparator.
     */
    public Comparator getComparator(Class<?> clazz, ComparatorContext context) {
        if (isAutoInit() && !_inited && !_initing) {
            initDefaultComparators();
        }

        if (context == null) {
            context = ComparatorContext.DEFAULT_CONTEXT;
        }
        Comparator object = _cache.getRegisteredObject(clazz, context);
        if (object != null) {
            return object;
        }
        else {
            return ObjectComparatorManager.getInstance().getDefaultComparator();
        }
    }

    /**
     * Compares the two objects. It will look up in {@code ObjectComparatorManager} to find the comparator and
     * compare.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return the compare result as defined in {@link Comparator#compare(Object, Object)}
     */

    public int compare(Object o1, Object o2) {
        return compare(o1, o2, ComparatorContext.DEFAULT_CONTEXT);
    }

    /**
     * Compares the two objects. It will look up in {@code ObjectComparatorManager} to find the comparator and
     * compare.
     *
     * @param o1      the first object to be compared.
     * @param o2      the second object to be compared.
     * @param context the comparator context
     * @return the compare result as defined in {@link Comparator#compare(Object, Object)}
     */

    public int compare(Object o1, Object o2, ComparatorContext context) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        else if (o1 == null) {
            return -1;
        }
        else if (o2 == null) {
            return 1;
        }

        // both not null

        Class<?> clazz;
        Class<?> clazz1 = o1.getClass();
        Class<?> clazz2 = o2.getClass();
        if (clazz1 == clazz2) {
            clazz = clazz1;
        }
        else if (clazz1.isAssignableFrom(clazz2)) {
            clazz = clazz1;
        }
        else if (clazz2.isAssignableFrom(clazz1)) {
            clazz = clazz2;
        }
        else if (clazz1.isAssignableFrom(Comparable.class) && clazz2.isAssignableFrom(Comparable.class)) {
            clazz = Comparable.class;
        }
        else {
            clazz = Object.class;
        }

        return compare(o1, o2, clazz, context);
    }

    /**
     * Compares the two objects. It will look up in {@code ObjectComparatorManager} to find the comparator and
     * compare. This method needs a third parameter which is the data type. This is useful when you have two objects
     * that have different data types but both extend the same super class. In this case, you may want the super class
     * as the key to look up in {@code ObjectComparatorManager}.
     *
     * @param o1    the first object to be compared.
     * @param o2    the second object to be compared.
     * @param clazz the data type of the two objects. If your two objects have the same type, you may just use {@link
     *              #compare(Object, Object)} methods.
     * @return the compare result as defined in {@link Comparator#compare(Object, Object)}
     */
    public int compare(Object o1, Object o2, Class<?> clazz) {
        return compare(o1, o2, clazz, ComparatorContext.DEFAULT_CONTEXT);
    }

    /**
     * Compares the two objects. It will look up in {@code ObjectComparatorManager} to find the comparator and
     * compare. If it is not found, we will convert the object to string and compare the two strings.
     *
     * @param o1      the first object to be compared.
     * @param o2      the second object to be compared.
     * @param clazz   the data type of the two objects. If your two objects have the same type, you may just use {@link
     *                #compare(Object, Object)} methods.
     * @param context the comparator context
     * @return the compare result as defined in {@link Comparator#compare(Object, Object)}
     */
    public int compare(Object o1, Object o2, Class<?> clazz, ComparatorContext context) {
        Comparator comparator = getComparator(clazz, context);
        if (comparator != null) {
            try {
                return comparator.compare(o1, o2);
            }
            catch (Exception e) {
                // ignore
            }
        }
        if (o1 == o2) {
            return 0;
        }
        else {
            if (o1 == null) {
                return -1;
            }
            else if (o2 == null) {
                return 1;
            }
            else { // otherwise, compare as string
                return o1.toString().compareTo(o2.toString());
            }
        }
    }

    /**
     * Checks the value of autoInit.
     *
     * @return true or false.
     * @see #setAutoInit(boolean)
     */
    public boolean isAutoInit() {
        return _autoInit;
    }

    /**
     * Sets autoInit to true or false. If autoInit is true, whenever someone tries to call methods like as toString or
     * fromString, {@link #initDefaultComparators()} will be called if it has never be called. By default, autoInit is
     * true.
     * <p>
     * This might affect the behavior if users provide their own comparators and want to overwrite default comparators.
     * In this case, instead of depending on autoInit to initialize default comparators, you should call {@link
     * #initDefaultComparators()} first, then call registerComparator to add your own comparators.
     *
     * @param autoInit false if you want to disable autoInit which means you either don't want those default comparators
     *                 registered or you will call {@link #initDefaultComparators()} yourself.
     */
    public void setAutoInit(boolean autoInit) {
        _autoInit = autoInit;
    }

    /**
     * Gets the available ComparatorContexts registered with the class.
     *
     * @param clazz the class.
     * @return the available ComparatorContext.
     */
    public ComparatorContext[] getComparatorContexts(Class<?> clazz) {
        return _cache.getKeys(clazz, new ComparatorContext[0]);
    }

    /**
     * Initialize default comparator. Please make sure you call this method before you use any comparator related
     * classes such as SortableTableModel.
     */
    public void initDefaultComparators() {
        if (_inited) {
            return;
        }

        _initing = true;

        try {
            registerComparator(Object.class, new NaturalOrderComparator());
            registerComparator(Boolean.class, new NaturalOrderComparator());

            NumberComparator numberComparator = new NumberComparator();
            registerComparator(Number.class, numberComparator);
            registerComparator(double.class, numberComparator);
            registerComparator(float.class, numberComparator);
            registerComparator(long.class, numberComparator);
            registerComparator(int.class, numberComparator);
            registerComparator(short.class, numberComparator);
            registerComparator(byte.class, numberComparator);

            NumberComparator absoluteNumberComparator = new NumberComparator();
            absoluteNumberComparator.setAbsolute(true);
            registerComparator(Number.class, absoluteNumberComparator, NumberComparator.CONTEXT_ABSOLUTE);
            registerComparator(double.class, absoluteNumberComparator, NumberComparator.CONTEXT_ABSOLUTE);
            registerComparator(float.class, absoluteNumberComparator, NumberComparator.CONTEXT_ABSOLUTE);
            registerComparator(long.class, absoluteNumberComparator, NumberComparator.CONTEXT_ABSOLUTE);
            registerComparator(int.class, absoluteNumberComparator, NumberComparator.CONTEXT_ABSOLUTE);
            registerComparator(short.class, absoluteNumberComparator, NumberComparator.CONTEXT_ABSOLUTE);
            registerComparator(byte.class, absoluteNumberComparator, NumberComparator.CONTEXT_ABSOLUTE);

            registerComparator(Comparable.class, new NaturalOrderComparator());
            registerComparator(String.class, Collator.getInstance());
            Collator caseInsensitiveCollator = Collator.getInstance();
            caseInsensitiveCollator.setStrength(Collator.PRIMARY);
            registerComparator(String.class, caseInsensitiveCollator, new ComparatorContext("Ignorecase")); //NON-NLS
            Collator secondaryCollator = Collator.getInstance();
            secondaryCollator.setStrength(Collator.SECONDARY);
            registerComparator(String.class, secondaryCollator, new ComparatorContext("Secondary")); //NON-NLS
            registerComparator(CharSequence.class, new CharSequenceComparator(), CharSequenceComparator.CONTEXT);
            registerComparator(CharSequence.class, new CharSequenceComparator(false), CharSequenceComparator.CONTEXT_IGNORE_CASE);
            registerComparator(CharSequence.class, new AlphanumComparator(), AlphanumComparator.CONTEXT);
            registerComparator(CharSequence.class, new AlphanumComparator(false), AlphanumComparator.CONTEXT_IGNORE_CASE);
        }
        finally {
            _initing = false;
            _inited = true;
        }

    }

    /**
     * If {@link #initDefaultComparators()} is called once, calling it again will have no effect because an internal
     * flag is set. This method will reset the internal flag so that you can call  {@link #initDefaultComparators()} in
     * case you unregister all comparators using {@link #unregisterAllComparators()}.
     */
    public void resetInit() {
        _inited = false;
    }

    public void clear() {
        resetInit();
        _cache.clear();
    }
}

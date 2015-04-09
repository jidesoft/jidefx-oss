/*
 * @(#)CacheMap.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.utils.comparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * An exact copy of the same name class from the JideFX Common Layer. Do it in order to remove the dependency on the
 * Common Layer. If you would like to use it directly, please use the one in the Common Layer.
 */
class CacheMap<T, K> {

    private HashMap<Class<?>, Cache<K, T>> _cache = new HashMap<>();

    private K _defaultContext; // used for fallback lookup.

    /**
     * Constructs a {@code CacheMap}.
     *
     * @param defaultContext the default context.
     */
    public CacheMap(K defaultContext) {
        _defaultContext = defaultContext;
    }

    static class Cache<K, T> extends HashMap<K, T> {
        private static final long serialVersionUID = 7764545350468551102L;

        public T getObject(K context) {
            return get(context);
        }

        public void setObject(K context, T object) {
            if (object == null) {
                remove(context);
            }
            else {
                put(context, object);
            }
        }
    }

    protected Cache<K, T> getCache(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz cannot be null");
        }
        return _cache.get(clazz);
    }

    /**
     * Gets the secondary keys that are registered with the class in CacheMap.
     *
     * @param clazz the class
     * @param a     the array to receive the keys.
     * @return the secondary keys.
     */
    public K[] getKeys(Class<?> clazz, K[] a) {
        Cache<K, T> cache = getCache(clazz);
        if (cache != null) {
            Set<K> set = cache.keySet();
            return set.toArray(a);
        }
        else {
            return a;
        }
    }

    protected Cache<K, T> initCache(Class<?> clazz) {
        Cache<K, T> cache = getCache(clazz);
        if (cache != null) {
            return cache;
        }
        else {
            cache = new Cache<>();
            _cache.put(clazz, cache);
            return cache;
        }
    }

    /**
     * Registers an object with the specified clazz and object.
     *
     * @param clazz   the class which is used as the key.
     * @param object  the object, or the value of the mapping
     * @param context the secondary key. It is used to register multiple objects to the same primary key (the clazz
     *                parameter in this case).
     */
    public void register(Class<?> clazz, T object, K context) {
        if (clazz == null) {
            throw new IllegalArgumentException("Parameter clazz cannot be null");
        }

        // register primitive type automatically
        if (TypeUtils.isPrimitiveWrapper(clazz)) {
            Class<?> primitiveType = TypeUtils.convertWrapperToPrimitiveType(clazz);
            register(primitiveType, object, context);
        }

        Cache<K, T> cache = initCache(clazz);
        cache.setObject(context, object);
    }

    /**
     * Unregisters the object associated with the specified class and context.
     *
     * @param clazz   the class
     * @param context the context
     */
    public void unregister(Class<?> clazz, K context) {
        Cache<K, T> cache = getCache(clazz);
        if (cache != null) {
            cache.setObject(context, null);
            if (cache.size() == 0) {
                _cache.remove(clazz);
            }
        }
    }

    /**
     * Gets registered object from CacheMap. The algorithm used to look up is <BR> 1. First check for exact match with
     * clazz and context.<BR> 2. If didn't find, look for interfaces that clazz implements using the exact context.<BR>
     * 3. If still didn't find, look for super class of clazz using the exact context. <BR> 4. If still didn't find,
     * using the exact clazz with default context.<BR> 5. If still didn't find, return null.<BR> If found a match in
     * step 1, 2, 3 or 4, it will return the registered object immediately.
     *
     * @param clazz   the class which is used as the primary key.
     * @param context the context which is used as the secondary key. This parameter could be null in which case the
     *                default context is used.
     * @return registered object the object associated with the class and the context.
     */
    public T getRegisteredObject(Class<?> clazz, K context) {
        if (clazz == null) {
            return null;
        }

        Cache<K, T> cache = getCache(clazz);

        if (cache == null || !cache.containsKey(context)) {
            List<Class<?>> classesToSearch = new ArrayList<>();

            classesToSearch.add(clazz);
            if (TypeUtils.isPrimitive(clazz)) {
                classesToSearch.add(TypeUtils.convertPrimitiveToWrapperType(clazz));
            }
            else if (TypeUtils.isPrimitiveWrapper(clazz)) {
                classesToSearch.add(TypeUtils.convertWrapperToPrimitiveType(clazz));
            }

            // Direct super interfaces, recursively
            addAllInterfaces(classesToSearch, clazz);

            Class<?> superClass = clazz;
            // Direct super class, recursively
            while (!superClass.isInterface()) {
                superClass = superClass.getSuperclass();
                if (superClass != null) {
                    classesToSearch.add(superClass);
                    addAllInterfaces(classesToSearch, superClass);
                }
                else {
                    break;
                }
            }

            if (!classesToSearch.contains(Object.class)) {
                classesToSearch.add(Object.class);  // use Object as default fallback.
            }

            // search to match context first
            for (Class<?> c : classesToSearch) {
                Cache<K, T> cacheForClass = getCache(c);
                if (cacheForClass != null) {
                    T object = cacheForClass.getObject(context);
                    if (object != null) {
                        return object;
                    }
                }
            }

            // fall back to default context
            if (!_defaultContext.equals(context)) {
                for (Class<?> c : classesToSearch) {
                    Cache<K, T> cacheForClass = getCache(c);
                    if (cacheForClass != null) {
                        T object = cacheForClass.getObject(_defaultContext);
                        if (object != null) {
                            return object;
                        }
                    }
                }
            }
        }

        if (cache != null) {
            T object = cache.getObject(context);
            if (object == null && !_defaultContext.equals(context)) {
                return getRegisteredObject(clazz, _defaultContext);
            }
            if (object != null) {
                return object;
            }
        }

        return null;
    }

    private void addAllInterfaces(List<Class<?>> list, Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> it : interfaces) {
            list.add(it);
            addAllInterfaces(list, it);
        }
    }

    /**
     * Gets the exact match registered object. Different from {@link #getRegisteredObject(Class, Object)} which will try
     * different context and super classes and interfaces to find match. This method will do an exact match.
     *
     * @param clazz   the class which is used as the primary key.
     * @param context the context which is used as the secondary key. This parameter could be null in which case the
     *                default context is used.
     * @return registered object the object associated with the class and the context.
     */
    public T getMatchRegisteredObject(Class<?> clazz, K context) {
        if (clazz == null) {
            return null;
        }

        if (context == null) {
            context = _defaultContext;
        }

        Cache<K, T> cache = getCache(clazz);
        if (cache != null) {
            T object = cache.getObject(context);
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    public List<T> getValues() {
        List<T> list = new ArrayList<>();
        Collection<Cache<K, T>> col = _cache.values();
        for (Cache<K, T> o : col) {
            Collection<T> col2 = o.values();
            for (T o2 : col2) {
                if (!list.contains(o2)) {
                    list.add(o2);
                }
            }
        }
        return list;
    }

    /**
     * Remove all registrations for the designated class.
     *
     * @param clazz the class
     */
    @SuppressWarnings("unchecked")
    public void remove(Class<?> clazz) {
        Cache<K, T> cache = getCache(clazz);
        if (cache != null) {
            Object[] keys = cache.keySet().toArray();
            for (Object context : keys) {
                cache.setObject((K) context, null);
            }
        }
        _cache.remove(clazz);
    }

    public void clear() {
        _cache.clear();
    }
}

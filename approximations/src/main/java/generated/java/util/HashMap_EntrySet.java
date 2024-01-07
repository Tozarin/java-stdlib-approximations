// Generated by the LibSL translator.  DO NOT EDIT!
// sources:
//  - java/util/HashMap.lsl:91
//  - java/util/HashMap.EntrySet.lsl:26
//
package generated.java.util;

import java.lang.ClassCastException;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.UnsupportedOperationException;
import java.lang.Void;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;
import runtime.LibSLRuntime;
import stub.java.util.stream.StreamLSL;

/**
 * HashMap_EntrySetAutomaton for HashMap_EntrySet ~> java.util.HashMap_EntrySet
 */
@SuppressWarnings({"all", "unchecked"})
@Approximate(stub.java.util.HashMap_EntrySet.class)
public final class HashMap_EntrySet implements LibSLRuntime.Automaton {
    static {
        Engine.assume(true);
    }

    public LibSLRuntime.Map<Object, Map.Entry<Object, Object>> storageRef;

    public HashMap parent;

    @LibSLRuntime.AutomatonConstructor
    public HashMap_EntrySet(Void __$lsl_token, final byte p0,
            final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> p1, final HashMap p2) {
        this.storageRef = p1;
        this.parent = p2;
    }

    @LibSLRuntime.AutomatonConstructor
    public HashMap_EntrySet(final Void __$lsl_token) {
        this(__$lsl_token, __$lsl_States.Initialized, null, null);
    }

    /**
     * [SUBROUTINE] HashMap_EntrySetAutomaton::_mapToEntryArray() -> array<Map_Entry<Object, Object>>
     * Source: java/util/HashMap.EntrySet.lsl:79
     */
    private Map.Entry<Object, Object>[] _mapToEntryArray() {
        Map.Entry<Object, Object>[] result = null;
        /* body */ {
            final int storageSize = this.storageRef.size();
            result = new Map.Entry[storageSize];
            if (storageSize != 0) {
                final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                int i = 0;
                for (i = 0; i < storageSize; i += 1) {
                    final Object curKey = unseen.anyKey();
                    result[i] = this.storageRef.get(curKey);
                    unseen.remove(curKey);
                }
                ;
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::add(HashMap_EntrySet, Object) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:114
     */
    public boolean add(Object e) {
        boolean result = false;
        /* body */ {
            if (true) {
                throw new UnsupportedOperationException();
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::addAll(HashMap_EntrySet, Collection) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:122
     */
    public boolean addAll(Collection c) {
        boolean result = false;
        /* body */ {
            if (true) {
                throw new UnsupportedOperationException();
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::clear(HashMap_EntrySet) -> void
     * Source: java/util/HashMap.EntrySet.lsl:129
     */
    public final void clear() {
        /* body */ {
            ((HashMap) ((Object) this.parent)).modCount += 1;
            final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> newStorage = new LibSLRuntime.Map<>(new LibSLRuntime.HashMapContainer<>());
            this.storageRef = newStorage;
            ((HashMap) ((Object) this.parent)).storage = newStorage;
        }
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::contains(HashMap_EntrySet, Object) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:140
     */
    public final boolean contains(Object o) {
        boolean result = false;
        /* body */ {
            result = false;
            if ((o instanceof Map.Entry)) {
                final Map.Entry<Object, Object> oEntry = ((Map.Entry<Object, Object>) o);
                final Object key = oEntry.getKey();
                if (this.storageRef.hasKey(key)) {
                    final Map.Entry<Object, Object> entry = this.storageRef.get(key);
                    result = LibSLRuntime.equals(entry, oEntry);
                }
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::containsAll(HashMap_EntrySet, Collection) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:159
     */
    public boolean containsAll(Collection c) {
        boolean result = false;
        /* body */ {
            result = true;
            final Iterator<Map.Entry<Object, Object>> iter = ((Iterator<Map.Entry<Object, Object>>) c.iterator());
            while (result && iter.hasNext()) {
                final Map.Entry<Object, Object> oEntry = iter.next();
                final Object key = oEntry.getKey();
                if (this.storageRef.hasKey(key)) {
                    final Map.Entry<Object, Object> entry = this.storageRef.get(key);
                    result = LibSLRuntime.equals(entry, oEntry);
                } else {
                    result = false;
                }
            }
            ;
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::equals(HashMap_EntrySet, Object) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:190
     */
    public boolean equals(Object other) {
        boolean result = false;
        /* body */ {
            if (other == this) {
                result = true;
            } else {
                result = false;
                if ((other instanceof Set)) {
                    final Collection c = ((Collection) other);
                    if (this.storageRef.size() == c.size()) {
                        try {
                            result = true;
                            final Iterator<Map.Entry<Object, Object>> iter = ((Iterator<Map.Entry<Object, Object>>) c.iterator());
                            while (result && iter.hasNext()) {
                                final Map.Entry<Object, Object> oEntry = iter.next();
                                final Object key = oEntry.getKey();
                                if (this.storageRef.hasKey(key)) {
                                    final Map.Entry<Object, Object> entry = this.storageRef.get(key);
                                    result = LibSLRuntime.equals(entry, oEntry);
                                } else {
                                    result = false;
                                }
                            }
                            ;
                        } catch (ClassCastException __$lsl_exception) {
                            result = false;
                        } catch (NullPointerException __$lsl_exception) {
                            result = false;
                        }
                        ;
                    }
                }
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::forEach(HashMap_EntrySet, Consumer) -> void
     * Source: java/util/HashMap.EntrySet.lsl:235
     */
    public final void forEach(Consumer userAction) {
        /* body */ {
            if (userAction == null) {
                throw new NullPointerException();
            }
            final int size = this.storageRef.size();
            if (size > 0) {
                final int expectedModCount = ((HashMap) ((Object) this.parent)).modCount;
                final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                int i = 0;
                for (i = 0; i < size; i += 1) {
                    final Object key = unseen.anyKey();
                    final Map.Entry<Object, Object> entry = this.storageRef.get(key);
                    userAction.accept(entry);
                    unseen.remove(key);
                }
                ;
                ((HashMap) ((Object) this.parent))._checkForComodification(expectedModCount);
            }
        }
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::hashCode(HashMap_EntrySet) -> int
     * Source: java/util/HashMap.EntrySet.lsl:269
     */
    public int hashCode() {
        int result = 0;
        /* body */ {
            result = LibSLRuntime.hashCode(this.storageRef);
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::isEmpty(HashMap_EntrySet) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:276
     */
    public boolean isEmpty() {
        boolean result = false;
        /* body */ {
            result = this.storageRef.size() == 0;
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::iterator(HashMap_EntrySet) -> Iterator
     * Source: java/util/HashMap.EntrySet.lsl:282
     */
    public final Iterator iterator() {
        Iterator result = null;
        /* body */ {
            result = (stub.java.util.HashMap_EntryIterator) ((Object) new HashMap_EntryIterator((Void) null, 
                /* state = */ HashMap_EntryIterator.__$lsl_States.Initialized, 
                /* parent = */ this.parent, 
                /* unseen = */ this.storageRef.duplicate(), 
                /* expectedModCount = */ ((HashMap) ((Object) this.parent)).modCount, 
                /* currentKey = */ null
            ));
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::parallelStream(HashMap_EntrySet) -> Stream
     * Source: java/util/HashMap.EntrySet.lsl:293
     */
    public Stream parallelStream() {
        Stream result = null;
        /* body */ {
            final Object[] items = _mapToEntryArray();
            result = (StreamLSL) ((Object) new generated.java.util.stream.StreamLSL((Void) null, 
                /* state = */ generated.java.util.stream.StreamLSL.__$lsl_States.Initialized, 
                /* storage = */ items, 
                /* length = */ items.length, 
                /* closeHandlers = */ Engine.makeSymbolicList(), 
                /* isParallel = */ true, 
                /* linkedOrConsumed = */ false
            ));
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::remove(HashMap_EntrySet, Object) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:306
     */
    public final boolean remove(Object o) {
        boolean result = false;
        /* body */ {
            result = false;
            if ((o instanceof Map.Entry)) {
                final Map.Entry<Object, Object> oEntry = ((Map.Entry<Object, Object>) o);
                final Object key = oEntry.getKey();
                if (this.storageRef.hasKey(key)) {
                    final Map.Entry<Object, Object> entry = this.storageRef.get(key);
                    if (LibSLRuntime.equals(entry, oEntry)) {
                        this.storageRef.remove(key);
                        ((HashMap) ((Object) this.parent)).modCount += 1;
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::removeAll(HashMap_EntrySet, Collection) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:331
     */
    public boolean removeAll(Collection c) {
        boolean result = false;
        /* body */ {
            if (c == null) {
                throw new NullPointerException();
            }
            result = false;
            final int startStorageSize = this.storageRef.size();
            final int cSize = c.size();
            if ((startStorageSize != 0) && (cSize != 0)) {
                if (startStorageSize > cSize) {
                    final Iterator<Map.Entry<Object, Object>> iter = ((Iterator<Map.Entry<Object, Object>>) c.iterator());
                    while (iter.hasNext()) {
                        final Map.Entry<Object, Object> entry = iter.next();
                        final Object curKey = ((AbstractMap_SimpleEntry) ((Object) entry)).key;
                        if (this.storageRef.hasKey(curKey)) {
                            if (LibSLRuntime.equals(entry, this.storageRef.get(curKey))) {
                                this.storageRef.remove(curKey);
                                ((HashMap) ((Object) this.parent)).modCount += 1;
                            }
                        }
                    }
                    ;
                } else {
                    final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                    int i = 0;
                    for (i = 0; i < startStorageSize; i += 1) {
                        final Object curKey = unseen.anyKey();
                        final Map.Entry<Object, Object> entry = this.storageRef.get(curKey);
                        if (c.contains(entry)) {
                            this.storageRef.remove(curKey);
                            ((HashMap) ((Object) this.parent)).modCount += 1;
                        }
                        unseen.remove(curKey);
                    }
                    ;
                }
                final int resultStorageSize = this.storageRef.size();
                result = startStorageSize != resultStorageSize;
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::removeIf(HashMap_EntrySet, Predicate) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:395
     */
    public boolean removeIf(Predicate filter) {
        boolean result = false;
        /* body */ {
            if (filter == null) {
                throw new NullPointerException();
            }
            result = false;
            final int startStorageSize = this.storageRef.size();
            if (startStorageSize != 0) {
                final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                int i = 0;
                for (i = 0; i < startStorageSize; i += 1) {
                    final Object curKey = unseen.anyKey();
                    final Map.Entry<Object, Object> entry = unseen.get(curKey);
                    if (filter.test(entry)) {
                        this.storageRef.remove(curKey);
                        ((HashMap) ((Object) this.parent)).modCount += 1;
                    }
                    unseen.remove(curKey);
                }
                ;
                final int resultStorageSize = this.storageRef.size();
                result = startStorageSize != resultStorageSize;
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::retainAll(HashMap_EntrySet, Collection) -> boolean
     * Source: java/util/HashMap.EntrySet.lsl:432
     */
    public boolean retainAll(Collection c) {
        boolean result = false;
        /* body */ {
            if (c == null) {
                throw new NullPointerException();
            }
            result = false;
            final int startStorageSize = this.storageRef.size();
            final int cSize = c.size();
            if ((startStorageSize != 0) && (cSize != 0)) {
                final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                int i = 0;
                for (i = 0; i < startStorageSize; i += 1) {
                    final Object curKey = unseen.anyKey();
                    final Map.Entry<Object, Object> entry = unseen.get(curKey);
                    if (!c.contains(entry)) {
                        this.storageRef.remove(curKey);
                        ((HashMap) ((Object) this.parent)).modCount += 1;
                    }
                }
                ;
                final int resultStorageSize = this.storageRef.size();
                result = startStorageSize != resultStorageSize;
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::size(HashMap_EntrySet) -> int
     * Source: java/util/HashMap.EntrySet.lsl:468
     */
    public final int size() {
        int result = 0;
        /* body */ {
            result = this.storageRef.size();
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::spliterator(HashMap_EntrySet) -> Spliterator
     * Source: java/util/HashMap.EntrySet.lsl:474
     */
    public final Spliterator spliterator() {
        Spliterator result = null;
        /* body */ {
            result = (stub.java.util.HashMap_EntrySpliterator) ((Object) new HashMap_EntrySpliterator((Void) null, 
                /* state = */ HashMap_EntrySpliterator.__$lsl_States.Initialized, 
                /* parent = */ this.parent, 
                /* entryStorage = */ _mapToEntryArray(), 
                /* index = */ 0, 
                /* fence = */ -1, 
                /* est = */ 0, 
                /* expectedModCount = */ 0
            ));
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::stream(HashMap_EntrySet) -> Stream
     * Source: java/util/HashMap.EntrySet.lsl:484
     */
    public Stream stream() {
        Stream result = null;
        /* body */ {
            final Object[] items = _mapToEntryArray();
            result = (StreamLSL) ((Object) new generated.java.util.stream.StreamLSL((Void) null, 
                /* state = */ generated.java.util.stream.StreamLSL.__$lsl_States.Initialized, 
                /* storage = */ items, 
                /* length = */ items.length, 
                /* closeHandlers = */ Engine.makeSymbolicList(), 
                /* isParallel = */ false, 
                /* linkedOrConsumed = */ false
            ));
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::toArray(HashMap_EntrySet) -> array<Object>
     * Source: java/util/HashMap.EntrySet.lsl:497
     */
    public Object[] toArray() {
        Object[] result = null;
        /* body */ {
            final int len = this.storageRef.size();
            result = new Object[len];
            if (len != 0) {
                final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                int i = 0;
                for (i = 0; i < len; i += 1) {
                    final Object key = unseen.anyKey();
                    result[i] = unseen.get(key);
                    unseen.remove(key);
                }
                ;
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::toArray(HashMap_EntrySet, IntFunction) -> array<Object>
     * Source: java/util/HashMap.EntrySet.lsl:525
     */
    public Object[] toArray(IntFunction generator) {
        Object[] result = null;
        /* body */ {
            final Object[] a = ((Object[]) generator.apply(0));
            final int aLen = a.length;
            final int len = this.storageRef.size();
            result = new Object[len];
            if (len != 0) {
                final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                int i = 0;
                for (i = 0; i < len; i += 1) {
                    final Object key = unseen.anyKey();
                    result[i] = unseen.get(key);
                    unseen.remove(key);
                }
                ;
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::toArray(HashMap_EntrySet, array<Object>) -> array<Object>
     * Source: java/util/HashMap.EntrySet.lsl:547
     */
    public Object[] toArray(Object[] a) {
        Object[] result = null;
        /* body */ {
            final int aLen = a.length;
            final int len = this.storageRef.size();
            if (aLen < len) {
                a = new Object[len];
            }
            result = a;
            if (len != 0) {
                final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                int i = 0;
                for (i = 0; i < len; i += 1) {
                    final Object key = unseen.anyKey();
                    result[i] = unseen.get(key);
                    unseen.remove(key);
                }
                ;
                if (aLen > len) {
                    result[len] = null;
                }
            }
        }
        return result;
    }

    /**
     * [FUNCTION] HashMap_EntrySetAutomaton::toString(HashMap_EntrySet) -> String
     * Source: java/util/HashMap.EntrySet.lsl:573
     */
    public String toString() {
        String result = null;
        /* body */ {
            final int size = this.storageRef.size();
            if (size == 0) {
                result = "[]";
            } else {
                result = "[";
                final int lastIndex = size - 1;
                final LibSLRuntime.Map<Object, Map.Entry<Object, Object>> unseen = this.storageRef.duplicate();
                int i = 0;
                for (i = 0; i < size; i += 1) {
                    final Object key = unseen.anyKey();
                    final Map.Entry<Object, Object> entry = unseen.get(key);
                    final Object value = ((AbstractMap_SimpleEntry) ((Object) entry)).value;
                    result = result.concat(LibSLRuntime.toString(key).concat("=").concat(LibSLRuntime.toString(value)));
                    if (i != lastIndex) {
                        result = result.concat(", ");
                    }
                    unseen.remove(key);
                }
                ;
                result = result.concat("]");
            }
        }
        return result;
    }

    public static final class __$lsl_States {
        public static final byte Initialized = (byte) 0;
    }

    @Approximate(HashMap_EntrySet.class)
    public static final class __hook {
        private __hook(Void o1, Void o2) {
            Engine.assume(false);
        }
    }
}

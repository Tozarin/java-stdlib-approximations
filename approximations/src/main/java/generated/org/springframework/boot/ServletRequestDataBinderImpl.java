package generated.org.springframework.boot;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.PinnedValueStorage;
import generated.org.springframework.boot.resolvers.ResolverUtils;
import jakarta.servlet.ServletRequest;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.core.CollectionFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.usvm.api.Engine;
import runtime.LibSLRuntime;

import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Approximate(ServletRequestDataBinder.class)
public class ServletRequestDataBinderImpl extends ServletRequestDataBinder {

    private static final String FIELD_SEPARATOR = ".";
    private static final int MAX_ARRAY_INDEX = 5;
    private static final int MAX_DEPTH = 5;

    public ServletRequestDataBinderImpl(Object target) {
        super(target);
    }

    public void bind(ServletRequest request) {
        symbolicBind(
                getTarget(),
                new GenericClass(getType()),
                "",
                null,
                0
        );
        SpringApplicationImpl._println("Bind finished");
    }

    private Class<?> getType() {
        if (getTargetType() != null)
            return getTargetType().toClass();

        if (getTarget() != null)
            return getTarget().getClass();

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(Class<T> c) {
        return (Class<T>) MethodType.methodType(c).wrap().returnType();
    }

    private static Map<String, Object> _getFieldTypes(Class<?> target, ClassLoader classLoader) {
        throw new IllegalStateException("This method must be approximated");
    }

    private Object getInputData(String name, Class<?> clazz) {
        Class<?> boxed = wrap(clazz);
        return PinnedValueStorage.getPinnedValue(PinnedValueSource.REQUEST_PARAM, name, boxed);
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        return ReflectionUtils.findField(clazz, fieldName);
    }

    private static void writeField(Object target, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            SpringApplicationImpl._println(String.format(
                    "Warning! Field %s of class %s could not be set due to IllegalAccessException!",
                    field.getName(),
                    target.getClass().getName()
            ));
        }
    }

    private static Object readField(Object target, Field field) {
        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            SpringApplicationImpl._println(String.format(
                    "Warning! Field %s of class %s could not be read due to IllegalAccessException!",
                    field.getName(),
                    target.getClass().getName()
            ));
        }

        return null;
    }

    private static Object newValue(GenericClass clazz) {
        Class<?> type = clazz.getClazz();

        try {
            if (type.isArray()) {
                Class<?> componentType = type.componentType();
                if (componentType.isArray()) {
                    Object array = Array.newInstance(componentType, 1);
                    Array.set(array, 0, Array.newInstance(componentType.componentType(), 0));
                    return array;
                }
                else {
                    return Array.newInstance(componentType, 0);
                }
            }
            else if (Collection.class.isAssignableFrom(type)) {
                return CollectionFactory.createCollection(type, null, 16);
            }
            else if (Map.class.isAssignableFrom(type)) {
                return CollectionFactory.createMap(type, null, 16);
            }
            Constructor<?> ctor = type.getDeclaredConstructor();
            return BeanUtils.instantiateClass(ctor);
        } catch (NoSuchMethodException e) {
            SpringApplicationImpl._println(String.format(
                    "Warning! Class %s has no default parameterless constructor!",
                    type.getName()
            ));
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void writeArrayIndex(
            Object collection,
            int index,
            Object value
    ) {
        if (collection.getClass().isArray()) {
            Array.set(collection, index, value);
        }

        if (collection instanceof List) {
            List list = (List)collection;
            list.set(index, value);
        }
    }

    private boolean canWriteField(String fieldName, Class<?> fieldType) {
        List<Class<?>> prohibitedTypes = List.of(Set.class);
        if (prohibitedTypes.stream().anyMatch(t -> t.isAssignableFrom(fieldType)))
            return false;
        String[] allowed = getAllowedFields();
        String[] disallowed = getDisallowedFields();
        return ((ObjectUtils.isEmpty(allowed) || PatternMatchUtils.simpleMatch(allowed, fieldName)) &&
                (ObjectUtils.isEmpty(disallowed) || !PatternMatchUtils.simpleMatch(disallowed, fieldName.toLowerCase())));
    }

    private static Object growArrayIfNecessary(
            Object array,
            int newSize,
            GenericClass elementType
    ) {
        int currentSize = Array.getLength(array);

        if (currentSize >= newSize)
            return array;

        Class<?> componentType = array.getClass().componentType();
        Object newArray = Array.newInstance(componentType, newSize);
        LibSLRuntime.ArrayActions.copy(array, 0, newArray, 0, currentSize);

        boolean shouldInitialize = !ResolverUtils.isPrimitive(componentType);

        if (shouldInitialize) {
            for (int i = currentSize; i < newSize; i++) {
                Array.set(newArray, i, newValue(elementType));
            }
        }


        return newArray;

    }

    private static void growCollectionIfNecessary(
            Collection<Object> collection,
            int length,
            GenericClass elementType
    ) {

        int currentSize = collection.size();
        if (currentSize >= length)
            return;

        boolean shouldInitialize = !ResolverUtils.isPrimitive(elementType.getClazz());

        for (int i = collection.size(); i < length + 1; i++) {
            if (shouldInitialize) {
                collection.add(newValue(elementType));
            } else {
                collection.add(null);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<Object> getOrAllocateByIndex(
            Object collection,
            GenericClass elementType,
            int index,
            int max_size
    ) {

        if (collection == null) {
            SpringApplicationImpl._println("Warning! Collection was null!");
            Engine.assume(false);
            throw new IllegalArgumentException("Collection cannot be null");
        }

        if (collection.getClass().isArray()) {
            Object newCollection = growArrayIfNecessary(collection, max_size, elementType);
            Object value = Array.get(newCollection, index);
            return Arrays.asList(newCollection, value);
        }

        if (collection instanceof List) {
            List list = (List)collection;
            growCollectionIfNecessary(list, max_size, elementType);
            Object value = list.get(index);
            return Arrays.asList(collection, value);
        }

        if (collection instanceof Iterable) {
            Iterable iterable = (Iterable)collection;

            if (collection instanceof Collection<?>)
                Engine.assume(max_size <= ((Collection)collection).size());

            Iterator<Object> it = iterable.iterator();
            for (int i = 0; it.hasNext(); i++) {
                Object elem = it.next();
                if (i == index)
                    return Arrays.asList(collection, elem);
            }
        }

        SpringApplicationImpl._println("Warning! Collection type was not supported!");
        Engine.assume(false);
        throw new IllegalArgumentException("Unsupported collection type");
    }

    private static Object getOrAllocateByField(
            Object parent,
            Field field,
            GenericClass childType
    ) {
        Object existing = readField(parent, field);

        if (existing != null)
            return existing;

        if (ResolverUtils.isPrimitive(childType.getClazz()))
            return null;

        existing = newValue(childType);
        writeField(parent, field, existing);

        return existing;
    }

    private boolean canGrowArray(Field field) {
        try {
            Class<?> introspectionClass = CachedIntrospectionResults.class;

            CachedIntrospectionResults introspection =
                    (CachedIntrospectionResults) introspectionClass
                    .getDeclaredMethod("forClass", Class.class)
                    .invoke(null, field.getDeclaringClass());

            PropertyDescriptor propertyDescriptor =
                    (PropertyDescriptor) introspectionClass.getDeclaredMethod("getPropertyDescriptor", String.class)
                    .invoke(introspection, field.getName());

            if (field.getType().isArray())
                return propertyDescriptor.getWriteMethod() != null;

            return true;
        } catch (Exception e) {
            SpringApplicationImpl._println("Warning! Error occurred " + e);
        }
        return false;
    }

    private int getSize(Object collection) {
        if (collection.getClass().isArray())
            return Array.getLength(collection);

        if (collection instanceof List)
            return ((List<?>) collection).size();

        return 0;
    }

    private Object symbolicBind(
            Object target,
            GenericClass genericClass,
            String path,
            Field sourceField,
            int depth
    ) {
        // TODO: Currently removed to reduce forks
        // if (Engine.makeSymbolicBoolean()) {
        //     return null;
        // }

        Class<?> clazz = genericClass.getClazz();
        List<GenericClass> typeArguments = genericClass.getGenerics();

        if (clazz == null) return null;

        if (depth > MAX_DEPTH) return null;

        if (ResolverUtils.isPrimitive(clazz)) {
            Object value = getInputData(path, clazz);
            Engine.assume(value != null);
            return value;
        }

        else if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
            int maxSize = MAX_ARRAY_INDEX;

            GenericClass elementType;

            if (clazz.isArray()) {
                elementType = new GenericClass(clazz.getComponentType());
            } else {
                elementType = typeArguments.get(0);
            }

            Object collection = target;

            if (!canGrowArray(sourceField)) {
                maxSize = getSize(collection);
            }

            for (int i = 0; i < maxSize; i++) {
                String pathWithIndex = String.format("%s[%d]", path, i);
                List<Object> collectionToValue = getOrAllocateByIndex(collection, elementType, i, maxSize);
                collection = collectionToValue.get(0);
                Object oldValue = collectionToValue.get(1);
                SpringApplicationImpl._println(oldValue == null ? "null" : oldValue.toString());
                Object newValue = symbolicBind(oldValue, elementType, pathWithIndex, sourceField, depth + 1);

                if (newValue != null)
                    writeArrayIndex(collection, i, newValue);
            }

            return collection;
        }

        else {
            ClassLoader classLoader = clazz.getClassLoader();
            Map<String, Object> fieldData = _getFieldTypes(clazz, classLoader);

            for (String fieldName : fieldData.keySet()) {
                String pathWithField = path.isEmpty() ? fieldName : path + FIELD_SEPARATOR + fieldName;
                GenericClass fieldType = new GenericClass(fieldData.get(fieldName));
                Field field = findField(clazz, fieldName);

                if (!canWriteField(fieldName, fieldType.getClazz()))
                    continue;

                Object oldValue = getOrAllocateByField(target, field, fieldType);
                Object newValue = symbolicBind(oldValue, fieldType, pathWithField, field, depth + 1);

                if (newValue != null)
                    writeField(target, field, newValue);
            }

            return target;
        }
    }

    private static class GenericClass {
        private final Class<?> clazz;
        private final List<GenericClass> typeArguments;

        @SuppressWarnings("rawtypes")
        public GenericClass(Object classWithGenerics) {
            typeArguments = new ArrayList<>();

            if (classWithGenerics instanceof List) {
                List casted = (List)classWithGenerics;
                clazz = (Class<?>)casted.get(0);
                for (Object generic : (List)casted.get(1))
                    typeArguments.add(new GenericClass(generic));
                return;
            }

            throw new IllegalStateException("class with generics was malformed");
        }

        public GenericClass(Class<?> clazz) {
            this.clazz = clazz;
            typeArguments = new ArrayList<>();
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public List<GenericClass> getGenerics() {
            return typeArguments;
        }
    }
}

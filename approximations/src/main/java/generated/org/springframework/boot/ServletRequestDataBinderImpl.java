package generated.org.springframework.boot;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.PinnedValueStorage;
import generated.org.springframework.boot.resolvers.ResolverUtils;
import jakarta.servlet.ServletRequest;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.core.CollectionFactory;
import org.springframework.core.ResolvableType;
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
                getType(),
                "",
                null,
                0
        );
        SpringApplicationImpl._println("Bind finished");
    }

    private GenericClass getType() {
        ResolvableType type = getTargetType();
        if (type != null)
            return new GenericClass(type.toClass(), getTarget(), true);

        Object target = getTarget();
        if (target != null)
            return new GenericClass(target.getClass(), target, false);

        return new GenericClass(null, null, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(Class<T> c) {
        return (Class<T>) MethodType.methodType(c).wrap().returnType();
    }

    private static Map<String, Object> _getFieldTypes(Class<?> target) {
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

    private static Object newValue(GenericClass type) {
        try {
            if (type.isArray()) {
                GenericClass componentType = type.componentType(null);
                if (componentType.isArray()) {
                    Object array = Array.newInstance(componentType.getInternalClass(), 1);
                    Object innerArray = Array.newInstance(componentType.componentType(null).getInternalClass(), 0);
                    Array.set(array, 0, innerArray);
                    return array;
                }

                return Array.newInstance(componentType.getInternalClass(), 0);
            }
            else if (type.isAssignableTo(Collection.class)) {
                return CollectionFactory.createCollection(type.getInternalClass(), null, 16);
            }
            else if (type.isAssignableTo(Map.class)) {
                return CollectionFactory.createMap(type.getInternalClass(), null, 16);
            }
            Constructor<?> ctor = type.getInternalClass().getDeclaredConstructor();
            return BeanUtils.instantiateClass(ctor);
        } catch (NoSuchMethodException e) {
            SpringApplicationImpl._println("Warning! Class has no default parameterless constructor!");
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void writeByIndex(
            Object collection,
            GenericClass type,
            int index,
            Object value
    ) {
        if (type.isArray()) {
            Array.set(collection, index, value);
        }

        if (collection instanceof List) {
            List list = (List)collection;
            list.set(index, value);
        }
    }

    private boolean canWriteField(String fieldName, GenericClass fieldType) {
        List<Class<?>> prohibitedTypes = List.of(Set.class);
        for (Class<?> prohibitedType : prohibitedTypes) {
            if (fieldType.isAssignableTo(prohibitedType))
                return false;
        }

        String[] allowed = getAllowedFields();
        String[] disallowed = getDisallowedFields();
        return ((ObjectUtils.isEmpty(allowed) || PatternMatchUtils.simpleMatch(allowed, fieldName)) &&
                (ObjectUtils.isEmpty(disallowed) || !PatternMatchUtils.simpleMatch(disallowed, fieldName.toLowerCase())));
    }

    private static Object growArrayIfNecessary(
            Object array,
            int newSize,
            GenericClass arrayType
    ) {
        int currentSize = Array.getLength(array);

        if (currentSize >= newSize)
            return array;

        GenericClass componentType = arrayType.componentType(null);
        Object newArray = Array.newInstance(componentType.getInternalClass(), newSize);
        LibSLRuntime.ArrayActions.copy(array, 0, newArray, 0, currentSize);

        boolean shouldInitialize = !componentType.isPrimitiveOrWrapper();

        if (shouldInitialize) {
            for (int i = currentSize; i < newSize; i++) {
                Array.set(newArray, i, newValue(componentType));
            }
        }

        return newArray;
    }

    private static void growCollectionIfNecessary(
            Collection<Object> collection,
            int length,
            GenericClass collectionType
    ) {
        int currentSize = collection.size();
        if (currentSize >= length)
            return;

        GenericClass elementType = collectionType.getTypeArg();
        boolean shouldInitialize = !elementType.isPrimitiveOrWrapper();

        for (int i = collection.size(); i < length + 1; i++) { // TODO kek
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
            GenericClass collectionType,
            int index,
            int max_size
    ) {

        if (collection == null) {
            SpringApplicationImpl._println("Warning! Collection was null!");
            Engine.assume(false);
            throw new IllegalArgumentException("Collection cannot be null");
        }

        if (collectionType.isArray()) {
            Object newCollection = growArrayIfNecessary(collection, max_size, collectionType);
            Object value = Array.get(newCollection, index);
            return Arrays.asList(newCollection, value);
        }

        if (collection instanceof List) {
            List list = (List)collection;
            growCollectionIfNecessary(list, max_size, collectionType);
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

        SpringApplicationImpl._internalLog("Warning! Collection type was not supported!", collectionType.getInternalClass().getName());
        Engine.assume(false);
        throw new IllegalArgumentException("Unsupported collection type");
    }

    private static Object getOrAllocateByField(
            Object parent,
            Field field,
            GenericClass childType
    ) {
        SpringApplicationImpl._internalLog("FIELD:", field.toString(), field.getType().toString());
        Object existing = readField(parent, field);

        if (existing != null)
            return existing;

        if (childType.isPrimitiveOrWrapper())
            return null;

        existing = newValue(childType);
        writeField(parent, field, existing);

        return existing;
    }

    private boolean canGrowArray(Field field) {
        try {
            if (!field.getType().isArray())
                return true;

            Class<?> introspectionClass = CachedIntrospectionResults.class;

            CachedIntrospectionResults introspection =
                    (CachedIntrospectionResults) introspectionClass
                    .getDeclaredMethod("forClass", Class.class)
                    .invoke(null, field.getDeclaringClass());

            PropertyDescriptor propertyDescriptor =
                    (PropertyDescriptor) introspectionClass.getDeclaredMethod("getPropertyDescriptor", String.class)
                    .invoke(introspection, field.getName());

            return propertyDescriptor.getWriteMethod() != null;
        } catch (Exception e) {
            SpringApplicationImpl._println("Warning! Error occurred " + e);
        }
        return false;
    }

    private int getSize(Object collection, GenericClass collectionType) {
        if (collectionType.isArray())
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

        if (genericClass.isNull()) return null;

        if (depth > MAX_DEPTH) return null;

        if (genericClass.isPrimitiveOrWrapper()) {
            Object value = getInputData(path, genericClass.getInternalClass());
            Engine.assume(value != null);
            return value;
        }

        boolean isArray = genericClass.isArray();
        if (isArray || genericClass.isAssignableTo(Collection.class)) {
            int maxSize = MAX_ARRAY_INDEX;

            Object collection = target;

            if (!canGrowArray(sourceField)) {
                maxSize = getSize(collection, genericClass);
            }

            for (int i = 0; i < maxSize; i++) {
                String pathWithIndex = String.format("%s[%d]", path, i);
                List<Object> collectionToValue = getOrAllocateByIndex(collection, genericClass, i, maxSize);
                collection = collectionToValue.get(0);
                Object oldValue = collectionToValue.get(1);
                SpringApplicationImpl._println(oldValue == null ? "null" : oldValue.toString());
                GenericClass elementType = genericClass.collectionElementType(oldValue);
                Object newValue = symbolicBind(oldValue, elementType, pathWithIndex, sourceField, depth + 1);

                if (newValue != null)
                    writeByIndex(collection, genericClass, i, newValue);
            }

            return collection;
        }

        else {
            Class<?> clazz = genericClass.getInternalClass();
            Map<String, Object> fieldData = _getFieldTypes(clazz);

            for (Map.Entry<String, Object> fData : fieldData.entrySet()) {
                String fieldName = fData.getKey();
                Object fieldClass = fData.getValue();
                String pathWithField = path.isEmpty() ? fieldName : path + FIELD_SEPARATOR + fieldName;
                GenericClass fieldType = new GenericClass(fieldClass);
                Field field = findField(clazz, fieldName);

                if (!canWriteField(fieldName, fieldType))
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
        private final boolean classIsConcrete;
        private final Class<?> clazz;
        private final Object target;
        private final List<GenericClass> typeArguments;

        @SuppressWarnings({"rawtypes", "PatternVariableCanBeUsed"})
        public GenericClass(Object classWithGenerics) {
            typeArguments = new ArrayList<>();

            if (classWithGenerics instanceof List) {
                classIsConcrete = true;
                target = null;
                List casted = (List)classWithGenerics;
                clazz = (Class<?>)casted.get(0);
                for (Object generic : (List)casted.get(1))
                    typeArguments.add(new GenericClass(generic));
                return;
            }

            throw new IllegalStateException("class with generics was malformed");
        }

        public GenericClass(Class<?> clazz, Object target, boolean classIsConcrete) {
            this.clazz = clazz;
            this.target = target;
            this.classIsConcrete = classIsConcrete;
            typeArguments = new ArrayList<>();
        }

        public boolean isArray() {
            if (classIsConcrete | target == null)
                return clazz.isArray();

            return Engine.typeIsArray(target);
        }

        public GenericClass componentType(Object newTarget) {
            if (classIsConcrete | target == null)
                return new GenericClass(clazz.componentType(), newTarget, classIsConcrete);

            return new GenericClass(Engine.arrayElementType(target), newTarget, false);
        }

        public GenericClass collectionElementType(Object newTarget) {
            if (isArray())
                return componentType(newTarget);

            return getTypeArg();
        }

        public GenericClass getTypeArg() {
            return typeArguments.get(0);
        }

        public boolean isAssignableTo(Class<?> clazz) {
            if (classIsConcrete | target == null)
                return clazz.isAssignableFrom(this.clazz);

            return Engine.typeIsSubtype(target, clazz);
        }

        public boolean isNull() {
            return clazz == null;
        }

        public boolean isPrimitiveOrWrapper() {
            if (classIsConcrete | target == null)
                return ResolverUtils.isPrimitiveOrWrapper(this.clazz);

            // TODO
            return ResolverUtils.isPrimitiveOrWrapper(this.clazz);
        }

        public Class<?> getInternalClass() {
            return clazz;
        }

        public List<GenericClass> getGenerics() {
            return typeArguments;
        }
    }
}

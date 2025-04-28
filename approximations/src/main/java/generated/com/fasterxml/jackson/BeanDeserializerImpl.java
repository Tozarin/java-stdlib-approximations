package generated.com.fasterxml.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import generated.org.springframework.boot.SpringApplicationImpl;
import generated.org.springframework.boot.SymbolicValueFactory;
import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.resolvers.ResolverUtils;
import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;

import java.io.IOException;
import java.io.Serial;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static generated.org.springframework.boot.pinnedValues.PinnedValueStorage.writePinnedValue;

@Approximate(BeanDeserializer.class)
public class BeanDeserializerImpl extends BeanDeserializer {

    @Serial
    private static final long serialVersionUID = 1L;

    protected BeanDeserializerImpl(BeanDeserializerBase src) {
        super(src);
    }

    public final static int MAX_DEPTH = 3;
    public final static int MAX_LENGTH = 3;

    private boolean isPrimitive(JavaType type) {
        return ResolverUtils.isPrimitiveOrWrapper(type.getRawClass());
    }

    private boolean isCollection(JavaType type) {
        return type.isArrayType() || type.isCollectionLikeType();
    }

    private void _writeToState(Object root) {
        writePinnedValue(PinnedValueSource.REQUEST_BODY, root);
    }

    public static boolean _concreteDeserialization() {
        // Returns false with JcApproximations inside interesting methods
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(Class<T> c) {
        return (Class<T>) MethodType.methodType(c).wrap().returnType();
    }

    public static List<Object> getPrimitive(JavaType type) {
        Class<?> boxed = wrap(type.getRawClass());
        Object result = Engine.makeSymbolic(boxed);
        Engine.assume(result != null);
        return Arrays.asList(result, result);
    }

    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        if (_concreteDeserialization())
            return deserializeReal(p, ctxt);

        Class<?> clazz = _valueInstantiator.getValueClass();

        if (ResolverUtils.isPrimitiveOrWrapper(clazz)) {
            Object result = SymbolicValueFactory.createSymbolic(clazz, true);
            _writeToState(result);
            return result;
        }

        return deserializeFromObject(p, ctxt);
    }

    public List<Object> generateSymbolicBean(
            JsonParser p,
            DeserializationContext ctxt,
            int depth
    ) throws IOException {
        if (depth > MAX_DEPTH)
            return Arrays.asList(null, null);

        Object bean = null;
        Object beanCopy = null;

        List<SettableBeanProperty> alreadySetProperties = new ArrayList<>();

        if (_valueInstantiator.canCreateUsingDefault()) {
            bean = _valueInstantiator.createUsingDefault(ctxt);
            beanCopy = _valueInstantiator.createUsingDefault(ctxt);
        }

        if (_valueInstantiator.canCreateFromObjectWith()) {
            Collection<SettableBeanProperty> properties = _propertyBasedCreator.properties();

            int propertyCount = properties.size();
            Object[] beanConstructorArguments = new Object[propertyCount];
            Object[] copyConstructorArguments = new Object[propertyCount];

            alreadySetProperties.addAll(properties);

            for (SettableBeanProperty property : properties) {
                List<Object> valueAndCopy = generateSymbolicPropertyValue(property, p, ctxt, depth);
                int index = property.getCreatorIndex();
                beanConstructorArguments[index] = valueAndCopy.get(0);
                copyConstructorArguments[index] = valueAndCopy.get(1);
            }

            bean = _valueInstantiator.createFromObjectWith(ctxt, beanConstructorArguments);
            beanCopy = _valueInstantiator.createFromObjectWith(ctxt, copyConstructorArguments);
        }

        if (bean == null) {
            SpringApplicationImpl._println(String.format(
                    "Warning! Could not instantiate bean of type %s",
                    _valueInstantiator.getValueClass().toString()
            ));
            Engine.assume(false);
        }

        for (SettableBeanProperty property : _beanProperties) {
            if (alreadySetProperties.contains(property))
                continue;

            List<Object> valueAndCopy = generateSymbolicPropertyValue(property, p, ctxt, depth);

            property.set(bean, valueAndCopy.get(0));
            property.set(beanCopy, valueAndCopy.get(1));
        }

        return Arrays.asList(bean, beanCopy);
    }

    private List<Object> generateSymbolicPropertyValue(
            SettableBeanProperty property,
            JsonParser p,
            DeserializationContext ctxt,
            int depth
    ) throws IOException {
        JsonDeserializer<Object> valueDeserializer = property.getValueDeserializer();
        JavaType propertyType = property.getType();
        return generateSymbolic(propertyType, valueDeserializer, p, ctxt, depth);
    }

    private List<Object> generateSymbolic(
            JavaType type,
            JsonDeserializer<Object> deserializer,
            JsonParser p,
            DeserializationContext ctxt,
            int depth
    ) throws IOException {
        if (isPrimitive(type))
            return getPrimitive(type);

        if (deserializer instanceof BeanDeserializer)
           return ((BeanDeserializerImpl)deserializer).generateSymbolicBean(p, ctxt, depth + 1);

        if (isCollection(type))
            return generateSymbolicCollection(type, deserializer, p, ctxt, depth);

        SpringApplicationImpl._println(String.format("Warning! Generating symbolic JSON property value did not hit any case (type: %s) (deser: %s)",
                type,
                deserializer
        ));
        return Arrays.asList(null, null);
    }

    @SuppressWarnings("unchecked")
    private List<Object> generateSymbolicCollection(
            JavaType type,
            JsonDeserializer<Object> deserializer,
            JsonParser p,
            DeserializationContext ctxt,
            int depth
    ) throws IOException {
        int length = MAX_LENGTH;

        if (type.isArrayType()) {
            JavaType componentType = type.getContentType();
            Object array = Array.newInstance(componentType.getRawClass(), length);
            Object arrayCopy = Array.newInstance(componentType.getRawClass(), length);

            if (isPrimitive(componentType)) {
                // Array of primitives, allocate and fill now
                List<Object> valueAndCopy;

                for (int i = 0; i < length; i++) {
                    valueAndCopy = getPrimitive(componentType);
                    Array.set(array, i, valueAndCopy.get(0));
                    Array.set(arrayCopy, i, valueAndCopy.get(1));
                }
            } else {
                // Array of non-primitive, go into inner deserializer
                List<Object> valueAndCopy;

                if (!(deserializer instanceof ObjectArrayDeserializer)) {
                    SpringApplicationImpl._println("Warning! Object array deserializer was not of an ObjectArrayDeserializer class!");
                    return Arrays.asList(null, null);
                }

                ObjectArrayDeserializer objectArrayDeserializer = (ObjectArrayDeserializer)deserializer;
                JsonDeserializer<Object> contentDeserializer = objectArrayDeserializer.getContentDeserializer();

                for (int i = 0; i < length; i++) {
                    valueAndCopy = generateSymbolic(componentType, contentDeserializer, p, ctxt, depth);
                    Array.set(array, i, valueAndCopy.get(0));
                    Array.set(arrayCopy, i, valueAndCopy.get(1));
                }
            }
            return Arrays.asList(array, arrayCopy);
        }

        // Case for collections
        if (deserializer instanceof ContainerDeserializerBase<?>) {
            ContainerDeserializerBase<?> collectionDeserializer = (ContainerDeserializerBase<?>) deserializer;
            ValueInstantiator instantiator = collectionDeserializer.getValueInstantiator();
            JsonDeserializer<Object> contentDeserializer = collectionDeserializer.getContentDeserializer();
            Collection<Object> collection = (Collection<Object>) instantiator.createUsingDefault(ctxt);
            Collection<Object> collectionCopy = (Collection<Object>)instantiator.createUsingDefault(ctxt);

            JavaType componentType = type.getContentType();
            List<Object> valueAndCopy;

            for (int i = 0; i < length; i++) {
                valueAndCopy = generateSymbolic(componentType, contentDeserializer, p, ctxt, depth);
                collection.add(valueAndCopy.get(0));
                collectionCopy.add(valueAndCopy.get(1));
            }

            return Arrays.asList(collection, collectionCopy);
        }

        SpringApplicationImpl._println(String.format(
                "Warning! Generating symbolic JSON collection did not hit any case (type: %s) (deserializer: %s)",
                type,
                deserializer
        ));

        return Arrays.asList(null, null);
    }

    public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (_concreteDeserialization())
            return deserializeFromObjectReal(p, ctxt);

        List<Object> beanAndCopy = generateSymbolicBean(p, ctxt, 0);
        _writeToState(beanAndCopy.get(1));
        return beanAndCopy.get(0);
    }

    // Real implementation for technical use in some spring methods
    public Object deserializeReal(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.isExpectedStartObjectToken()) {
            if (this._vanillaProcessing) {
                return this.vanillaDeserialize(p, ctxt, p.nextToken());
            } else {
                p.nextToken();
                return this._objectIdReader != null ? this.deserializeWithObjectId(p, ctxt) : this.deserializeFromObject(p, ctxt);
            }
        } else {
            return this._deserializeOther(p, ctxt, p.currentToken());
        }
    }

    private final Object vanillaDeserialize(JsonParser p, DeserializationContext ctxt, JsonToken t) throws IOException {
        Object bean = this._valueInstantiator.createUsingDefault(ctxt);
        if (p.hasTokenId(5)) {
            p.assignCurrentValue(bean);
            String propName = p.currentName();

            do {
                p.nextToken();
                SettableBeanProperty prop = this._beanProperties.find(propName);
                if (prop != null) {
                    try {
                        prop.deserializeAndSet(p, ctxt, bean);
                    } catch (Exception var8) {
                        Exception e = var8;
                        this.wrapAndThrow(e, bean, propName, ctxt);
                    }
                } else {
                    this.handleUnknownVanilla(p, ctxt, bean, propName);
                }
            } while((propName = p.nextFieldName()) != null);
        }

        return bean;
    }

    public Object deserializeFromObjectReal(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (this._objectIdReader != null && this._objectIdReader.maySerializeAsObject() && p.hasTokenId(5) && this._objectIdReader.isValidReferencePropertyName(p.currentName(), p)) {
            return this.deserializeFromObjectId(p, ctxt);
        } else {
            Object bean;
            if (this._nonStandardCreation) {
                if (this._unwrappedPropertyHandler != null) {
                    return this.deserializeWithUnwrapped(p, ctxt);
                } else if (this._externalTypeIdHandler != null) {
                    return this.deserializeWithExternalTypeId(p, ctxt);
                } else {
                    bean = this.deserializeFromObjectUsingNonDefault(p, ctxt);
                    return bean;
                }
            } else {
                bean = this._valueInstantiator.createUsingDefault(ctxt);
                p.assignCurrentValue(bean);
                if (p.canReadObjectId()) {
                    Object id = p.getObjectId();
                    if (id != null) {
                        this._handleTypedObjectId(p, ctxt, bean, id);
                    }
                } else if (this._objectIdReader != null && p.hasTokenId(2) && ctxt.isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)) {
                    ctxt.reportUnresolvedObjectId(this._objectIdReader, bean);
                }

                if (this._injectables != null) {
                    this.injectValues(ctxt, bean);
                }

                if (this._needViewProcesing) {
                    Class<?> view = ctxt.getActiveView();
                    if (view != null) {
                        return this.deserializeWithView(p, ctxt, bean, view);
                    }
                }

                if (p.hasTokenId(5)) {
                    String propName = p.currentName();

                    do {
                        p.nextToken();
                        SettableBeanProperty prop = this._beanProperties.find(propName);
                        if (prop != null) {
                            try {
                                prop.deserializeAndSet(p, ctxt, bean);
                            } catch (Exception var7) {
                                Exception e = var7;
                                this.wrapAndThrow(e, bean, propName, ctxt);
                            }
                        } else {
                            this.handleUnknownVanilla(p, ctxt, bean, propName);
                        }
                    } while((propName = p.nextFieldName()) != null);
                }

                return bean;
            }
        }
    }
}

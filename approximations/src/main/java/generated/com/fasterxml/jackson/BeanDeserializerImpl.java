package generated.com.fasterxml.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import generated.org.springframework.boot.SpringApplicationImpl;
import generated.org.springframework.boot.SymbolicValueFactory;
import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.resolvers.ResolverUtils;
import org.jacodb.approximation.annotation.Approximate;

import java.io.IOException;
import java.io.Serial;

import static generated.org.springframework.boot.pinnedValues.PinnedValueStorage.writePinnedValue;

@Approximate(BeanDeserializer.class)
public class BeanDeserializerImpl extends BeanDeserializer {

    @Serial
    private static final long serialVersionUID = 1L;
    
    protected BeanDeserializerImpl(BeanDeserializerBase src) {
        super(src);
    }

    public static int _iteration = 0;
    public final static int MAX_ITERATION = 1;

    private boolean _isJsonPrimitive(JavaType type) {
        return ResolverUtils.isPrimitive(type.getRawClass());
    }

    private void _writeToState(Object root) {
        writePinnedValue(PinnedValueSource.REQUEST_BODY, root);
    }

    public static boolean _concreteDeserialization() {
        // Returns false with JcApproximations inside interesting methods
        return true;
    }

    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        if (_concreteDeserialization())
            return deserializeReal(p, ctxt);

        boolean isFirstRun = _iteration == 0;
        _iteration++;

        if (_iteration > MAX_ITERATION)
            return null;

        Object empty = _valueInstantiator.createUsingDefault(ctxt);
        Class<?> clazz = empty.getClass();

        // Forks a lot on building string
        SpringApplicationImpl._println(String.format("[Deserializing (deserialize)] Iteration: %d; Type: %s", _iteration, clazz));

        Object result;

        if (ResolverUtils.isPrimitive(clazz))
            result = SymbolicValueFactory.createSymbolic(clazz, true);
        else
            result = deserializeFromObject(p, ctxt);

        if (isFirstRun) {
            _iteration = 0;
            _writeToState(result);
        }

        return result;
    }
    
    public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (_concreteDeserialization())
            return deserializeFromObjectReal(p, ctxt);

        final Object bean = _valueInstantiator.createUsingDefault(ctxt);
        final Class<?> clazz = bean.getClass();

        // Forks a lot on building string
        // SpringApplicationImpl._println(String.format("[Deserializing (deserializeFromObject)] Iteration: %d; Type: %s", _iteration, clazz));

        for (SettableBeanProperty property : _beanProperties) {
            if (_isJsonPrimitive(property.getType())) {
                Object value = SymbolicValueFactory.createSymbolic(property.getType().getRawClass(), true);
                property.set(bean, value);
            }
            else
                property.deserializeAndSet(p, ctxt, bean);
        }
        return bean;
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

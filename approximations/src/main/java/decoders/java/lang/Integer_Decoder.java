package decoders.java.lang;

import org.jacodb.api.jvm.*;
import org.usvm.api.decoder.DecoderApi;
import org.usvm.api.decoder.DecoderFor;
import org.usvm.api.decoder.ObjectData;
import org.usvm.api.decoder.ObjectDecoder;

import java.util.Collections;
import java.util.List;

@DecoderFor(Integer.class)
public final class Integer_Decoder implements ObjectDecoder {
    private volatile JcMethod cached_Integer_valueOf = null;
    private volatile JcField cached_Integer_value = null;

    @Override
    public <T> T createInstance(final JcClassOrInterface approximation,
                                final ObjectData<T> approximationData,
                                final DecoderApi<T> decoder) {
        JcMethod valueOf = cached_Integer_valueOf;
        // TODO: add synchronization if needed
        if (valueOf == null) {
            // looking for constructor and data field
            final List<JcMethod> methods = approximation.getDeclaredMethods();
            for (int i = 0, c = methods.size(); i != c; i++) {
                JcMethod m = methods.get(i);

                if (!"valueOf".equals(m.getName())) continue;

                List<JcParameter> params = m.getParameters();
                if (params.size() != 1) continue;
                if (!"int".equals(params.get(0).getType().getTypeName())) continue;

                cached_Integer_valueOf = valueOf = m;
                break;
            }

            final List<JcField> fields = approximation.getDeclaredFields();
            for (int i = 0, c = fields.size(); i != c; i++) {
                JcField f = fields.get(i);
                if ("value".equals(f.getName())) {
                    cached_Integer_value = f;
                    break;
                }
            }
        }

        // extract the info
        final int value = approximationData.getIntField(cached_Integer_value);

        // assemble into a call
        final List<T> args = Collections.singletonList(decoder.createIntConst(value));
        return decoder.invokeMethod(valueOf, args);
    }

    @Override
    public <T> void initializeInstance(final JcClassOrInterface approximation,
                                       final ObjectData<T> approximationData,
                                       final T outputInstance,
                                       final DecoderApi<T> decoder) {
        // nothing
    }
}

package encoders.java.util;

import generated.java.util.set.TreeSetImpl;
import org.usvm.api.encoder.EncoderFor;
import org.usvm.api.encoder.ObjectEncoder;

import java.util.Set;
import java.util.TreeSet;

@EncoderFor(TreeSet.class)
public class TreeSet_Encoder implements ObjectEncoder {

    @Override
    @SuppressWarnings("unchecked")
    public Object encode(Object object) {
        Set<Object> set = (Set<Object>) object;
        return new TreeSetImpl<>(set);
    }
}

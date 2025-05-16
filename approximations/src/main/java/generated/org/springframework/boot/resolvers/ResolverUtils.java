package generated.org.springframework.boot.resolvers;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.PinnedValueStorage;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.util.UrlPathHelper;
import org.usvm.api.Engine;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.util.UrlPathHelper.rawPathInstance;

public class ResolverUtils {
    public static String getNonEmptySymbolicString(PinnedValueSource source, String name) {
        String string = PinnedValueStorage.getPinnedValue(source, name, String.class);
        Engine.assume(string != null);
        Engine.assume(!string.isEmpty());
        return string;
    }

    public static Object createNullPinned(PinnedValueSource source, String name, Class<?> clazz) {
        PinnedValueStorage.writePinnedValue(source, name, null, clazz);
        return PinnedValueStorage.getPinnedValue(source, name, clazz);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        List<Class<?>> primitives = Arrays.asList(
                Boolean.class,
                Byte.class,
                Short.class,
                Character.class,
                Integer.class,
                Long.class,
                Float.class,
                Double.class,
                String.class,
                LocalDate.class
        );
        return primitives.contains(clazz) || clazz.isPrimitive();
    }

    public static boolean supportsMatrix(NativeWebRequest request) {
        boolean removeSemicolon = UrlPathHelper.rawPathInstance.shouldRemoveSemicolonContent();
        boolean hasPathVariable = request.getContextPath().contains("{");
        return !removeSemicolon && hasPathVariable;
    }
}

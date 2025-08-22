package generated.java.lang;

import java.io.Serial;
import java.io.Serializable;
import java.lang.StackTraceElement;
import java.lang.String;

import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;

@SuppressWarnings("unused")
@Approximate(java.lang.Throwable.class)
public class ThrowableImpl implements Serializable {

    @Serial
    private static final long serialVersionUID = -3042686055658047285L;

    private static final String NULL_CAUSE_MESSAGE = "Cannot suppress a null exception.";

    private static final String SELF_SUPPRESSION_MESSAGE = "Self-suppression not permitted";

    private static final String CAUSE_CAPTION = "Caused by: ";

    private static final String SUPPRESSED_CAPTION = "Suppressed: ";

    static {
        Engine.assume(true);
    }

    public final synchronized void addSuppressed(java.lang.Throwable exception) { }

    public synchronized ThrowableImpl fillInStackTrace() {
        return this;
    }

    public StackTraceElement[] getStackTrace() {
        int size = Engine.makeSymbolicInt();
        Engine.assume(size >= 0);
        Engine.assume(size < 99);
        return Engine.makeSymbolicArray(StackTraceElement.class, size);
    }

    public final synchronized java.lang.Throwable[] getSuppressed() {
        int size = Engine.makeSymbolicInt();
        Engine.assume(size >= 0);
        Engine.assume(size < 99);
        return Engine.makeSymbolicArray(java.lang.Throwable.class, size);
    }

    public void printStackTrace() { }

    public void setStackTrace(StackTraceElement[] stackTrace) { }
}

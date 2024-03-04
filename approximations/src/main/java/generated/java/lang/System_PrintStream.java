// Generated by the LibSL translator.  DO NOT EDIT!
// sources:
//  - java/lang/System.lsl:83
//  - java/lang/System.StdOut.lsl:22
//
package generated.java.lang;

import java.io.PrintStream;
import java.lang.CharSequence;
import java.lang.IndexOutOfBoundsException;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.String;
import java.lang.StringIndexOutOfBoundsException;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.Locale;
import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;
import runtime.LibSLRuntime;

/**
 * System_PrintStreamAutomaton for System_PrintStream ~> java.lang.System_PrintStream
 */
@SuppressWarnings({"all", "unchecked"})
@Approximate(stub.java.lang.System_PrintStream.class)
public final class System_PrintStream implements LibSLRuntime.Automaton {
    static {
        Engine.assume(true);
    }

    public boolean closed;

    public boolean error;

    @LibSLRuntime.AutomatonConstructor
    public System_PrintStream(Void __$lsl_token, final byte p0, final boolean p1,
            final boolean p2) {
        this.closed = p1;
        this.error = p2;
    }

    @LibSLRuntime.AutomatonConstructor
    public System_PrintStream(final Void __$lsl_token) {
        this(__$lsl_token, __$lsl_States.Initialized, false, false);
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::append(System_PrintStream, CharSequence) -> PrintStream
     * Source: java/lang/System.StdOut.lsl:94
     */
    public PrintStream append(CharSequence csq) {
        PrintStream result = null;
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
            result = ((PrintStream) ((Object) this));
        }
        return result;
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::append(System_PrintStream, CharSequence, int, int) -> PrintStream
     * Source: java/lang/System.StdOut.lsl:102
     */
    public PrintStream append(CharSequence csq, int start, int end) {
        PrintStream result = null;
        /* body */ {
            if (csq == null) {
                csq = "null";
            }
            final int size = csq.length();
            if ((start < 0) || (end >= size)) {
                throw new StringIndexOutOfBoundsException();
            }
            if (this.closed) {
                this.error = true;
            }
            result = ((PrintStream) ((Object) this));
        }
        return result;
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::append(System_PrintStream, char) -> PrintStream
     * Source: java/lang/System.StdOut.lsl:117
     */
    public PrintStream append(char c) {
        PrintStream result = null;
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
            result = ((PrintStream) ((Object) this));
        }
        return result;
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::checkError(System_PrintStream) -> boolean
     * Source: java/lang/System.StdOut.lsl:125
     */
    public boolean checkError() {
        boolean result = false;
        /* body */ {
            result = this.error;
        }
        return result;
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::close(System_PrintStream) -> void
     * Source: java/lang/System.StdOut.lsl:131
     */
    public void close() {
        /* body */ {
            this.closed = true;
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::flush(System_PrintStream) -> void
     * Source: java/lang/System.StdOut.lsl:138
     */
    public void flush() {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::format(System_PrintStream, Locale, String, array<Object>) -> PrintStream
     * Source: java/lang/System.StdOut.lsl:145
     */
    public PrintStream format(Locale l, String format, Object[] args) {
        PrintStream result = null;
        /* body */ {
            if (format == null) {
                throw new NullPointerException();
            }
            if (this.closed) {
                this.error = true;
            }
            result = ((PrintStream) ((Object) this));
        }
        return result;
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::format(System_PrintStream, String, array<Object>) -> PrintStream
     * Source: java/lang/System.StdOut.lsl:156
     */
    public PrintStream format(String format, Object[] args) {
        PrintStream result = null;
        /* body */ {
            if (format == null) {
                throw new NullPointerException();
            }
            if (this.closed) {
                this.error = true;
            }
            result = ((PrintStream) ((Object) this));
        }
        return result;
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, Object) -> void
     * Source: java/lang/System.StdOut.lsl:167
     */
    public void print(Object obj) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, String) -> void
     * Source: java/lang/System.StdOut.lsl:173
     */
    public void print(String s) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, boolean) -> void
     * Source: java/lang/System.StdOut.lsl:179
     */
    public void print(boolean b) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, char) -> void
     * Source: java/lang/System.StdOut.lsl:185
     */
    public void print(char c) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, array<char>) -> void
     * Source: java/lang/System.StdOut.lsl:191
     */
    public void print(char[] s) {
        /* body */ {
            if (s == null) {
                throw new NullPointerException();
            }
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, double) -> void
     * Source: java/lang/System.StdOut.lsl:200
     */
    public void print(double d) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, float) -> void
     * Source: java/lang/System.StdOut.lsl:206
     */
    public void print(float f) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, int) -> void
     * Source: java/lang/System.StdOut.lsl:212
     */
    public void print(int i) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::print(System_PrintStream, long) -> void
     * Source: java/lang/System.StdOut.lsl:218
     */
    public void print(long l) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::printf(System_PrintStream, Locale, String, array<Object>) -> PrintStream
     * Source: java/lang/System.StdOut.lsl:224
     */
    public PrintStream printf(Locale l, String format, Object[] args) {
        PrintStream result = null;
        /* body */ {
            if ((l == null) || (format == null) || (args == null)) {
                throw new NullPointerException();
            }
            if (this.closed) {
                this.error = true;
            }
            result = ((PrintStream) ((Object) this));
        }
        return result;
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::printf(System_PrintStream, String, array<Object>) -> PrintStream
     * Source: java/lang/System.StdOut.lsl:235
     */
    public PrintStream printf(String format, Object[] args) {
        PrintStream result = null;
        /* body */ {
            if ((format == null) || (args == null)) {
                throw new NullPointerException();
            }
            if (this.closed) {
                this.error = true;
            }
            result = ((PrintStream) ((Object) this));
        }
        return result;
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream) -> void
     * Source: java/lang/System.StdOut.lsl:246
     */
    public void println() {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, Object) -> void
     * Source: java/lang/System.StdOut.lsl:252
     */
    public void println(Object x) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, String) -> void
     * Source: java/lang/System.StdOut.lsl:258
     */
    public void println(String x) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, boolean) -> void
     * Source: java/lang/System.StdOut.lsl:264
     */
    public void println(boolean x) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, char) -> void
     * Source: java/lang/System.StdOut.lsl:270
     */
    public void println(char x) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, array<char>) -> void
     * Source: java/lang/System.StdOut.lsl:276
     */
    public void println(char[] x) {
        /* body */ {
            if (x == null) {
                throw new NullPointerException();
            }
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, double) -> void
     * Source: java/lang/System.StdOut.lsl:285
     */
    public void println(double x) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, float) -> void
     * Source: java/lang/System.StdOut.lsl:291
     */
    public void println(float x) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, int) -> void
     * Source: java/lang/System.StdOut.lsl:297
     */
    public void println(int x) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::println(System_PrintStream, long) -> void
     * Source: java/lang/System.StdOut.lsl:303
     */
    public void println(long x) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::write(System_PrintStream, array<byte>) -> void
     * Source: java/lang/System.StdOut.lsl:309
     */
    public void write(byte[] b) throws java.io.IOException {
        /* body */ {
            if (b == null) {
                throw new NullPointerException();
            }
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::write(System_PrintStream, array<byte>, int, int) -> void
     * Source: java/lang/System.StdOut.lsl:320
     */
    public void write(byte[] buf, int off, int len) {
        /* body */ {
            if (buf == null) {
                throw new NullPointerException();
            }
            final int size = buf.length;
            if ((off < 0) || ((off + len) > size)) {
                throw new IndexOutOfBoundsException();
            }
            if (this.closed) {
                this.error = true;
            }
        }
    }

    /**
     * [FUNCTION] System_PrintStreamAutomaton::write(System_PrintStream, int) -> void
     * Source: java/lang/System.StdOut.lsl:333
     */
    public void write(int b) {
        /* body */ {
            if (this.closed) {
                this.error = true;
            }
        }
    }

    public static final class __$lsl_States {
        public static final byte Initialized = (byte) 0;
    }

    @Approximate(System_PrintStream.class)
    public static final class __hook {
        private __hook(Void o1, Void o2) {
            Engine.assume(false);
        }
    }
}

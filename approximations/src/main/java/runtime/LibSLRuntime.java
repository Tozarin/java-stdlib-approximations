package runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import generated.java.lang.StringImpl;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicIdentityMap;
import org.usvm.api.SymbolicList;
import org.usvm.api.SymbolicMap;

@SuppressWarnings("unused")
public final class LibSLRuntime {

    private static final Object SOMETHING = LibSLRuntime.class;

    @Target(ElementType.CONSTRUCTOR)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DirectCallOnly {
    }

    public static abstract class LibSLException extends Error {

        @java.io.Serial
        private static final long serialVersionUID = 4980196508277280341L;

        private LibSLException(final String msg) {
            super(msg);
        }
    }

    public static final class StateCheckException extends LibSLException {

        @java.io.Serial
        private static final long serialVersionUID = 4980196508277280343L;

        private StateCheckException(final String msg) {
            super(msg);
        }
    }

    public static final class SemanticViolationException extends LibSLException {

        @java.io.Serial
        private static final long serialVersionUID = 4980196508277280344L;

        private SemanticViolationException(final String msg) {
            super(msg);
        }
    }

    public static final class IncompleteSpecificationException extends LibSLException {

        @java.io.Serial
        private static final long serialVersionUID = 4980196508277280345L;

        private IncompleteSpecificationException(final String msg) {
            // #question: can we pass class and method names here? (counterargument: string bloat)
            super(msg);
        }
    }


    public static void error(final String msg) {
        Engine.assume(msg != null);
        throw new SemanticViolationException(msg);
    }

    public static void todo() {
        throw new IncompleteSpecificationException("TODO");
    }

    public static void not_implemented() {
        throw new IncompleteSpecificationException("NOT_IMPLEMENTED");
    }


    private static volatile int guid = 0;
    private static final Object guidLock = new Object();

    @SuppressWarnings("NonAtomicOperationOnVolatileField")
    public static int getUniqueId() {
        // TODO: enable synchronization when parallel execution is allowed
        //synchronized (guidLock) {
        return ++guid;
        //}
    }

    private static final int BUFF_SIZE_BYTE = 4;
    private static final int BUFF_SIZE_SHORT = 6;
    private static final int BUFF_SIZE_INT = 11;
    private static final int BUFF_SIZE_LONG = 20;

    public static String toString(final boolean v) {
        return v ? "true" : "false";
    }

    private static byte digitToByte(long i) {
        return (byte) ('0' + i);
    }

    private static String numberToString(long n, int buffSize) {
        byte[] bytes = new byte[buffSize];

        final boolean isNegative = n < 0;
        if (isNegative)
            n = -n;

        int len = 0;
        int pos = buffSize;
        while (n != 0) {
            pos--;
            len++;
            bytes[pos] = digitToByte(n % 10);
            n /= 10;
        }

        if (isNegative) {
            pos--;
            len++;
            bytes[pos] = '-';
        }

        return new String(bytes, pos, len);
    }

    @SuppressWarnings("ConstantConditions")
    public static String toString(byte v) {
        if (v == 0) return "0";
        if (v == Byte.MIN_VALUE) return "-128";
        if (v == Byte.MAX_VALUE) return "127";
        // TODO: add more common cases

        Engine.assume(v > Byte.MIN_VALUE);
        Engine.assume(v < Byte.MAX_VALUE);

        return numberToString(v, BUFF_SIZE_BYTE);
    }

    @SuppressWarnings("ConstantConditions")
    public static String toString(short v) {
        if (v == 0) return "0";
        if (v == Short.MIN_VALUE) return "-32768";
        if (v == Short.MAX_VALUE) return "32767";
        // TODO: add more common cases

        Engine.assume(v > Short.MIN_VALUE);
        Engine.assume(v < Short.MAX_VALUE);

        return numberToString(v, BUFF_SIZE_SHORT);
    }

    @SuppressWarnings("ConstantConditions")
    public static String toString(int v) {
        if (v == 0) return "0";
        if (v == Integer.MIN_VALUE) return "-2147483648";
        if (v == Integer.MAX_VALUE) return "2147483647";
        // TODO: add more common cases

        Engine.assume(v > Integer.MIN_VALUE);
        Engine.assume(v < Integer.MAX_VALUE);

        return numberToString(v, BUFF_SIZE_INT);
    }

    @SuppressWarnings("ConstantConditions")
    public static String toString(long v) {
        if (v == 0L) return "0";
        if (v == Long.MIN_VALUE) return "-9223372036854775808";
        if (v == Long.MAX_VALUE) return "9223372036854775807";
        // TODO: add more common cases

        Engine.assume(v > Long.MIN_VALUE);
        Engine.assume(v < Long.MAX_VALUE);

        return numberToString(v, BUFF_SIZE_LONG);
    }

    public static String toString(char v) {
        return new String(StringImpl._charToBytes(v));
    }

    // TODO: do we need more variants for other primitive array types?
    public static String toString(char[] v) {
        return new String(StringImpl._getBytes(v));
    }

    private static final float FLOAT_MULTIPLIER_REGULAR = 1e+6f; // 8 decimal positions
    private static final int FLOAT_MULTIPLIER_REGULAR_count = 6;
    private static final int FLOAT_MULTIPLIER_REGULAR_int = (int) FLOAT_MULTIPLIER_REGULAR;

    private static final float FLOAT_MULTIPLIER_SCIENTIFIC = 1e+6f; // 8 decimal positions
    private static final int FLOAT_MULTIPLIER_SCIENTIFIC_count = 6;
    private static final int FLOAT_MULTIPLIER_SCIENTIFIC_int = (int) FLOAT_MULTIPLIER_SCIENTIFIC;

    @SuppressWarnings("ManualMinMaxCalculation")
    public static String toString(float v) {
        if (v != v)
            return "NaN";
        if (v == Float.POSITIVE_INFINITY)
            return "Infinity";
        if (v == Float.NEGATIVE_INFINITY)
            return "-Infinity";
        if (v == Float.MAX_VALUE)
            return "3.4028235E38";
        if (v == Float.MIN_VALUE)
            return "1.4E-45";
        if (v == 0f)
            return 1f / v == Float.NEGATIVE_INFINITY ? "-0.0" : "0.0";

        final boolean isNegative = v < 0f;
        if (isNegative)
            v = -v;

        // decide on the formatting
        final String result;
        if (v >= 1e-3f && v < 1e+7f) {
            // use regular notation

            // split into main parts
            int integral = (int) v;
            float remainder = (v - integral) * FLOAT_MULTIPLIER_REGULAR;
            int fraction = (int) remainder;

            // corrections
            if (remainder - (float) fraction >= 0.5f) {
                // rounding of last decimal digit
                fraction++;

                // overflow?
                if (fraction >= FLOAT_MULTIPLIER_REGULAR_int) {
                    fraction = 0;
                    integral++;

                    // overflow?
                    if (integral >= 10)
                        integral = 1;
                }
            }

            // prepare pieces
            final String integralStr = toString(integral);
            final String fractionStr = prepareFloatFraction(fraction, FLOAT_MULTIPLIER_REGULAR_count);

            // format pieces together
            String tmp = integralStr.concat(".").concat(fractionStr);
            final int maxCount = 9;
            final int count = maxCount < tmp.length() ? maxCount : tmp.length();
            result = tmp;//.substring(0, count);
        } else {
            // use scientific notation

            // forcing value into [1..9] range
            int exp = 0;
            // convert to base 10 using log2
            if (v >= 10.0f) {
                if (v >= 1E+32f) {
                    v *= 1E-32f;
                    exp += 32;
                }
                if (v >= 1E+16f) {
                    v *= 1E-16f;
                    exp += 16;
                }
                if (v >= 1E+8f) {
                    v *= 1E-8f;
                    exp += 8;
                }
                if (v >= 1E+4f) {
                    v *= 1E-4f;
                    exp += 4;
                }
                if (v >= 1E+2f) {
                    v *= 1E-2f;
                    exp += 2;
                }
                if (v >= 1E+1f) {
                    v *= 1E-1f;
                    exp++;
                }
            }
            if (v > 0.0f && v <= 1.0f) {
                if (v < 1E-31f) {
                    v *= 1E32f;
                    exp -= 32;
                }
                if (v < 1E-15f) {
                    v *= 1E16f;
                    exp -= 16;
                }
                if (v < 1E-7f) {
                    v *= 1E8f;
                    exp -= 8;
                }
                if (v < 1E-3f) {
                    v *= 1E4f;
                    exp -= 4;
                }
                if (v < 1E-1f) {
                    v *= 1E2f;
                    exp -= 2;
                }
                if (v < 1E-0f) {
                    v *= 1E1f;
                    exp--;
                }
            }

            // split into main parts
            int integral = (int) v;
            float remainder = (v - integral) * FLOAT_MULTIPLIER_SCIENTIFIC;
            int fraction = (int) remainder;

            // corrections
            if (remainder - (float) fraction >= 0.5f) {
                // rounding of last decimal digit
                fraction++;

                // overflow?
                if (fraction >= FLOAT_MULTIPLIER_SCIENTIFIC_int) {
                    fraction = 0;
                    integral++;

                    // overflow?
                    if (integral >= 10) {
                        integral = 1;
                        exp++;
                    }
                }
            }

            // prepare pieces
            final String integralStr = toString(integral);
            final String fractionStr = prepareFloatFraction(fraction, FLOAT_MULTIPLIER_SCIENTIFIC_count);
            final String exponentStr = toString(exp);

            // format pieces together
            result = integralStr.concat(".").concat(fractionStr).concat("E").concat(exponentStr);
        }

        // put everything together
        return (isNegative ? "-" : "").concat(result);
    }

    @SuppressWarnings("ExplicitArrayFilling")
    private static String prepareFloatFraction(int decimal, final int expectedFractionLength) {
        if (decimal == 0)
            return "0";

        // cut and count trailing zeroes
        int cutZeroes = 0;
        while (decimal % 10 == 0) {
            decimal /= 10;
            cutZeroes++;
        }

        final String decimals = toString(decimal);
        final int decimalLength = decimals.length();

        if (decimalLength < expectedFractionLength) {
            final int leadingZeroCount = expectedFractionLength - cutZeroes - decimalLength;
            final char[] zeroes = new char[leadingZeroCount];
            for (int i = 0; i < leadingZeroCount; i++)
                zeroes[i] = '0';

            return new String(zeroes).concat(decimals);
        } else {
            return decimals;
        }
    }


    private static final double DOUBLE_MULTIPLIER_REGULAR = 1e+15d; // 16 decimal positions
    private static final int DOUBLE_MULTIPLIER_REGULAR_count = 15;
    private static final long DOUBLE_MULTIPLIER_REGULAR_long = (long) DOUBLE_MULTIPLIER_REGULAR;

    private static final double DOUBLE_MULTIPLIER_SCIENTIFIC = 1e+15d; // 16 decimal positions
    private static final int DOUBLE_MULTIPLIER_SCIENTIFIC_count = 15;
    private static final long DOUBLE_MULTIPLIER_SCIENTIFIC_long = (long) DOUBLE_MULTIPLIER_SCIENTIFIC;

    @SuppressWarnings("ManualMinMaxCalculation")
    public static String toString(double v) {
        if (v != v)
            return "NaN";
        if (v == Double.POSITIVE_INFINITY)
            return "Infinity";
        if (v == Double.NEGATIVE_INFINITY)
            return "-Infinity";
        if (v == Double.MAX_VALUE)
            return "1.7976931348623157E308";
        if (v == Double.MIN_VALUE)
            return "4.9E-324";
        if (v == 0d)
            return 1d / v == Double.NEGATIVE_INFINITY ? "-0.0" : "0.0";

        final boolean isNegative = v < 0d;
        if (isNegative)
            v = -v;

        // decide on the formatting
        final String result;
        if (v >= 1e-3d && v < 1e+7d) {
            // use regular notation

            // split into main parts
            int integral = (int) v;
            double remainder = (v - integral) * DOUBLE_MULTIPLIER_REGULAR;
            long fraction = (long) remainder;

            // corrections
            if (remainder - (double) fraction >= 0.5d) {
                // rounding of last decimal digit
                fraction++;

                // overflow?
                if (fraction >= DOUBLE_MULTIPLIER_REGULAR_long) {
                    fraction = 0;
                    integral++;

                    // overflow?
                    if (integral >= 10)
                        integral = 1;
                }
            }

            // prepare pieces
            final String integralStr = toString(integral);
            final String fractionStr = prepareDoubleFraction(fraction, DOUBLE_MULTIPLIER_REGULAR_count);

            // format pieces together
            String tmp = integralStr.concat(".").concat(fractionStr);
            final int maxCount = 18;
            final int count = maxCount < tmp.length() ? maxCount : tmp.length();
            result = tmp;//.substring(0, count);
        } else {
            // use scientific notation

            // forcing value into [1..9] range
            int exp = 0;
            // convert to base 10 using log2
            if (v >= 10.0) {
                if (v >= 1E+256) {
                    v *= 1E-256;
                    exp += 256;
                }
                if (v >= 1E+128) {
                    v *= 1E-128;
                    exp += 128;
                }
                if (v >= 1E+64) {
                    v *= 1E-64;
                    exp += 64;
                }
                if (v >= 1E+32) {
                    v *= 1E-32;
                    exp += 32;
                }
                if (v >= 1E+16) {
                    v *= 1E-16;
                    exp += 16;
                }
                if (v >= 1E+8) {
                    v *= 1E-8;
                    exp += 8;
                }
                if (v >= 1E+4) {
                    v *= 1E-4;
                    exp += 4;
                }
                if (v >= 1E+2) {
                    v *= 1E-2;
                    exp += 2;
                }
                if (v >= 1E+1) {
                    v *= 1E-1;
                    exp++;
                }
            }
            if (v > 0.0 && v <= 1.0) {
                if (v < 1E-255) {
                    v *= 1E256;
                    exp -= 256;
                }
                if (v < 1E-127) {
                    v *= 1E128;
                    exp -= 128;
                }
                if (v < 1E-63) {
                    v *= 1E64;
                    exp -= 64;
                }
                if (v < 1E-31) {
                    v *= 1E32;
                    exp -= 32;
                }
                if (v < 1E-15) {
                    v *= 1E16;
                    exp -= 16;
                }
                if (v < 1E-7) {
                    v *= 1E8;
                    exp -= 8;
                }
                if (v < 1E-3) {
                    v *= 1E4;
                    exp -= 4;
                }
                if (v < 1E-1) {
                    v *= 1E2;
                    exp -= 2;
                }
                if (v < 1E-0) {
                    v *= 1E1;
                    exp--;
                }
            }

            // split into main parts
            int integral = (int) v;
            double remainder = (v - integral) * DOUBLE_MULTIPLIER_SCIENTIFIC;
            long fraction = (long) remainder;

            // corrections
            if (remainder - (double) fraction >= 0.5d) {
                // rounding of last decimal digit
                fraction++;

                // overflow?
                if (fraction >= DOUBLE_MULTIPLIER_SCIENTIFIC_long) {
                    fraction = 0;
                    integral++;

                    // overflow?
                    if (integral >= 10) {
                        integral = 1;
                        exp++;
                    }
                }
            }

            // prepare pieces
            final String integralStr = toString(integral);
            final String fractionStr = prepareDoubleFraction(fraction, DOUBLE_MULTIPLIER_SCIENTIFIC_count);
            final String exponentStr = toString(exp);

            // format pieces together
            result = integralStr.concat(".").concat(fractionStr).concat("E").concat(exponentStr);
        }

        // put everything together
        return (isNegative ? "-" : "").concat(result);
    }

    @SuppressWarnings("ExplicitArrayFilling")
    private static String prepareDoubleFraction(long decimal, final int expectedFractionLength) {
        if (decimal == 0)
            return "0";

        // cut and count trailing zeroes
        int cutZeroes = 0;
        while (decimal % 10 == 0) {
            decimal /= 10;
            cutZeroes++;
        }

        final String decimals = toString(decimal);
        final int decimalLength = decimals.length();

        if (decimalLength < expectedFractionLength) {
            final int leadingZeroCount = expectedFractionLength - cutZeroes - decimalLength;
            final char[] zeroes = new char[leadingZeroCount];
            for (int i = 0; i < leadingZeroCount; i++)
                zeroes[i] = '0';

            return new String(zeroes).concat(decimals);
        } else {
            return decimals;
        }
    }


    public static <V> String toString(final SymbolicList<V> v) {
        int counter = v.size();
        if (counter == 0)
            return "[]";
        Engine.assume(counter > 0);

        // TODO: use less complex approach
        String res = "[";

        for (int i = 0, c = counter; i != c; i++) {
            res = res.concat(toString(v.get(i)));

            if (counter-- > 1)
                res = res.concat(", ");
        }

        return res.concat("]");
    }

    public static <K, V> String toString(final Map<K, V> v) {
        int count = v.size();
        if (count == 0)
            return "{}";
        Engine.assume(count > 0);

        // TODO: use less complex approach
        String res = "{";

        final Map.Container<K, V> unseen = v.map.duplicate();
        while (count != 0) {
            final K key = unseen.anyKey();
            final V value = unseen.get(key);
            unseen.remove(key);

            res = res
                    .concat(toString(key))
                    .concat("=")
                    .concat(toString(value));

            if (count-- > 1)
                res = res.concat(", ");
        }

        return res.concat("}");
    }

    public static String toString(final Object v) {
        return v == null ? "null" : v.toString();
    }

    @SuppressWarnings("ConstantConditions")
    public static String toString(final Object[] objects) {
        int counter = objects.length;
        Engine.assume(counter >= 0);
        if (counter == 0)
            return "[]";

        String str = "[";

        for (int i = 0, c = counter; i < c; i++) {
            str = str.concat(toString(objects[i]));

            if (counter-- > 1)
                str = str.concat(", ");
        }

        return str.concat("]");
    }

    // fool-proofing
    public static String toString(final String v) {
        return v == null ? "null" : v;
    }


    public static int hashCode(final boolean v) {
        // NOTE: copied from https://github.com/openjdk/jdk11/blob/master/src/java.base/share/classes/java/lang/Boolean.java#L227
        return v ? 1231 : 1237;
    }

    public static int hashCode(final byte v) {
        return (int) v;
    }

    public static int hashCode(final short v) {
        return (int) v;
    }

    public static int hashCode(final int v) {
        return v;
    }

    @SuppressWarnings("UseHashCodeMethodInspection")
    public static int hashCode(final long v) {
        return (int) (v ^ (v >>> 32));
    }

    public static int hashCode(final char v) {
        return (int) v;
    }

    public static int hashCode(final float v) {
        // FIXME: use less complex approach
        return Float.hashCode(v);
    }

    public static int hashCode(final double v) {
        // FIXME: use less complex approach
        return Double.hashCode(v);
    }

    public static <V> int hashCode(SymbolicList<V> v, int start, int end) {
        if (v == null)
            return 0;

        Engine.assume(start >= 0);
        Engine.assume(end >= 0 && end <= v.size());
        int res = 1;
        for (int i = start; i < end; i++)
            res = 31 * res + hashCode(v.get(i));

        return res;
    }

    public static <V> int hashCode(SymbolicList<V> v) {
        return hashCode(v, 0, v.size());
    }

    public static <K, V> int hashCode(final Map<K, V> v) {
        if (v == null)
            return 0;

        int count = v.size();
        if (count == 0)
            return 1;
        Engine.assume(count > 0);

        // TODO: use less complex approach
        int res = 1;

        final Map.Container<K, V> unseen = v.map.duplicate();
        while (count != 0) {
            final K key = unseen.anyKey();

            res += hashCode(key) ^ hashCode(unseen.get(key));

            unseen.remove(key);
            count--;
        }

        return res;
    }

    public static int hashCode(final Object v) {
        return v == null ? 0 : v.hashCode();
    }

    @SuppressWarnings({"ConstantConditions", "ForLoopReplaceableByForEach"})
    public static int hashCode(final Object[] v) {
        if (v == null)
            return 0;

        final int count = v.length;
        Engine.assume(count >= 0);
        if (count == 0)
            return 1;

        int res = 1;

        for (int i = 0; i < count; i++)
            res = 31 * res + hashCode(v[i]);

        return res;
    }


    public static boolean equals(final boolean a, final boolean b) {
        return a == b;
    }

    public static boolean equals(final byte a, final byte b) {
        return a == b;
    }

    public static boolean equals(final short a, final short b) {
        return a == b;
    }

    public static boolean equals(final int a, final int b) {
        return a == b;
    }

    public static boolean equals(final long a, final long b) {
        return a == b;
    }

    public static boolean equals(final char a, final char b) {
        return a == b;
    }

    public static boolean equals(final float a, final float b) {
        // FIXME: use Float.compare(a, b) ?
        return a == b;
    }

    public static boolean equals(final double a, final double b) {
        // FIXME: use Double.compare(a, b) ?
        return a == b;
    }

    public static <V> boolean equals(final SymbolicList<V> a, final SymbolicList<V> b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;

        int length = a.size();
        if (b.size() != length)
            return false;

        Engine.assume(length >= 0);
        for (int i = 0; i < length; i++)
            if (!equals(a.get(i), b.get(i)))
                return false;

        return true;
    }

    public static <K> boolean equals(final Map<K, ?> a, final Map<K, ?> b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;

        int length = a.size();
        if (b.size() != length)
            return false;

        if (length == 0)
            return true;
        Engine.assume(length >= 0);

        final Map.Container<K, ?> unseen = a.map.duplicate();
        while (length != 0) {
            final K key = unseen.anyKey();

            if (!b.hasKey(key))
                return false;
            if (!equals(a.get(key), b.get(key)))
                return false;

            unseen.remove(key);
            length--;
        }
        return true;
    }

    public static boolean equals(final Object a, final Object b) {
        if (a == b)
            return true;

        if (a == null || b == null)
            return false;

        return a.equals(b);
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean equals(final Object[] a, final Object[] b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;

        final int length = a.length;
        if (b.length != length)
            return false;

        Engine.assume(length >= 0);
        for (int i = 0; i < length; i++)
            if (!equals(a[i], b[i]))
                return false;

        return true;
    }


    // A marker class to attach unknown actions to. Makes it easier to do manual code inspections.
    public static final class UnknownAction {
    }


    // a helper class for complex "array<T>"-related actions
    public static final class ArrayActions {

        @SuppressWarnings({"DataFlowIssue", "SuspiciousSystemArraycopy"})
        public static <T> void copy(final Object src, final int srcPos,
                                    final Object dst, final int dstPos,
                                    final int count) {
            if (count <= 0)
                return;

            Engine.assume(src != null);
            Engine.assume(dst != null);

            System.arraycopy(src, srcPos, dst, dstPos, count);
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fill(final byte[] arr, final byte value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            for (int i = 0; i < count; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fill(final short[] arr, final short value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            for (int i = 0; i < count; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fill(final int[] arr, final int value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            for (int i = 0; i < count; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fill(final long[] arr, final long value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            for (int i = 0; i < count; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fill(final float[] arr, final float value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            for (int i = 0; i < count; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fill(final double[] arr, final double value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            for (int i = 0; i < count; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fill(final char[] arr, final char value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            for (int i = 0; i < count; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static <T> void fill(final T[] arr, final T value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            for (int i = 0; i < count; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fillRange(final byte[] arr,
                                     final int fromIndex, final int toIndex,
                                     final byte value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            Engine.assume(0 <= fromIndex);
            Engine.assume(fromIndex <= toIndex);
            Engine.assume(toIndex <= count);

            for (int i = fromIndex; i < toIndex; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fillRange(final short[] arr,
                                     final int fromIndex, final int toIndex,
                                     final short value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            Engine.assume(0 <= fromIndex);
            Engine.assume(fromIndex <= toIndex);
            Engine.assume(toIndex <= count);

            for (int i = fromIndex; i < toIndex; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fillRange(final int[] arr,
                                     final int fromIndex, final int toIndex,
                                     final int value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            Engine.assume(0 <= fromIndex);
            Engine.assume(fromIndex <= toIndex);
            Engine.assume(toIndex <= count);

            for (int i = fromIndex; i < toIndex; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fillRange(final long[] arr,
                                     final int fromIndex, final int toIndex,
                                     final long value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            Engine.assume(0 <= fromIndex);
            Engine.assume(fromIndex <= toIndex);
            Engine.assume(toIndex <= count);

            for (int i = fromIndex; i < toIndex; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fillRange(final float[] arr,
                                     final int fromIndex, final int toIndex,
                                     final float value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            Engine.assume(0 <= fromIndex);
            Engine.assume(fromIndex <= toIndex);
            Engine.assume(toIndex <= count);

            for (int i = fromIndex; i < toIndex; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fillRange(final double[] arr,
                                     final int fromIndex, final int toIndex,
                                     final double value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            Engine.assume(0 <= fromIndex);
            Engine.assume(fromIndex <= toIndex);
            Engine.assume(toIndex <= count);

            for (int i = fromIndex; i < toIndex; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static void fillRange(final char[] arr,
                                     final int fromIndex, final int toIndex,
                                     final char value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            Engine.assume(0 <= fromIndex);
            Engine.assume(fromIndex <= toIndex);
            Engine.assume(toIndex <= count);

            for (int i = fromIndex; i < toIndex; i++)
                arr[i] = value;
        }

        @SuppressWarnings({"DataFlowIssue", "ConstantValue"})
        public static <T> void fillRange(final T[] arr,
                                         final int fromIndex, final int toIndex,
                                         final T value) {
            Engine.assume(arr != null);

            final int count = arr.length;
            Engine.assume(count >= 0);

            Engine.assume(0 <= fromIndex);
            Engine.assume(fromIndex <= toIndex);
            Engine.assume(toIndex <= count);

            for (int i = fromIndex; i < toIndex; i++)
                arr[i] = value;
        }
    }

    // a helper class for complex "list<T>"-related actions
    public static final class ListActions {
        private static final int INDEX_NOT_FOUND = -1;

        private static void addAssumptions(SymbolicList<?> list, int from, int to) {
            // general assumptions for this function to do something useful
            Engine.assume(list != null);
            Engine.assume(0 <= from);
            Engine.assume(from <= to);
        }

        public static int find(SymbolicList<?> list, Object value, int from, int to) {
            addAssumptions(list, from, to);

            // TODO: is there a more efficient solution?
            if (value == null) {
                for (int i = from; i < to; i++) {
                    if (list.get(i) == null)
                        return i;
                }
                return INDEX_NOT_FOUND;
            }

            for (int i = from; i < to; i++) {
                final Object item = list.get(i);
                if (value == item || value.equals(item))
                    return i;
            }

            return INDEX_NOT_FOUND;
        }

        public static int findBack(SymbolicList<?> list, Object value, int from, int to) {
            addAssumptions(list, from, to);

            // TODO: is there a more efficient solution?
            if (value == null) {
                for (int i = to - 1; i >= from; i--) {
                    if (list.get(i) == null)
                        return i;
                }
                return INDEX_NOT_FOUND;
            }

            for (int i = to - 1; i >= from; i--) {
                final Object item = list.get(i);
                if (value == item || value.equals(item))
                    return i;
            }

            return INDEX_NOT_FOUND;
        }
    }


    @SuppressWarnings("DataFlowIssue")
    public static final class Map<K, V> {
        Container<K, V> map;

        public static abstract class Container<CK, CV> {
            protected static final byte KIND_HASHMAP = 1;
            protected static final byte KIND_IDENTITY_MAP = 2;

            protected final byte kind;

            protected Container(final byte kind) {
                this.kind = kind;
            }

            abstract void merge(Container<CK, CV> container);

            abstract Container<CK, CV> getCleanInstance();

            abstract Container<CK, CV> duplicate();

            // other methods are proxies for USVM objects

            abstract boolean containsKey(CK key);

            abstract CK anyKey();

            abstract CV get(CK key);

            abstract void set(CK key, CV value);

            abstract void remove(CK key);

            abstract int size();
        }

        public Map(final Container<K, V> container) {
            this.map = container;
        }

        public boolean hasKey(final K key) {
            Engine.assume(map != null);
            return map.containsKey(key);
        }

        public K anyKey() {
            Engine.assume(map != null);
            return map.anyKey();
        }

        public V get(final K key) {
            Engine.assume(map != null);
            return map.get(key);
        }

        public void set(final K key, final V value) {
            Engine.assume(map != null);
            map.set(key, value);
        }

        public void remove(final K key) {
            Engine.assume(map != null);
            map.remove(key);
        }

        public int size() {
            Engine.assume(map != null);
            return map.size();
        }

        public void union(final Map<K, V> other) {
            Engine.assume(map != null);

            final Container<K, V> otherMap = other.map;
            Engine.assume(otherMap != null);

            if (map.kind == otherMap.kind) {
                map.merge(otherMap);
            } else {
                int count = otherMap.size();
                if (count != 0) {
                    Engine.assume(count > 0);

                    final Container<K, V> unseen = otherMap.duplicate();
                    while (count != 0) {
                        final K key = unseen.anyKey();

                        // behaving exactly as compatible versions
                        map.set(key, unseen.get(key));

                        unseen.remove(key);
                        count--;
                    }
                }
            }
        }

        public void intersection(final Map<K, V> other) {
            Engine.assume(map != null);

            final Container<K, V> otherMap = other.map;
            Engine.assume(otherMap != null);

            final Container<K, V> thisMap = map;
            map = thisMap.getCleanInstance();
            Engine.assume(map != null);

            int count = otherMap.size();
            if (count != 0) {
                Engine.assume(count > 0);

                final Container<K, V> unseen = otherMap.duplicate();
                while (count != 0) {
                    final K key = unseen.anyKey();

                    if (thisMap.containsKey(key))
                        // preferring items from the other container (similar to "union")
                        map.set(key, unseen.get(key));

                    unseen.remove(key);
                    count--;
                }
            }
        }

        public Map<K, V> duplicate() {
            Engine.assume(map != null);
            return new Map<>(map.duplicate());
        }

        @Override
        public String toString() {
            return LibSLRuntime.toString(this);
        }

        @Override
        public int hashCode() {
            return LibSLRuntime.hashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Map) {
                @SuppressWarnings("unchecked") final Map<K, V> otherMap = (Map<K, V>) obj;
                return LibSLRuntime.equals(this, otherMap);
            } else {
                return false;
            }
        }
    }


    public static final class HashMapContainer<K, V> extends Map.Container<K, V> {
        private final SymbolicMap<K, V> map = Engine.makeSymbolicMap();

        public HashMapContainer() {
            super(KIND_HASHMAP);
        }

        @SuppressWarnings("DataFlowIssue")
        @Override
        public void merge(Map.Container<K, V> container) {
            Engine.assume(container instanceof HashMapContainer);
            HashMapContainer<K, V> other = (HashMapContainer<K, V>) container;
            map.merge(other.map);
        }

        @Override
        public Map.Container<K, V> getCleanInstance() {
            return new HashMapContainer<>();
        }

        @Override
        public Map.Container<K, V> duplicate() {
            final HashMapContainer<K, V> obj = new HashMapContainer<>();
            obj.map.merge(this.map);
            return obj;
        }

        @Override
        public boolean containsKey(K key) {
            return map.containsKey(key);
        }

        @Override
        public K anyKey() {
            return map.anyKey();
        }

        @Override
        public V get(K key) {
            return map.get(key);
        }

        @Override
        public void set(K key, V value) {
            map.set(key, value);
        }

        @Override
        public void remove(K key) {
            map.remove(key);
        }

        @Override
        public int size() {
            return map.size();
        }
    }


    public static final class IdentityMapContainer<K, V> extends Map.Container<K, V> {
        private final SymbolicIdentityMap<K, V> map = Engine.makeSymbolicIdentityMap();

        public IdentityMapContainer() {
            super(KIND_IDENTITY_MAP);
        }

        @SuppressWarnings("DataFlowIssue")
        @Override
        public void merge(Map.Container<K, V> container) {
            Engine.assume(container instanceof IdentityMapContainer);
            IdentityMapContainer<K, V> other = (IdentityMapContainer<K, V>) container;
            map.merge(other.map);
        }

        @Override
        public Map.Container<K, V> getCleanInstance() {
            return new IdentityMapContainer<>();
        }

        @Override
        public Map.Container<K, V> duplicate() {
            final IdentityMapContainer<K, V> obj = new IdentityMapContainer<>();
            obj.map.merge(this.map);
            return obj;
        }

        @Override
        public boolean containsKey(K key) {
            return map.containsKey(key);
        }

        @Override
        public K anyKey() {
            return map.anyKey();
        }

        @Override
        public V get(K key) {
            return map.get(key);
        }

        @Override
        public void set(K key, V value) {
            map.set(key, value);
        }

        @Override
        public void remove(K key) {
            map.remove(key);
        }

        @Override
        public int size() {
            return map.size();
        }
    }

}

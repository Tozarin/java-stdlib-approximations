package generated.java.lang;

import java.io.Serializable;
import java.lang.Object;
import java.lang.StringIndexOutOfBoundsException;
import java.util.Arrays;

import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;
import runtime.LibSLRuntime;

@SuppressWarnings({"unused", "overrides"})
@Approximate(java.lang.String.class)
public class StringImpl implements Serializable {

    @java.io.Serial
    private static final long serialVersionUID = -6849794470754667710L;

    private static final int STRING_LENGTH_MAX = 50;

    static final byte UTF16 = 1;

    static final byte LATIN1 = 0;

    static final boolean COMPACT_STRINGS;

    static {
        // Enforce coder == UTF8
        COMPACT_STRINGS = true;
    }

    private final byte[] value;

    // TODO: everywhere add assume 'coder == LATIN1' #Approx
    private final byte coder;

    public static StringImpl _emptyString = new StringImpl(new byte[] {});

    public static byte _currentCoder() {
        return COMPACT_STRINGS ? LATIN1 : UTF16;
    }

    private int _charPosToBytePos(int charPos) {
        return charPos << coder;
    }

    private int _bytePosToCharPos(int charPos) {
        return charPos >> coder;
    }

    private static void _checkOffset(int offset, int length) {
        if (offset < 0 || offset >= length)
            throw new IndexOutOfBoundsException();
    }

    public static byte[] _charToBytes(char value) {
        if (COMPACT_STRINGS) {
            byte[] bytes = new byte[1];
            Engine.assume(value <= 0xFF);
            bytes[0] = (byte) value;
            return bytes;
        }

        byte[] bytes = new byte[2];
        bytes[0] = (byte) value;
        bytes[1] = (byte) (value >> 8);
        return bytes;
    }

    private static char _charFrom2Bytes(byte fst, byte snd) {
        return (char) (((char) fst) | (((char) snd) << 8));
    }

    public static char _charFromByte(byte b) {
        return (char) (((int) b) & 0xFF);
    }

    public static char _bytesToChar(byte[] bytes, int bytePos) {
        if (COMPACT_STRINGS)
            return _charFromByte(bytes[bytePos]);

        return _charFrom2Bytes(bytes[bytePos], bytes[bytePos + 1]);
    }

    public static void _addCharToBytes(byte[] bytes, int index, char value) {
        if (COMPACT_STRINGS) {
            Engine.assume(value <= 0xFF);
            bytes[index] = (byte) value;
        } else {
            bytes[index++] = (byte) value;
            bytes[index] = (byte) (value >> 8);
        }
    }

    public static byte[] _getBytes(char[] chars) {
        int size = chars.length << _currentCoder();
        byte[] bytes = new byte[size];
        if (COMPACT_STRINGS) {
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                Engine.assume(c <= 0xFF);
                bytes[i] = (byte) chars[i];
            }
        } else {
            int byteIndex = 0;
            for (char c : chars) {
                bytes[byteIndex++] = (byte) c;
                bytes[byteIndex] = (byte) (c >> 8);
                byteIndex++;
            }
        }

        return bytes;
    }

    public static char[] _getChars(byte[] bytes) {
        int size = bytes.length >> _currentCoder();
        char[] chars = new char[size];
        if (COMPACT_STRINGS) {
            for (int i = 0; i < bytes.length; i++) {
                byte c = bytes[i];
                chars[i] = _charFromByte(bytes[i]);
            }
        } else {
            int byteIndex = 0;
            for (int i = 0; i < chars.length; i++) {
                char c = _charFrom2Bytes(bytes[byteIndex], bytes[byteIndex + 1]);
                chars[i] = c;
                byteIndex += 2;
            }
        }

        return chars;
    }

    private StringImpl(byte[] value, byte coder) {
        Engine.assume(coder == _currentCoder());
        this.value = value;
        this.coder = coder;
    }

    public StringImpl() {
        this(new byte[0], _currentCoder());
    }

    public StringImpl(StringImpl original) {
        this(original.value, original.coder);
    }

    public StringImpl(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    private static void _checkBoundsOffCount(int offset, int count, int length) {
        if (offset < 0 || count < 0 || offset > length - count) {
            throw new StringIndexOutOfBoundsException();
        }
    }

    public StringImpl(byte[] bytes, int offset, int length) {
        _checkBoundsOffCount(offset, length, bytes.length);
        this.value = new byte[length];
        this.coder = _currentCoder();
        LibSLRuntime.ArrayActions.copy(bytes, offset, this.value, 0, length);
    }

    public static StringImpl copyValueOf(char[] data) {
        byte[] bytes = _getBytes(data);
        return new StringImpl(bytes);
    }

    public static StringImpl copyValueOf(char[] data, int offset, int count) {
        char[] segment = new char[count];
        LibSLRuntime.ArrayActions.copy(data, offset, segment, 0, count);
        return copyValueOf(segment);
    }

    public static java.lang.String valueOf(Object x) {
        return LibSLRuntime.toString(x);
    }

    public static java.lang.String valueOf(boolean x) {
        return LibSLRuntime.toString(x);
    }

    public static StringImpl valueOf(char x) {
        byte[] bytes = _charToBytes(x);
        return new StringImpl(bytes);
    }

    public static StringImpl valueOf(char[] data) {
        byte[] bytes = _getBytes(data);
        return new StringImpl(bytes);
    }

    public static StringImpl valueOf(char[] data, int offset, int count) {
        return copyValueOf(data, offset, count);
    }

    public static java.lang.String valueOf(double x) {
        return LibSLRuntime.toString(x);
    }

    public static java.lang.String valueOf(float x) {
        return LibSLRuntime.toString(x);
    }

    public static java.lang.String valueOf(int x) {
        return LibSLRuntime.toString(x);
    }

    public static java.lang.String valueOf(long x) {
        return LibSLRuntime.toString(x);
    }

    @SuppressWarnings("DataFlowIssue")
    private void _assumeInvariants(StringImpl obj) {
        Engine.assume(obj.coder == _currentCoder());
        Engine.assume(obj.value != null);
        int len = obj.value.length >> obj.coder;
        Engine.assume(len >= 0);
        Engine.assumeSymbolic(obj, len <= STRING_LENGTH_MAX);
    }

    private void _assumeInvariants() {
        _assumeInvariants(this);
    }

    boolean isLatin1() {
        _assumeInvariants();
        return _currentCoder() == LATIN1;
    }

    byte coder() {
        _assumeInvariants();
        return coder;
    }

    public int length() {
        _assumeInvariants();
        return value.length >> coder;
    }

    public char charAt(int index) {
        _assumeInvariants();

        int byteIndex = _charPosToBytePos(index);
        _checkOffset(byteIndex, this.value.length);

        return _bytesToChar(this.value, byteIndex);
    }

    public StringImpl concat(StringImpl str) {
        _assumeInvariants();
        _assumeInvariants(str);

        byte[] otherVal = str.value;
        int otherLen = otherVal.length;
        if (otherLen == 0)
            return this;

        int newLength = this.value.length + otherLen;
        byte[] newValue = new byte[newLength];
        LibSLRuntime.ArrayActions.copy(this.value, 0, newValue, 0, this.value.length);
        LibSLRuntime.ArrayActions.copy(otherVal, 0, newValue, this.value.length, otherLen);
        return new StringImpl(newValue);
    }

    public byte[] getBytes() {
        _assumeInvariants();
        return Arrays.copyOf(this.value, this.value.length);
    }

    public void getBytes(int srcBegin, int srcEnd, byte[] dst, int dstBegin) {
        _assumeInvariants();

        int srcByteBegin = _charPosToBytePos(srcBegin);
        int srcByteEnd = _charPosToBytePos(srcEnd);
        int dstByteBegin = _charPosToBytePos(dstBegin);

        if (srcByteBegin < 0)
            throw new StringIndexOutOfBoundsException(srcBegin);

        if (this.value.length < srcByteEnd)
            throw new StringIndexOutOfBoundsException(srcEnd);

        int count = srcByteEnd - srcByteBegin;
        if (count < 0)
            throw new StringIndexOutOfBoundsException(count);

        LibSLRuntime.ArrayActions.copy(this.value, srcByteBegin, dst, dstByteBegin, count);
    }

    public static boolean latin1Equals(byte[] value, byte[] other) {
        /*
            `Engine.arrayEquals` checks arrays lengths:
                - if they are equal, but symbolic, returns null
                - Otherwise checks equality of each element and returns non-null value
         */
        Boolean arrayEquals = Engine.arrayEquals(value, other);
        if (arrayEquals != null)
            return arrayEquals;

        for(int i = 0; i < value.length; ++i) {
            if (value[i] != other[i]) {
                return false;
            }
        }

        return true;
    }

    public boolean equals(Object anObject) {
        _assumeInvariants();
        if (this == anObject) {
            return true;
        } else {
            if (anObject instanceof StringImpl) {
                StringImpl aString = (StringImpl)anObject;
                Engine.assume(aString.value != value);
                _assumeInvariants(aString);
                return (!COMPACT_STRINGS || this.coder == aString.coder) && latin1Equals(this.value, aString.value);
            }

            return false;
        }
    }

    public boolean isEmpty() {
        _assumeInvariants();
        return value.length == 0;
    }
}

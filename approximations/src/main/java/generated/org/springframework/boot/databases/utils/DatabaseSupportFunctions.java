package generated.org.springframework.boot.databases.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class DatabaseSupportFunctions {

    // region Comparers

    static Integer basicComparer(Object left, Object right) {
        if (left == null && right == null) return null;
        if (left == null) return -1;
        if (right == null) return 1;
        return null;
    }

    static Integer comparer(String left, String right) {
        Integer base = basicComparer(left, right);
        return base == null ? left.compareTo(right) : base;
    }
    static Integer comparer(Integer left, Integer right) {
        Integer base = basicComparer(left, right);
        return base == null ? left - right : base;
    }
    static Integer comparer(Long left, Long right) {
        Integer base = basicComparer(left, right);
        return base == null ? left.compareTo(right) : base;
    }
    static Integer comparer(Boolean left, Boolean right) {
        Integer base = basicComparer(left, right);
        return base == null ? left.compareTo(right) : base;
    }
    static Integer comparer(Float left, Float right) {
        Integer base = basicComparer(left, right);
        return base == null ? left.compareTo(right) : base;
    }
    static Integer comparer(Double left, Double right) {
        Integer base = basicComparer(left, right);
        return base == null ? left.compareTo(right) : base;
    }
    static Integer comparer(BigInteger left, BigInteger right) {
        Integer base = basicComparer(left, right);
        return base == null ? left.compareTo(right) : base;
    }
    static Integer comparer(BigDecimal left, BigDecimal right) {
        Integer base = basicComparer(left, right);
        return base == null ? left.compareTo(right) : base;
    }
    static Integer comparer(LocalDateTime left, LocalDateTime right) {
        Integer base = basicComparer(left, right);
        return base == null ? left.compareTo(right) : base;
    }

    // endregion

    // region Equals

    static Boolean equals(String left, String right) { return left.equals(right); }
    static Boolean equals(Integer left, Integer right) { return left.equals(right); }
    static Boolean equals(Long left, Long right) { return left.equals(right); }
    static Boolean equals(Boolean left, Boolean right) { return left.equals(right); }
    static Boolean equals(Float left, Float right) { return left.equals(right); }
    static Boolean equals(Double left, Double right) { return left.equals(right); }
    static Boolean equals(BigInteger left, BigInteger right) { return left.equals(right); }
    static Boolean equals(BigDecimal left, BigDecimal right) { return left.equals(right); }
    static Boolean equals(LocalDateTime left, LocalDateTime right) { return left.equals(right); }

    // endregion

    // region Like function

    // https://stackoverflow.com/a/54437062
    public static boolean like(String expr, String pattern, String esc, boolean caseSenc) {
        char escCh = esc == null ? null : esc.charAt(0);
        int exprLength = expr.length();
        int patternLength = pattern.length();

        if (exprLength == 0 || patternLength == 0) {
            return false;
        }

        boolean fuzzy = false;
        char lastCharOfExp = 0;
        int positionOfSource = 0;

        for (int i = 0; i < patternLength; i++) {
            char ch = pattern.charAt(i);

            boolean escape = false;
            if (lastCharOfExp == escCh) {
                if (ch == '%' || ch == '_') {
                    escape = true;
                }
            }

            if (!escape && ch == '%') {
                fuzzy = true;
            } else if (!escape && ch == '_') {
                if (positionOfSource >= exprLength) {
                    return false;
                }
                positionOfSource++;

            } else if (ch != escCh) {
                if (positionOfSource >= exprLength) {
                    return false;
                }

                if (lastCharOfExp == '%') {
                    int tp = expr.indexOf(ch);
                    if (tp == -1) {
                        return false;
                    }

                    if (tp >= positionOfSource) {
                        positionOfSource = tp + 1;
                        if (i == patternLength - 1 && positionOfSource < exprLength) {
                            return false;
                        }
                    } else {
                        return false;
                    }

                } else if (compareChars(expr.charAt(positionOfSource), ch, caseSenc)) {
                    positionOfSource++;

                } else {
                    return false;
                }
            }

            lastCharOfExp = ch;
        }

        return fuzzy || positionOfSource >= exprLength;
    }

    private static boolean compareChars(char l, char r, boolean caseSenc) {
        return caseSenc ?
                l == r
                : Character.toLowerCase(l) == Character.toLowerCase(r);
    }

    // endregion
}

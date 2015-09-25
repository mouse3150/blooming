package cn.com.esrichina.adapter.utils;

public final class QuickMath {

    private QuickMath() {
    }

    public static boolean isPowerOfTwo(long x) {
        return (x & (x - 1)) == 0;
    }

    public static int modPowerOfTwo(int a, int b) {
        return a & (b - 1);
    }

    public static long modPowerOfTwo(long a, int b) {
        return a & (b - 1);
    }

    public static int nextPowerOfTwo(int value) {
        if (!isPowerOfTwo(value)) {
            value--;
            value |= value >> 1;
            value |= value >> 2;
            value |= value >> 4;
            value |= value >> 8;
            value |= value >> 16;
            value++;
        }
        return value;
    }

    public static long nextPowerOfTwo(long value) {
        if (!isPowerOfTwo(value)) {
            value--;
            value |= value >> 1;
            value |= value >> 2;
            value |= value >> 4;
            value |= value >> 8;
            value |= value >> 16;
            value |= value >> 32;
            value++;
        }
        return value;
    }

    public static int log2(int value) {
        return 31 - Integer.numberOfLeadingZeros(value);
    }

    public static int log2(long value) {
        return 63 - Long.numberOfLeadingZeros(value);
    }

    public static int divideByAndCeilToInt(double d, int k) {
        return (int) Math.ceil(d / k);
    }

    public static long divideByAndCeilToLong(double d, int k) {
        return (long) Math.ceil(d / k);
    }

    public static int divideByAndRoundToInt(double d, int k) {
        return (int) Math.rint(d / k);
    }

    public static long divideByAndRoundToLong(double d, int k) {
        return (long) Math.rint(d / k);
    }

    public static int normalize(int value, int factor) {
        return divideByAndCeilToInt(value, factor) * factor;
    }

    public static long normalize(long value, int factor) {
        return divideByAndCeilToLong(value, factor) * factor;
    }

    /**
     * Compares two integers
     *
     * @param i1 First number to compare with second one
     * @param i2 Second number to compare with first one
     * @return +1 if i1 > i2, -1 if i2 > i1, 0 if i1 and i2 are equals
     */
    public static int compareIntegers(int i1, int i2) {
        if (i1 > i2) {
            return +1;
        } else if (i2 > i1) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Compares two longs
     *
     * @param l1 First number to compare with second one
     * @param l2 Second number to compare with first one
     * @return +1 if l1 > l2, -1 if l2 > l1, 0 if l1 and l2 are equals
     */
    public static int compareLongs(long l1, long l2) {
        if (l1 > l2) {
            return +1;
        } else if (l2 > l1) {
            return -1;
        } else {
            return 0;
        }
    }
}

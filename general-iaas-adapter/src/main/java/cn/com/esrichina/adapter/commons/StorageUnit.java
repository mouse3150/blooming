package cn.com.esrichina.adapter.commons;


import static cn.com.esrichina.adapter.utils.QuickMath.divideByAndRoundToInt;

/**
 * 内存单位：B,KB,MB,GB
 *
 */
public enum StorageUnit {

    /**
     * Bytes
     */
    BYTES {
        public long convert(long value, StorageUnit m) {
            return m.toBytes(value);
        }

        public long toBytes(long value) {
            return value;
        }

        public long toKiloBytes(long value) {
            return divideByAndRoundToInt(value, K);
        }

        public long toMegaBytes(long value) {
            return divideByAndRoundToInt(value, M);
        }

        public long toGigaBytes(long value) {
            return divideByAndRoundToInt(value, G);
        }
    },

    /**
     * Kilobytes
     */
    KILOBYTES {
        public long convert(long value, StorageUnit m) {
            return m.toKiloBytes(value);
        }

        public long toBytes(long value) {
            return value * K;
        }

        public long toKiloBytes(long value) {
            return value;
        }

        public long toMegaBytes(long value) {
            return divideByAndRoundToInt(value, K);
        }

        public long toGigaBytes(long value) {
            return divideByAndRoundToInt(value, M);
        }
    },

    /**
     * Megabytes
     */
    MEGABYTES {
        public long convert(long value, StorageUnit m) {
            return m.toMegaBytes(value);
        }

        public long toBytes(long value) {
            return value * M;
        }

        public long toKiloBytes(long value) {
            return value * K;
        }

        public long toMegaBytes(long value) {
            return value;
        }

        public long toGigaBytes(long value) {
            return divideByAndRoundToInt(value, K);
        }
    },

    /**
     * Gigabytes
     */
    GIGABYTES {
        public long convert(long value, StorageUnit m) {
            return m.toGigaBytes(value);
        }

        public long toBytes(long value) {
            return value * G;
        }

        public long toKiloBytes(long value) {
            return value * M;
        }

        public long toMegaBytes(long value) {
            return value * K;
        }

        public long toGigaBytes(long value) {
            return value;
        }
    };

    static final int POWER = 10;
    static final int K = 1 << POWER;
    static final int M = 1 << (POWER * 2);
    static final int G = 1 << (POWER * 3);

    public abstract long convert(long value, StorageUnit m);

    public abstract long toBytes(long value);

    public abstract long toKiloBytes(long value);

    public abstract long toMegaBytes(long value);

    public abstract long toGigaBytes(long value);
}

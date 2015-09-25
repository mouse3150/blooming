package cn.com.esrichina.adapter.commons;


public final class Memory {

    private static final int PRETTY_FORMAT_LIMIT = 10;

    private final long value;
    private final StorageUnit unit;

    public Memory(long value) {
        this(value, StorageUnit.BYTES);
    }

    public Memory(long value, StorageUnit unit) {
        if (value < 0) {
            throw new IllegalArgumentException("Memory size cannot be negative! -> " + value);
        }
        if (unit == null) {
            throw new NullPointerException("Memory Unit is required!");
        }
        this.value = value;
        this.unit = unit;
    }

    public long getValue() {
        return value;
    }

    public StorageUnit getUnit() {
        return unit;
    }

    public long bytes() {
        return unit.toBytes(value);
    }

    public long kiloBytes() {
        return unit.toKiloBytes(value);
    }

    public long megaBytes() {
        return unit.toMegaBytes(value);
    }

    public long gigaBytes() {
        return unit.toGigaBytes(value);
    }

    public static Memory parse(String value) {
        return parse(value, StorageUnit.BYTES);
    }

    public static Memory parse(String value, StorageUnit defaultUnit) {
        if (value == null || value.length() == 0) {
            return new Memory(0, StorageUnit.BYTES);
        }

        StorageUnit unit = defaultUnit;
        final char last = value.charAt(value.length() - 1);
        if (!Character.isDigit(last)) {
            value = value.substring(0, value.length() - 1);
            switch (last) {
                case 'g':
                case 'G':
                    unit = StorageUnit.GIGABYTES;
                    break;

                case 'm':
                case 'M':
                    unit = StorageUnit.MEGABYTES;
                    break;

                case 'k':
                case 'K':
                    unit = StorageUnit.KILOBYTES;
                    break;

                default:
                    throw new IllegalArgumentException("Could not determine memory unit of " + value + last);
            }
        }

        return new Memory(Long.parseLong(value), unit);
    }

    public String toPrettyString() {
        return toPrettyString(value, unit);
    }

    @Override
    public String toString() {
        return value + " " + unit.toString();
    }


    public static String toPrettyString(long size) {
        return toPrettyString(size, StorageUnit.BYTES);
    }

    public static String toPrettyString(long size, StorageUnit unit) {
        if (unit.toGigaBytes(size) >= PRETTY_FORMAT_LIMIT) {
            return unit.toGigaBytes(size) + " GB";
        }
        if (unit.toMegaBytes(size) >= PRETTY_FORMAT_LIMIT) {
            return unit.toMegaBytes(size) + " MB";
        }
        if (unit.toKiloBytes(size) >= PRETTY_FORMAT_LIMIT) {
            return unit.toKiloBytes(size) + " KB";
        }
        if (size % StorageUnit.K == 0) {
            return unit.toKiloBytes(size) + " KB";
        }
        return size + " bytes";
    }
}

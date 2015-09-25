package cn.com.esrichina.adapter.commons;

public class Storage {
	private static final int PRETTY_FORMAT_LIMIT = 10;

    private final long value;
    private final StorageUnit unit;

    public Storage(long value) {
        this(value, StorageUnit.GIGABYTES);
    }

    public Storage(long value, StorageUnit unit) {
        if (value < 0) {
            throw new IllegalArgumentException("Storage size cannot be negative! -> " + value);
        }
        if (unit == null) {
            throw new NullPointerException("Storage Unit is required!");
        }
        
        if (unit == StorageUnit.BYTES || unit == StorageUnit.KILOBYTES) {
            throw new NullPointerException("Storage Unit is unreasonable!");
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

    public long megaBytes() {
        return unit.toMegaBytes(value);
    }

    public long gigaBytes() {
        return unit.toGigaBytes(value);
    }

    public static Storage parse(String value) {
        return parse(value, StorageUnit.GIGABYTES);
    }

    public static Storage parse(String value, StorageUnit defaultUnit) {
        if (value == null || value.length() == 0) {
            return new Storage(0, StorageUnit.GIGABYTES);
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

                default:
                    throw new IllegalArgumentException("Could not determine storage unit of " + value + last);
            }
        }

        return new Storage(Long.parseLong(value), unit);
    }

    public String toPrettyString() {
        return toPrettyString(value, unit);
    }

    @Override
    public String toString() {
        return value + " " + unit.toString();
    }


    public static String toPrettyString(long size) {
        return toPrettyString(size, StorageUnit.GIGABYTES);
    }

    public static String toPrettyString(long size, StorageUnit unit) {
        if (unit.toGigaBytes(size) >= PRETTY_FORMAT_LIMIT) {
            return unit.toGigaBytes(size) + " GB";
        }
        if (unit.toMegaBytes(size) >= PRETTY_FORMAT_LIMIT) {
            return unit.toMegaBytes(size) + " MB";
        }

        return size + " GB";
    }
}

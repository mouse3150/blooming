package cn.com.esrichina.adapter.commons;

public class Cpu {
	
	private CpuUnit unit;
	private long value;
	
	public Cpu(long value, CpuUnit unit) {
        if (value < 0) {
            throw new IllegalArgumentException("Cpu size cannot be negative! -> " + value);
        }
        if (unit == null) {
            throw new NullPointerException("CpuUnit is required!");
        }
		this.unit = unit;
		this.value = value;
	}

	public CpuUnit getUnit() {
		return unit;
	}

	public void setUnit(CpuUnit unit) {
		this.unit = unit;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value + " " + unit.toString();
	}
	
	
}

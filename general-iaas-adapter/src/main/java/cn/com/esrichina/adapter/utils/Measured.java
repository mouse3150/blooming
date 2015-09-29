package cn.com.esrichina.adapter.utils;



import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class Measured<B extends UnitOfMeasure,U extends B> {
    @SuppressWarnings("unchecked")
	static public <T extends Measured<?,?>> T valueOf( Class<T> type, String str) {
        StringBuilder numeric = new StringBuilder();
        StringBuilder unit = new StringBuilder();
        boolean parsingUnits = false;
        
        for( int i=0; i<str.length(); i++ ) {
            char c = str.charAt(i);
            
            if( !parsingUnits ) {
                if( Character.isDigit(c) || c == '.' || c == ',' ) {
                    numeric.append(c);
                }
                else if( Character.isLetter(c) ) {
                    parsingUnits = true;
                    unit.append(c);
                }
            }
            else {
                unit.append(c);
            }
        }
        double quantity = Double.parseDouble(numeric.toString());
        String uomName = unit.toString().trim().toLowerCase();

        try {
            Method m = type.getDeclaredMethod("valueOf", Number.class, String.class);
        
            return (T)m.invoke(null, quantity, uomName);
        }
        catch( Exception e ) {
            throw new IllegalArgumentException(e);
        }
    }
    
    private Number   quantity;
    private U        unitOfMeasure;
    
    public Measured() { }
    
    public Measured(Number value, U uom) {
        this.quantity = value;
        this.unitOfMeasure = uom;
    }
    
    @SuppressWarnings("unchecked")
    public Measured<B,U> add(Measured<B,?> amount) {
        try {
            @SuppressWarnings("rawtypes") Constructor<? extends Measured> c = getClass().getConstructor(Number.class, unitOfMeasure.getRootUnitOfMeasure());

            return c.newInstance(doubleValue() + amount.convertTo(getUnitOfMeasure()).doubleValue(), getUnitOfMeasure());
        }
        catch( Exception e ) {
            e.printStackTrace();
            throw new RuntimeException("Can't happen: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T extends B> Measured<B,T> convertTo(T targetUom) {
        double converted = doubleValue() * (getUnitOfMeasure().getBaseUnitConversion()/targetUom.getBaseUnitConversion()); 
        
        try {
            @SuppressWarnings("rawtypes") Constructor<? extends Measured> c = getClass().getConstructor(Number.class, targetUom.getRootUnitOfMeasure());

            return c.newInstance(converted, targetUom);
        }
        catch( Exception e ) {
            e.printStackTrace();
            throw new RuntimeException("Can't happen: " + e.getMessage());
        }        
    }
    
    public double doubleValue() {
        return quantity.doubleValue();
    }

    public float floatValue() {
        return quantity.floatValue();
    }
    
    public Number getQuantity() {
        return quantity;
    }
    
    public U getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public int intValue() {
        return quantity.intValue();
    }
    
    public long longValue() {
        return quantity.longValue();
    }

    public short shortValue() {
        return quantity.shortValue();
    }

    @SuppressWarnings("unchecked")
	public Measured<B,U> subtract(Measured<B,?> amount) {
        try {
            @SuppressWarnings("rawtypes") Constructor<? extends Measured> c = getClass().getConstructor(Number.class, unitOfMeasure.getRootUnitOfMeasure());

            return c.newInstance(doubleValue() - amount.convertTo(getUnitOfMeasure()).doubleValue(), getUnitOfMeasure());
        }
        catch( Exception e ) {
            e.printStackTrace();
            throw new RuntimeException("Can't happen: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return getUnitOfMeasure().format(getQuantity());
    }

    public void setQuantity(Number quantity) {
        this.quantity = quantity;
    }

    public void setUnitOfMeasure(U unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}

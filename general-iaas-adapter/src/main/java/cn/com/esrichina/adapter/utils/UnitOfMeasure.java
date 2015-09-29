package cn.com.esrichina.adapter.utils;



public abstract class UnitOfMeasure {
    public UnitOfMeasure() { } 
    
    public boolean equals(Object ob) {
        return (ob != null && ob.getClass().getName().equals(getClass().getName()));
    }
    
    public abstract double getBaseUnitConversion();
    
    public abstract String format(Number quantity);

    public abstract UnitOfMeasure getBaseUnit();
    
    public abstract Class<? extends UnitOfMeasure> getRootUnitOfMeasure();

    public abstract <B extends UnitOfMeasure,U extends B> Measured<B,U> newQuantity(Number quantity);
}

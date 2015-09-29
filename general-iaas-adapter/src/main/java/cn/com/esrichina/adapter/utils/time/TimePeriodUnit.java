package cn.com.esrichina.adapter.utils.time;



import cn.com.esrichina.adapter.utils.Measured;
import cn.com.esrichina.adapter.utils.UnitOfMeasure;
import cn.com.esrichina.adapter.utils.UnknownUnitOfMeasure;

public abstract class TimePeriodUnit extends UnitOfMeasure {
    static public TimePeriodUnit valueOf(String uom) {
        if( uom.equals("s") || uom.equals("second") || uom.equals("seconds") || uom.equals("sec") ) {
            return TimePeriod.SECOND;
        }
        else if( uom.length() < 1 || uom.equals("minute") || uom.equals("minutes") || uom.equals("m") || uom.equals("min") ) {
            return TimePeriod.MINUTE;
        }
        else if( uom.equals("hour") || uom.equals("hours") || uom.equals("h") || uom.equals("hrs") || uom.equals("hr") ) {
            return TimePeriod.HOUR;
        }
        else if( uom.equals("day") || uom.equals("days") || uom.equals("d") ) {
            return TimePeriod.DAY;
        }
        else if( uom.equals("week") || uom.equals("weeks") || uom.equals("w") || uom.equals("wks") || uom.equals("wk") ) {
            return TimePeriod.WEEK;
        }
        else if( uom.equals("ms") || uom.equals("millisecondsecond") || uom.equals("milliseconds") || uom.equals("millis") ) {
            return TimePeriod.MILLISECOND;
        }
        else if( uom.equals("μs") || uom.equals("microecondsecond") || uom.equals("microseconds") || uom.equals("micros") ) {
            return TimePeriod.MICROSECOND;
        }
        throw new UnknownUnitOfMeasure(uom);
    }
    
    @Override
    public Class<TimePeriodUnit> getRootUnitOfMeasure() {
        return TimePeriodUnit.class;
    }

    @Override
    public UnitOfMeasure getBaseUnit() {
        return TimePeriod.HOUR;
    }

    @Override
    public <B extends UnitOfMeasure, U extends B> Measured<B, U> newQuantity(Number quantity) {
        return new TimePeriod(quantity, this);
    }
}

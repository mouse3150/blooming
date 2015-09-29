package cn.com.esrichina.adapter.utils.time;

import cn.com.esrichina.adapter.utils.Measured;


public class TimePeriod<T extends TimePeriodUnit> extends Measured<TimePeriodUnit,T> {
    static public final Microsecond MICROSECOND = new Microsecond();
    static public final Millisecond MILLISECOND = new Millisecond();
    static public final Second      SECOND      = new Second();
    static public final Minute      MINUTE      = new Minute();
    static public final Hour        HOUR        = new Hour();
    static public final Day         DAY         = new Day();
    static public final Week        WEEK        = new Week();
    
    static public void main(String ... args) {
        TimePeriod<? extends TimePeriodUnit> memory = TimePeriod.valueOf(args[0]);
        TimePeriodUnit uom = TimePeriodUnit.valueOf(args[1]);

        System.out.println(memory.convertTo(uom));  
    }
        
    @SuppressWarnings("unchecked")
    static public TimePeriod<? extends TimePeriodUnit> valueOf(String str) {
        return Measured.valueOf(TimePeriod.class, str);
    }
    
    static public TimePeriod<? extends TimePeriodUnit> valueOf(Number quantity, String uomName) {
        return new TimePeriod<TimePeriodUnit>(quantity, TimePeriodUnit.valueOf(uomName));
    }

    public TimePeriod() { }
    
    public TimePeriod(Number quantity, T uom) {
        super(quantity, uom);
    }
}

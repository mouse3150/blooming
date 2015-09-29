package cn.com.esrichina.adapter.utils.time;


import java.text.ChoiceFormat;
import java.text.MessageFormat;

public class Microsecond extends TimePeriodUnit {
    static public TimePeriod<Microsecond> valueOf(short microseconds) {
        return new TimePeriod<Microsecond>(microseconds, TimePeriod.MICROSECOND);
    }

    static public TimePeriod<Microsecond> valueOf(int microseconds) {
        return new TimePeriod<Microsecond>(microseconds, TimePeriod.MICROSECOND);
    }

    static public TimePeriod<Microsecond> valueOf(long microseconds) {
        return new TimePeriod<Microsecond>(microseconds, TimePeriod.MICROSECOND);
    }

    static public TimePeriod<Microsecond> valueOf(double microseconds) {
        return new TimePeriod<Microsecond>(microseconds, TimePeriod.MICROSECOND);
    }

    static public TimePeriod<Microsecond> valueOf(float microseconds) {
        return new TimePeriod<Microsecond>(microseconds, TimePeriod.MICROSECOND);
    }

    static public TimePeriod<Microsecond> valueOf(Number microseconds) {
        return new TimePeriod<Microsecond>(microseconds, TimePeriod.MICROSECOND);
    }
    
    public Microsecond() { }
    
    @Override
    public double getBaseUnitConversion() {
        return (1.0/3600000000.0);
    }
    
    @Override
    public String format(Number quantity) {
        MessageFormat fmt = new MessageFormat("{0}");

        fmt.setFormatByArgumentIndex(0, new ChoiceFormat(new double[] {0,1,2}, new String[] {"0 microseconds","1 microsecond","{0,number} microseconds"}));
        return fmt.format(new Object[] { quantity });
    }
}

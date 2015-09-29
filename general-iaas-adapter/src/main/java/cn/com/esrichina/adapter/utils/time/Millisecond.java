package cn.com.esrichina.adapter.utils.time;


import java.text.ChoiceFormat;
import java.text.MessageFormat;

public class Millisecond extends TimePeriodUnit {
    static public TimePeriod<Millisecond> valueOf(short milliseconds) {
        return new TimePeriod<Millisecond>(milliseconds, TimePeriod.MILLISECOND);
    }

    static public TimePeriod<Millisecond> valueOf(int milliseconds) {
        return new TimePeriod<Millisecond>(milliseconds, TimePeriod.MILLISECOND);
    }

    static public TimePeriod<Millisecond> valueOf(long milliseconds) {
        return new TimePeriod<Millisecond>(milliseconds, TimePeriod.MILLISECOND);
    }

    static public TimePeriod<Millisecond> valueOf(double milliseconds) {
        return new TimePeriod<Millisecond>(milliseconds, TimePeriod.MILLISECOND);
    }

    static public TimePeriod<Millisecond> valueOf(float milliseconds) {
        return new TimePeriod<Millisecond>(milliseconds, TimePeriod.MILLISECOND);
    }

    static public TimePeriod<Millisecond> valueOf(Number milliseconds) {
        return new TimePeriod<Millisecond>(milliseconds, TimePeriod.MILLISECOND);
    }
    
    public Millisecond() { }
    
    @Override
    public double getBaseUnitConversion() {
        return (1.0/3600000.0);
    }
    
    @Override
    public String format(Number quantity) {
        MessageFormat fmt = new MessageFormat("{0}");

        fmt.setFormatByArgumentIndex(0, new ChoiceFormat(new double[] {0,1,2}, new String[] {"0 milliseconds","1 millisecond","{0,number} milliseconds"}));
        return fmt.format(new Object[] { quantity });
    }
}

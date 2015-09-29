package cn.com.esrichina.adapter.utils.time;


import java.text.ChoiceFormat;
import java.text.MessageFormat;

public class Minute extends TimePeriodUnit {
    static public TimePeriod<Minute> valueOf(short minutes) {
        return new TimePeriod<Minute>(minutes, TimePeriod.MINUTE);
    }

    static public TimePeriod<Minute> valueOf(int minutes) {
        return new TimePeriod<Minute>(minutes, TimePeriod.MINUTE);
    }

    static public TimePeriod<Minute> valueOf(long minutes) {
        return new TimePeriod<Minute>(minutes, TimePeriod.MINUTE);
    }

    static public TimePeriod<Minute> valueOf(double minutes) {
        return new TimePeriod<Minute>(minutes, TimePeriod.MINUTE);
    }

    static public TimePeriod<Minute> valueOf(float minutes) {
        return new TimePeriod<Minute>(minutes, TimePeriod.MINUTE);
    }

    static public TimePeriod<Minute> valueOf(Number minutes) {
        return new TimePeriod<Minute>(minutes, TimePeriod.MINUTE);
    }
    
    public Minute() { }
    
    public double getBaseUnitConversion() {
        return 1.0/60.0;
    }
    
    @Override
    public String format(Number quantity) {
        MessageFormat fmt = new MessageFormat("{0}");

        fmt.setFormatByArgumentIndex(0, new ChoiceFormat(new double[] {0,1,2}, new String[] {"0 minutes","1 minute","{0,number} minutes"}));
        return fmt.format(new Object[] { quantity });
    }
}

package cn.com.esrichina.adapter.utils.time;

import java.text.ChoiceFormat;
import java.text.MessageFormat;

public class Hour extends TimePeriodUnit {
    static public TimePeriod<Hour> valueOf(short hours) {
        return new TimePeriod<Hour>(hours, TimePeriod.HOUR);
    }

    static public TimePeriod<Hour> valueOf(int hours) {
        return new TimePeriod<Hour>(hours, TimePeriod.HOUR);
    }

    static public TimePeriod<Hour> valueOf(long hours) {
        return new TimePeriod<Hour>(hours, TimePeriod.HOUR);
    }

    static public TimePeriod<Hour> valueOf(double hours) {
        return new TimePeriod<Hour>(hours, TimePeriod.HOUR);
    }

    static public TimePeriod<Hour> valueOf(float hours) {
        return new TimePeriod<Hour>(hours, TimePeriod.HOUR);
    }

    static public TimePeriod<Hour> valueOf(Number hours) {
        return new TimePeriod<Hour>(hours, TimePeriod.HOUR);
    }
    
    public Hour() { }
    
    public double getBaseUnitConversion() {
        return 1.0;
    }
    
    @Override
    public String format(Number quantity) {
        MessageFormat fmt = new MessageFormat("{0}");

        fmt.setFormatByArgumentIndex(0, new ChoiceFormat(new double[] {0,1,2}, new String[] {"0 hours","1 hour","{0,number} hours"}));
        return fmt.format(new Object[] { quantity });
    }
}

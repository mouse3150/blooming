package cn.com.esrichina.adapter.utils.time;

import java.text.ChoiceFormat;
import java.text.MessageFormat;

public class Day extends TimePeriodUnit {
    static public TimePeriod<Day> valueOf(short days) {
        return new TimePeriod<Day>(days, TimePeriod.DAY);
    }

    static public TimePeriod<Day> valueOf(int days) {
        return new TimePeriod<Day>(days, TimePeriod.DAY);
    }

    static public TimePeriod<Day> valueOf(long days) {
        return new TimePeriod<Day>(days, TimePeriod.DAY);
    }

    static public TimePeriod<Day> valueOf(double days) {
        return new TimePeriod<Day>(days, TimePeriod.DAY);
    }

    static public TimePeriod<Day> valueOf(float days) {
        return new TimePeriod<Day>(days, TimePeriod.DAY);
    }

    static public TimePeriod<Day> valueOf(Number days) {
        return new TimePeriod<Day>(days, TimePeriod.DAY);
    }
    
    public Day() { }
    
    public double getBaseUnitConversion() {
        return 24.0;
    }
    
    @Override
    public String format(Number quantity) {
        MessageFormat fmt = new MessageFormat("{0}");

        fmt.setFormatByArgumentIndex(0, new ChoiceFormat(new double[] {0,1,2}, new String[] {"0 days","1 day","{0,number} days"}));
        return fmt.format(new Object[] { quantity });
    }
}

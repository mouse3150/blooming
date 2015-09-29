package cn.com.esrichina.adapter.utils.time;


import java.text.ChoiceFormat;
import java.text.MessageFormat;

public class Second extends TimePeriodUnit {
    static public TimePeriod<Second> valueOf(short seconds) {
        return new TimePeriod<Second>(seconds, TimePeriod.SECOND);
    }

    static public TimePeriod<Second> valueOf(int seconds) {
        return new TimePeriod<Second>(seconds, TimePeriod.SECOND);
    }

    static public TimePeriod<Second> valueOf(long seconds) {
        return new TimePeriod<Second>(seconds, TimePeriod.SECOND);
    }

    static public TimePeriod<Second> valueOf(double seconds) {
        return new TimePeriod<Second>(seconds, TimePeriod.SECOND);
    }

    static public TimePeriod<Second> valueOf(float seconds) {
        return new TimePeriod<Second>(seconds, TimePeriod.SECOND);
    }

    static public TimePeriod<Second> valueOf(Number seconds) {
        return new TimePeriod<Second>(seconds, TimePeriod.SECOND);
    }

    public Second() { }
    
    @Override
    public double getBaseUnitConversion() {
        return (1.0/3600.0);
    }
    
    @Override
    public String format(Number quantity) {
        MessageFormat fmt = new MessageFormat("{0}");

        fmt.setFormatByArgumentIndex(0, new ChoiceFormat(new double[] {0,1,2}, new String[] {"0 seconds","1 second","{0,number} seconds"}));
        return fmt.format(new Object[] { quantity });
    }
}

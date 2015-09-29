package cn.com.esrichina.adapter.utils.time;



import java.text.ChoiceFormat;
import java.text.MessageFormat;

public class Week extends TimePeriodUnit {
    static public TimePeriod<Week> valueOf(short weeks) {
        return new TimePeriod<Week>(weeks, TimePeriod.WEEK);
    }

    static public TimePeriod<Week> valueOf(int weeks) {
        return new TimePeriod<Week>(weeks, TimePeriod.WEEK);
    }

    static public TimePeriod<Week> valueOf(long weeks) {
        return new TimePeriod<Week>(weeks, TimePeriod.WEEK);
    }

    static public TimePeriod<Week> valueOf(double weeks) {
        return new TimePeriod<Week>(weeks, TimePeriod.WEEK);
    }

    static public TimePeriod<Week> valueOf(float weeks) {
        return new TimePeriod<Week>(weeks, TimePeriod.WEEK);
    }

    static public TimePeriod<Week> valueOf(Number weeks) {
        return new TimePeriod<Week>(weeks, TimePeriod.WEEK);
    }

    public Week() { }
    
    public double getBaseUnitConversion() {
        return 168.0;
    }
    
    @Override
    public String format(Number quantity) {
        MessageFormat fmt = new MessageFormat("{0}");

        fmt.setFormatByArgumentIndex(0, new ChoiceFormat(new double[] {0,1,2}, new String[] {"0 weeks","1 week","{0,number} weeks"}));
        return fmt.format(new Object[] { quantity });
    }
}

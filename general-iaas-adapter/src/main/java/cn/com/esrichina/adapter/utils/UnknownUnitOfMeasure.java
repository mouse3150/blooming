package cn.com.esrichina.adapter.utils;


@SuppressWarnings("serial")
public class UnknownUnitOfMeasure extends RuntimeException {
    /**
     * Constructs an error from the specified unknown UoM
     * @param uomName the name of the UoM that is unknown
     */
    public UnknownUnitOfMeasure(String uomName) {
        super(uomName);
    }
}

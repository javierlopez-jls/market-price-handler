package com.scib.fx.mph.util;

import java.time.format.DateTimeFormatter;

/**
 * Constant class
 */
public final class Constants {

    /**
     * Field separator into csv input message
     */
    public static final String CSV_FIELD_SEPARATOR = ",";

    /**
     * message fields size
     */
    public static final int CSV_FIELD_SIZE = 5;

    // Indexes

    /**
     * ID index at message
     */
    public static final int PRICE_ID_INDEX = 0;

    /**
     * instrument index at message
     */
    public static final int PRICE_INSTRUMENT_INDEX = 1;

    /**
     * bid price index
     */
    public static final int PRICE_BID_INDEX = 2;

    /**
     * ask price index
     */
    public static final int PRICE_ASK_INDEX = 3;
    /**
     * time stamp index
     */
    public static final int PRICE_TIMESTAMP_INDEX = 4;

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");
    

    /**
     * Hidden constructor by super
     */
    private Constants() {
        super();
    }
}

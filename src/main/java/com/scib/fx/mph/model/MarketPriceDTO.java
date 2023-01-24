package com.scib.fx.mph.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarketPriceDTO {

    /**
     * Price Unique id
     */
    private final long id;

    /**
     * Instrument name
     */
    private final String instrument;

    /**
     * Bid price
     */
    private final BigDecimal bid;

    /**
     * Ask price
     */
    private final BigDecimal ask;

    /**
     * Timestamp of price
     */
    private final LocalDateTime timestamp;

    /**
     * Hidden constructor by params
     */
    private MarketPriceDTO(final long id, final String instrument, final BigDecimal bid, final BigDecimal ask, final LocalDateTime timestamp) {
        this.id = id;
        this.instrument = instrument;
        this.bid = bid;
        this.ask = ask;
        this.timestamp = timestamp;
    }
}

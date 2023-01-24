package com.scib.fx.mph.service;

import com.scib.fx.mph.listener.IMarketPriceListener;
import com.scib.fx.mph.model.MarketPriceDTO;
import com.scib.fx.mph.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class MarketPriceService implements IMarketPriceListener {

    /** Default commission at prices: 0.1% */
    private static final BigDecimal COMMISSION = new BigDecimal("0.001");

    /**
     * MarketPriceDTO organized by instrument name
     */
    private final ConcurrentMap<String, MarketPriceDTO> priceByInstrument = new ConcurrentHashMap<>();

    /**
     * @return a unmodifiable map with last prices organized by instrument name
     */
    public Map<String, MarketPriceDTO> getPriceByInstrument() {
        return Collections.unmodifiableMap(priceByInstrument);
    }

    /**
     * @return instrument last price if it is present. {@code null} if it is absent
     * @throws  ResponseStatusException if instrument is not found
     */
    public MarketPriceDTO getLastPrice(final String instrument) {
        MarketPriceDTO dto = priceByInstrument.get(instrument);
        if(dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Last price for Instrument " + instrument + " not found");
        }
        return dto;
    }


    public String exportAll() {
        StringBuilder builder = new StringBuilder();
        priceByInstrument.values().forEach(price -> builder.append(toCSV(price)).append(System.lineSeparator()));
        return builder.toString();
    }

    @Override
    public void onMessages(final String message) {
        log.debug("onMessage : new message received [{}]", message);
        if (StringUtils.isEmpty(message)) {
            log.warn("onMessage: Empty message received.");
            return;
        }
        message.lines().forEach(this::treatNewPrice);
    }


    //------------------------------
    //      Private methods
    //------------------------------

    private void treatNewPrice(final String line) {
        try {
            MarketPriceDTO price = this.fromCSV(line);
            priceByInstrument.put(price.getInstrument(), price);
        } catch (Exception e) {
            log.error("onMessage : Unexpected error treating line [{}]. ", line, e);
        }
    }

    private MarketPriceDTO fromCSV(final String message) {
        String[] csv = StringUtils.split(message, Constants.CSV_FIELD_SEPARATOR);
        if (csv == null || csv.length != Constants.CSV_FIELD_SIZE) {
            throw new IllegalArgumentException("Unexpected message format : " + message);
        }

        final long id = Long.parseLong(csv[Constants.PRICE_ID_INDEX]);
        final String instrument = csv[Constants.PRICE_INSTRUMENT_INDEX];
        final BigDecimal bid = this.bidMargin(new BigDecimal(csv[Constants.PRICE_BID_INDEX]));
        final BigDecimal ask = this.askMargin(new BigDecimal(csv[Constants.PRICE_ASK_INDEX]));
        final LocalDateTime timeStamp = LocalDateTime.parse(csv[Constants.PRICE_TIMESTAMP_INDEX], Constants.DATE_FORMATTER);

        return MarketPriceDTO.builder().id(id).instrument(instrument).bid(bid).ask(ask).timestamp(timeStamp).build();
    }

    private String toCSV(final MarketPriceDTO dto) {

        return dto.getId() + Constants.CSV_FIELD_SEPARATOR +
                dto.getInstrument() + Constants.CSV_FIELD_SEPARATOR +
                dto.getBid() + Constants.CSV_FIELD_SEPARATOR +
                dto.getAsk() + Constants.CSV_FIELD_SEPARATOR +
                dto.getTimestamp().format(Constants.DATE_FORMATTER);
    }


    /**
     * Return a valid BID price for client after applied our margins
     * @param original original price to apply margin
     * @return Bid price with margin applied
     */
    private BigDecimal bidMargin(final BigDecimal original) {
        return original.subtract(original.multiply(COMMISSION));
    }

    /**
     * Return a valid BID price for client after applied our margins
     * @param original original price to apply margin
     * @return Bid price with margin applied
     */
    private BigDecimal askMargin(final BigDecimal original) {
        return original.add(original.multiply(COMMISSION));
    }
}

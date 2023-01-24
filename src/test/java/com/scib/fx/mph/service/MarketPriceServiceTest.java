package com.scib.fx.mph.service;

import com.scib.fx.mph.model.MarketPriceDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MarketPriceServiceTest {

    private MarketPriceService service;

    @BeforeEach
    void setUp(){
        service = new MarketPriceService();
    }

    @Test
    void getPriceByInstrument() {
        Assertions.assertNotNull(service.getPriceByInstrument());
        Assertions.assertTrue(service.getPriceByInstrument().isEmpty());

        //include a price
        service.onMessages("1,EUR/USD,1.1000,1.2000,01-06-2020 12:01:01:001");

        Assertions.assertNotNull(service.getPriceByInstrument());
        Assertions.assertFalse(service.getPriceByInstrument().isEmpty());
    }

    @Test
    void getLastPrice() {
        Assertions.assertNotNull(service.getPriceByInstrument());
        Assertions.assertTrue(service.getPriceByInstrument().isEmpty());

        for(int i = 1; i < 9; i++) {
            BigDecimal originalBid = new BigDecimal(i + ".100");
            BigDecimal originalAsk = new BigDecimal(i + ".200");

            BigDecimal margin = new BigDecimal("0.001");

            final String instrument = "EUR/USD";
            //include a price
            service.onMessages( i + "," + instrument + "," + originalBid + "," + originalAsk + ",01-06-2020 12:01:01:001");

            MarketPriceDTO dto = service.getLastPrice(instrument);
            Assertions.assertNotNull(dto);
            Assertions.assertEquals(i, dto.getId());
            Assertions.assertNotEquals(originalBid, dto.getBid());
            Assertions.assertNotEquals(originalAsk, dto.getAsk());

            Assertions.assertEquals(originalBid.subtract(originalBid.multiply(margin)), dto.getBid());
            Assertions.assertEquals(originalAsk.add(originalAsk.multiply(margin)), dto.getAsk());
        }
    }

    @Test
    void exportAll() {
        BigDecimal originalBid = new BigDecimal(1 + ".100");
        BigDecimal originalAsk = new BigDecimal(1 + ".200");

        BigDecimal margin = new BigDecimal("0.001");

        final String instrument = "EUR/USD";
        //include a price
        service.onMessages( "1," + instrument + "," + originalBid + "," + originalAsk + ",01-06-2020 12:01:01:001");
        String export = service.exportAll();
        Assertions.assertNotNull(export);
        final String expected = "1," + instrument + ","
                + originalBid.subtract(originalBid.multiply(margin)) + ","
                + originalAsk.add(originalAsk.multiply(margin))
                + ",01-06-2020 12:01:01:001" + System.lineSeparator();

        Assertions.assertEquals(expected, export);
    }

    @Test
    void onMessages() {
        //empty message -
        Assertions.assertDoesNotThrow(() -> service.onMessages(""));

        //invalid formant --> no exception and no price
        Assertions.assertDoesNotThrow(() -> service.onMessages("2,EUR/USD,1.1000,1.2000"));
        Assertions.assertNotNull(service.getPriceByInstrument());
        Assertions.assertTrue(service.getPriceByInstrument().isEmpty());

        //valid formant --> no exception and no price
        Assertions.assertDoesNotThrow(() -> service.onMessages("3,EUR/USD,1.1000,1.2000,01-06-2020 12:01:01:001"));
        Assertions.assertNotNull(service.getPriceByInstrument());
        Assertions.assertFalse(service.getPriceByInstrument().isEmpty());
        Assertions.assertEquals(1, service.getPriceByInstrument().size());
    }
}
package com.scib.fx.mph.service;

import com.scib.fx.mph.listener.IMarketPriceListener;
import com.scib.fx.mph.model.MarketPriceDTO;
import com.scib.fx.mph.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@AllArgsConstructor
@EnableScheduling
public class RandomPricerGenerator {

    private IMarketPriceListener listener;

    private final String[] instruments = { "EUR/USD", "EUR/JPY", "GBP/USD" };

    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void publishPrice() {
        StringBuilder message = new StringBuilder();


        for(String inst : instruments) {
            if(RandomUtils.nextBoolean()){
                continue;
            }

            long randomPrice = RandomUtils.nextLong(1000, 2000);
            long offset = RandomUtils.nextLong(100, 200);

            MarketPriceDTO dto = MarketPriceDTO.builder()
                    .id(idGenerator.incrementAndGet())
                    .instrument(inst)
                    .bid(BigDecimal.valueOf(randomPrice - offset, 3))
                    .ask(BigDecimal.valueOf(randomPrice + offset, 3))
                    .timestamp(LocalDateTime.now())
                    .build();

            message.append(toCSV(dto)).append(System.lineSeparator());
        }

        listener.onMessages(message.toString());


    }


    private String toCSV(final MarketPriceDTO dto) {

        return dto.getId() + Constants.CSV_FIELD_SEPARATOR +
                dto.getInstrument() + Constants.CSV_FIELD_SEPARATOR +
                dto.getBid() + Constants.CSV_FIELD_SEPARATOR +
                dto.getAsk() + Constants.CSV_FIELD_SEPARATOR +
                dto.getTimestamp().format(Constants.DATE_FORMATTER);
    }
}

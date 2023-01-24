package com.scib.fx.mph.controller;

import com.scib.fx.mph.model.MarketPriceDTO;
import com.scib.fx.mph.service.MarketPriceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class MarketPriceController {

    /** Service */
    private final MarketPriceService marketPriceService;

    /** Constructor by params*/
    @Autowired
    public MarketPriceController(final MarketPriceService service) {
        this.marketPriceService = service;
    }

    /**
     * Get an individual price by instrument name
     */
    @GetMapping(value = "/price")
    @Operation(summary = "Get last price of Instrument with commission applied")
    public ResponseEntity<MarketPriceDTO> getMarketPrice(@RequestParam("instrument") String name) {
        log.debug("getLastPrice: instrument [{}]", name);
        MarketPriceDTO dto = marketPriceService.getLastPrice(name);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/price/export")
    @Operation(summary = "Export last price of Instrument with commission applied into a CSV file")
    public void export(final HttpServletResponse response) {
        try {
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().print(marketPriceService.exportAll());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Unexpected error generating file: ", e);
        }
    }
}

package com.scib.fx.mph.controller;

import com.scib.fx.mph.model.MarketPriceDTO;
import com.scib.fx.mph.service.MarketPriceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ExtendWith(MockitoExtension.class)
class MarketPriceControllerTest {

    @Mock
    private MarketPriceService service;

    @InjectMocks
    private MarketPriceController controller;

    @Test
    void getMarketPrice() {
        final String instrument = "an-instrument";
        MarketPriceDTO dto = Mockito.mock(MarketPriceDTO.class);
        Mockito.when(service.getLastPrice(instrument)).thenReturn(dto);
        ResponseEntity<MarketPriceDTO> response = controller.getMarketPrice(instrument);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(dto, response.getBody());
        Mockito.verify(service).getLastPrice(instrument);
    }

    @Test
    void export() throws IOException {
        final String txt = "1,2,3,4,5\n6,7,8,9,0";
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = Mockito.mock(PrintWriter.class);

        Mockito.when(service.exportAll()).thenReturn(txt);
        Mockito.when(response.getWriter()).thenReturn(writer);

        controller.export(response);

        Mockito.verify(service).exportAll();
        Mockito.verify(response).getWriter();
        Mockito.verify(writer).print(txt);
    }
}
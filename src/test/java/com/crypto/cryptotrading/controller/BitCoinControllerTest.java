package com.crypto.cryptotrading.controller;

import com.crypto.cryptotrading.Exception.EntityNotFoundException;
import com.crypto.cryptotrading.domain.Prices;
import com.crypto.cryptotrading.service.BitCoinServiceImpl;
import com.crypto.cryptotrading.util.RollingAverageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class BitCoinControllerTest {

    @Mock
    BitCoinServiceImpl bitCoinService;

    @Mock
    RollingAverageUtil rollingAverageUtil;

    @InjectMocks
    BitCoinController bitCoinController;

    private List<Prices> pricesList = new ArrayList<>();

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = setupController(bitCoinController, new ExceptionHandler());
        Prices price = new Prices();
        price.setPrice(6000.00);
        LocalDate date = LocalDate.of(2018, 10, 12);
        price.setTime(date);
        pricesList.add(price);
        objectMapper = new ObjectMapper();
    }

    protected MockMvc setupController(Object controller, Object controllerAdvice) {
        return MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(controllerAdvice)
                .alwaysDo(MockMvcResultHandlers.print())
                .setCustomArgumentResolvers()
                .build();
    }

    @Test
    public void getAllPrices_HappyPathTest() throws Exception {
        doReturn(pricesList).when(bitCoinService).findAll();
        MvcResult result = mockMvc.perform(get("/api/btc/all")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getAllPrices_NotFoundTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(bitCoinService).findAll();
        MvcResult result = mockMvc.perform(get("/api/btc/all")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).contains("EntityNotFoundException");
    }

    @Test
    public void getAllPricesWithCustomDate_HappyPathTest() throws Exception {
        doReturn(pricesList).when(bitCoinService).findByCustomDate(any(), any());
        MvcResult result = mockMvc.perform(get("/api/btc/all?startDate=2018-09-01&endDate=2018-09-06")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getAllPricesWithCustomDate_NotFoundTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(bitCoinService).findByCustomDate(any(), any());
        MvcResult result = mockMvc.perform(get("/api/btc/all?startDate=2018-09-01&endDate=2018-09-06")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).contains("EntityNotFoundException");
    }

    @Test
    public void getAllPricesLastWeek_HappyPathTest() throws Exception {
        doReturn(pricesList).when(bitCoinService).findByCustomDate(any(), any());
        MvcResult result = mockMvc.perform(get("/api/btc/lastWeek")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getAllPricesLastWeek_NotFoundTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(bitCoinService).findByCustomDate(any(), any());
        MvcResult result = mockMvc.perform(get("/api/btc/lastWeek")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).contains("EntityNotFoundException");
    }

    @Test
    public void getAllPricesLastMonth_HappyPathTest() throws Exception {
        doReturn(pricesList).when(bitCoinService).findByCustomDate(any(), any());
        MvcResult result = mockMvc.perform(get("/api/btc/lastWeek")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getAllPricesLastMonth_NotFoundTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(bitCoinService).findByCustomDate(any(), any());
        MvcResult result = mockMvc.perform(get("/api/btc/lastWeek")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).contains("EntityNotFoundException");
    }

    @Test
    public void getTradeDecision_HappyPathTest() throws Exception {
        doReturn(pricesList).when(bitCoinService).findByCustomDate(any(), any());
        MvcResult result = mockMvc.perform(get("/api/btc/trade/decision?numOfDays=10")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getTradeDecision_NotFoundTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(bitCoinService).findByCustomDate(any(), any());
        MvcResult result = mockMvc.perform(get("/api/btc/trade/decision?numOfDays=10")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).contains("EntityNotFoundException");
    }
}

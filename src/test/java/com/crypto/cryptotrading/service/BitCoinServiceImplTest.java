package com.crypto.cryptotrading.service;

import com.crypto.cryptotrading.Exception.EntityNotFoundException;
import com.crypto.cryptotrading.domain.Prices;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;



public class BitCoinServiceImplTest {

    @Mock
    BitCoinServiceImpl bitCoinServiceImpl;

    private List<Prices> pricesList = new ArrayList<>();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Prices price = new Prices();
        price.setPrice(6000.00);
        price.setTime(LocalDate.now());
        pricesList.add(price);
    }

    @Test
    public void findAll_HappyPathTest() {
        when(bitCoinServiceImpl.findAll()).thenReturn(pricesList);
        assertThat(bitCoinServiceImpl.findAll()).isSameAs(pricesList);
    }

    @Test
    public void findAll_NotFound() {
        thrown.expect(EntityNotFoundException.class);
        when(bitCoinServiceImpl.findAll()).thenThrow(EntityNotFoundException.class);
        bitCoinServiceImpl.findAll();
    }

    @Test
    public void findByCustomDate_HappyPathTest() {
        doReturn(pricesList).when(bitCoinServiceImpl).findByCustomDate(any(), any());
        assertThat(bitCoinServiceImpl.findByCustomDate(any(), any())).isSameAs(pricesList);
    }

    @Test
    public void findByCustomDate_NotFound() {
        thrown.expect(EntityNotFoundException.class);
        when(bitCoinServiceImpl.findByCustomDate(any(), any())).thenThrow(EntityNotFoundException.class);
        bitCoinServiceImpl.findByCustomDate(any(), any());
    }
}

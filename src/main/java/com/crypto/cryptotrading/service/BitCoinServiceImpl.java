package com.crypto.cryptotrading.service;

import com.crypto.cryptotrading.Exception.EntityNotFoundException;
import com.crypto.cryptotrading.domain.BitCoin;
import com.crypto.cryptotrading.domain.Prices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = {"btc"})
public class BitCoinServiceImpl implements BitCoinService {

    private BitCoin btc = new BitCoin();

    @PostConstruct
    private void fillPrices() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {

            JSONObject json = new JSONObject(IOUtils.toString(new URL("https://www.coinbase.com/api/v2/prices/BTC-USD/historic?period=all"), Charset.forName("UTF-8")));
            btc = objectMapper.readValue(json.get("data").toString(), BitCoin.class);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Cacheable
    public List<Prices> findAll() {
        List<Prices> priceList = this.btc.getPrices();
        if (priceList.isEmpty()) {
            throw new EntityNotFoundException("Did not find bitcoin data");
        }
        return priceList;
    }

    @Cacheable
    public List<Prices> findByCustomDate(LocalDate startDate, LocalDate endDate) {
        List<Prices> priceList = new ArrayList<>();
        if (this.btc != null) {
            priceList = this.btc.getPrices().stream().filter(prices ->
                    ((prices.getTime().isAfter(startDate) &&
                            prices.getTime().isBefore(endDate)) ||
                            prices.getTime().isEqual(startDate) ||
                            prices.getTime().isEqual(endDate))).collect(Collectors.toList());
        }
        if (priceList.isEmpty()) {
            throw new EntityNotFoundException("Did not find bitcoin data");
        }

        return priceList;
    }

}

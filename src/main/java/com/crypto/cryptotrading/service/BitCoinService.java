package com.crypto.cryptotrading.service;

import com.crypto.cryptotrading.domain.Prices;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface BitCoinService {

    List<Prices> findAll();

    List<Prices> findByCustomDate(LocalDate startDate, LocalDate endDate);
}

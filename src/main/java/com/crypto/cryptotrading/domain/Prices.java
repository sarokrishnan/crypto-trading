package com.crypto.cryptotrading.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Prices {

    Double price;

    LocalDate time;

    Double movingAverage;

    public Prices() {
    }

    public Prices(Double price, LocalDate time, Double movingAverage) {
        this.movingAverage = movingAverage;
        this.time = time;
        this.price = price;
    }

    public Double getMovingAverage() {
        return movingAverage;
    }

    public void setMovingAverage(Double movingAverage) {
        this.movingAverage = movingAverage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }
}

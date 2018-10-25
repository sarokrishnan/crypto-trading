package com.crypto.cryptotrading.util;

import com.crypto.cryptotrading.domain.Prices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class RollingAverageUtil {
    private final Queue<Double> prices = new LinkedList<Double>();
    private final long interval;
    private double total;

    @Autowired
    public RollingAverageUtil() {
        this.interval = 0;
    }

    public RollingAverageUtil(long interval) {
        assert interval > 0 : "Not a positive value";
        this.interval = interval;
    }

    public void addNewPrice(double price) {
        total += price;
        prices.add(price);
        if (prices.size() > interval) {
            total -= prices.remove();
        }
    }

    public double getAvg() {
        if (prices.isEmpty()) return 0.0;
        return total / prices.size();
    }

    public String maxProfit(List<Prices> prices) {


        if (prices.size() <= 1) return "Not Sufficient Data";

        double minPrice = prices.get(0).getPrice();
        double maxSoFar = Integer.MIN_VALUE;
        double profitSoFar = Integer.MIN_VALUE;
        double percentage = 0;



        for (Prices price : prices){
            profitSoFar = price.getPrice() - minPrice;
            minPrice = Math.min(minPrice, price.getPrice());
            maxSoFar = Math.max(profitSoFar, maxSoFar);
            percentage = ((profitSoFar * 100) / maxSoFar);
        }

        return percentage <= 0 ? "HOLD" : (percentage >= 80) ? "SELL" : "BUY";
    }
 
}
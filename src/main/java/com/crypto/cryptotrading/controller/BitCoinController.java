
package com.crypto.cryptotrading.controller;

import com.crypto.cryptotrading.domain.Prices;
import com.crypto.cryptotrading.service.BitCoinService;
import com.crypto.cryptotrading.util.RollingAverageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequestMapping(value = "/api/btc")
public class BitCoinController {


    private BitCoinService service;
    private RollingAverageUtil rollingAvg;

    @Autowired
    BitCoinController(BitCoinService service, RollingAverageUtil rollingAvg) {
        this.service = service;
        this.rollingAvg = rollingAvg;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAll(
            @RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value="movingAvg", required = false) boolean isMovingAvg) {
        List<Prices> priceList = (startDate != null && endDate!=null) ?
                service.findByCustomDate(startDate,endDate) :
                service.findAll();


        long daysBetween = (startDate != null && endDate!=null) ? DAYS.between(startDate, endDate) : 0;


        if(!priceList.isEmpty() && isMovingAvg){
            priceList.forEach(price -> {
                rollingAvg.addNewPrice(price.getPrice());
                price.setMovingAverage(rollingAvg.getAvg());
            });
        }
        return new ResponseEntity<Object>(priceList.isEmpty() ? "No Data Found" : priceList, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/lastWeek")
    public ResponseEntity<Object> getLastWeek() {

        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.minusDays(7+now.getDayOfWeek().getValue()-1);
        LocalDate weekEnd = now.minusDays(now.getDayOfWeek().getValue());

        List<Prices> priceList = service.findByCustomDate(weekStart, weekEnd);
        return new ResponseEntity<Object>(priceList.isEmpty() ? "No Data Found" : priceList, HttpStatus.OK);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/trade/decision")
    public ResponseEntity<Object> getTradeDecision(
            @RequestParam(value="numOfDays", required = false) int days
    ) {

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(days);

        List<Prices> priceList = service.findByCustomDate(startDate, now);

        return new ResponseEntity<Object>(rollingAvg.maxProfit(priceList), HttpStatus.OK);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/lastMonth")
    public ResponseEntity<Object> getLastMonth() {

        LocalDate now = LocalDate.now();
        LocalDate previousMonth = now.minusMonths(1);
        LocalDate monthStart = previousMonth.withDayOfMonth(1);
        LocalDate monthEnd = previousMonth.withDayOfMonth(previousMonth.getMonth().maxLength());

        List<Prices> priceList = service.findByCustomDate(monthStart, monthEnd);
        return new ResponseEntity<Object>(priceList.isEmpty() ? "No Data Found" : priceList, HttpStatus.OK);
    }

}

package com.hsmdevelopers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomOrder {
    private int customOrderId;
    private String orderName;
    private int orderCount;
    private Date date;
    private Time placeTime;

    public CustomOrder(String orderName, int orderCount, Date date, Time placeTime) {
        this.orderName = orderName;
        this.orderCount = orderCount;
        this.date = date;
        this.placeTime = placeTime;
    }
}

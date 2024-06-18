package com.hsmdevelopers.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderTm {
    private String name;
    private String department;
    private Date date;
    private Time placeTime;
    private String taken;
    private Time takenTime;
}

package com.hsmdevelopers.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomOrderTm {
    private String orderName;
    private int count;
    private Time orderTime;
}

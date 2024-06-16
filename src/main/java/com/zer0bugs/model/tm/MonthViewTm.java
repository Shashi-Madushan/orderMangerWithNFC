package com.zer0bugs.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MonthViewTm {
    private Date data;
    private int totalOrder;
    private int totalTaken;
}

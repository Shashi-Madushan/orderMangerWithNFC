package com.zer0bugs.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomOrderTm {
    private String description;
    private int orderCount;
}

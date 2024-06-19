package com.hsmdevelopers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomOrderItem {
    private int customOrderItemId;
    private String description;
    private int count;
    private int customOrderId;

    public CustomOrderItem(String description, int count, int customOrderId) {
        this.description = description;
        this.count = count;
        this.customOrderId = customOrderId;
    }
}

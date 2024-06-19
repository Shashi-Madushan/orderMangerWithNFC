package com.hsmdevelopers.model.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomOrderItemTm {
    private String description;
    private int quantity;
    private Button deleteButton;
}

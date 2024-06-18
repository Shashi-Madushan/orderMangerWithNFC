package com.hsmdevelopers.model.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTm {
    private String username;
    private String password;
    private Button deleteButton;
}

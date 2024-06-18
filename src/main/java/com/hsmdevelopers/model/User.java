package com.hsmdevelopers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private int id;
    private String userName;
    private String password;

    public User(String userName, String password) {
        this.password = password;
        this.userName = userName;
    }
}

package com.zer0bugs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection;
    private Connection connection;

    private DbConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_organizer", "root", "Ijse@123");
        //connection = DriverManager.getConnection("jdbc:mysql://OrderManager_couldleft:107f984b8db3ecb169b61026d8fe7e6d818671dc@uqe.h.filess.io:3307/OrderManager_couldleft");
    }

    public static DbConnection getInstance() throws SQLException {
        if(dbConnection == null) {
            dbConnection = new DbConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}

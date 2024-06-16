package com.zer0bugs.repo;


import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class FilterOrdersRepo {
    public static ObservableList<OrderTm> generateReport(LocalDate startDate, LocalTime startTime, LocalTime endTime) throws SQLException {
        System.out.println("sd st et");
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        String sql ="SELECT * FROM orders a JOIN employee e ON e.employee_id = a.employee_id WHERE date = ? AND place_time BETWEEN ? AND ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1,startDate);
        statement.setObject(2,startTime);
        statement.setObject(3,endTime);
        ResultSet set = statement.executeQuery();

        while (set.next()) {
            if (!set.getBoolean("deleted")){
                list.add(new OrderTm(set.getString("name"), set.getString("department"), set.getDate("date"), set.getTime("place_time"),set.getString("taken"), set.getTime("taken_time")));
            }
        }
        return list;
    }

    public static  ObservableList<OrderTm> generateReport(LocalDate startDate, LocalDate endDate) throws SQLException {
        System.out.println("sd ed");
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        String sql ="SELECT * FROM orders a JOIN employee e ON e.employee_id = a.employee_id WHERE date BETWEEN ? AND ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1,startDate);
        statement.setObject(2,endDate);
        ResultSet set = statement.executeQuery();

        while (set.next()) {
            if (!set.getBoolean("deleted")){
                list.add(new OrderTm(set.getString("name"), set.getString("department"), set.getDate("date"), set.getTime("place_time"),set.getString("taken"), set.getTime("taken_time")));
            }
        }
        return list;
    }

    public static ObservableList<OrderTm> generateReport(LocalDate startDate) throws SQLException {
        System.out.println("t day");
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        String sql = " SELECT * FROM orders a JOIN employee e ON e.employee_id = a.employee_id WHERE date BETWEEN ? AND ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1,startDate);
        statement.setObject(2,LocalDate.now());
        ResultSet set = statement.executeQuery();

        while (set.next()) {
            if (!set.getBoolean("deleted")){
                list.add(new OrderTm(set.getString("name"), set.getString("department"), set.getDate("date"), set.getTime("place_time"),set.getString("taken"), set.getTime("taken_time")));
            }
        }
        return list;
    }

    public static ObservableList<OrderTm> generateReport(LocalTime startTime, LocalTime endTime) throws SQLException {
        System.out.println(" all");
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM orders a JOIN employee e ON e.employee_id = a.employee_id WHERE place_time BETWEEN ? AND ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);

        statement.setObject(1,startTime);
        statement.setObject(2,endTime);
        ResultSet set = statement.executeQuery();

        while (set.next()) {
            if (!set.getBoolean("deleted")){
                list.add(new OrderTm(set.getString("name"), set.getString("department"), set.getDate("date"), set.getTime("place_time"),set.getString("taken"), set.getTime("taken_time")));
            }
        }
        return list;
    }
}

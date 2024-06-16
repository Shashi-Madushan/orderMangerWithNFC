package com.zer0bugs.repo;

import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class DayViewRepo {
    public static ObservableList<OrderTm> generateReport(LocalTime startTime, LocalTime endTime, Date passedDate) throws SQLException {
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM orders a JOIN employee e ON e.employee_id = a.employee_id WHERE date = ? AND place_time BETWEEN ? AND ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1,passedDate);
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

    public static ObservableList<OrderTm> getData(Date date) throws SQLException {
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM employee e JOIN orders o ON e.employee_id = o.employee_id WHERE date = ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1, date);
        ResultSet set = statement.executeQuery();
        while (set.next()) {
            if (!set.getBoolean("deleted")){
                list.add(new OrderTm(set.getString("name"), set.getString("department"), set.getDate("date"), set.getTime("place_time"),set.getString("taken"), set.getTime("taken_time")));
            }
        }
        return list;
    }

    public static ObservableList<OrderTm> generateReport(LocalTime startTime, Date passedDate) throws SQLException {
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM orders a JOIN employee e ON e.employee_id = a.employee_id WHERE date = ? AND place_time >= ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1,passedDate);
        statement.setObject(2,startTime);
        ResultSet set = statement.executeQuery();

        while (set.next()) {
            if (!set.getBoolean("deleted")){
                list.add(new OrderTm(set.getString("name"), set.getString("department"), set.getDate("date"), set.getTime("place_time"),set.getString("taken"), set.getTime("taken_time")));
            }
        }
        return list;
    }
}

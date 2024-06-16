package com.zer0bugs.repo;

import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.tm.MonthViewTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MonthViewRepo {
    public static ObservableList<MonthViewTm> getData(int year, int month) throws SQLException {
        ObservableList<MonthViewTm> list = FXCollections.observableArrayList();

        String sql = "SELECT date,deleted,SUM(place_time_count) AS place_time_count,SUM(taken_time_count) AS taken_time_count FROM (SELECT orders.date,e.deleted,COUNT(orders.place_time) AS place_time_count,COUNT(orders.taken_time) AS taken_time_count FROM orders LEFT JOIN employee e ON e.employee_id = orders.employee_id WHERE YEAR(orders.date) = ? AND MONTH(orders.date) = ? GROUP BY orders.date, e.deleted UNION ALL SELECT c.date, NULL AS deleted,SUM(c.order_count) AS place_time_count,SUM(c.order_count) AS taken_time_count FROM custom_orders c WHERE YEAR(c.date) = ? AND MONTH(c.date) = ? GROUP BY c.date) AS combined GROUP BY date, deleted ORDER BY date";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setInt(1,year);
        statement.setInt(2,month);
        statement.setInt(3,year);
        statement.setInt(4,month);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            if (resultSet.getBoolean("deleted") != true){
                list.add(new MonthViewTm(resultSet.getDate(1),resultSet.getInt(3),resultSet.getInt(4)));
            }
        }
        return list;
    }
}

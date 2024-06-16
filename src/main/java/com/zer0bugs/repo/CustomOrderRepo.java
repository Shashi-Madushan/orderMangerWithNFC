package com.zer0bugs.repo;

import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.tm.CustomOrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

public class CustomOrderRepo {

    public static boolean save(String description, int orderCount) throws SQLException {
        String sql = "INSERT INTO custom_orders (description, order_count, date, place_time) VALUES (?,?,?,?)";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1, description);
        statement.setInt(2, orderCount);
        statement.setObject(3, LocalDate.now());
        statement.setObject(4, LocalTime.now());

        return statement.executeUpdate() > 0;
    }

    public static int getOrderCount() throws SQLException {
        String sql = "SELECT SUM(order_count) FROM custom_orders WHERE date = ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1, LocalDate.now());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static ObservableList<CustomOrderTm> getData() throws SQLException {
        ObservableList<CustomOrderTm> list = FXCollections.observableArrayList();

        String sql = "SELECT description,order_count FROM custom_orders WHERE date = ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1, LocalDate.now());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            list.add(new CustomOrderTm(resultSet.getString("description"), resultSet.getInt("order_count")));
        }
        return list;
    }

    public static int[] getLast5DaysCustomOrderCount() {
        String sql = "WITH RECURSIVE date_series AS (SELECT CURDATE() AS date UNION ALL SELECT date - INTERVAL 1 DAY FROM date_series WHERE date > CURDATE() - INTERVAL 4 DAY)SELECT ds.date, SUM(order_count) AS custom_order_count FROM date_series ds LEFT JOIN custom_orders o ON ds.date = o.date GROUP BY ds.date ORDER BY ds.date";

        int[] result = new int[5];
        int i = 0;
        try {
            ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
            while (set.next()) {
                if(set.getObject("custom_order_count") != null){
                    result[i] = set.getInt("custom_order_count");
                } else {
                    result[i] = 0;
                }
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int[] getThisMonthCustomOrderCount() {
        String sql = "WITH RECURSIVE date_series AS (SELECT LAST_DAY(CURDATE() - INTERVAL 1 MONTH) + INTERVAL 1 DAY AS date UNION ALL SELECT date + INTERVAL 1 DAY FROM date_series WHERE date < LAST_DAY(CURDATE()))SELECT ds.date, SUM(order_count) AS custom_order_count FROM date_series ds LEFT JOIN custom_orders o ON ds.date = o.date GROUP BY ds.date ORDER BY ds.date";

        int currentMonthDays = YearMonth.now().lengthOfMonth();
        int[] result = new int[currentMonthDays];
        int i = 0;
        try {
            ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
            while (set.next()) {
                result[i] = set.getInt("custom_order_count");
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

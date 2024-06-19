package com.hsmdevelopers.repo;

import com.hsmdevelopers.db.DbConnection;
import com.hsmdevelopers.model.CustomOrder;
import com.hsmdevelopers.model.tm.CustomOrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

public class CustomOrderRepo {

    public static boolean save(CustomOrder customOrder) throws SQLException {
        String sql = "INSERT INTO custom_orders (custom_order_id,order_name, order_count, date, place_time) VALUES (?,?,?,?,?)";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setInt(1, customOrder.getCustomOrderId());
        statement.setString(2, customOrder.getOrderName());
        statement.setInt(3, customOrder.getOrderCount());
        statement.setObject(4, customOrder.getDate());
        statement.setObject(5, customOrder.getPlaceTime());

        return statement.executeUpdate() > 0;
    }

    public static int getOrderCountTotal() throws SQLException {
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

    public static int getOrderCount() {
        String sql = "SELECT COUNT(custom_order_id) FROM custom_orders";

        try {
            ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static int getCountByDate(Date passedDate) {
        String sql = "SELECT SUM(order_count) FROM custom_orders WHERE date = ?";

        try {
            PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setObject(1, passedDate);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

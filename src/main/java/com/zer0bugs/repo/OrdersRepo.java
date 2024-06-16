package com.zer0bugs.repo;


import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;

public class OrdersRepo {
    public static ObservableList<OrderTm> getData() throws SQLException {
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM employee e JOIN orders o ON e.employee_id = o.employee_id ORDER BY date";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
        while (set.next()) {
            if (!set.getBoolean("deleted")){
                list.add(new OrderTm(set.getString("name"), set.getString("department"), set.getDate("date"), set.getTime("place_time"),set.getString("taken"), set.getTime("taken_time")));
            }
        }
        return list;
    }

    public static int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT employee_id) AS distinct_employee_count FROM attendance WHERE date = ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1, LocalDate.now());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("distinct_employee_count");
        }
        return 0;
    }

    public static int[] getLast5DaysCount(){
        String sql = "WITH RECURSIVE date_series AS (SELECT CURDATE() AS date UNION ALL SELECT date - INTERVAL 1 DAY FROM date_series WHERE date > CURDATE() - INTERVAL 4 DAY)SELECT ds.date, COUNT(DISTINCT o.order_id) AS distinct_order_count FROM date_series ds LEFT JOIN orders o ON ds.date = o.date GROUP BY ds.date ORDER BY ds.date";

        int[] result = new int[5];
        int i = 0;
        try {
            ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
            while (set.next()) {
                result[i] = set.getInt("distinct_order_count");
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int getTotalOrder() throws SQLException {
        String sql = "SELECT COUNT(order_id) FROM orders WHERE date = ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1, LocalDate.now());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getTakenOrderCount() throws SQLException {
        String sql = "SELECT COUNT(order_id) FROM orders WHERE date = ? AND taken = ?";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setObject(1, LocalDate.now());
        statement.setObject(2, "Taken");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int[] getLast5DaysTakenOrderCount() {
        String sql = "WITH RECURSIVE date_series AS (SELECT CURDATE() AS date UNION ALL SELECT date - INTERVAL 1 DAY FROM date_series WHERE date > CURDATE() - INTERVAL 4 DAY)SELECT ds.date, COUNT(o.order_id) AS taken_order_count FROM date_series ds LEFT JOIN orders o ON ds.date = o.date AND taken = 'Taken' GROUP BY ds.date ORDER BY ds.date";

        int[] result = new int[5];
        int i = 0;
        try {
            ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
            while (set.next()) {
                result[i] = set.getInt("taken_order_count");
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int[] getThisMonthOrderCount() {
        String sql = "WITH RECURSIVE date_series AS (SELECT LAST_DAY(CURDATE() - INTERVAL 1 MONTH) + INTERVAL 1 DAY AS date UNION ALL SELECT date + INTERVAL 1 DAY FROM date_series WHERE date < LAST_DAY(CURDATE()))SELECT ds.date, COUNT(DISTINCT o.order_id) AS order_count FROM date_series ds LEFT JOIN orders o ON ds.date = o.date GROUP BY ds.date ORDER BY ds.date";

        int currentMonthDays = YearMonth.now().lengthOfMonth();
        int[] result = new int[currentMonthDays];
        int i = 0;
        try {
            ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
            while (set.next()) {
                result[i] = set.getInt("order_count");
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int[] getThisMonthTakenOrderCount() {
        String sql = "WITH RECURSIVE date_series AS (SELECT LAST_DAY(CURDATE() - INTERVAL 1 MONTH) + INTERVAL 1 DAY AS date UNION ALL SELECT date + INTERVAL 1 DAY FROM date_series WHERE date < LAST_DAY(CURDATE()))SELECT ds.date, COUNT(o.order_id) AS taken_order_count FROM date_series ds LEFT JOIN orders o ON ds.date = o.date AND taken = 'Taken' GROUP BY ds.date ORDER BY ds.date";

        int currentMonthDays = YearMonth.now().lengthOfMonth();
        int[] result = new int[currentMonthDays];
        int i = 0;
        try {
            ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
            while (set.next()) {
                result[i] = set.getInt("taken_order_count");
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

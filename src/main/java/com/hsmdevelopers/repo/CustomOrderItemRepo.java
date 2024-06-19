package com.hsmdevelopers.repo;

import com.hsmdevelopers.db.DbConnection;
import com.hsmdevelopers.model.CustomOrderItem;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomOrderItemRepo {
    public static boolean save(ArrayList<CustomOrderItem> orderItems) throws SQLException {
        for (CustomOrderItem orderItem : orderItems) {
            boolean isSave = saveItem(orderItem);
            if (!isSave){
                return false;
            }
        }
        return true;
    }

    private static boolean saveItem(CustomOrderItem orderItems) throws SQLException {
        String sql = "INSERT INTO custom_orders_items (description, count, custom_order_id) VALUES (?,?,?)";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1, orderItems.getDescription());
        statement.setInt(2, orderItems.getCount());
        statement.setInt(3, orderItems.getCustomOrderId());
        return statement.executeUpdate() > 0;
    }
}

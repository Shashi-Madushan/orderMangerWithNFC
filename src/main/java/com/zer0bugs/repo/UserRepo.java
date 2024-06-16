package com.zer0bugs.repo;

import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.User;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {
    public static boolean checkUser(User user) throws SQLException {
        String sql = "SELECT password FROM user WHERE user_name = ?";

        PreparedStatement stmt = DbConnection.getInstance().getConnection().prepareStatement(sql);
        stmt.setString(1, user.getUserName());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            if(user.getPassword().equals(rs.getString("password"))) {
                return true;
            } else {
                new Alert(Alert.AlertType.ERROR,"Passwords do not match").show();
                return false;
            }
        } else {
            new Alert(Alert.AlertType.ERROR,"User not found").show();
            return false;
        }
    }
}

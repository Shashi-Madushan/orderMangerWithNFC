package com.zer0bugs.repo;

import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.User;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {
    public static User user;

    public static boolean checkUser(User user1) throws SQLException {
        String sql = "SELECT * FROM user WHERE user_name = ?";

        PreparedStatement stmt = DbConnection.getInstance().getConnection().prepareStatement(sql);
        stmt.setString(1, user1.getUserName());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            if(user1.getPassword().equals(rs.getString("password"))) {
                user = new User(rs.getInt("id"),rs.getString("user_name"), rs.getString("password"));
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

    public static boolean changeUserDetails(User user1) throws SQLException {
        String sql = "UPDATE user SET user_name = ? , password = ? WHERE id = ?";

        PreparedStatement stmt = DbConnection.getInstance().getConnection().prepareStatement(sql);
        stmt.setString(1, user1.getUserName());
        stmt.setString(2, user1.getPassword());
        stmt.setInt(3, user.getId());
        return stmt.executeUpdate() > 0;
    }
}

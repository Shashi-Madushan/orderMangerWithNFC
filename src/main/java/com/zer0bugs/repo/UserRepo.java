package com.zer0bugs.repo;

import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.User;
import com.zer0bugs.model.tm.UserTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public static ObservableList<User> getAllData() throws SQLException {
        ObservableList<User> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM user";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();
        while(set.next()) {
            if (set.getInt("id") == 1) {
                continue;
            }
            list.add(new User(set.getInt("id"),set.getString("user_name"), set.getString("password")));
        }
        return list;
    }

    public static boolean delete(UserTm userTm) throws SQLException {
        String sql = "DELETE FROM user WHERE user_name = ?";

        PreparedStatement stmt = DbConnection.getInstance().getConnection().prepareStatement(sql);
        stmt.setString(1, userTm.getUsername());
        return stmt.executeUpdate() > 0;
    }

    public static boolean save(User user) throws SQLException {
        String sql = "INSERT INTO user (user_name, password) VALUES (?, ?)";

        PreparedStatement stmt = DbConnection.getInstance().getConnection().prepareStatement(sql);
        stmt.setString(1, user.getUserName());
        stmt.setString(2, user.getPassword());
        return stmt.executeUpdate() > 0;
    }
}

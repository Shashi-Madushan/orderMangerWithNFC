package com.hsmdevelopers.repo;



import com.hsmdevelopers.db.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileRepo {

    public static boolean addFilePath(String filePath)  {
        try {
            String sql = "INSERT INTO file_path (file_path) VALUES (?)";

            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setObject(1, filePath);

            return preparedStatement.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }

    }

    public static boolean updateFilePath( String filePath) {
        try {
            String sql = "UPDATE file_path SET file_path = ? WHERE id = ?";

            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setObject(1, filePath);
            preparedStatement.setObject(2, 1);/// eka row ekk thiyena hinda

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isFilePathTableEmpty() {
        try {
            String sql = "SELECT COUNT(*) FROM file_path";

            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getInt(1)!=0){
                    return false;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getFilePath() {
        try {
            String sql = "SELECT file_path FROM file_path WHERE id = ?";

            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setObject(1, 1);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("file_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
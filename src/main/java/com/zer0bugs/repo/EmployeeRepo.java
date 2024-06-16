package com.zer0bugs.repo;


import com.zer0bugs.db.DbConnection;
import com.zer0bugs.model.Employee;
import com.zer0bugs.model.tm.EmployeeTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeRepo {

    public static boolean save(Employee employee) throws SQLException {
        String sql ="INSERT INTO employee (employee_id, name, department) VALUES(?,?,?)";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setObject(1,employee.getId());
        preparedStatement.setObject(2,employee.getName());
        preparedStatement.setObject(3,employee.getDepartment());

        return preparedStatement.executeUpdate() > 0;
    }

    public static ObservableList<Employee> getEmployees() throws SQLException {
        ObservableList<Employee> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM employee";

        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();

        while (set.next()) {
            if (set.getBoolean("deleted") == false){
                list.add(new Employee(set.getString("employee_id"),set.getString("name"),set.getString("department")));
            }
        }
        return list;
    }

    public static boolean delete(EmployeeTm selectedIndex) throws SQLException {
        String sql = "UPDATE employee SET deleted = ? WHERE employee_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setObject(1,true);
        preparedStatement.setObject(2,selectedIndex.getId());

        return preparedStatement.executeUpdate() > 0;
    }

    public static Employee search(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,id);
        ResultSet set = preparedStatement.executeQuery();
        if (set.next()) {
            return new Employee(set.getString("employee_id"),set.getString("name"),set.getString("department"));
        }
        return null;
    }
    public static int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS employee_count FROM employee WHERE deleted = false";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet set = preparedStatement.executeQuery();

        if (set.next()) {
            return set.getInt("employee_count");
        }

        return 0;
    }

    public static boolean bulckSaveDataToDatabase(List<String[]> data) {
        try {
            String insertQuery = "INSERT INTO employee (employee_id, name, department) VALUES (?, ?, ?) " ;
            PreparedStatement pstmt = DbConnection.getInstance().getConnection().prepareStatement(insertQuery);

            for (String[] row : data) {
                pstmt.setString(1, row[0]);
                pstmt.setString(2, row[1]);
                pstmt.setString(3, row[2]);
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            return results.length == data.size();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

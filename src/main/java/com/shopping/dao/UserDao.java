package com.shopping.dao;

import com.shopping.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    Logger logger = Logger.getLogger(UserDao.class.getName());
    public User getUserFromName(String name){
        User user = new User();
        Connection connectionToDB = H2DataBaseConnection.getConnectionToDB();
        try {
            PreparedStatement preparedStatement = connectionToDB
                    .prepareStatement("select * from users where username = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setUsername(resultSet.getString("username"));
                user.setAge(resultSet.getInt("age"));
                user.setGender(resultSet.getString("gender"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Can't get user with name :" + name);
        }
        return  user;
    }
}

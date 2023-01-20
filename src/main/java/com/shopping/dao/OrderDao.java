package com.shopping.dao;

import com.shopping.model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDao {
    public static final Logger logger = Logger.getLogger(OrderDao.class.getName());

    public List<Order> getOrderByUserId(int userId){
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement preparedStatement = H2DataBaseConnection.getConnectionToDB()
                .prepareStatement("select * from orders where user_id=?")) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setUserId(resultSet.getInt("user_id"));
                order.setOrderDate(resultSet.getDate("order_date"));
                order.setNoOfItems(resultSet.getInt("no_of_items"));
                order.setTotalAmount(resultSet.getDouble("total_amount"));
                orders.add(order);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query error or connection fault", e);
        }
        return orders;
    }
}

package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.model.Order;

import java.io.IOException;
import java.util.List;

public interface OrderSerivce {
    void addOrder(Integer orderNumber, Order order)
            throws OrderDataValidationException, OrderDataDuplicationException, IOException, OrderPersistenceException;

    List<Order> getOrders()
            throws OrderDataValidationException, OrderDataDuplicationException, IOException, OrderPersistenceException;

    Order getOrder(Integer orderNumber, String date)
            throws OrderDataValidationException, OrderDataDuplicationException, IOException, OrderPersistenceException;

    Order removeOrder(Integer orderNumber, String date)
            throws OrderDataValidationException, OrderDataDuplicationException, IOException, OrderPersistenceException;

    Integer getPreviousOrderNumber() throws OrderDataValidationException, OrderDataDuplicationException, OrderPersistenceException;

    void writeOrderNumberToFile(Integer orderNumber) throws OrderDataValidationException, OrderDataDuplicationException, OrderPersistenceException;
    List<Order> getOrdersBaseOnDate(String date) throws OrderDataValidationException, OrderDataDuplicationException, IOException, OrderPersistenceException;

    void editOrder(Integer orderNumber, Order order) throws OrderDataDuplicationException, OrderDataValidationException, IOException, OrderPersistenceException;

    void exportOrders() throws OrderDataDuplicationException, OrderDataValidationException, IOException, OrderPersistenceException;
}

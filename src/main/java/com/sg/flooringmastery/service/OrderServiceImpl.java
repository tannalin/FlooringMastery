package com.sg.flooringmastery.service;

import com.sg.flooringmastery.model.Order;

import java.util.List;

public class OrderServiceImpl implements OrderSerivce {
    @Override
    public Order addOrder(Integer orderNumber, Order order) throws OrderDataValidationException, OrderDataDuplicationException {
        return null;
    }

    @Override
    public List<Order> getOrders() throws OrderDataValidationException, OrderDataDuplicationException {
        return null;
    }

    @Override
    public Order getOrder(Integer orderNumber, String date) throws OrderDataValidationException, OrderDataDuplicationException {
        return null;
    }

    @Override
    public Order removeOrder(Integer orderNumber, String date) throws OrderDataValidationException, OrderDataDuplicationException {
        return null;
    }

    @Override
    public Integer getPreviousOrderNumber() throws OrderDataValidationException, OrderDataDuplicationException {
        return null;
    }

    @Override
    public void writeOrderNumberToFile(Integer orderNumber) throws OrderDataValidationException, OrderDataDuplicationException {

    }

    @Override
    public List<Order> getOrdersBaseOnDate(String date) throws OrderDataValidationException, OrderDataDuplicationException {
        return null;
    }

    @Override
    public void editOrder(Integer orderNumber, Order order) throws OrderDataDuplicationException, OrderDataValidationException {

    }

    @Override
    public void exportOrders() throws OrderDataDuplicationException, OrderDataValidationException {

    }

    public boolean validateCustomer(String customerName) {
        return customerName != null && customerName.trim().length() > 0;
    }
}

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;


public class OrderDaoFileImplTest {

    OrderDaoFileImpl testDao;
    @BeforeEach
    void setUp() {
        testDao = new OrderDaoFileImpl("DataFiles/");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testGetPreviousNumber() throws OrderPersistenceException, IOException {
        Integer orderNumber = 5;
        Order order = new Order(orderNumber);
        String date = "05062021";
        order.setOrderDate(date);
        order.setCustomerName("Jilin");
        order.setArea("256");
        order.setProduct("Wood");
        order.setTax("CA");
        Assert.assertEquals(order.getCustomerName(),"Jilin");
        Assert.assertEquals(order.getOrderNumber(),
                Integer.valueOf(5));

    }
}
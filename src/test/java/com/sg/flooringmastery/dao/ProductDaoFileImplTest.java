package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoFileImplTest {

    ProductDao testDao;

    public ProductDaoFileImplTest() {
        testDao = new ProductDaoFileImpl();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testGetProduct() throws OrderPersistenceException {
        Product p = testDao.getProduct("Wood");
        assertEquals(p.getCostPerSquareFoot(),new BigDecimal("5.15"));
        assertEquals(p.getLaborCostPerSquareFoot(),new BigDecimal("4.75"));
    }
    @Test
    public void testGetAllProducts() throws OrderPersistenceException {
        Product p = testDao.getProduct("Wood");
        assertEquals(p.getCostPerSquareFoot(),new BigDecimal("5.15"));
        assertEquals(p.getLaborCostPerSquareFoot(),new BigDecimal("4.75"));
    }
}
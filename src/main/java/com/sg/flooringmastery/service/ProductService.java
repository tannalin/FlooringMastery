package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.model.Product;

import java.util.List;

public interface ProductService {
    /**
     * Returns a List of all products on the file.
     *
     * @return Order List containing all products on the file.
     * @throws OrderPersistenceException
     */
    List<Product> getProducts()
            throws OrderPersistenceException;

    /**
     * Returns the Product object associated with the given productType
     * Returns null if no such Product exists
     *
     * @param productType of the Product to retrieve
     * @return the Product object associated with the productType,
     * null if no such Product exists
     * @throws OrderPersistenceException
     */
    Product getProduct(String productType)
            throws OrderPersistenceException;
}

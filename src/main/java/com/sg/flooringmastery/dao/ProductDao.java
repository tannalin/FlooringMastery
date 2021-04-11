package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;

import java.util.List;

public interface ProductDao {
    /**
     * Returns a List of all products on the file.
     *
     * @return Order List containing all products on the file.
     * @throws OrderDaoException
     */
    List<Product> getProducts()
            throws OrderDaoException;

    /**
     * Returns the Product object associated with the given productType
     * Returns null if no such Product exists
     *
     * @param productType of the Product to retrieve
     * @return the Product object associated with the productType,
     * null if no such Product exists
     * @throws OrderDaoException
     */
    Product getProduct(String productType)
            throws OrderDaoException;
}

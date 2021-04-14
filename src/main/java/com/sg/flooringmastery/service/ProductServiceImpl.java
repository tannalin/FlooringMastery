package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.model.Product;

import java.util.List;

public class ProductServiceImpl implements ProductService{

    private ProductDao dao;

    public ProductServiceImpl(ProductDao dao){
        this.dao = dao;
    }
    /**
     * Returns a List of all products on the file.
     *
     * @return Order List containing all products on the file.
     * @throws OrderPersistenceException OrderPersistenceException
     */
    @Override
    public List<Product> getProducts() throws OrderPersistenceException {
        return dao.getProducts();
    }
    /**
     * Returns the Product object associated with the given productType
     * Returns null if no such Product exists
     *
     * @param productType of the Product to retrieve
     * @return the Product object associated with the productType,
     * null if no such Product exists
     * @throws OrderPersistenceException OrderPersistenceException
     */
    @Override
    public Product getProduct(String productType) throws OrderPersistenceException {
        return dao.getProduct(productType);
    }
}

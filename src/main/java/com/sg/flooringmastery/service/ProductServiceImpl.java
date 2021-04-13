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
    @Override
    public List<Product> getProducts() throws OrderPersistenceException {
        return dao.getProducts();
    }

    @Override
    public Product getProduct(String productType) throws OrderPersistenceException {
        return dao.getProduct(productType);
    }
}

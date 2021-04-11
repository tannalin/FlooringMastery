package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ProductDaoFileImpl implements ProductDao{
    private Map<String,Product > products = new HashMap<>();
    public static final String PRODUCTS_FILE = "Data/products.txt";
    public static final String DELIMITER = ",";

    @Override
    public List<Product> getProducts() throws OrderDaoException {
        loadProductsFile();
        return new ArrayList<Product>(products.values());
    }

    @Override
    public Product getProduct(String productType) throws OrderDaoException {
        loadProductsFile();
        return products.get(productType);
    }

    private Product unmarshallProduct(String productAsText){

        String[] productTokens = productAsText.split(DELIMITER);
        String productType = productTokens[0];
        Product productFromFile = new Product(productType);
        productFromFile.setCostPerSquareFoot(productTokens[1]);
        productFromFile.setLaborCostPerSquareFoot(productTokens[2]);
        return productFromFile;
    }

    private void loadProductsFile() throws OrderDaoException {
        Scanner scanner;
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderDaoException(
                    "-_- Could not load roster data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        Product currentProduct;
        scanner.nextLine();//skip first line
        while (scanner.hasNextLine()) {

            currentLine = scanner.nextLine();

            currentProduct = unmarshallProduct(currentLine);

            products.put(currentProduct.getProductType(), currentProduct);
        }
        // close scanner
        scanner.close();
    }
}

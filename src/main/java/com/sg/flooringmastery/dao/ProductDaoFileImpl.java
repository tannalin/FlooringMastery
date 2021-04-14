package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ProductDaoFileImpl implements ProductDao{
    private Map<String,Product > products = new HashMap<>();
    public static final String PRODUCTS_FILE = "DataFiles/Data/products.txt";
    public static final String DELIMITER = ",";
    /**
     * Returns a List of all products on the file.
     *
     * @return Order List containing all products on the file.
     * @throws OrderPersistenceException OrderPersistenceException
     */
    @Override
    public List<Product> getProducts() throws OrderPersistenceException {
        loadProductsFile();
        return new ArrayList<>(products.values());
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

    private void loadProductsFile() throws OrderPersistenceException {
        Scanner scanner;
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderPersistenceException(
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

package com.sg.flooringmastery.dao;
import com.sg.flooringmastery.model.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class TaxDaoFileImpl implements TaxDao{
    private Map<String, Tax> taxes = new HashMap<>();
    public static final String TAXES_FILE = "Data/Taxes.txt";
    public static final String DELIMITER = ",";

    @Override
    public List<Tax> getTaxes() throws OrderDaoException {
        loadTaxesFile();
        return new ArrayList<Tax>(taxes.values());
    }

    @Override
    public Tax getTax(String stateAB) throws OrderDaoException {
        loadTaxesFile();
        return taxes.get(stateAB);
    }

    private Tax unmarshallTax(String taxesAsText){

        String[] taxTokens = taxesAsText.split(DELIMITER);
        String stateAB = taxTokens[0];
        Tax taxFromFile = new Tax(stateAB);
        taxFromFile.setStateName(taxTokens[1]);
        taxFromFile.setTaxRate(taxTokens[2]);
        return taxFromFile;
    }

    private void loadTaxesFile() throws OrderDaoException {
        Scanner scanner;
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderDaoException(
                    "-_- Could not load roster data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        Tax currentTax;
        scanner.nextLine();//skip first line
        while (scanner.hasNextLine()) {

            currentLine = scanner.nextLine();

            currentTax = unmarshallTax(currentLine);

            taxes.put(currentTax.getStateAbbreviation(), currentTax);
        }
        // close scanner
        scanner.close();
    }
}

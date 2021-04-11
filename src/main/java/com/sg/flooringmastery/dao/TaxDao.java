package com.sg.flooringmastery.dao;
import com.sg.flooringmastery.model.Tax;

import java.util.List;

public interface TaxDao {
    /**
     * Returns a List of all Taxes on the file.
     *
     * @return Order List containing all Taxes on the file.
     * @throws OrderDaoException
     */
    List<Tax> getTaxes()
            throws OrderDaoException;

    /**
     * Returns the Tax object associated with the given StateAB
     * Returns null if no such Tax exists
     *
     * @param StateAB of the Tax to retrieve
     * @return the Tax object associated with the StateAB,
     * null if no such Tax exists
     * @throws OrderDaoException
     */
    Tax getTax(String StateAB)
            throws OrderDaoException;
}

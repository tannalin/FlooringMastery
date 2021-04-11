package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;

import java.io.IOException;
import java.util.List;

public interface OrderDao {
    /**
     * Adds the given Order to the file and associates it with the given
     * Order id. If there is already a student associated with the given
     * Order id it will return that student object, otherwise it will
     * return null.
     *
     * @param orderNumber id with which Order is to be associated
     * @param order Order to be added to the roster
     * @return the Order object previously associated with the given
     * Order id if it exists, null otherwise
     * @throws OrderDaoException
     */
    Order addOrder(Integer orderNumber, Order order)
            throws OrderDaoException, IOException;

    /**
     * Returns a List of all orders on the file.
     *
     * @return Order List containing all Orders on the file.
     * @throws OrderDaoException
     */
    List<Order> getOrders()
            throws OrderDaoException, IOException;

    /**
     * Returns the Order object associated with the given student id.
     * Returns null if no such Order exists
     *
     * @param orderNumber ID of the Order to retrieve
     * @param date
     * @return the Order object associated with the given Order id,
     * null if no such Order exists
     * @throws OrderDaoException
     */
    Order getOrder(Integer orderNumber, String date)
            throws OrderDaoException, IOException;

    /**
     * Removes from the roster the Order associated with the given id.
     * Returns the Order object that is being removed or null if
     * there is no Order associated with the given id
     *
     * @param OrderId id of Order to be removed
     * @return Order object that was removed or null if no Order
     * was associated with the given Order id
     * @throws OrderDaoException
     */
    Order removeOrder(String OrderId)
            throws OrderDaoException, IOException;

    Integer getPreviousOrderNumber() throws OrderDaoException;

    void writeOrderNumberToFile(Integer orderNumber) throws OrderDaoException;
    List<Order> getOrdersBaseOnDate(String date) throws OrderDaoException, IOException;
}
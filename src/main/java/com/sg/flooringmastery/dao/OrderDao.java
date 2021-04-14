package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;

import java.io.IOException;
import java.util.List;

public interface OrderDao {
    /**
     * Adds the given Order to the file and associates it with the given
     * Order number. If there is already a student associated with the given
     * Order number it will return that Order object, otherwise it will
     * return null.
     *
     * @param orderNumber id with which Order is to be associated
     * @param order Order to be added to the roster
     * @return the Order object previously associated with the given
     * Order id if it exists, null otherwise
     * @throws OrderPersistenceException
     */
    Order addOrder(Integer orderNumber, Order order)
            throws OrderPersistenceException, IOException;

    /**
     * Returns a List of all orders on the file.
     *
     * @return Order List containing all Orders on the file.
     * @throws OrderPersistenceException
     */
    List<Order> getOrders()
            throws OrderPersistenceException, IOException;

    /**
     * Returns the Order object associated with the given number.
     * Returns null if no such Order exists
     *
     * @param orderNumber ID of the Order to retrieve
     * @param date
     * @return the Order object associated with the given Order number,
     * null if no such Order exists
     * @throws OrderPersistenceException
     */
    Order getOrder(Integer orderNumber, String date)
            throws OrderPersistenceException, IOException;

    /**
     * Removes from the roster the Order associated with the given number.
     * Returns the Order object that is being removed or null if
     * there is no Order associated with the given number
     *
     * @param orderNumber number of Order to be removed
     * @param date date of Order to be removed
     * @return Order object that was removed or null if no Order
     * was associated with the given Order number
     * @throws OrderPersistenceException
     */
    Order removeOrder(Integer orderNumber, String date)
            throws OrderPersistenceException, IOException;


    /**
     * @return the number that store in the file that keep the number auto increment
     * @throws OrderPersistenceException
     */
    Integer getPreviousOrderNumber() throws OrderPersistenceException;

    /**
     * store the order number in the file that keep the number auto increment
     * @param orderNumber
     * @throws OrderPersistenceException
     */
    void writeOrderNumberToFile(Integer orderNumber) throws OrderPersistenceException;

    /**
     * @param date the date that user want to have the list of this date
     * @return a list of orders
     * @throws OrderPersistenceException
     * @throws IOException
     */
    List<Order> getOrdersBaseOnDate(String date) throws OrderPersistenceException, IOException;


    /**
     * will edit the order base on the info user provide
     * @param orderNumber
     * @param order
     * @throws IOException
     * @throws OrderPersistenceException
     */
    void editOrder(Integer orderNumber, Order order) throws IOException, OrderPersistenceException;

    /**
     * action to export orders to back up file
     * @throws IOException
     * @throws OrderPersistenceException
     */
    void exportOrders() throws IOException, OrderPersistenceException;
}
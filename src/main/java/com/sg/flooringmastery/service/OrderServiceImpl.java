package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderAuditDao;
import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.model.Order;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderServiceImpl implements OrderSerivce {

    OrderDao dao;
    private OrderAuditDao auditDao;

    public OrderServiceImpl(OrderDao dao, OrderAuditDao myAuditDao) {
        this.dao = dao;
        this.auditDao = myAuditDao;
    }

    /**
     * @param orderNumber id with which Order is to be associated
     * @param order       Order to be added to the roster
     * @throws OrderDataValidationException
     * @throws OrderDataDuplicationException
     * @throws IOException
     * @throws OrderPersistenceException
     */
    @Override
    public void addOrder(Integer orderNumber, Order order) throws OrderDataValidationException, OrderDataDuplicationException, IOException, OrderPersistenceException {
        if (dao.getOrder(orderNumber, order.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy"))) != null) {
            throw new OrderDataDuplicationException(
                    "ERROR: Could not create Order.  Order Number"
                            + orderNumber
                            + " already exists");
        }
        dao.addOrder(orderNumber, order);
        validateOrder(order);

        auditDao.writeAuditEntry(
                "Order " + orderNumber + " CREATED.");

    }

    @Override
    public List<Order> getOrders() throws IOException, OrderPersistenceException {
        return dao.getOrders();
    }

    @Override
    public Order getOrder(Integer orderNumber, String date) throws IOException, OrderPersistenceException {
        return dao.getOrder(orderNumber,date);
    }

    @Override
    public Order removeOrder(Integer orderNumber, String date) throws IOException, OrderPersistenceException {
        return dao.removeOrder(orderNumber,date);
    }

    @Override
    public Integer getPreviousOrderNumber() throws OrderPersistenceException {
        return dao.getPreviousOrderNumber();
    }

    @Override
    public void writeOrderNumberToFile(Integer orderNumber) throws OrderPersistenceException {
        dao.writeOrderNumberToFile(orderNumber);
    }

    @Override
    public List<Order> getOrdersBaseOnDate(String date) throws IOException, OrderPersistenceException {
        return dao.getOrdersBaseOnDate(date);
    }

    @Override
    public void editOrder(Integer orderNumber, Order order) throws IOException, OrderPersistenceException {
        dao.editOrder(orderNumber,order);
    }

    @Override
    public void exportOrders() throws IOException, OrderPersistenceException {
        dao.exportOrders();
    }

    /**
     * validate the order before add it
     * @param order
     * @throws OrderDataValidationException
     * @throws OrderPersistenceException
     */
    private void validateOrder(Order order) throws OrderDataValidationException, OrderPersistenceException {
        boolean validate = true;
        String message = "";
        if(!order.validateDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy")))){
            validate = false;
            message = "Invalid date!\r\n";
        }
        if(!order.validateCustomer(order.getCustomerName())){
            validate = false;
            message = "Invalid customer name!\r\n";
        }

        if(!order.validateState(order.getTax().getStateAbbreviation())){
            validate = false;
            message = "Invalid state!\r\n";
        }
        if(!order.validateProduct(order.getProduct().getProductType())){
            validate = false;
            message = "Invalid product!\r\n";
        }
        if(!order.validateArea(order.getArea().doubleValue())){
            validate = false;
            message = "Invalid area!\r\n";
        }
        if(!validate){
            throw new OrderDataValidationException(message);
        }

    }

}

package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.OrderDaoException;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.view.OrderView;
import java.io.IOException;
import java.util.List;

public class OrderController {
    private OrderView view;
    private OrderDao orderDao;
    private ProductDao productDao;
    final int CONFIRM = 1;

    public OrderController(OrderDao orderDao, ProductDao productDao, OrderView view) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.view = view;
    }

    public void run()  {
        boolean keepGoing = true;
        int menuSelection;
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        listOrdersBaseOnDate();
                        break;
                    case 2:
                        createOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportOrders();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }
            exitMessage();
        } catch (OrderDaoException | IOException e) {
            view.displayErrorMessage(e.getMessage());
        } finally {
            view.printMenuAndGetSelection();
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void createOrder() throws OrderDaoException, IOException {
        view.displayCreateOrderBanner();
        Order newOrder= view.getNewOrderInfo(orderDao.getPreviousOrderNumber(), productDao.getProducts());
        view.displayDisplayOrderBanner();
        view.displayOrder(newOrder);
        int confirm = view.readConfirmPlaceOrderChoice();
        if(confirm == CONFIRM) {
            orderDao.writeOrderNumberToFile(newOrder.getOrderNumber());
            orderDao.addOrder(newOrder.getOrderNumber(), newOrder);
            view.displayCreateSuccessBanner();
        } else {
            view.displayOrderCancelBanner();
        }
    }

    private void listOrdersBaseOnDate() throws OrderDaoException, IOException {
        view.displayDisplayOrdersBanner();
        String date = view.readDate(true);
        List<Order> orderList = orderDao.getOrdersBaseOnDate(date);
        view.displayOrderList(orderList);
    }

    private void editOrder() throws OrderDaoException, IOException {
        view.displayDisplayOrderBanner();
        Integer orderNumber = view.readOrderNumber();
        String date = view.readDate(true);
        Order order = orderDao.getOrder(orderNumber,date);
        String beforeUpdate = order.toString();
        order = view.getEditInfo(order,productDao.getProducts());
        view.displayOrder(order);
        int confirm = view.readConfirmUpdateOrderChoice();
        if (!beforeUpdate.equals(order.toString())&&confirm == CONFIRM) {
            orderDao.editOrder(order.getOrderNumber(), order);
            view.displayOrderUpdatedBanner();
        } else {
            view.displayKeepSameInfoBanner();
        }
    }

    private void removeOrder() throws OrderDaoException, IOException {
        boolean isQueryMode = true;
        view.displayRemoveOrderBanner();
        Integer orderNumber = view.readOrderNumber();
        String date =  view.readDate(isQueryMode);
        Order removeOrder = orderDao.removeOrder(orderNumber, date);
        view.displayRemoveResult(removeOrder);
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

    private void exportOrders() throws IOException, OrderDaoException {
        orderDao.exportOrders();
        view.displayExportSuccessBanner();
    }
}

package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.service.OrderDataDuplicationException;
import com.sg.flooringmastery.service.OrderDataValidationException;
import com.sg.flooringmastery.service.OrderSerivce;
import com.sg.flooringmastery.service.ProductService;
import com.sg.flooringmastery.view.OrderView;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class OrderController {
    private OrderView view;
    private OrderSerivce orderSerivce;
    private ProductService productService;
    final int CONFIRM = 1;

    public OrderController(OrderSerivce orderSerivce, ProductService productService, OrderView view) {
        this.orderSerivce = orderSerivce;
        this.productService = productService;
        this.view = view;
    }

    public void run() {
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
        } catch (OrderPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Provide the choice allow user to choose
     * @return int
     */
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }


    /**
     * Create new order
     * @throws OrderPersistenceException
     */
    private void createOrder() throws OrderPersistenceException {
        view.displayCreateOrderBanner();
        try {
            Order newOrder = view.getNewOrderInfo(orderSerivce.getPreviousOrderNumber(), productService.getProducts());
            view.displayDisplayOrderBanner();
            view.displayOrder(newOrder);
            int confirm = view.readConfirmPlaceOrderChoice();
            if (confirm == CONFIRM) {
                orderSerivce.addOrder(newOrder.getOrderNumber(), newOrder);
                orderSerivce.writeOrderNumberToFile(newOrder.getOrderNumber());
                view.displayCreateSuccessBanner();
            } else {
                view.displayOrderCancelBanner();
            }
        }catch( IOException | OrderDataDuplicationException | OrderDataValidationException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * To show all the orders base the date that user provide
     * @throws OrderPersistenceException
     */
    private void listOrdersBaseOnDate() throws OrderPersistenceException {

        view.displayDisplayOrdersBanner();
        String date = view.readDate(true);
        List<Order> orderList = null;
        try {
            orderList = orderSerivce.getOrdersBaseOnDate(date);
        } catch (OrderDataValidationException | OrderDataDuplicationException | IOException e) {
            e.printStackTrace();
        }
        view.displayOrderList(orderList);
    }

    /**
     * call to get the order number which user want to edit
     * @throws OrderPersistenceException
     */
    private void editOrder() throws OrderPersistenceException {
        try {
            view.displayDisplayOrderBanner();
            Integer orderNumber = view.readOrderNumber();
            String date = view.readDate(true);
            Order order = orderSerivce.getOrder(orderNumber, date);
            if(order != null) {
                String beforeUpdate = order.toString();

                order = view.getEditInfo(order, productService.getProducts());
                view.displayOrder(order);
                int confirm = view.readConfirmUpdateOrderChoice();
                if (!beforeUpdate.equals(order.toString()) && confirm == CONFIRM) {
                    orderSerivce.editOrder(order.getOrderNumber(), order);
                    view.displayOrderUpdatedBanner();
                } else {
                    view.displayKeepSameInfoBanner();
                }
            } else {
                view.displayErrorMessage("No such order!");
            }
        }catch( IOException | OrderDataDuplicationException | OrderDataValidationException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * call to get the order number which user want to remove
     * @throws OrderPersistenceException
     */
    private void removeOrder() throws OrderPersistenceException {
        Order removeOrder = null;
        boolean isQueryMode = true;
        try {
            view.displayRemoveOrderBanner();
            Integer orderNumber = view.readOrderNumber();
            String date =  view.readDate(isQueryMode);
            removeOrder =orderSerivce.getOrder(orderNumber,date);
        if(removeOrder != null) {
            orderSerivce.removeOrder(orderNumber, date);
        }
        } catch (OrderDataValidationException | OrderDataDuplicationException | IOException e) {
            e.printStackTrace();
        }

        view.displayRemoveResult(removeOrder);
    }

    /**
     * Export data
     */
    private void exportOrders() {
        try {
            orderSerivce.exportOrders();
        } catch (OrderDataDuplicationException | OrderDataValidationException | IOException | OrderPersistenceException e) {
            e.printStackTrace();
        }
        view.displayExportSuccessBanner();
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }


}

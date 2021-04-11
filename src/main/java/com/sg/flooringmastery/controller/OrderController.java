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

    public OrderController(OrderDao orderDao, ProductDao productDao, OrderView view) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.view = view;
    }

    public void run()  {
        boolean keepGoing = true;
        int menuSelection = 0;
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
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void createOrder() throws OrderDaoException, IOException {
        view.displayCreateOrderBanner();
        Order newOrder= view.getNewOrderInfo(orderDao.getPreviousOrderNumber(), productDao.getProducts());
        orderDao.writeOrderNumberToFile(newOrder.getOrderNumber());
        orderDao.addOrder(newOrder.getOrderNumber(), newOrder);
        view.displayCreateSuccessBanner();
    }

    private void listOrdersBaseOnDate() throws OrderDaoException, IOException {
        view.displayDisplayAllBanner();
        String date = view.getDateChoice();
        List<Order> orderList = orderDao.getOrdersBaseOnDate(date);
        view.displayOrderList(orderList);
    }


    private void editOrder() throws OrderDaoException, IOException {
        view.displayDisplayStudentBanner();
        Integer orderNumber = Integer.parseInt(view.getOrderNumberChoice());
        String date = view.getDateChoice();

        Order order = orderDao.getOrder(orderNumber,date);
        
    }

    private void removeOrder() throws OrderDaoException {
        //view.displayRemoveStudentBanner();
        //String studentId = view.getStudentIdChoice();
       // Student removedStudent = dao.removeStudent(studentId);
       // view.displayRemoveResult(removedStudent);
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}

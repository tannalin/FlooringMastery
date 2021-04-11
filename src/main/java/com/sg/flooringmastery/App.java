package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.OrderController;
import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.view.OrderView;
import com.sg.flooringmastery.view.UserIO;
import com.sg.flooringmastery.view.UserIOConsoleImpl;

import java.text.ParseException;


public class App {
    public static void main(String[] args) throws OrderDaoException, ParseException {
        UserIO myIo = new UserIOConsoleImpl();
        OrderView myView = new OrderView(myIo);
        OrderDao myOrderDao = new OrderDaoFileImpl();
        ProductDao myProductDao = new ProductDaoFileImpl();
        OrderController controller = new OrderController(myOrderDao, myProductDao, myView);
        controller.run();
    }
}

package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.OrderController;
import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.service.*;
import com.sg.flooringmastery.view.OrderView;
import com.sg.flooringmastery.view.UserIO;
import com.sg.flooringmastery.view.UserIOConsoleImpl;

import java.io.IOException;


public class App {
    public static void main(String[] args) {
        UserIO myIo = new UserIOConsoleImpl();
        OrderView myView = new OrderView(myIo);
        OrderDao myOrderDao = new OrderDaoFileImpl();
        OrderAuditDao myOrderAuditor = new OrderAuditDaoImpl();
        OrderSerivce myOrderService = new OrderServiceImpl(myOrderDao, myOrderAuditor);
        ProductDao myProductDao = new ProductDaoFileImpl();
        ProductService myProductService = new ProductServiceImpl(myProductDao);
        OrderController controller = new OrderController(myOrderService, myProductService, myView);
        controller.run();
    }
}

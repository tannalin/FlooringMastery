package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dao.OrderDaoException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;

import java.util.List;

public class OrderView {
    private UserIO io;

    public OrderView(UserIO io) {
        this.io = io;
    }

    public Order getNewOrderInfo(Integer previousOrderNumber, List<Product> products) throws OrderDaoException {
        Order currentOrder = new Order(previousOrderNumber + 1);
        String orderDate = io.readDate("Please enter the order date(format: MMddyyyy (06302021)");
        currentOrder.setOrderDate(orderDate);

        String customerName = io.readString("Please enter the customer Name",false);
        String taxState = io.readString("Please enter state ",false);
        displayProductsList(products);
        String productType = io.readString("Please enter product type, EX:Wood",false);
        String area = io.readString("Please enter area",false);


        currentOrder.setCustomerName(customerName);
        currentOrder.setTax(taxState);
        currentOrder.setArea(area);
        currentOrder.setProduct(productType);
        return currentOrder;
    }

    public void displayCreateOrderBanner() {
        io.print("=== Create Order ===");
    }

    public void displayProductsList(List<Product> products) throws OrderDaoException {
        io.print("Products:");
        for (Product currentProduct : products) {
            String orderInfo = String.format("#%s : %s %s",
                    currentProduct.getProductType(),currentProduct.getCostPerSquareFoot(),currentProduct.getLaborCostPerSquareFoot());
            io.print(orderInfo);
        }
    }

    public void displayCreateSuccessBanner() throws OrderDaoException {
        io.readString(
                "Order successfully created.  Please hit enter to continue",true);
    }

    public void displayOrderList(List<Order> orderList) throws OrderDaoException {
        for (Order currentOrder : orderList) {
            io.print(currentOrder.toString());
        }
        io.readString("Please hit enter to continue.",true);
    }

    public void displayDisplayAllBanner() {
        io.print("=== Display Orders ===");
    }

    public void displayDisplayStudentBanner () {
        io.print("=== Display Order ===");
    }

    public String getOrderNumberChoice() throws OrderDaoException {
        return io.readString("Please enter the order number.",false);
    }


    public void displayRemoveOrderBanner () {
        io.print("=== Remove Order ===");
    }

    public void displayRemoveResult(Order orderRecord) throws OrderDaoException {
        if(orderRecord != null){
            io.print("Order successfully removed.");
        }else{
            io.print("No such order.");
        }
        io.readString("Please hit enter to continue.",true);
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }

    public int printMenuAndGetSelection() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public String getDateChoice() throws OrderDaoException {
        return io.readString("Please enter the order date(format: MMddyyyy (06302021)",false);
    }
}

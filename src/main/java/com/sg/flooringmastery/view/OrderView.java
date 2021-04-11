package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dao.OrderDaoException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class OrderView {
    private UserIO io;
    private Order currentOrder = new Order();
    private boolean isEditMode = false;
    private boolean isQueryMode = false;

    public OrderView(UserIO io) {
        this.io = io;
    }

    public Order getNewOrderInfo(Integer previousOrderNumber, List<Product> products) throws OrderDaoException {
        Order currentOrder = new Order(previousOrderNumber + 1);
        String orderDate = io.readDate("Please enter the order date(format: MMddyyyy (06302021))");
        currentOrder.setOrderDate(orderDate);
        String customerName = readCustomerName(isEditMode);
        String taxState = readStateAb(isEditMode);

        //String taxState = io.readString("Please enter state ");
        displayProductsList(products);
        String productType =readProductType(isEditMode);
                //String productType = io.readString("Please enter product type, EX:Wood");
        String area = io.readString("Please enter area");


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
                "Order successfully created.  Please hit enter to continue");
    }

    public void displayOrderList(List<Order> orderList) throws OrderDaoException {
        for (Order currentOrder : orderList) {
            io.print(currentOrder.toString());
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayDisplayAllBanner() {
        io.print("=== Display Orders ===");
    }

    public void displayDisplayStudentBanner () {
        io.print("=== Display Order ===");
    }

    public String getOrderNumberChoice() throws OrderDaoException {
        return io.readString("Please enter the order number.");
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
        io.readString("Please hit enter to continue.");
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
        return io.readString("Please enter the order date(format: MMddyyyy (06302021)");
    }

    public String readCustomerName(boolean isEditMode) {
        String message = "Please enter the customer Name";
        String customerName = "";
        if(isEditMode){
            message += " : " + currentOrder.getCustomerName();
        }
        customerName = io.readString(message);
        if(isEditMode){
            if(currentOrder.getCustomerName().equals(customerName) || !io.hasContent(customerName)){
                customerName = currentOrder.getCustomerName();
            }
        } else {
            while(!io.hasContent(customerName)){
                message = "Invalid input! " + message;
                customerName = io.readString(message);
            }
        }
        return customerName;
    }


    public String readStateAb(boolean isEditMode) throws OrderDaoException {
        String message = "Please enter the State abbreviation (ex: CA)";
        String stateAb = "";
        boolean isValidate = false;
        while(!isValidate) {
            if (isEditMode) {
                message += " : " + currentOrder.getTax().getStateAbbreviation();
            }
            stateAb = io.readString(message);
            if (isEditMode) {
                if (currentOrder.getCustomerName().equals(stateAb) || !io.hasContent(stateAb)) {
                    return currentOrder.getTax().getStateAbbreviation();
                }
            }

            if (currentOrder.validateState(stateAb)) {
                return stateAb;
            }else{
                message = "Invalid input! " + message;
            }
        }
        return stateAb;
    }

    public String readProductType(boolean isEditMode) throws OrderDaoException {
        String message = "Please enter the Product type (ex: Wood)";
        String productType = "";
        boolean isValidate = false;
        while(!isValidate) {
            if (isEditMode) {
                message += " : " + currentOrder.getProduct().getProductType();
            }
            productType = io.readString(message);
            if (isEditMode) {
                if (currentOrder.getProduct().getProductType().equals(productType) || !io.hasContent(productType)) {
                    return currentOrder.getTax().getStateAbbreviation();
                }
            }

            if (currentOrder.validateProduct(productType)) {
                return productType;
            }else{
                message = "Invalid input! " + message;
            }
        }
        return productType;
    }
    public String readDate(boolean isQueryMode) throws OrderDaoException {
        String message = "Please enter the order date(format: MMddyyyy (06302021))";
        boolean isValidInput = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String sOrderDate = io.readString(message);
        //if(LocalDate.parse((String.format(sOrderDate, formatter))))
        while (!isValidInput) {
            LocalDate orderDate = LocalDate.parse(sOrderDate,formatter);

            isValidInput = true;
            LocalDate START = LocalDate.now();
            if(LocalDate.parse(sOrderDate,formatter).isBefore(START)||!isQueryMode){
                isValidInput = false;
            }
        }
        return sOrderDate;
    }
}

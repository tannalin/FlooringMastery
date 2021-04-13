package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderView {
    private UserIO io;
    private Order currentOrder = new Order();
    private boolean isEditMode = false;
    private boolean isQueryMode = false;

    public OrderView(UserIO io) {
        this.io = io;
    }

   //print menu and get choice from user input
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

    //get order information from user
    public Order getNewOrderInfo(Integer previousOrderNumber, List<Product> products) throws OrderPersistenceException {
        //set info to orders
        Order currentOrder = new Order(previousOrderNumber + 1);
        String orderDate = readDate(isQueryMode);
        currentOrder.setOrderDate(orderDate);
        String customerName = readCustomerName(isEditMode);
        String taxState = readStateAb(isEditMode);
        displayProductsList(products);
        String productType = readProductType(isEditMode);
        String area = readArea(isEditMode);
        //set info to orders
        currentOrder.setCustomerName(customerName);
        currentOrder.setTax(taxState);
        currentOrder.setArea(area);
        currentOrder.setProduct(productType);
        return currentOrder;
    }
    public Order getEditInfo(Order order,List<Product> products) throws OrderPersistenceException {
        isEditMode = true;
        currentOrder = order;
        String customerName = readCustomerName(isEditMode);
        String taxState = readStateAb(isEditMode);
        displayProductsList(products);
        String productType = readProductType(isEditMode);
        String area = readArea(isEditMode);
        //set info to orders
        order.setCustomerName(customerName);
        order.setTax(taxState);
        order.setArea(area);
        order.setProduct(productType);
        return order;
    }

    //Methods for Read information/choices by user input
    public Integer readOrderNumber() {
        return io.readInt("Please enter the order number.");
    }
    public String readCustomerName(boolean isEditMode) {
        String message = "Please enter the customer Name";
        String customerName;
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
    public String readStateAb(boolean isEditMode) throws OrderPersistenceException {
        String message = "Please enter the State abbreviation (ex: CA)";
        String stateAb;
        while(true) {
            if (isEditMode) {
                message = "Please enter the State abbreviation (ex: CA): " + currentOrder.getTax().getStateAbbreviation();
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
                message = "Invalid input! Please enter the State abbreviation (ex: CA)";
            }
        }
    }
    public String readProductType(boolean isEditMode) throws OrderPersistenceException {
        String message = "Please enter the Product type (ex: Wood)";
        while(true) {
            if (isEditMode) {
                message += " : " + currentOrder.getProduct().getProductType();
            }
            String productType = io.readString(message);
            if (isEditMode) {
                if (currentOrder.getProduct().getProductType().equals(productType) || !io.hasContent(productType)) {
                    return currentOrder.getProduct().getProductType();
                }
            }
            if (currentOrder.validateProduct(productType)) {
                return productType;
            }else{
                message = "Invalid input! Please enter the Product type (ex: Wood)";
            }
        }

    }
    public String readDate(boolean isQueryMode) {
        String message = "Please enter the order date(format: MMddyyyy (06302021))";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");

        while (true) {
            String sOrderDate = io.readString(message);

            if(currentOrder.validateDate(sOrderDate)) {
                if (isQueryMode) {
                    return sOrderDate;
                }
                LocalDate START = LocalDate.now();
                if (!LocalDate.parse(sOrderDate, formatter).isBefore(START)) {
                    return sOrderDate;
                } else {
                    message = "Date must be in future! Please enter the order date(format: MMddyyyy (06302021))";
                }

            }else {
                message = "Invalid date! Please enter the order date(format: MMddyyyy (06302021))" ;
            }
        }
    }
    public String readArea(boolean isEditMode){
        String message = "Please enter area";
        while(true){
            if (isEditMode) {
                message = "Please enter area: (" + currentOrder.getArea().toString() +"): ";
                String areaString = io.readString(message);
                if (!io.hasContent(areaString)) {
                    return currentOrder.getArea().toString();
                }else {
                    try {
                        Double.parseDouble(areaString);
                        if(currentOrder.validateArea(Double.parseDouble(areaString)))
                            return areaString;
                    } catch (NumberFormatException e) {
                        io.print("Invalid input!");

                    }
                }
            }else {
                double areaDouble = io.readBigDecimal(message);

                if (currentOrder.validateArea(areaDouble)) {
                    return areaDouble + "";
                } else {
                    message = "Invalid input! Please enter area: ";
                }
            }
        }
    }
    public int readConfirmPlaceOrderChoice() {
        return io.readInt("Please Confirm to place this order (Press 1 for yes and 2 for no)",1,2);
    }
    public int readConfirmUpdateOrderChoice() {
        return io.readInt("Please Confirm to place this order (Press 1 for yes and 2 for no)",1,2);
    }

    //Methods for display queries depends on user choice frm main menu
    public void displayOrder(Order currentOrder) {
        io.print("Order: OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
        io.print(currentOrder.toString());
    }
    public void displayProductsList(List<Product> products) {
        io.print("Products:");
        for (Product currentProduct : products) {
            String orderInfo = String.format("#%s : %s %s",
                    currentProduct.getProductType(),currentProduct.getCostPerSquareFoot(),currentProduct.getLaborCostPerSquareFoot());
            io.print(orderInfo);
        }
    }
    public void displayOrderList(List<Order> orderList) {
        if(orderList != null){
            for (Order currentOrder : orderList) {
                io.print(currentOrder.toString());
            }
            io.readString("Please hit enter to continue.");
        } else {
            displayErrorMessage("No orders on this date");
        }
    }

    //Display banners to show information about the statues of applications
    public void displayOrderUpdatedBanner() {
        io.print("=== Order successfully updated ===");
        io.readString("Please hit enter to continue.");
    }
    public void displayExportSuccessBanner() {
        io.print("=== Orders successfully Exported to Backup/DataExport.txt ===");
        io.readString("Please hit enter to continue.");
    }
    public void displayCreateSuccessBanner() {
        io.readString(
                "---Order successfully added-------\r\n.  Please hit enter to continue");
    }
    public void displayCreateOrderBanner() {
        io.print("=== Create a new Order ===");
    }
    public void displayDisplayOrdersBanner() {
        io.print("=== Display Orders for date picked===");
        io.print("Order: OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
    }
    public void displayKeepSameInfoBanner() {
        io.print("=== Keep Previous Information ===");
        io.readString("Please hit enter to continue.");
    }
    public void displayDisplayOrderBanner () {
        io.print("=== Display Order for update ===");
    }
    public void displayRemoveOrderBanner () {
        io.print("=== Order remove ===");
    }
    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }
    public void displayRemoveResult(Order orderRecord)  {
        if(orderRecord != null){
            io.print("Order successfully removed.");
        }else{
            io.print("No such order.");
        }
        io.readString("Please hit enter to continue.");
    }
    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }
    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
        io.readString("Please hit enter to continue.");
    }
    public void displayOrderCancelBanner() {
        io.print("=== Order Canceled ===");
    }
}

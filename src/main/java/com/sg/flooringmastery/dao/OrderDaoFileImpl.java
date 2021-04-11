package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private Map<Integer,Order> orders = new HashMap<>();
    private List<Order> newOrders = new ArrayList<>();
    private Scanner scanner;
    public static final String DELIMITER = ",";
    public static final String ORDER_NUMBER_FILE = "Data/OrderNumberFile.txt";

    @Override
    public Order addOrder(Integer orderNumber, Order order) throws OrderDaoException, IOException {

        System.out.println(order.toString());
        loadOrdersFile();
        Order newOrder = orders.put(orderNumber, order);
        newOrders.add(order);
        writeNewOrders();
        return newOrder;
    }

    @Override
    public List<Order> getOrders() throws OrderDaoException, IOException {
        loadOrdersFile();
        return new ArrayList<>(orders.values());
    }
    @Override
    public List<Order> getOrdersBaseOnDate(String date) throws OrderDaoException, IOException {
        loadOrdersFileBaseOnDate(date);
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order getOrder(Integer orderNumber, String date) throws OrderDaoException, IOException {
        loadOrdersFile();
        return orders.get(orderNumber);
    }

    @Override
    public Order removeOrder(String OrderId) throws OrderDaoException, IOException {
        loadOrdersFile();
        return null;
    }


    public Integer getPreviousOrderNumber() throws OrderDaoException {
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ORDER_NUMBER_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderDaoException(
                    "-_- Could not load order number data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine = scanner.nextLine();
        // close scanner
        scanner.close();

        return Integer.parseInt(currentLine);

    }

    @Override
    public void writeOrderNumberToFile(Integer orderNumber) throws OrderDaoException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(ORDER_NUMBER_FILE));
        } catch (IOException e) {
            throw new OrderDaoException(
                    "Could not save order number");
        }
        out.println(orderNumber);
        out.flush();
        out.close();
    }


    private Order unmarshallOrder(String orderAsText, String dataString) throws OrderDaoException {

        String[] orderTokens = orderAsText.split(DELIMITER);
        String orderNumber = orderTokens[0];
        Order orderFromFile = new Order(Integer.parseInt(orderNumber));
        orderFromFile.setCustomerName(orderTokens[1]);
        String productType = orderTokens[4];
        orderFromFile.setArea(orderTokens[5]);
        String stateAB = orderTokens[2];
        orderFromFile.setProduct(productType);
        orderFromFile.setTax(stateAB);
        orderFromFile.setOrderDate(dataString);
        return orderFromFile;
    }

    private void loadOrdersFile() throws OrderDaoException, IOException {
        String[] pathNames;
        File folder = new File("Orders");
        pathNames = folder.list();
        BufferedReader reader;

        for (String pathName: pathNames) {

            try {
                reader = new BufferedReader(new FileReader(folder + "/" + pathName));

            } catch (FileNotFoundException e) {
                throw new OrderDaoException(
                        "-_- Could not load order data into memory.", e);
            }
            // currentLine holds the most recent line read from the file
            String currentLine;
            Order currentOrder;
            reader.readLine(); // this will read the first line

            while ((currentLine=reader.readLine()) != null){

                currentOrder = unmarshallOrder(currentLine,pathName.substring(7,15));

                orders.put(currentOrder.getOrderNumber(), currentOrder);
            }
        }
    }

    private void loadOrdersFileBaseOnDate(String date) throws OrderDaoException, IOException {

        String pathName = "Orders/Orders_" + date + ".txt";
        BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(pathName));

            } catch (FileNotFoundException e) {
                throw new OrderDaoException(
                        "-_- Something went wrong or no orders on that date!", e);
            }
            // currentLine holds the most recent line read from the file
            String currentLine;
            Order currentOrder;
            reader.readLine(); // this will read the first line

            while ((currentLine=reader.readLine()) != null){

                currentOrder = unmarshallOrder(currentLine,pathName.substring(14,22));

                orders.put(currentOrder.getOrderNumber(), currentOrder);
            }

    }

    private String marshallOrder(Order aOrder, boolean withDate){
        String orderAsText = aOrder.getOrderNumber() + DELIMITER;
        orderAsText += aOrder.getCustomerName() + DELIMITER;
        orderAsText += aOrder.getTax().getStateAbbreviation() + DELIMITER;
        orderAsText += aOrder.getTax().getTaxRate() + DELIMITER;
        orderAsText += aOrder.getProduct().getProductType() + DELIMITER;
        orderAsText += aOrder.getArea() + DELIMITER;
        orderAsText += aOrder.getProduct().getCostPerSquareFoot() + DELIMITER;
        orderAsText += aOrder.getProduct().getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += aOrder.calculateMaterialCost() + DELIMITER;
        orderAsText += aOrder.calculateLaborCost() + DELIMITER;
        orderAsText += aOrder.calculateTax() + DELIMITER;
        orderAsText += aOrder.calculateTotal();

        if(withDate) {
            orderAsText += DELIMITER + aOrder.getOrderDate();
        }

        return orderAsText;
    }



    /**
     * Writes all students in the roster out to a ROSTER_FILE.  See loadRoster
     * for file format.
     *
     * @throws OrderDaoException if an error occurs writing to the file
     */
    private void writeOrders(boolean isExport) throws OrderDaoException, IOException {
        PrintWriter out;
        List<Order> orderList = this.getOrders();
        for (Order currentOrder : orderList) {

            String dateString = currentOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy"));
            String pathName = "Orders/Orders_" + dateString + ".txt";

            try {
                File file = new File(pathName);
                out = new PrintWriter(file);
                if (file.createNewFile()) {
                    out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
                }
            } catch (IOException e) {
                throw new OrderDaoException(
                        "Could not save order data.", e);
            }
            String orderAsText = marshallOrder(currentOrder, isExport);

            out.append(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
            out.close();
        }
    }
    /**
     * Writes all students in the roster out to a ROSTER_FILE.  See loadRoster
     * for file format.
     *
     * @throws OrderDaoException if an error occurs writing to the file
     */
    private void writeNewOrders() throws OrderDaoException, IOException {


        for(Order newOrder:newOrders) {
            System.out.println(newOrder.toString());
            String dateString = newOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy"));
            String pathName = "Orders/Orders_" + dateString + ".txt";

            try {
                File file = new File(pathName);
                boolean isNewFile = file.createNewFile();

                FileWriter fstream = new FileWriter(pathName, true);
                BufferedWriter out = new BufferedWriter(fstream);
                if (isNewFile) {
                    out.write("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
                }
                String orderAsText = marshallOrder(newOrder, false);
                out.newLine();
                out.write(orderAsText);
                // force PrintWriter to write line to the file
                out.close();
            } catch (IOException e) {
                throw new OrderDaoException(
                        "Could not save order data.", e);
            }


        }

    }
}

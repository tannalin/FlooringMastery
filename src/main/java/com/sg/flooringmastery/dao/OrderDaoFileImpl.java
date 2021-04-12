package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private Map<Integer,Order> orders = new HashMap<>();
    private Map<String,ArrayList<Order>> ordersByDate = new HashMap<>();
    private Scanner scanner;
    public static final String DELIMITER = ",";
    public static final String ORDER_NUMBER_FILE = "Data/OrderNumberFile.txt";

    @Override
    public Order addOrder(Integer orderNumber, Order order) throws OrderDaoException, IOException {
        loadOrdersFile();
        Order newOrder = orders.put(orderNumber, order);
        String date2String = order.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy"));
        if(ordersByDate.containsKey(date2String)) {
            ordersByDate.get(date2String).add(order);
        }else{
            ArrayList<Order> newList = new ArrayList<>();
            newList.add(order);
            ordersByDate.put(date2String, newList);
        }
        writeOrders();
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
    public void editOrder(Integer orderNumber, Order order) throws IOException, OrderDaoException {
        loadOrdersFile();
        String date2String = order.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy"));
        ArrayList<Order> arrayList = ordersByDate.get(date2String);
        Integer index = arrayList.indexOf(orders.get(orderNumber));
        arrayList.set(index,order);
        orders.replace(orderNumber, order);
        writeOrders();
    }

    @Override
    public Order getOrder(Integer orderNumber, String date) throws OrderDaoException, IOException {
        loadOrdersFile();
        return orders.get(orderNumber);
    }

    @Override
    public Order removeOrder(Integer orderNumber, String date) throws OrderDaoException, IOException {
        loadOrdersFile();
        Order removeOrder = orders.remove(orderNumber);
        ArrayList orderList = ordersByDate.get(date);
        orderList.remove(removeOrder);
        writeOrders();
        return removeOrder;
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
        String[] pathNameLists;
        File folder = new File("Orders");
        pathNameLists = folder.list();
        BufferedReader reader;

        for (String pathName: pathNameLists) {

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
            List<Order> orderlist = new ArrayList<>();
            while ((currentLine=reader.readLine()) != null){

                currentOrder = unmarshallOrder(currentLine,pathName.substring(7,15));
                orderlist.add(currentOrder);
                orders.put(currentOrder.getOrderNumber(), currentOrder);

            }
            ordersByDate.put(pathName.substring(7,15), (ArrayList<Order>) orderlist);
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
    private void writeOrders() throws OrderDaoException {
        PrintWriter out;

        for (String key: ordersByDate.keySet()){
            String pathName = "Orders/Orders_" + key + ".txt";

            try {
                out = new PrintWriter(new FileWriter(pathName));
            } catch (IOException e) {
                throw new OrderDaoException(
                        "Could not save student data.", e);
            }
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            ArrayList<Order> orderList = ordersByDate.get(key);
            String orderAsText;
            for (Order currentOrder : orderList ) {
                // turn a Student into a String
                orderAsText = marshallOrder(currentOrder,false);
                // write the Student object to the file
                out.println(orderAsText);
                // force PrintWriter to write line to the file
                out.flush();
            }
            // Clean up
            out.close();
        }
    }

}

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private Map<Integer,Order> orders = new HashMap<>();
    private Map<String,ArrayList<Order>> ordersByDate = new HashMap<>();
    private Scanner scanner;
    private static String directory;
    public static String DELIMITER = ",";
    public static String ORDER_NUMBER_FILE ;
    public static String BACK_UP_FILE ;
    public static String ORDERS_FILE_DIRECTORY;
    public static int COUNT_DELIMITER = 11;

    public OrderDaoFileImpl(String testDirectory) {
        directory = testDirectory;
       ORDER_NUMBER_FILE = directory + "Data/OrderNumberFile.txt";
       BACK_UP_FILE = directory + "Backup/DataExport.txt";
        ORDERS_FILE_DIRECTORY = directory;
    }

    public OrderDaoFileImpl() {
       directory = "DataFiles/";
        ORDER_NUMBER_FILE = directory + "Data/OrderNumberFile.txt";
        BACK_UP_FILE = directory + "Backup/DataExport.txt";
        ORDERS_FILE_DIRECTORY = directory;
    }

    /**
     * Adds the given Order to the file and associates it with the given
     * Order number. If there is already a student associated with the given
     * Order number it will return that Order object, otherwise it will
     * return null.
     *
     * @param orderNumber id with which Order is to be associated
     * @param order Order to be added to the roster
     * @return the Order object previously associated with the given
     * Order id if it exists, null otherwise
     * @throws OrderPersistenceException
     */
    @Override
    public Order addOrder(Integer orderNumber, Order order) throws OrderPersistenceException, IOException {
        loadOrdersFile();
        //add to order list
        Order newOrder = orders.put(orderNumber, order);
        //Add to order list map by date
        String date2String = order.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy"));
        //if we already has order on this date, add to that array list
        if(ordersByDate.containsKey(date2String)) {
            ordersByDate.get(date2String).add(order);
        }else{
            //if we have no order on this date, create a new array list
            ArrayList<Order> newList = new ArrayList<>();
            newList.add(order);
            ordersByDate.put(date2String, newList);
        }

        //write to file
        writeOrders();
        return newOrder;
    }
    /**
     * Returns a List of all orders on the file.
     *
     * @return Order List containing all Orders on the file.
     * @throws OrderPersistenceException
     */
    @Override
    public List<Order> getOrders() throws OrderPersistenceException, IOException {
        loadOrdersFile();
        return new ArrayList<>(orders.values());
    }
    /**
     * @param date the date that user want to have the list of this date
     * @return a list of orders
     * @throws OrderPersistenceException
     * @throws IOException
     */
    @Override
    public List<Order> getOrdersBaseOnDate(String date) throws OrderPersistenceException, IOException {
        loadOrdersFile();
        //we get the array list base on that date
        if(ordersByDate.containsKey(date)) {
            return new ArrayList<>(ordersByDate.get(date));
        } else {
            return null;
        }
    }
    /**
     * will edit the order base on the info user provide
     * @param orderNumber
     * @param order
     * @throws IOException
     * @throws OrderPersistenceException
     */
    @Override
    public void editOrder(Integer orderNumber, Order order) throws IOException, OrderPersistenceException {
        loadOrdersFile();
        String date2String = order.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy"));
        ArrayList<Order> arrayList = ordersByDate.get(date2String);
        Integer index = arrayList.indexOf(orders.get(orderNumber));
        arrayList.set(index,order);
        orders.replace(orderNumber, order);
        writeOrders();
    }

    /**
     * Returns the Order object associated with the given number.
     * Returns null if no such Order exists
     *
     * @param orderNumber ID of the Order to retrieve
     * @param date
     * @return the Order object associated with the given Order number,
     * null if no such Order exists
     * @throws OrderPersistenceException
     */
    @Override
    public Order getOrder(Integer orderNumber, String date) throws OrderPersistenceException, IOException {
        loadOrdersFile();
        Order order = orders.get(orderNumber);
        if(order != null && order.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy")).equals(date)){
            return order;
        }
        return null;

    }
    /**
     * Removes from the roster the Order associated with the given number.
     * Returns the Order object that is being removed or null if
     * there is no Order associated with the given number
     *
     * @param orderNumber number of Order to be removed
     * @param date date of Order to be removed
     * @return Order object that was removed or null if no Order
     * was associated with the given Order number
     * @throws OrderPersistenceException
     */
    @Override
    public Order removeOrder(Integer orderNumber, String date) throws OrderPersistenceException, IOException {
        loadOrdersFile();
        System.out.println(getOrder(orderNumber,date));
        if(getOrder(orderNumber,date) == null){
            throw new OrderPersistenceException("No such order!");
        }
        Order removeOrder = orders.remove(orderNumber);
        ArrayList orderList = ordersByDate.get(date);
        orderList.remove(removeOrder);
        //also remove it from the order list map by date
        ordersByDate.get(date).remove(removeOrder);
        if(ordersByDate.get(date).size() == 0){
            //delete file by date
            File dateObj = new File(ORDERS_FILE_DIRECTORY + "Orders/Orders_" + date +".txt");
            dateObj.delete();
        }
        writeOrders();
        return removeOrder;
    }

    /**
     * Export orders to back up file
     * @throws IOException
     * @throws OrderPersistenceException
     */
    @Override
    public void exportOrders() throws IOException, OrderPersistenceException {
        loadOrdersFile();
        backupOrders();
    }
    /**
     * @return the number that store in the file that keep the number auto increment
     * @throws OrderPersistenceException
     */

    public Integer getPreviousOrderNumber() throws OrderPersistenceException {

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ORDER_NUMBER_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderPersistenceException(
                    "-_- Could not load order number data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine = scanner.nextLine();
        // close scanner
        scanner.close();

        return Integer.parseInt(currentLine);

    }
    /**
     * store the order number in the file that keep the number auto increment
     * @param orderNumber
     * @throws OrderPersistenceException
     */
    @Override
    public void writeOrderNumberToFile(Integer orderNumber) throws OrderPersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(ORDER_NUMBER_FILE));
        } catch (IOException e) {
            throw new OrderPersistenceException(
                    "Could not save order number");
        }
        out.println(orderNumber);
        out.flush();
        out.close();
    }

    /**
     * return un order object which retrieve from the data files line by line
     * @param orderAsText
     * @param dataString
     * @return
     * @throws OrderPersistenceException
     */
    private Order unmarshallOrder(String orderAsText, String dataString) throws OrderPersistenceException {
        //count how many "," in this line of text
        int countDelimiter = getCountDelimiter(orderAsText);
        //get the index of first ","
        int indexOfFirstDelimiter = getIndexOfFirstDelimiter(orderAsText);
        //get the index of the "," that after customer name
        int indexOfCountDelimiter = getIndexOfCountDelimiter(countDelimiter - COUNT_DELIMITER + 2,orderAsText );
        //get order number by get the string first index to the first ","
        String orderNumber = orderAsText.substring(0, indexOfFirstDelimiter);
        Order orderFromFile = new Order(Integer.parseInt(orderNumber));
        //get the customer name by get the string after first index of ",", to the index of "," that after Customer name
        String customerName = orderAsText.substring(indexOfFirstDelimiter + 1,indexOfCountDelimiter);
        orderFromFile.setCustomerName(customerName);
        //the split the rest string, and get the rest of information base on the order of token
        String[] orderTokens = orderAsText.substring(indexOfCountDelimiter + 1).split(DELIMITER);
        String productType = orderTokens[2];
        orderFromFile.setArea(orderTokens[3]);
        String stateAB = orderTokens[0];
        orderFromFile.setProduct(productType);
        orderFromFile.setTax(stateAB);
        orderFromFile.setOrderDate(dataString);
        return orderFromFile;
    }

    /**
     * get the index of the "," that after customer name
     * @param i
     * @param orderAsText
     * @return
     */
    private int getIndexOfCountDelimiter(int i, String orderAsText) {
        int index = 0;
        int count = 0;
        for(; index < orderAsText.length(); index++){
            if ((orderAsText.charAt(index)+"").equals(DELIMITER)) {
                count++;
                if(count == i) break;
            }
        }
        return index;
    }

    /**
     * get the index of first ","
     * @param orderAsText
     * @return
     */
    private int getIndexOfFirstDelimiter(String orderAsText) {
        int index = 0;
        for(; index < orderAsText.length(); index++){
            if ((orderAsText.charAt(index)+"").equals(DELIMITER)) {
                break;
            }
        }
        return index;
    }

    /** Load all the orders and return all the orders
     * @throws OrderPersistenceException
     * @throws IOException
     */
    private void loadOrdersFile() throws OrderPersistenceException, IOException {
        String[] pathNameLists;
        File folder = new File(ORDERS_FILE_DIRECTORY + "Orders");
        pathNameLists = folder.list();
        BufferedReader reader;

        if (pathNameLists != null) {
            for (String pathName: pathNameLists) {

                try {
                    reader = new BufferedReader(new FileReader(folder + "/" + pathName));

                } catch (FileNotFoundException e) {
                    throw new OrderPersistenceException(
                            "-_- Could not load order data into memory.", e);
                }
                // currentLine holds the most recent line read from the file
                String currentLine;
                Order currentOrder;
                reader.readLine(); // this will read the first line
                ArrayList<Order> orderList = new ArrayList<>();
                while ((currentLine = reader.readLine()) != null){

                    currentOrder = unmarshallOrder(currentLine,pathName.substring(7,15));
                    orderList.add(currentOrder);
                    orders.put(currentOrder.getOrderNumber(), currentOrder);

                }
                ordersByDate.put(pathName.substring(7,15), orderList);
            }
        }
    }

    /**
     * to string before write into file ot back up the orders
     * @param aOrder
     * @param withDate when it is for back up, we also need the date string
     * @return
     */
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
     * Writes all orders in the orderList out to files. See Orders/
     *
     * @throws OrderPersistenceException if an error occurs writing to the file
     */
    private void writeOrders() throws OrderPersistenceException {
        PrintWriter out;

        for (String key: ordersByDate.keySet()){
            String pathName = ORDERS_FILE_DIRECTORY + "Orders/Orders_" + key + ".txt";

            try {
                out = new PrintWriter(new FileWriter(pathName));
            } catch (IOException e) {
                throw new OrderPersistenceException(
                        "Could not save orders data.", e);
            }
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            ArrayList<Order> orderList = ordersByDate.get(key);
            String orderAsText;
            for (Order currentOrder : orderList ) {
                orderAsText = marshallOrder(currentOrder,false);
                out.println(orderAsText);
                out.flush();
            }
            // Clean up
            out.close();
        }
    }

    /**
     * action to export orders to back up file
     * @throws IOException
     * @throws OrderPersistenceException
     */
    public void backupOrders() throws OrderPersistenceException, IOException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(BACK_UP_FILE));
        } catch (IOException e) {
            throw new OrderPersistenceException(
                    "Could not save orders data.", e);
        }
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate" +
                "");
        ArrayList<Order> orderList = (ArrayList<Order>) getOrders();
        Collections.sort(orderList);
        Collections.reverse(orderList);
        String orderAsText;
        for (Order currentOrder : orderList ) {
            // turn a Student into a String
            orderAsText = marshallOrder(currentOrder,true);
            // write the Student object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();


    }

    /**
     * count how many "," in this line of text
     * @param text
     * @return
     */
    public int getCountDelimiter(String text){
        int count = 0;
        for(int i = 0; i < text.length(); i++){
            if ((text.charAt(i)+"").equals(DELIMITER)) {
                count++;
            }
        }
        return count;
    }

}

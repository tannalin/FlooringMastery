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
    public static final String BACK_UP_FILE = "Backup/DataExport.txt";
    public static final int COUNT_DELIMTER = 11;

    @Override
    public Order addOrder(Integer orderNumber, Order order) throws OrderPersistenceException, IOException {
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
    public List<Order> getOrders() throws OrderPersistenceException, IOException {
        loadOrdersFile();
        return new ArrayList<>(orders.values());
    }

    @Override
    public List<Order> getOrdersBaseOnDate(String date) throws OrderPersistenceException, IOException {
        loadOrdersFile();
        if(ordersByDate.containsKey(date)) {
            return new ArrayList<>(ordersByDate.get(date));
        } else {
            return null;
        }
    }

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

    @Override
    public Order getOrder(Integer orderNumber, String date) throws OrderPersistenceException, IOException {
        loadOrdersFile();
        return orders.get(orderNumber);
    }

    @Override
    public Order removeOrder(Integer orderNumber, String date) throws OrderPersistenceException, IOException {
        loadOrdersFile();
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
            File dateObj = new File("Orders/Orders_" + date +".txt");
            dateObj.delete();
        }
        writeOrders();
        return removeOrder;
    }

    @Override
    public void exportOrders() throws IOException, OrderPersistenceException {
        loadOrdersFile();
        backupOrders();
    }

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

    private Order unmarshallOrder(String orderAsText, String dataString) throws OrderPersistenceException {
        int countDelimiter = getCountDelimiter(orderAsText);
        int indexOfFirstDelimeter = getIndexOfFirstDelimiter(orderAsText);
        int indexOfCountDelimeter = getIndexOfCountDelimiter(countDelimiter - COUNT_DELIMTER + 2,orderAsText );
        String orderNumber = orderAsText.substring(0,indexOfFirstDelimeter);
        Order orderFromFile = new Order(Integer.parseInt(orderNumber));
        String customerName = orderAsText.substring(indexOfFirstDelimeter + 1,indexOfCountDelimeter);
        orderFromFile.setCustomerName(customerName);
        String[] orderTokens = orderAsText.substring(indexOfCountDelimeter + 1).split(DELIMITER);
        String productType = orderTokens[2];
        orderFromFile.setArea(orderTokens[3]);
        String stateAB = orderTokens[0];
        orderFromFile.setProduct(productType);
        orderFromFile.setTax(stateAB);
        orderFromFile.setOrderDate(dataString);
        return orderFromFile;
    }

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

    private int getIndexOfFirstDelimiter(String orderAsText) {
        int index = 0;
        for(; index < orderAsText.length(); index++){
            if ((orderAsText.charAt(index)+"").equals(DELIMITER)) {
                break;
            }
        }
        return index;
    }

    private void loadOrdersFile() throws OrderPersistenceException, IOException {
        String[] pathNameLists;
        File folder = new File("Orders");
        pathNameLists = folder.list();
        BufferedReader reader;

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
            List<Order> orderlist = new ArrayList<>();
            while ((currentLine=reader.readLine()) != null){

                currentOrder = unmarshallOrder(currentLine,pathName.substring(7,15));
                orderlist.add(currentOrder);
                orders.put(currentOrder.getOrderNumber(), currentOrder);

            }
            ordersByDate.put(pathName.substring(7,15), (ArrayList<Order>) orderlist);
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
     * Writes all orders in the orderList out to files. See Orders/
     * for file format.
     *
     * @throws OrderPersistenceException if an error occurs writing to the file
     */
    private void writeOrders() throws OrderPersistenceException {
        PrintWriter out;

        for (String key: ordersByDate.keySet()){
            String pathName = "Orders/Orders_" + key + ".txt";

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
     * Writes all orders in the orderList out to files. See Orders/
     * for file format.
     *
     * @throws OrderPersistenceException if an error occurs writing to the file
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

package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dao.OrderDaoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO{

    final private Scanner console = new Scanner(System.in);

    /**
     *
     * A very simple method that takes in a message to display on the console
     * and then waits for a integer answer from the user to return.
     *
     * @param msg - String of information to display to the user.
     *
     */
    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    /**
     *
     * A simple method that takes in a message to display on the console,
     * and then waits for an answer from the user to return.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as string
     */
    @Override
    public String readString(String msgPrompt, boolean isAllowNull) throws OrderDaoException {
        String readString = "";
        if(!isAllowNull){
            boolean invalidInput = true;
            while (invalidInput) {
                try {
                    System.out.println(msgPrompt);
                    readString = console.nextLine();
                    if(hasContent(readString)){
                        invalidInput = false;
                    } else {
                        throw new OrderDaoException("Input is empty. Please try again.");
                    }
                } catch (OrderDaoException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.out.println(msgPrompt);
            readString = console.nextLine();
        }
        return readString;
    }
    /**
     *
     * A simple method that takes in a message to display on the console,
     * and then waits for an answer from the user to return.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as string
     */
    @Override
    public String readString(String msgPrompt) {
        System.out.println(msgPrompt);
        return console.nextLine();
    }

    @Override
    public String readDate(String msgPrompt) throws OrderDaoException {
        boolean invalidInput = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String sOrderDate = "";

        while (invalidInput) {
            try {
                sOrderDate = this.readString(msgPrompt,false);
                LocalDate orderDate = LocalDate.parse(sOrderDate,formatter);
                LocalDate START = LocalDate.now();
                if(orderDate.isBefore(START)){
                    throw new OrderDaoException("Order date must be in future");
                }
                invalidInput = false;
            } catch (Exception e) {
                throw new OrderDaoException("Input should in format MMddyyyy (12302012). Please try again.");
            }
        }
        System.out.println(sOrderDate);
        return sOrderDate;
    }

    /**
     *
     * A simple method that takes in a message to display on the console,
     * and continually reprompts the user with that message until they enter a double
     * to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as double
     */
    @Override
    public double readBigDecimal(String msgPrompt) {
        while (true) {
            try {
                return Double.parseDouble(this.readString(msgPrompt,false));
            } catch (NumberFormatException | OrderDaoException e) {
                this.print("Input error. Please try again.");
            }
        }
    }
    /**
     *
     * A slightly more complex method that takes in a message to display on the console,
     * and continually reprompts the user with that message until they enter an integer
     * within the specified min/max range to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @param min - minimum acceptable value for return
     * @param max - maximum acceptable value for return
     * @return an integer value as an answer to the message prompt within the min/max range
     */
    @Override
    public int readInt(String msgPrompt, int min, int max) {
        int result;
        do {
            result = readInt(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

    /**
     *
     * A simple method that takes in a message to display on the console,
     * and continually reprompts the user with that message until they enter an integer
     * to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as integer
     */
    @Override
    public int readInt(String msgPrompt) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                // print the message msgPrompt (ex: asking for the # of cats!)
                String stringValue = this.readString(msgPrompt,false);
                // Get the input line, and try and parse
                num = Integer.parseInt(stringValue); // if it's 'bob' it'll break
                invalidInput = false; // or you can use 'break;'
            } catch (NumberFormatException | OrderDaoException e) {
                // If it explodes, it'll go here and do this.
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }
    /**
     WARNING: This method is extremely common, and should be in a utility class.
     (It really should be in the JDK, as a static method of the String class.)
     */
    private boolean hasContent(String string) {
        return (string != null && string.trim().length() > 0);
    }


}

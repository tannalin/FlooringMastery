package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dao.OrderDaoException;

public interface UserIO {
    void print(String msg);

    double readBigDecimal(String prompt);
    int readInt(String prompt);

    int readInt(String prompt, int min, int max);


    String readDate(String s) throws OrderDaoException;

    String readString(String prompt);
    boolean hasContent(String string);
}

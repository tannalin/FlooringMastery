package com.sg.flooringmastery.view;

public interface UserIO {
    void print(String msg);
    double readBigDecimal(String prompt);
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);
    String readString(String prompt);
    boolean hasContent(String string);
}

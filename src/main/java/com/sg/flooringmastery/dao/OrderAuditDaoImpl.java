package com.sg.flooringmastery.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class OrderAuditDaoImpl implements OrderAuditDao {

    public static final String AUDIT_FILE = "DataFiles/Data/audit.txt";

    /**
     * @param entry write logs to file  "Data/audit.txt"
     * @throws OrderPersistenceException OrderPersistenceException
     */
    @Override
    public void writeAuditEntry(String entry) throws OrderPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not persist audit information.", e);
        }

        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp.toString() + " : " + entry);
        out.flush();
    }
}

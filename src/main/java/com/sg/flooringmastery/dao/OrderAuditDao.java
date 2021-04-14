package com.sg.flooringmastery.dao;

public interface OrderAuditDao {
    /**
     * @param entry write logs to file  "Data/audit.txt"
     * @throws OrderPersistenceException OrderPersistenceException
     */
    void writeAuditEntry(String entry) throws OrderPersistenceException;
}

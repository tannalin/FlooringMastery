package com.sg.flooringmastery.dao;

public interface OrderAuditDao {
    public void writeAuditEntry(String entry) throws OrderPersistenceException;
}

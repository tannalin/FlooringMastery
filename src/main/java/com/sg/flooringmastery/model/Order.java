package com.sg.flooringmastery.model;
import com.sg.flooringmastery.dao.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 *
 */
public class Order{
    private LocalDate orderDate;
    private Integer orderNumber;
    private String customerName;
    private Tax tax;
    private Product product;
    private BigDecimal area;

    public Order(){

    }
    public Order(Integer orderNumber, String customerName, Tax tax, Product product, BigDecimal area) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.tax = tax;
        this.product = product;
        this.area = area;
    }


    public Order(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public Tax getTax() {
        return tax;
    }
    public void setTax(String stateAB) throws OrderDaoException {
        TaxDao taxDao = new TaxDaoFileImpl();
        Tax tax = taxDao.getTax(stateAB);
        this.tax = tax;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(String productType) throws OrderDaoException {
        ProductDao productDao = new ProductDaoFileImpl();
        Product product = productDao.getProduct(productType);
        this.product = product;
    }
    public BigDecimal getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = new BigDecimal(area);
    }

    /**
     * @return BigDecimal labor Cost
     */
    public BigDecimal calculateLaborCost(){
        BigDecimal laborCost = this.area.multiply(this.product.getCostPerSquareFoot()).setScale(2, RoundingMode.CEILING);
        return laborCost;
    }
    /**
     * @return BigDecimal labor Cost
     */
    public BigDecimal calculateMaterialCost(){
        BigDecimal materialCost = this.area.multiply(this.product.getCostPerSquareFoot()).setScale(2, RoundingMode.CEILING);
        return materialCost;
    }

    /**
     * @return BigDecimal Tax
     */
    public BigDecimal calculateTax(){
        BigDecimal tax = this.tax.getTaxRate().multiply(calculateMaterialCost().add(calculateLaborCost())).setScale(2, RoundingMode.CEILING);
        return tax;
    }
    /**
     * @return BigDecimal total cost
     */
    public BigDecimal calculateTotal(){
        BigDecimal total = calculateMaterialCost().add(calculateTax()).add(calculateLaborCost());
        return total;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String sOrderDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        LocalDate orderDate = LocalDate.parse(sOrderDate,formatter);
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderDate=" + orderDate +
                ", orderNumber=" + orderNumber +
                ", customerName='" + customerName + '\'' +
                ", tax=" + tax +
                ", product=" + product +
                ", area=" + area +
                '}';
    }

    public boolean validateState(String stateAb) throws OrderDaoException {
        TaxDao taxDao = new TaxDaoFileImpl();
        List<Tax> taxes = taxDao.getTaxes();
        for (Tax tax:taxes)
             {if(tax.getStateAbbreviation().equals(stateAb)){
                 return true;
             }
        }
        return false;
    }

    public boolean validateProduct(String productType) throws OrderDaoException {
        ProductDao productDao = new ProductDaoFileImpl();
        Product product = productDao.getProduct(productType);
        return product != null;
    }
}




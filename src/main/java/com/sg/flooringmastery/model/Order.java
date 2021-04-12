package com.sg.flooringmastery.model;
import com.sg.flooringmastery.dao.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class Order implements Comparable {
    private LocalDate orderDate;
    private Integer orderNumber;
    private String customerName;
    private Tax tax;
    private Product product;
    private BigDecimal area;

    public Order() { }
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
        this.tax  = taxDao.getTax(stateAB);
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(String productType) throws OrderDaoException {
        ProductDao productDao = new ProductDaoFileImpl();
        this.product = productDao.getProduct(productType);
    }
    public BigDecimal getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = new BigDecimal(area).setScale(2, RoundingMode.CEILING);
    }

    /**
     * @return BigDecimal labor Cost
     */
    public BigDecimal calculateLaborCost(){
        return this.area.multiply(this.product.getCostPerSquareFoot()).setScale(2, RoundingMode.CEILING);
    }
    /**
     * @return BigDecimal labor Cost
     */
    public BigDecimal calculateMaterialCost(){
        return this.area.multiply(this.product.getCostPerSquareFoot()).setScale(2, RoundingMode.CEILING);
    }

    /**
     * @return BigDecimal Tax
     */
    public BigDecimal calculateTax(){
        return this.tax.getTaxRate().multiply(calculateMaterialCost().add(calculateLaborCost())).setScale(2, RoundingMode.CEILING);
    }
    /**
     * @return BigDecimal total cost
     */
    public BigDecimal calculateTotal(){
        return calculateMaterialCost().add(calculateTax()).add(calculateLaborCost());
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String sOrderDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        this.orderDate = LocalDate.parse(sOrderDate,formatter);
    }

    @Override
    public String toString() {
        return "    " + orderNumber + "   " + customerName + "   " + tax.getStateName() + "   " + tax.getTaxRate() + "   " + product.getProductType() + "   " + area + "   " + product.getCostPerSquareFoot() + "   " + product.getLaborCostPerSquareFoot() + " "+
                calculateMaterialCost() + "   " + calculateLaborCost() + "   " + calculateTax() + "   " + calculateTotal();
    }

    public boolean validateState(String stateAb) throws OrderDaoException {
        TaxDao taxDao = new TaxDaoFileImpl();
        Tax tax = taxDao.getTax(stateAb);
        return tax != null;
    }

    public boolean validateProduct(String productType) throws OrderDaoException {
        ProductDao productDao = new ProductDaoFileImpl();
        Product product = productDao.getProduct(productType);
        return product != null;
    }

    public boolean validateArea(double areaDouble) {
        final int MIN_AREA = 100;
        BigDecimal area = new BigDecimal(areaDouble).setScale(2, RoundingMode.CEILING);
        return area.intValue() > MIN_AREA;
    }

    public boolean validateDate(String dateString)  {
        String regex = "^(1[0-2]|0[1-9])(3[01]|[12][0-9]|0[1-9])[0-9]{4}$";
        if(dateString.matches(regex)){
            int month = Integer.parseInt(dateString.substring(0,2));
            int days = Integer.parseInt(dateString.substring(2,4));
            int year = Integer.parseInt(dateString.substring(4));
            if (days == 31  && (month== 4|| month == 6 || month == 9 || month == 11)){
                return false;
            } else if (days >= 30 && month == 2) {
                return false;
            } else if (month == 2 && days == 29 &&!( year % 4 == 0 && year % 100 != 0 || year % 400 == 0)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    @Override
    public int compareTo(Object o) {
        return ((Order) o).getOrderNumber().compareTo(this.getOrderNumber());
    }
}




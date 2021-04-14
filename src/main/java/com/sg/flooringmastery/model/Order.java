package com.sg.flooringmastery.model;
import com.sg.flooringmastery.dao.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
    public void setTax(String stateAB) throws OrderPersistenceException {
        TaxDao taxDao = new TaxDaoFileImpl();
        this.tax  = taxDao.getTax(stateAB);
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(String productType) throws OrderPersistenceException {
        ProductDao productDao = new ProductDaoFileImpl();
        this.product = productDao.getProduct(productType);
    }
    public BigDecimal getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = new BigDecimal(area).setScale(2, RoundingMode.CEILING);
    }
    public LocalDate getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String sOrderDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        this.orderDate = LocalDate.parse(sOrderDate,formatter);
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

    @Override
    public String toString() {
        return "    " + orderNumber + "   " + customerName + "   " + tax.getStateName() + "   " + tax.getTaxRate() + "   " + product.getProductType() + "   " + area + "   " + product.getCostPerSquareFoot() + "   " + product.getLaborCostPerSquareFoot() + " "+
                calculateMaterialCost() + "   " + calculateLaborCost() + "   " + calculateTax() + "   " + calculateTotal();
    }

    /** validate state must be on the list of taxes file
     * @param stateAb the state abbreviation
     * @return boolean is validate ot not
     * @throws OrderPersistenceException
     */
    public boolean validateState(String stateAb) throws OrderPersistenceException {
        TaxDao taxDao = new TaxDaoFileImpl();
        Tax tax = taxDao.getTax(stateAb);
        return tax != null;
    }

    /** validate the product must be on the list of products
     * @param productType the product type stirng
     * @return boolean is validate
     * @throws OrderPersistenceException
     */
    public boolean validateProduct(String productType) throws OrderPersistenceException {
        ProductDao productDao = new ProductDaoFileImpl();
        Product product = productDao.getProduct(productType);
        return product != null;
    }

    /**validate the area must be more than 100
     * @param areaDouble area that is validate or not
     * @return boolean is validate
     */
    public boolean validateArea(double areaDouble) {
        final int MIN_AREA = 100;
        BigDecimal area = new BigDecimal(areaDouble).setScale(2, RoundingMode.CEILING);
        return area.intValue() > MIN_AREA;
    }

    /**validate the date must be an date that in future
     * @param dateString date string that retrieve from outside
     * @return boolean is validate
     */
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
            } else return month != 2 || days != 29 || (year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
        } else {
            return false;
        }
    }

    /**
     * @param customerName customer name
     * @return boolean is validate
     */
    public boolean validateCustomer(String customerName) {
        return customerName != null && customerName.trim().length() > 0;
    }

    /**
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        return ((Order) o).getOrderNumber().compareTo(this.getOrderNumber());
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.customerName);
        hash = 89 * hash + Objects.hashCode(this.orderNumber);
        hash = 89 * hash + Objects.hashCode(this.orderDate);
        hash = 89 * hash + Objects.hashCode(this.tax);
        hash = 89 * hash + Objects.hashCode(this.product);
        hash = 89 * hash + Objects.hashCode(this.area);
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.orderNumber, other.orderNumber)) {
            return false;
        }
        return Objects.equals(this.area, other.area);
    }
}




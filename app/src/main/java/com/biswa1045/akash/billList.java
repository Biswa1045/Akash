package com.biswa1045.akash;

import java.io.Serializable;

public class billList implements Serializable {

    private String id;
    private String customerName;
    private String time;
    private String invoice;
    private String total;
    private String location;
    public billList() {

    }

    public billList(String id, String customerName, String time,String invoice, String total,String location) {
        id =id;
        customerName=customerName;
        time=time;
        invoice=invoice;
        total=total;
        location=location;

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}


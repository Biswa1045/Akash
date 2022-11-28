package com.biswa1045.akash;

import java.io.Serializable;

public class product implements Serializable {

    private String productname;

    private String productprice;
    private String productstuck;
    private String img;
    public product() {

    }

    public product(String productname, String productprice, String productstuck,String img) {
         productname = productname;
        productprice = productprice;
        productstuck =productstuck;
        img = img;
    }

    public String getProductstuck() {
        return productstuck;
    }

    public void setProductstuck(String productstuck) {
        this.productstuck = productstuck;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}


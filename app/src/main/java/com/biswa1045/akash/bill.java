package com.biswa1045.akash;

import java.io.Serializable;

public class bill implements Serializable {

    private String productname;
    private String productprice;
    private String productqntl;
    private String img;
    public bill() {

    }

    public bill(String productname, String productprice, String productqntl,String img) {
        productname = productname;
        productprice = productprice;
        productqntl =productqntl;
        img = img;

    }



    public String getProductqntl() {
        return productqntl;
    }

    public void setProductqntl(String productqntl) {
        this.productqntl = productqntl;
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


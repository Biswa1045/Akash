package com.biswa1045.akash;

import java.io.Serializable;

public class check implements Serializable {

    private boolean isChecked =false ;
    private String productname;
    private String productprice;
    private String productstuck;
    private String img;
    public check(boolean isChecked, String productname,String productprice,String productstuck,String img ) {
        this.isChecked = isChecked;
        this.productname = productname;
        this.productprice = productprice;
        this.productstuck = productstuck;
        this.img = img;

    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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

    public String getProductstuck() {
        return productstuck;
    }

    public void setProductstuck(String productstuck) {
        this.productstuck = productstuck;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

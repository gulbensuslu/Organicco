package com.example.organicco;

public class Products{

    String product_name;
    String image;
    String kind;
    String region;
    float product_price;

    public Products() {

    }

    public Products(String product_name, String image, String kind, String region, float product_price) {
           this.product_name = product_name;
           this.image = image;
           this.kind = kind;
           this.region = region;
           this.product_price = product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getImage() {
        return image;
    }

    public String getKind() {
        return kind;
    }

    public String getRegion() {
        return region;
    }

    public float getProduct_price() {
        return product_price;
    }
}
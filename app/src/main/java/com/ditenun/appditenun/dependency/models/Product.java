package com.ditenun.appditenun.dependency.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String productCode;
    private String productName;
    private String productSize;
    private Double productPrice;
    private Integer productQty;
    private String productImageUrl;

    public Product(){ }

    protected Product(Parcel in) {
        productCode = in.readString();
        productName = in.readString();
        productSize = in.readString();
        if (in.readByte() == 0) {
            productPrice = null;
        } else {
            productPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            productQty = null;
        } else {
            productQty = in.readInt();
        }
        productImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productCode);
        dest.writeString(productName);
        dest.writeString(productSize);
        if (productPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(productPrice);
        }
        if (productQty == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(productQty);
        }
        dest.writeString(productImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductQty() {
        return productQty;
    }

    public void setProductQty(Integer productQty) {
        this.productQty = productQty;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

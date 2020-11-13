package com.ditenun.appditenun.function.activity.commerce.delivery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class DeliveryViewModel extends AndroidViewModel {

    private List<Product> productList = new ArrayList<>();
    private String address;
    private double shippingPrice;
    private double promo;

    private SingleLiveEvent<String> submitAddressEvent = new SingleLiveEvent<>();

    public DeliveryViewModel(@NonNull Application application) {
        super(application);
    }

    public void submitAddress(String value) {
        this.address = value;
        this.submitAddressEvent.postValue(address);
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getAddress() {
        return address;
    }

    public double calculateTotalOrderPrice() {
        double totalPrice = 0.0;
        if (productList != null && !productList.isEmpty()) {
            for (Product item : productList) {
                totalPrice += (item.getPrice() * item.getQty());
            }
        }
        return totalPrice;
    }

    public double calculateNettTotalPrice() {
        double nettTotalPrice = 0.0;
        nettTotalPrice = (calculateTotalOrderPrice() + shippingPrice) - promo;
        return nettTotalPrice;
    }

    public SingleLiveEvent<String> getSubmitAddressEvent() {
        return submitAddressEvent;
    }
}

package com.ditenun.appditenun.function.activity.commerce.delivery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Product;

import java.util.ArrayList;
import java.util.List;

public class DeliveryViewModel extends AndroidViewModel {

    private List<Product> productList = new ArrayList<>();

    public DeliveryViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}

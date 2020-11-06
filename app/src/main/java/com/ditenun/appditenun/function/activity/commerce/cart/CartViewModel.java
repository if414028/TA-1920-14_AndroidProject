package com.ditenun.appditenun.function.activity.commerce.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.network.TenunNetworkInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CartViewModel extends AndroidViewModel {

    @Inject
    TenunNetworkInterface tenunNetworkInterface;

    private Double totalPrice;
    private List<Product> productList = new ArrayList<>();
    private MutableLiveData<List<Product>> productListLiveData = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
    }

    public void addProduct(Product product) {
        this.productList.add(product);
        productListLiveData.postValue(productList);
    }

    public Double calculateTotalPrice() {
        Double totalPrice = 0.0;
        for (Product item : productList) {
            totalPrice += (item.getProductPrice() * item.getProductQty());
        }
        return totalPrice;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public MutableLiveData<List<Product>> getProductListLiveData() {
        return productListLiveData;
    }
}

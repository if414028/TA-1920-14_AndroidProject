package com.ditenun.appditenun.function.activity.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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

    public CartViewModel(@NonNull Application application) {
        super(application);

        setupProductList();
    }

    private void setupProductList(){
        Product product = new Product();
        product.setProductName("Ulos Mangiring");
        product.setProductCode("1");
        product.setProductSize("Free Size");
        product.setProductPrice(1350000.0);
        product.setProductQty(2);
        productList.add(product);
    }

    public List<Product> getProductList() {
        return productList;
    }
}

package com.ditenun.appditenun.function.activity.dashboard.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.ditenun.appditenun.dependency.models.Category;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.network.TenunNetworkInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ProductHomeViewModel extends AndroidViewModel {

    @Inject
    TenunNetworkInterface tenunNetworkInterface;

    private List<Product> newArrivalsProductList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    public ProductHomeViewModel(@NonNull Application application) {
        super(application);
        setupListNewArrivals();
        setupCategoryList();
    }

    private void setupListNewArrivals() {
        Product product1 = new Product();
        product1.setProductName("Ulos Mangiring");
        product1.setProductCode("1");
        product1.setProductSize("Free Size");
        product1.setProductPrice(1350000.0);
        product1.setProductQty(2);
        newArrivalsProductList.add(product1);

        Product product2 = new Product();
        product2.setProductName("Ulos Mangiring");
        product2.setProductCode("1");
        product2.setProductSize("Free Size");
        product2.setProductPrice(1350000.0);
        product2.setProductQty(2);
        newArrivalsProductList.add(product2);

        Product product3 = new Product();
        product3.setProductName("Ulos Mangiring");
        product3.setProductCode("1");
        product3.setProductSize("Free Size");
        product3.setProductPrice(1350000.0);
        product3.setProductQty(2);
        newArrivalsProductList.add(product3);

        Product product4 = new Product();
        product4.setProductName("Ulos Mangiring");
        product4.setProductCode("1");
        product4.setProductSize("Free Size");
        product4.setProductPrice(1350000.0);
        product4.setProductQty(2);
        newArrivalsProductList.add(product4);
    }

    private void setupCategoryList(){
        Category category1 = new Category();
        category1.setCategoryId("1");
        category1.setCategoryName("category");
        categoryList.add(category1);

        Category category2 = new Category();
        category2.setCategoryId("1");
        category2.setCategoryName("category");
        categoryList.add(category2);

        Category category3 = new Category();
        category3.setCategoryId("1");
        category3.setCategoryName("category");
        categoryList.add(category3);

        Category category4 = new Category();
        category4.setCategoryId("1");
        category4.setCategoryName("category");
        categoryList.add(category4);
    }

    public List<Product> getNewArrivalsProductList() {
        return newArrivalsProductList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }
}
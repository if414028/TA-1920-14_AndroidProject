package com.ditenun.appditenun.function.activity.commerce.dashboard.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ditenun.appditenun.dependency.models.Category;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.modules.WooCommerceApiClient;
import com.ditenun.appditenun.dependency.network.WooCommerceApiInterface;
import com.ditenun.appditenun.function.util.SingleLiveEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductHomeViewModel extends AndroidViewModel {

    private SingleLiveEvent<Void> successGetListProductEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<DatabaseError> errorGetListProductEvent = new SingleLiveEvent<>();
    private List<Product> newArrivalsProductList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    private SingleLiveEvent<Void> successGetListCategories = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> errorGetListCategories = new SingleLiveEvent<>();

    public void fetchAllProduct() {
        WooCommerceApiInterface apiInterface = WooCommerceApiClient.createService(WooCommerceApiInterface.class, WooCommerceApiClient.CONSUMER_KEY, WooCommerceApiClient.CONSUMER_SECRET);
        Call<List<Product>> call = apiInterface.getListProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null && !response.body().isEmpty()) {
                    newArrivalsProductList = response.body();
                    successGetListProductEvent.callFromBackgroundThread();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                errorGetListProductEvent.callFromBackgroundThread();
            }
        });
    }

    public ProductHomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void clearProductList() {
        this.newArrivalsProductList.clear();
    }

    public void fetchListCategories() {
        WooCommerceApiInterface apiInterface = WooCommerceApiClient.createService(WooCommerceApiInterface.class, WooCommerceApiClient.CONSUMER_KEY, WooCommerceApiClient.CONSUMER_SECRET);
        Call<List<Category>> call = apiInterface.getListCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.body() != null && !response.body().isEmpty()) {
                    categoryList = response.body();
                    successGetListCategories.callFromBackgroundThread();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                errorGetListCategories.callFromBackgroundThread();
            }
        });
    }

    public List<Product> getNewArrivalsProductList() {
        return newArrivalsProductList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public SingleLiveEvent<Void> getSuccessGetListProductEvent() {
        return successGetListProductEvent;
    }

    public SingleLiveEvent<DatabaseError> getErrorGetListProductEvent() {
        return errorGetListProductEvent;
    }

    public SingleLiveEvent<Void> getSuccessGetListCategories() {
        return successGetListCategories;
    }

    public SingleLiveEvent<Void> getErrorGetListCategories() {
        return errorGetListCategories;
    }
}
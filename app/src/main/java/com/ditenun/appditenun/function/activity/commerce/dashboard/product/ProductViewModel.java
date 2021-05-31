package com.ditenun.appditenun.function.activity.commerce.dashboard.product;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

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

public class ProductViewModel extends ViewModel {

    private SingleLiveEvent<Void> successGetListProductEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<DatabaseError> errorGetListProductEvent = new SingleLiveEvent<>();
    private List<Product> productList = new ArrayList<>();

    public void fetchListProduct() {
        WooCommerceApiInterface apiInterface = WooCommerceApiClient.createService(WooCommerceApiInterface.class, WooCommerceApiClient.CONSUMER_KEY, WooCommerceApiClient.CONSUMER_SECRET);
        Call<List<Product>> call = apiInterface.getListProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null && !response.body().isEmpty()) {
                    productList = response.body();
                    successGetListProductEvent.callFromBackgroundThread();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                errorGetListProductEvent.callFromBackgroundThread();
            }
        });
    }

    public void clearProductList() {
        this.productList.clear();
    }

    public SingleLiveEvent<Void> getSuccessGetListProductEvent() {
        return successGetListProductEvent;
    }

    public SingleLiveEvent<DatabaseError> getErrorGetListProductEvent() {
        return errorGetListProductEvent;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
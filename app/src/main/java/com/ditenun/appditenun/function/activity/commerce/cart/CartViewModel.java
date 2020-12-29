package com.ditenun.appditenun.function.activity.commerce.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.network.TenunNetworkInterface;
import com.ditenun.appditenun.function.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CartViewModel extends AndroidViewModel {

    @Inject
    TenunNetworkInterface tenunNetworkInterface;

    private Order order;
    private SingleLiveEvent<Void> successGetOrderEvent = new SingleLiveEvent<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
    }

    public Double calculateTotalPrice() {
        Double totalPrice = 0.0;
        for (Product item : order.getProduct()) {
            totalPrice += (item.getPrice() * item.getQty());
        }
        return totalPrice;
    }

    public MutableLiveData<Void> getSuccessGetOrderEvent() {
        return successGetOrderEvent;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        successGetOrderEvent.callFromBackgroundThread();
    }
}

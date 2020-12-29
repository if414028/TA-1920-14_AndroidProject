package com.ditenun.appditenun.function.activity.commerce.payment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.function.util.SingleLiveEvent;

public class ReviewPaymentViewModel extends AndroidViewModel {

    private Order order;
    private SingleLiveEvent<Void> successGetOrderEvent = new SingleLiveEvent<>();

    public ReviewPaymentViewModel(@NonNull Application application) {
        super(application);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        successGetOrderEvent.callFromBackgroundThread();
    }

    public SingleLiveEvent<Void> getSuccessGetOrderEvent() {
        return successGetOrderEvent;
    }
}

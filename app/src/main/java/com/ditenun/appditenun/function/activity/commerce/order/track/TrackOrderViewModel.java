package com.ditenun.appditenun.function.activity.commerce.order.track;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.function.util.SingleLiveEvent;

public class TrackOrderViewModel extends AndroidViewModel {

    private Order order;
    private SingleLiveEvent<Void> successGetDetailOrder = new SingleLiveEvent<>();

    public TrackOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        successGetDetailOrder.callFromBackgroundThread();
    }

    public SingleLiveEvent<Void> getSuccessGetDetailOrder() {
        return successGetDetailOrder;
    }
}

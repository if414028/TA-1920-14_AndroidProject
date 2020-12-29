package com.ditenun.appditenun.function.activity.commerce.order.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.function.util.SingleLiveEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListOrderViewModel extends AndroidViewModel {

    private FirebaseDatabase rootDatabase;
    private DatabaseReference orderReference;

    private List<Order> orderList = new ArrayList<>();

    private SingleLiveEvent<Void> successGetOrderList = new SingleLiveEvent<>();
    private SingleLiveEvent<DatabaseError> errorGetOrderList = new SingleLiveEvent<>();

    public ListOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchListOrder() {
        rootDatabase = FirebaseDatabase.getInstance();
        orderReference = rootDatabase.getReference("orders");
        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Order order = child.getValue(Order.class);
                        orderList.add(order);
                    }
                    successGetOrderList.callFromBackgroundThread();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorGetOrderList.postValue(databaseError);
            }
        });
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public SingleLiveEvent<Void> getSuccessGetOrderList() {
        return successGetOrderList;
    }

    public SingleLiveEvent<DatabaseError> getErrorGetOrderList() {
        return errorGetOrderList;
    }
}

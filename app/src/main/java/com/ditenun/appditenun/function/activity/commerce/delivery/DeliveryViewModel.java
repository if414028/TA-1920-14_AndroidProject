package com.ditenun.appditenun.function.activity.commerce.delivery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.models.Shipping;
import com.ditenun.appditenun.function.util.SingleLiveEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryViewModel extends AndroidViewModel {

    private Order order;
    private LatLng currentPosition;

    private FirebaseDatabase rootDatabase;
    private DatabaseReference shippingMethodReference;

    private SingleLiveEvent<String> submitAddressEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Void> successGetListShippingMethod = new SingleLiveEvent<>();
    private SingleLiveEvent<DatabaseError> errorGetListShippingMethod = new SingleLiveEvent<>();
    private List<Shipping> listShippingMethod = new ArrayList<>();

    public DeliveryViewModel(@NonNull Application application) {
        super(application);
    }

    public void submitAddress(String value) {
        this.order.setAddress(value);
        this.submitAddressEvent.postValue(order.getAddress());
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void getAllShippingMethod() {
        rootDatabase = FirebaseDatabase.getInstance();
        shippingMethodReference = rootDatabase.getReference("shipping");
        shippingMethodReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Shipping shipping = child.getValue(Shipping.class);
                        listShippingMethod.add(shipping);
                    }
                    successGetListShippingMethod.callFromBackgroundThread();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorGetListShippingMethod.postValue(databaseError);
            }
        });
    }

    public void selectShippingMethod(Shipping selectedValue){
        this.order.setShipping(selectedValue);
    }

    public double calculateTotalOrderPrice() {
        double totalPrice = 0.0;
        if (order.getProduct() != null && !order.getProduct().isEmpty()) {
            for (Product item : order.getProduct()) {
                totalPrice += (item.getPriceInDouble() * item.getPurchasedStock());
            }
        }
        return totalPrice;
    }

    public double calculateNettTotalPrice() {
        double nettTotalPrice = 0.0;
        double shippingPrice = 0.0;
        if (order.getShipping() != null) shippingPrice = order.getShipping().getShipPrice();
        nettTotalPrice = (calculateTotalOrderPrice() + shippingPrice);
        return nettTotalPrice;
    }

    public SingleLiveEvent<String> getSubmitAddressEvent() {
        return submitAddressEvent;
    }

    public void setCurrentPosition(LatLng currentPosition) {
        this.currentPosition = currentPosition;
    }

    public LatLng getCurrentPosition() {
        return currentPosition;
    }

    public SingleLiveEvent<Void> getSuccessGetListShippingMethod() {
        return successGetListShippingMethod;
    }

    public SingleLiveEvent<DatabaseError> getErrorGetListShippingMethod() {
        return errorGetListShippingMethod;
    }

    public List<Shipping> getListShippingMethod() {
        return listShippingMethod;
    }
}

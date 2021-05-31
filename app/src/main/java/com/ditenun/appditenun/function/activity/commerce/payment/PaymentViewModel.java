package com.ditenun.appditenun.function.activity.commerce.payment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.PaymentMethod;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.util.SingleLiveEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PaymentViewModel extends AndroidViewModel {

    private FirebaseDatabase rootDatabase;
    private DatabaseReference paymentMethodReference;
    private DatabaseReference submitOrderReference;

    private Order order;
    private List<PaymentMethod> paymentMethodList = new ArrayList<>();
    private SingleLiveEvent<Void> successGetOrderEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> successGetPaymentMethodList = new SingleLiveEvent<>();
    private SingleLiveEvent<DatabaseError> errorGetPaymentMethodList = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> submitOrderEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> successSubmitOrder = new SingleLiveEvent<>();

    public PaymentViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchPaymentMethodList() {
        rootDatabase = FirebaseDatabase.getInstance();
        paymentMethodReference = rootDatabase.getReference("payments");
        paymentMethodReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        PaymentMethod paymentMethod = child.getValue(PaymentMethod.class);
                        paymentMethodList.add(paymentMethod);
                    }
                    successGetPaymentMethodList.callFromBackgroundThread();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorGetPaymentMethodList.postValue(databaseError);
            }
        });
    }

    public void submitOrder() {
        prepareOrder();
        rootDatabase = FirebaseDatabase.getInstance();
        submitOrderReference = rootDatabase.getReference();
        DatabaseReference newSubmitOrderReference = submitOrderReference.child("orders").push();
        newSubmitOrderReference.setValue(order);
        successSubmitOrder.callFromBackgroundThread();
    }

    private void prepareOrder() {
        order.setOrderDate(getCurrentTime());
        order.setOrderNo("ORDER" + order.getOrderDate());
        order.setOrderStatusCode("WFP");
        order.setOrderStatus("Menunggu Pembayaran");
        order.setPaymentStatus("Menunggu Pembayaran");
        order.setPaymentStatusCode("WFP");
        order.setShippingStatus(null);
    }

    private String getCurrentTime() {
        String currentTimeString = "";
        Date currentDate = Calendar.getInstance().getTime();
        currentTimeString = String.valueOf(currentDate.getTime());
        return currentTimeString;
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

    public void setPaymentMethod(PaymentMethod selectedValue) {
        this.order.setPaymentMethod(selectedValue);
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

    public SingleLiveEvent<Void> getSuccessGetPaymentMethodList() {
        return successGetPaymentMethodList;
    }

    public SingleLiveEvent<DatabaseError> getErrorGetPaymentMethodList() {
        return errorGetPaymentMethodList;
    }

    public List<PaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public SingleLiveEvent<Void> getSuccessSubmitOrder() {
        return successSubmitOrder;
    }
}

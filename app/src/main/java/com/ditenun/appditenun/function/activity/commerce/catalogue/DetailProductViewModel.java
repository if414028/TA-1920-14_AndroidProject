package com.ditenun.appditenun.function.activity.commerce.catalogue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.util.SingleLiveEvent;

import rx.Single;

public class DetailProductViewModel extends AndroidViewModel {

    private SingleLiveEvent<Void> successGetDetailProduct = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> increasePurchaseQtyEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> decreasePurchaseQtyEvent = new SingleLiveEvent<>();
    private Product product;
    private Order order;

    public DetailProductViewModel(@NonNull Application application) {
        super(application);
    }

    public void increaseProductQty() {
        if (product != null && product.getQty() != null) {
            int currentQty = product.getQty();
            int resultQty = currentQty + 1;
            product.setQty(resultQty);
            increasePurchaseQtyEvent.postValue(product.getQty());
        }
    }

    public void decreaseProductQty() {
        if (product != null && product.getQty() != null) {
            int currentQty = product.getQty();
            int resultQty;
            if (currentQty > 1) resultQty = currentQty - 1;
            else resultQty = 1;
            product.setQty(resultQty);
            decreasePurchaseQtyEvent.postValue(product.getQty());
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        successGetDetailProduct.callFromBackgroundThread();
    }

    public SingleLiveEvent<Void> getSuccessGetDetailProduct() {
        return successGetDetailProduct;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public SingleLiveEvent<Integer> getIncreasePurchaseQtyEvent() {
        return increasePurchaseQtyEvent;
    }

    public SingleLiveEvent<Integer> getDecreasePurchaseQtyEvent() {
        return decreasePurchaseQtyEvent;
    }
}

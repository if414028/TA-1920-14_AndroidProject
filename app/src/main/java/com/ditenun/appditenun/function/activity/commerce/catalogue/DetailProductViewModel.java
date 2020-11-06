package com.ditenun.appditenun.function.activity.commerce.catalogue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Product;

public class DetailProductViewModel extends AndroidViewModel {

    private MutableLiveData<Product> productLiveData = new MutableLiveData<>();

    public DetailProductViewModel(@NonNull Application application) {
        super(application);
    }

    public void setProduct(Product item) {
        productLiveData.postValue(item);
    }

    public void increaseProductQty() {
        Product product = productLiveData.getValue();
        if (product != null && product.getProductQty() != null) {
            int currentQty = product.getProductQty();
            int resultQty = currentQty + 1;
            product.setProductQty(resultQty);
            productLiveData.postValue(product);
        }
    }

    public void decreaseProductQty() {
        Product product = productLiveData.getValue();
        if (product != null && product.getProductQty() != null) {
            int currentQty = product.getProductQty();
            int resultQty;
            if (currentQty > 1) resultQty = currentQty - 1;
            else resultQty = 1;
            product.setProductQty(resultQty);
            productLiveData.postValue(product);
        }
    }

    public MutableLiveData<Product> getProduct() {
        return productLiveData;
    }
}

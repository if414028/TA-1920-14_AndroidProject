package com.ditenun.appditenun.function.activity.commerce.catalogue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.models.ProductColor;
import com.ditenun.appditenun.function.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import rx.Single;

public class DetailProductViewModel extends AndroidViewModel {

    private SingleLiveEvent<Void> successGetDetailProduct = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> increasePurchaseQtyEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> decreasePurchaseQtyEvent = new SingleLiveEvent<>();
    private Product product;
    private Order order;
    private List<ProductColor> productColorList = new ArrayList<>();

    public DetailProductViewModel(@NonNull Application application) {
        super(application);
        setupProductColor();
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

    private void setupProductColor() {
        ProductColor colorRed = new ProductColor();
        colorRed.setSelected(true);
        colorRed.setColorId(1);
        colorRed.setColorCode("RED");
        colorRed.setColorName("RED");
        productColorList.add(colorRed);

        ProductColor colorGreen = new ProductColor();
        colorGreen.setSelected(false);
        colorGreen.setColorId(2);
        colorGreen.setColorCode("GREEN");
        colorGreen.setColorName("GREEN");
        productColorList.add(colorGreen);

        ProductColor colorBlue = new ProductColor();
        colorBlue.setSelected(false);
        colorBlue.setColorId(3);
        colorBlue.setColorCode("BLUE");
        colorBlue.setColorName("BLUE");
        productColorList.add(colorBlue);
    }

    public void setSelectedColor(String colorCode) {
        for (ProductColor colorStatus : productColorList) {
            if (colorStatus.getColorCode().equalsIgnoreCase(colorCode)) {
                colorStatus.setSelected(true);
            } else {
                colorStatus.setSelected(false);
            }
        }
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

    public List<ProductColor> getProductColorList() {
        return productColorList;
    }
}

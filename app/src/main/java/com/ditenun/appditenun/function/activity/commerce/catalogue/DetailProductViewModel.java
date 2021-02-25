package com.ditenun.appditenun.function.activity.commerce.catalogue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.models.ProductAttributes;
import com.ditenun.appditenun.dependency.models.ProductColor;
import com.ditenun.appditenun.dependency.modules.WooCommerceApiClient;
import com.ditenun.appditenun.dependency.network.WooCommerceApiInterface;
import com.ditenun.appditenun.function.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Single;

public class DetailProductViewModel extends AndroidViewModel {

    private SingleLiveEvent<Void> successGetDetailProduct = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> errorGetDetailProduct = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> increasePurchaseQtyEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> decreasePurchaseQtyEvent = new SingleLiveEvent<>();
    private Product product;
    private Order order;
    private List<String> productColorList = new ArrayList<>();
    private String selectedColor;
    private List<String> productSizeList = new ArrayList<>();
    private String selectedSize;

    public DetailProductViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchDetailProduct(Integer id) {
        if (id != null) {
            WooCommerceApiInterface apiInterface = WooCommerceApiClient.createService(WooCommerceApiInterface.class, WooCommerceApiClient.CONSUMER_KEY, WooCommerceApiClient.CONSUMER_SECRET);
            Call<Product> call = apiInterface.getDetailProduct(id);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    product = response.body();
                    setProductSizeList(product);
                    setProductColorList(product);
                    successGetDetailProduct.callFromBackgroundThread();
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    errorGetDetailProduct.callFromBackgroundThread();
                }
            });
        }
    }

    public void increaseProductQty() {
        if (product != null) {
            int currentQty = product.getPurchasedStock();
            int resultQty = currentQty + 1;
            product.setPurchasedStock(resultQty);
            increasePurchaseQtyEvent.postValue(product.getPurchasedStock());
        }
    }

    public void decreaseProductQty() {
        if (product != null) {
            int currentQty = product.getPurchasedStock();
            int resultQty;
            if (currentQty > 1) resultQty = currentQty - 1;
            else resultQty = 1;
            product.setPurchasedStock(resultQty);
            decreasePurchaseQtyEvent.postValue(product.getPurchasedStock());
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        successGetDetailProduct.callFromBackgroundThread();
    }

    public void setSelectedColor(String color) {
        selectedColor = color;
    }

    public void setSelectedSize(String size){
        selectedSize = size;
    }

    private void setProductSizeList(Product product) {
        if (product.getAttributes() != null) {
            for (ProductAttributes attributes : product.getAttributes()) {
                if (attributes.getName().equalsIgnoreCase("Ukuran")) {
                    productSizeList = attributes.getOptions();
                }
            }
        }
    }

    private void setProductColorList(Product product) {
        if (product.getAttributes() != null) {
            for (ProductAttributes attributes : product.getAttributes()) {
                if (attributes.getName().equalsIgnoreCase("Warna")) {
                    if (attributes.getOptions() != null && !attributes.getOptions().isEmpty()) {
                        productColorList = attributes.getOptions();
                        selectedColor = attributes.getOptions().get(0);
                    }
                }
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

    public SingleLiveEvent<Void> getErrorGetDetailProduct() {
        return errorGetDetailProduct;
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public List<String> getProductColorList() {
        return productColorList;
    }

    public List<String> getProductSizeList() {
        return productSizeList;
    }
}

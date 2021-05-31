package com.ditenun.appditenun.dependency.network;

import com.ditenun.appditenun.dependency.models.Category;
import com.ditenun.appditenun.dependency.models.Order;
import com.ditenun.appditenun.dependency.models.PaymentMethod;
import com.ditenun.appditenun.dependency.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WooCommerceApiInterface {

    @GET("wp-json/wc/v3/products/categories")
    Call<List<Category>> getListCategories();

    @GET("wp-json/wc/v3/products")
    Call<List<Product>> getListProducts();

    @GET("wp-json/wc/v3/products/{id}")
    Call<Product> getDetailProduct(@Path("id") Integer id);

    @GET("wp-json/wc/v3/payment_gateways")
    Call<List<PaymentMethod>> getListPaymentGetaways();

    @GET("wp-json/wc/v3/orders")
    Call<List<Order>> getListOrders();

    @GET("wp-json/wc/v3/orders")
    Call<Order> getDetailOrder(@Query("id") String id);
}

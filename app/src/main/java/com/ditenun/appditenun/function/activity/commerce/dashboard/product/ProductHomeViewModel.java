package com.ditenun.appditenun.function.activity.commerce.dashboard.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ditenun.appditenun.dependency.models.Category;
import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.dependency.network.TenunNetworkInterface;
import com.ditenun.appditenun.function.util.SingleLiveEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ProductHomeViewModel extends AndroidViewModel {

    private FirebaseDatabase rootDatabase;
    private DatabaseReference reference;

    @Inject
    TenunNetworkInterface tenunNetworkInterface;

    private SingleLiveEvent<Void> successGetListProductEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<DatabaseError> errorGetListProductEvent = new SingleLiveEvent<>();
    private List<Product> newArrivalsProductList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    public void fetchAllProduct() {
        rootDatabase = FirebaseDatabase.getInstance();
        reference = rootDatabase.getReference("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Product product = child.getValue(Product.class);
                        List<String> imageUrls = new ArrayList<>();
                        if (product != null) {
                            for (String imageUrl : product.getImageUrls()) {
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                }
                            }
                            product.setImageUrls(imageUrls);
                            product.setQty(1);
                            newArrivalsProductList.add(product);
                        }
                    }
                }
                successGetListProductEvent.callFromBackgroundThread();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorGetListProductEvent.postValue(databaseError);
            }
        });
    }

    public ProductHomeViewModel(@NonNull Application application) {
        super(application);
        setupCategoryList();
    }

    public void clearProductList(){
        this.newArrivalsProductList.clear();
    }

    private void setupCategoryList() {
        Category category1 = new Category();
        category1.setCategoryId("1");
        category1.setCategoryName("category");
        category1.setCategoryImage("https://picsum.photos/id/271/200/300");
        categoryList.add(category1);

        Category category2 = new Category();
        category2.setCategoryId("1");
        category2.setCategoryName("category");
        category2.setCategoryImage("https://picsum.photos/id/272/200/300");
        categoryList.add(category2);

        Category category3 = new Category();
        category3.setCategoryId("1");
        category3.setCategoryName("category");
        category3.setCategoryImage("https://picsum.photos/id/273/200/300");
        categoryList.add(category3);

        Category category4 = new Category();
        category4.setCategoryId("1");
        category4.setCategoryName("category");
        category4.setCategoryImage("https://picsum.photos/id/274/200/300");
        categoryList.add(category4);
    }

    public List<Product> getNewArrivalsProductList() {
        return newArrivalsProductList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public SingleLiveEvent<Void> getSuccessGetListProductEvent() {
        return successGetListProductEvent;
    }

    public SingleLiveEvent<DatabaseError> getErrorGetListProductEvent() {
        return errorGetListProductEvent;
    }
}
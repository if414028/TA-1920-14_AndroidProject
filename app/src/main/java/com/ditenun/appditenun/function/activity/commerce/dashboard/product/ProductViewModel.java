package com.ditenun.appditenun.function.activity.commerce.dashboard.product;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.ditenun.appditenun.dependency.models.Product;
import com.ditenun.appditenun.function.util.SingleLiveEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {

    private FirebaseDatabase rootDatabase;
    private DatabaseReference reference;

    private SingleLiveEvent<Void> successGetListProductEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<DatabaseError> errorGetListProductEvent = new SingleLiveEvent<>();
    private List<Product> productList = new ArrayList<>();

    public void fetchListProduct() {
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
                            productList.add(product);
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

    public void clearProductList() {
        this.productList.clear();
    }

    public SingleLiveEvent<Void> getSuccessGetListProductEvent() {
        return successGetListProductEvent;
    }

    public SingleLiveEvent<DatabaseError> getErrorGetListProductEvent() {
        return errorGetListProductEvent;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
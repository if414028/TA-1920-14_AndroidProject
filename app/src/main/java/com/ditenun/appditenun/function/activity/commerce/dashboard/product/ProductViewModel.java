package com.ditenun.appditenun.function.activity.commerce.dashboard.product;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductViewModel extends ViewModel {

    private FirebaseDatabase rootDatabase;
    private DatabaseReference reference;

    public void fetchListProduct() {
        rootDatabase = FirebaseDatabase.getInstance();
        reference = rootDatabase.getReference("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
package com.ditenun.appditenun.function.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ditenun.appditenun.R;

import javax.inject.Inject;

import io.realm.Realm;

public class DashboardActivity extends AppCompatActivity {

    @Inject
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }
}
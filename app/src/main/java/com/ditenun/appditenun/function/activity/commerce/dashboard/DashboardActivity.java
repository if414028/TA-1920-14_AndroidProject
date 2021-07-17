package com.ditenun.appditenun.function.activity.commerce.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDashboardBinding;
import com.ditenun.appditenun.function.activity.commerce.cart.CartActivity;
import com.ditenun.appditenun.function.activity.commerce.dashboard.account.AccountFragment;
import com.ditenun.appditenun.function.activity.commerce.dashboard.product.ProductFragment;
import com.ditenun.appditenun.function.activity.commerce.dashboard.product.ProductHomeFragment;
import com.ditenun.appditenun.function.activity.commerce.order.list.ListOrderActivity;

import javax.inject.Inject;

import io.realm.Realm;

public class DashboardActivity extends AppCompatActivity {

    @Inject
    Realm realm;

    private ActivityDashboardBinding binding;

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    public static final String ARG_SUCCESS_CREATE_ORDER = "success_create_order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        initLayout();
        getAdditionalData();
    }

    private void initLayout() {
        loadFragment(ProductHomeFragment.newInstance());
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.bottom_nav_home: {
                    fragment = ProductHomeFragment.newInstance();
                    break;
                }
                case R.id.bottom_nav_product: {
                    fragment = ProductFragment.newInstance();
                    break;
                }
//                case R.id.bottom_nav_cart: {
//                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
//                    startActivity(intent);
//                    break;
//                }
//                case R.id.bottom_nav_account: {
//                    fragment = AccountFragment.newInstance();
//                    break;
//                }
            }
            return loadFragment(fragment);
        });
        requestPermission();
    }

    private Boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ly_content, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void requestPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getAdditionalData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(ARG_SUCCESS_CREATE_ORDER)) {
                if (intent.getBooleanExtra(ARG_SUCCESS_CREATE_ORDER, false)) {
                    Intent intentListOrder = new Intent(getApplicationContext(), ListOrderActivity.class);
                    startActivity(intentListOrder);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            }
        }
    }
}
package com.ditenun.appditenun.function.activity.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.ActivityDashboardBinding;
import com.ditenun.appditenun.function.activity.dashboard.account.AccountFragment;
import com.ditenun.appditenun.function.activity.dashboard.home.DashboardFragment;
import com.ditenun.appditenun.function.activity.dashboard.product.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import io.realm.Realm;

public class DashboardActivity extends AppCompatActivity {

    @Inject
    Realm realm;

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        initLayout();
    }

    private void initLayout() {
        loadFragment(DashboardFragment.newInstance());
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.bottom_nav_home: {
                        fragment = DashboardFragment.newInstance();
                        break;
                    }
                    case R.id.bottom_nav_product: {
                        fragment = ProductFragment.newInstance();
                        break;
                    }
                    case R.id.bottom_nav_cart:{
                        Toast.makeText(DashboardActivity.this, "open cart", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.bottom_nav_account:{
                        fragment = AccountFragment.newInstance();
                        break;
                    }
                }
                return loadFragment(fragment);
            }
        });
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
}
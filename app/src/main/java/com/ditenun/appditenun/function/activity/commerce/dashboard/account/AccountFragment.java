package com.ditenun.appditenun.function.activity.commerce.dashboard.account;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.AcoountFragmentBinding;
import com.ditenun.appditenun.function.activity.LoginActivity;
import com.ditenun.appditenun.function.activity.commerce.order.list.ListOrderActivity;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private AcoountFragmentBinding binding;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.acoount_fragment, container, false);

        initLayout();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

    }

    private void initLayout() {
        binding.btnListTransaction.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ListOrderActivity.class);
            startActivity(intent);
        });
    }

}
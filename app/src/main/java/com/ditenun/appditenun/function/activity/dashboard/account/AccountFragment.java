package com.ditenun.appditenun.function.activity.dashboard.account;

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

    private void initLayout(){
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE).edit().putBoolean("logged", false).apply();
                startLoginActivity();
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);

        getActivity().finish();
    }

}
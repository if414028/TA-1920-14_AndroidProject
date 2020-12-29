package com.ditenun.appditenun.function.activity.commerce.delivery;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.databinding.FragmentAddAddressBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddAddressFragment extends DialogFragment implements OnMapReadyCallback {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;

    private FragmentAddAddressBinding binding;
    private DeliveryViewModel viewModel;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Address address;
    private LocationManager locationManager;

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.filterDialogTheme);
        viewModel = ViewModelProviders.of(getActivity()).get(DeliveryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false);

        initLayout();
        initMap();
        return binding.getRoot();
    }

    private void initLayout() {
        binding.btnBack.setOnClickListener(view -> dismiss());
        binding.etAddress.setText(viewModel.getOrder().getAddress());
        binding.btnSubmit.setOnClickListener(view -> {
            String address = binding.etAddress.getText().toString();
            viewModel.submitAddress(address);
            dismiss();
        });
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_layout);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        BitmapDescriptor icon = getMarkerIconFromDrawable(getActivity().getResources().getDrawable(R.drawable.ic_baseline_location_on_24));

        if (viewModel.getCurrentPosition() == null){
            Marker myMarker = mMap.addMarker(new MarkerOptions().position(getCurrentLocation()).draggable(false).icon(icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getCurrentLocation(), 18f));
            viewModel.setCurrentPosition(myMarker.getPosition());
        } else {
            mMap.addMarker(new MarkerOptions().position(viewModel.getCurrentPosition()).draggable(false).icon(icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewModel.getCurrentPosition(), 18f));
        }

        try {
            address = geocoder.getFromLocation(viewModel.getCurrentPosition().latitude, viewModel.getCurrentPosition().longitude, 1).get(0);
            binding.etAddress.setText(address.getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.setOnCameraMoveListener(() -> {
            mMap.clear();
            binding.marker.setVisibility(View.VISIBLE);
        });

        mMap.setOnCameraIdleListener(() -> {
            binding.marker.setVisibility(View.GONE);
            MarkerOptions markerOptions = new MarkerOptions().position(mMap.getCameraPosition().target).icon(icon);
            Marker myMarker = mMap.addMarker(markerOptions);
            viewModel.setCurrentPosition(myMarker.getPosition());

            try {
                address = geocoder.getFromLocation(viewModel.getCurrentPosition().latitude, viewModel.getCurrentPosition().longitude, 1).get(0);
                binding.etAddress.setText(address.getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private LatLng getCurrentLocation() {
        double lat = 0;
        double lng = 0;
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            lat = getLastKnownLocation().getLatitude();
            lng = getLastKnownLocation().getLongitude();
        }
        return new LatLng(lat, lng);
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map_layout));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}

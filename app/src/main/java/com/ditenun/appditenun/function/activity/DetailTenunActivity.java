package com.ditenun.appditenun.function.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ditenun.appditenun.R;
//import com.ditenun.appditenun.R2;
import com.ditenun.appditenun.dependency.App;
import com.ditenun.appditenun.dependency.models.MotifTenun;
import com.ditenun.appditenun.dependency.models.Tenun;
import com.ditenun.appditenun.dependency.modules.APIModule;
import com.ditenun.appditenun.dependency.network.TenunNetworkInterface;
import com.ditenun.appditenun.function.adapter.RecyclerViewAdapterMotif;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.Sort;
import timber.log.Timber;

public class DetailTenunActivity extends AppCompatActivity {
    public static final String KEY_ID_TENUN = "id_key";

    @Inject
    Realm realm;
    @BindView(R.id.thumbTenun)
    ImageView thumbTenun;
    @BindView(R.id.descTenun)
    TextView textDescTenun;
    @BindView(R.id.historyTenun)
    TextView textHistoryTenun;
    @BindView(R.id.functionTenun)
    TextView textFunctionTenun;
    @BindView(R.id.originTenun)
    TextView textOriginTenun;
    @BindView(R.id.titleTenun)
    TextView textTitleTenun;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    List<MotifTenun> listMotif = Collections.EMPTY_LIST;
    //    MaterialDialog dialog2;
    private Tenun tenun;

    @Inject
    TenunNetworkInterface tenunNetworkInterface;

    public static Intent createIntent(Context context, String id) {
        Intent intent = new Intent(context, DetailTenunActivity.class);
        intent.putExtra(KEY_ID_TENUN, id);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tenun);

        App.get(getApplicationContext()).getInjector().inject(this);
        ButterKnife.bind(this);
        listMotif = new ArrayList<MotifTenun>();

        btnBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        Intent returnIntent = getIntent();
        String idTenun = returnIntent.getStringExtra(KEY_ID_TENUN);

        listMotif = realm.where(MotifTenun.class).equalTo("idTenun", idTenun).findAll().sort("id", Sort.ASCENDING);
        Timber.i("Jumlah Motif: ", listMotif.size());
        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerviewulos_id);
        RecyclerViewAdapterMotif myAdapter = new RecyclerViewAdapterMotif(this, listMotif);
        myrv.setLayoutManager(new GridLayoutManager(this, 3));
        myrv.setAdapter(myAdapter);

        tenun = realm.where(Tenun.class)
                .equalTo("id", idTenun)
                .findFirst();
        String imgUrl = APIModule.ENDPOINT + tenun.getImageSrc();
        Picasso.with(getApplicationContext()).load(imgUrl).into(thumbTenun);
        setContent();
    }

    private void setContent() {
        textHistoryTenun.setText(tenun.getSejarahTenun());
        textDescTenun.setText(tenun.getDeskripsiTenun());
        textTitleTenun.setText(tenun.getNamaTenun());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

package com.ditenun.appditenun.function.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import com.ditenun.appditenun.function.adapter.CustomListAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.dependency.App;
import com.ditenun.appditenun.dependency.models.StockImage;
import com.ditenun.appditenun.function.adapter.ViewPagerAdapter;
import com.ditenun.appditenun.function.fragment.MyMotifFragment;
import com.ditenun.appditenun.function.fragment.NationalMotifFragment;
import com.ditenun.appditenun.function.util.BitmapUtils;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class HomeActivity extends AppCompatActivity  {

    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_PICK_GALERY = 101;
    private static final int VERIFICATION_IMAGE = 102;
    private static final int TIME_INTERVAL = 2000;
    private int STORAGE_PERMISSION_CODE = 1;

    @Inject
    Realm realm;

    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;

    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;

//    @BindView(R.id.linearLayoutCamera)
//    LinearLayout linearLayoutCamera;

    @BindView(R.id.linearLayoutTambahFotooruploadimage)
    LinearLayout linearLayoutTambahFoto;

//    @BindView(R.id.linearLayoutDE)
//    LinearLayout linearLayoutDE;

    @BindView(R.id.linearLayoutKK)
    LinearLayout linearLayoutDK;

    @BindView(R.id.linearLayoutKG)
    LinearLayout linearLayoutKG;



    private long backButtonPressTime;

    private DrawerLayout dl;
    private NavigationView nv;
    private ActionBarDrawerToggle t;


    Button logoutBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dl = findViewById(R.id.container);
        nv = findViewById(R.id.navigation_view);
        logoutBtn = findViewById(R.id.logoutBtn);

        //Save image from Verification of Image Qualiaty
        try {
            Bundle bundle = getIntent().getExtras();
            String imagePath = bundle.getString("pathImage");
            System.out.println("Masuk sini "+imagePath);
//
            Intent data = new Intent();
//            //---set the data to pass back---
            data.putExtra("pathImage", imagePath);
            System.out.println("masuk sini 2");
            setResult(RESULT_OK, data);
////            //---close the activity---
////            finish();
            startActivityForResult(data,VERIFICATION_IMAGE);
//
////            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
////            saveStockImage(myBitmap);
        } catch (Exception e) {
//           showErrorMessage(e.getMessage());
        }

        t = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.close){

            @Override
            public void onDrawerOpened (View drawerView){
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }

        };

        dl.setDrawerListener(t);
        dl.post(new Runnable() {
            @Override
            public void run() {
                t.syncState();
            }
        });



//        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        View hView = nv.getHeaderView(0);


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
//                    case R.id.account:
//                        Toast.makeText(HomeActivity.this, "My Account",Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.settings:
//                        Toast.makeText(HomeActivity.this, "Settings",Toast.LENGTH_SHORT).show();
//                        break;
                    case R.id.feedback:
                        startFeedbackActivity();
                        break;
                    case R.id.faq:
                        startFaqActivity();
                        break;
                    case R.id.privacy:
                        Uri uri = Uri.parse( "https://sites.google.com/view/ditenun-privacy/home" );
                        startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("logged", false).apply();
                startLoginActivity();
            }
        });

        App.get(this).getInjector().inject(this);

        ButterKnife.bind(this);

        setupTab();

        registerListener();

        //Request permission
        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            requestForStoragePermission();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        t.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        t.onConfigurationChanged(newConfig);
    }

    //Permission
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this ).setTitle("Permission").setMessage("Need to read your storage")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(HomeActivity.this,new String[] {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },STORAGE_PERMISSION_CODE);
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },STORAGE_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission allowed",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void requestForStoragePermission() {
        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

                new AlertDialog.Builder(this ).setTitle("Permission").setMessage("Need to read your storage")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(HomeActivity.this,new String[] {
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },STORAGE_PERMISSION_CODE);
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this,new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },STORAGE_PERMISSION_CODE);
            }
        }

    }

    public static Intent createIntent(Context context) {
        return new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupTab() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(new NationalMotifFragment(), getResources().getString(R.string.national_motif));
        adapter.add(new MyMotifFragment(), getResources().getString(R.string.my_motif));

        mainViewPager.setAdapter(adapter);

        mainTabLayout.setupWithViewPager(mainViewPager);
    }

    private void registerListener() {
//        linearLayoutCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startCameraActivity();
//            }
//        });

        linearLayoutTambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startImageChooserActivity();
                Intent intent = new Intent(HomeActivity.this,VerifikasiuloasActivity.class);
                startActivity(intent);
            }
        });
//        linearLayoutDE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                builder.setTitle("Pilih jenis template");
//
//                LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View row = inflater.inflate(R.layout.row_item_template,null);
//                final ListView li = (ListView)row.findViewById(R.id.list_row_item);
//                li.setTextFilterEnabled(true);
//                li.setAdapter(new CustomListAdapter(HomeActivity.this));
//
//                li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        switch (i){
//                            case 0:
//                                Intent intent1 = new Intent(HomeActivity.this, ChooseBasicSadum.class);
//                                startActivity(intent1);
//                                break;
//                            case 1:
//                                Intent intent2 = new Intent(HomeActivity.this, BM1Activity.class);
//                                startActivity(intent2);
//                                break;
//                            case 2:
//                                Intent intent3 = new Intent(HomeActivity.this,RagiIdupActivity.class);
//                                startActivity(intent3);
//                                break;
//                            case 3:
//                                Intent intent4 = new Intent(HomeActivity.this,MangiringActivity.class);
//                                startActivity(intent4);
//                                break;
//                            case 4:
//                                Intent intent5 = new Intent(HomeActivity.this,CustomActivity.class);
//                                startActivity(intent5);
//                                break;
//
//                        }
//                    }
//                });
//
//                builder.setView(row);
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });

        linearLayoutDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditKristikActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutKG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,KlasifikasiulosActivity.class);
                startActivity(intent);
            }
        });

//        linearLayoutKG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this,VerifikasiuloasActivity.class);
//                startActivity(intent);
//            }
//        });

//        linearLayoutKG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this,KualitasGambarActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    public void onBackPressed() {
//        if (backButtonPressTime + TIME_INTERVAL > System.currentTimeMillis()) {
//            moveTaskToBack(true);
//            finish();
//        } else {
//            Toast.makeText(getBaseContext(), getResources().getString(R.string.press_once_more_to_exit), Toast.LENGTH_SHORT).show();
//        }
//        backButtonPressTime = System.currentTimeMillis();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        //Save image from Verification of Image Qualiaty
//        try {
//            Bundle bundle = getIntent().getExtras();
//            String imagePath = bundle.getString("pathImage");
//            System.out.println("Masuk sini "+imagePath);
//            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
//            saveStockImage(myBitmap);
//        } catch (Exception e) {
////           showErrorMessage(e.getMessage());
//        }
        if (requestCode == VERIFICATION_IMAGE) {
            System.out.println("masuk sini dulu Bro");
//            String returnedResult = data.getData().toString();
            Bundle bundle = getIntent().getExtras();
            String imagePath = bundle.getString("pathImage");
            System.out.println("Masuk sini ngga ya "+imagePath);
//            String returnedResult = data.getDataString();
//            System.out.println("Masuk sini ngga "+returnedResult);
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            saveStockImage(myBitmap);
            // OR
            // String returnedResult = data.getDataString();
        }


        else if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                System.out.println("image path camera "+ imageBitmap.toString());
                saveStockImage(imageBitmap);
            }
        } else if (requestCode == REQUEST_ID_PICK_GALERY && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri imageUri = data.getData();

                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    saveStockImage(imageBitmap);
                } catch (IOException e) {
                    showErrorMessage(e.getMessage());
                }
            } else if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);

                    Uri imageUri = item.getUri();

                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        saveStockImage(imageBitmap);
                    } catch (IOException e) {
                        showErrorMessage(e.getMessage());
                    }
                }
            }
        }
    }

    private void saveStockImage(Bitmap bitmap) {
        Number lastId = realm.where(StockImage.class).max("id");
        int nextId = (lastId == null) ? 1 : lastId.intValue() + 1;

        realm.beginTransaction();
        StockImage stockImage = realm.createObject(StockImage.class, nextId);
        stockImage.setBytes(BitmapUtils.convertToBytes(bitmap));
        stockImage.setName("StockImage_" + stockImage.getId());
        realm.insertOrUpdate(stockImage);
        realm.commitTransaction();
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    private void startImageChooserActivity() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_image)), REQUEST_ID_PICK_GALERY);
    }


    private void showErrorMessage(String message) {
        Toast.makeText(this, getResources().getString(R.string.error) + ": " + message, Toast.LENGTH_LONG).show();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);

        finish();
    }

    private void startFeedbackActivity() {
        Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);

        finish();
    }

    private void startFaqActivity() {
        Intent intent = new Intent(getApplicationContext(), FaqActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);

        finish();
    }



}

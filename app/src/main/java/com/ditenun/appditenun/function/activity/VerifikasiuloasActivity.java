package com.ditenun.appditenun.function.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.dependency.models.StockImage;
import com.ditenun.appditenun.dependency.models.uploadResponseVer;
import com.ditenun.appditenun.dependency.modules.APIModule;
import com.ditenun.appditenun.dependency.network.TenunNetworkInterface;
import com.ditenun.appditenun.function.util.BitmapUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.inject.Inject;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifikasiuloasActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private String selectedImagePath;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private ImageView iv_getGallery;
    private Button buttonVer;
    private Button buttonSave;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String pathImage="";
    private Bitmap thumbnailImage = null;
    ProgressDialog progressDoalog;
    private TextView resultPred;
    private static String[] PERMISSION_CAMERA = {Manifest.permission.CAMERA};
    private Call<uploadResponseVer> callData;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasiuloas);
        buttonSave = findViewById(R.id.btn_savegambar_verifikasi);
        imageView = findViewById(R.id.iv_showimage_verifikasi);
        buttonVer=findViewById(R.id.btn_verifikasi_verifikasi);
        resultPred=findViewById(R.id.tv_hasil_verifikasi);
        iv_getGallery=findViewById(R.id.iv_getGallery_verifikasi);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (ActivityCompat.checkSelfPermission(VerifikasiuloasActivity.this, String.valueOf(PERMISSION_CAMERA)) != PackageManager.PERMISSION_GRANTED) {
                    getPermissionCamera();
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }
            }
        });

        iv_getGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });


        buttonVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pathImage.equals("")){
                    uploadGambar();
                }
                else{
                    Toast.makeText(VerifikasiuloasActivity.this, "Take a Picture First", Toast.LENGTH_LONG).show();
                }

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("image path 2 " + pathImage);
                if(!pathImage.equals("")){
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.putExtra("pathImage", pathImage);
                    startActivity(intent);
                    Toast.makeText(VerifikasiuloasActivity.this, "Gambar berhasil disimpan", Toast.LENGTH_LONG).show();
                }
                else
                {
                    System.out.println("masuk sini2");
                    Toast.makeText(VerifikasiuloasActivity.this, "Pilih gambar terlebih dahulu", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, pathImage, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            System.out.println("Image path"+getRealPathFromURI(tempUri));
            pathImage=getRealPathFromURI(tempUri);
            saveImageToStorage(pathImage);
        }
        else if (resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            System.out.println("Image path gallery before "+selectedImageUri);
            pathImage = getPath(selectedImageUri);
            System.out.println("Image path "+pathImage);
            imageView.setImageURI(selectedImageUri);
//            saveImageToStorage(pathImage);
            if (pathImage == null){
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                if( inputStream != null ){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Uri tempUri = getImageUri(getApplicationContext(), bmp);
                pathImage = getPath(tempUri);
            }
        }
        else if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                saveStockImage(imageBitmap);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void saveImageToStorage(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        options.inSampleSize = 5;
        thumbnailImage = BitmapFactory.decodeFile(path, options);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);

        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            thumbnailImage.compress(Bitmap.CompressFormat.JPEG, Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP ? 50 : 40, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadGambar(){
        File file = new File(pathImage);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part uploadImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        TenunNetworkInterface service = APIModule.getRetrofitVerandKlasInstance().create(TenunNetworkInterface.class);

        progressDoalog = ProgressDialog.show(VerifikasiuloasActivity.this,"Upload", "Gambar sedang di proses..");
        progressDoalog.show();

        callData = service.uploadImageVer(uploadImage);

        callData.enqueue(new Callback<uploadResponseVer>() {
            @Override
            public void onResponse(Call<uploadResponseVer> call, Response<uploadResponseVer> response) {
                progressDoalog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        resultPred.setText(response.body().getPred());
                    }else{
                        Log.d("Response Message",response.message());
                        Log.d("Response Code",Integer.toString(response.code()));
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Exception message [" + e.getMessage() + "]");
                }
            }
            @Override
            public void onFailure(Call<uploadResponseVer> call, Throwable t) {
                progressDoalog.dismiss();
                System.out.println("ini response nya "+t.getMessage());
                Toast.makeText(VerifikasiuloasActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getPermissionCamera() {
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            requestPermissions(PERMISSIONS, MY_CAMERA_PERMISSION_CODE);
        } else {
            requestPermissions(PERMISSIONS, MY_CAMERA_PERMISSION_CODE);
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
}
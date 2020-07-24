package com.ditenun.appditenun.function.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ditenun.appditenun.R;

public class DetailKlasifikasiGambarActivity extends AppCompatActivity {


    TextView text;
    ImageView image;
    TextView profession,kualitasText;
    Button kualitasButton;
    ProgressDialog dialog;

    public static String IMG_PROFESSION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_klasifikasi_gambar);

        text = findViewById(R.id.grid_text);
        image = findViewById(R.id.grid_img);
        profession = findViewById(R.id.profession_text);
        kualitasText = findViewById(R.id.kualitas_text);
        kualitasButton = findViewById(R.id.cek_kualitas_button);

        dialog = new ProgressDialog(DetailKlasifikasiGambarActivity.this);
        dialog.setMessage("Menentukan kualitas gambar");
        dialog.setCanceledOnTouchOutside(false);

        //IMG_PROFESSION = text.getText().toString();
        assert getSupportActionBar()!= null;

        Intent intent = getIntent();
        text.setText(intent.getStringExtra("textx"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(text.getText().toString());

        image.setImageResource(intent.getIntExtra("imagex",0));
        profession.setText(intent.getStringExtra("profession"));

        String professionString = profession.getText().toString();

        int imgprofession = Integer.parseInt(professionString);
        kualitasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                Handler handler =new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(imgprofession % 2 == 0){
                            kualitasText.setText("Gambar Kualitas Buruk");
                            kualitasText.setTextColor(getResources().getColor(R.color.darkRed));
                            kualitasButton.setText("Cek Ulang");
                        }
                        else{
                            kualitasText.setText("Gambar Kualitas Baik");
                            kualitasText.setTextColor(getResources().getColor(R.color.green_500));
                            kualitasButton.setText("Cek Ulang");
                        }
                        dialog.dismiss();
                    }
                },3000);


            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

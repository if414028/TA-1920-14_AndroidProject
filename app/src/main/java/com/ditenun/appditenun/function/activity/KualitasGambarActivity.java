package com.ditenun.appditenun.function.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toolbar;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.dependency.models.Users;
import com.ditenun.appditenun.function.adapter.GalleryAdapter;
import com.ditenun.appditenun.function.adapter.GridViewAdapter;

import java.util.ArrayList;

public class KualitasGambarActivity extends AppCompatActivity {


    private ArrayList<Users> users;
    private GridView gallery;
    private GalleryAdapter galleryAdapter;
    private String[] names={
            "Gambar 1","Gambar 2","Gambar 3","Gambar 4","Gambar 5",
            "Gambar 6","Gambar 7","Gambar 8","Gambar 9","Gambar 10",
            "Gambar 11","Gambar 12","Gambar 13","Gambar 14","Gambar 15",
            "Gambar 16","Gambar 17","Gambar 18","Gambar 19","Gambar 20",

    };
    private String[] professions={
            "1","2","3","4","5",
            "6","7","8","9","10",
            "11","12","13","14","15",
            "16","17","18","19","20",
    };
    private int[] photos={
            R.drawable.sample_1,R.drawable.sample_2,R.drawable.sample_3,R.drawable.sample_4,R.drawable.sample_5,
            R.drawable.sample_6,R.drawable.sample_7,R.drawable.sample_8,R.drawable.sample_9,R.drawable.sample_10,
            R.drawable.sample_11,R.drawable.sample_12,R.drawable.sample_13,R.drawable.sample_14,R.drawable.sample_15,
            R.drawable.sample_16,R.drawable.sample_17,R.drawable.sample_18,R.drawable.sample_19,R.drawable.sample_20,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kualitas_gambar);

        users=new ArrayList<>();

        gallery=(GridView)findViewById(R.id.gallery);
        galleryAdapter=new GalleryAdapter(users,this);
        gallery.setAdapter(galleryAdapter);

        //ActionBar
        assert getSupportActionBar()!= null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Cek Kualitas gambar");


        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailKlasifikasiGambarActivity.class);
                intent.putExtra("textx",names[position]);
                intent.putExtra("imagex",photos[position]);
                intent.putExtra("profession",professions[position]);
                startActivity(intent);

            }
        });

        getDatas();


    }
    // getting all the datas
    private void getDatas(){
        for(int count=0;count<names.length;count++){
            users.add(new Users(names[count],professions[count],photos[count]));
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }




}

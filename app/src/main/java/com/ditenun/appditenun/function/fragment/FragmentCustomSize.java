package com.ditenun.appditenun.function.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ditenun.appditenun.R;
import com.ditenun.appditenun.function.activity.CustomActivity;


public class FragmentCustomSize extends Fragment {

    EditText heightsisi,widthsisi;
    EditText tinggictr,lebarctr;
    Button selesai;
    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_custom_size,container,false);
        heightsisi = v.findViewById(R.id.heightsizeid);
        widthsisi = v.findViewById(R.id.widthsizeid);
        tinggictr = v.findViewById(R.id.tinggitengah);
        lebarctr =v.findViewById(R.id.lebartengah);


        selesai = v.findViewById(R.id.btnselesai);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hs = heightsisi.getText().toString();
                String ws = widthsisi.getText().toString();
                String ht = tinggictr.getText().toString();
                String wt = lebarctr.getText().toString();

                int hhs = Integer.parseInt(hs);
                int hht = Integer.parseInt(ht);
                int wws = Integer.parseInt(ws);
                int wwt = Integer.parseInt(wt);

                if(hhs < hht){
                    tinggictr.setError("Too long");
                    Toast.makeText(getActivity(), "Panjang tengah tidak melebihi panjang template", Toast.LENGTH_SHORT).show();
                }
                if(wws < wwt){
                    tinggictr.setError("Too long");
                    Toast.makeText(getActivity(), "Lebar tengah tidak melebihi panjang template", Toast.LENGTH_SHORT).show();
                }


                if (TextUtils.isEmpty(heightsisi.getText()) && TextUtils.isEmpty(widthsisi.getText()) && TextUtils.isEmpty(lebarctr.getText()) && TextUtils.isEmpty(tinggictr.getText())) {
                    heightsisi.setError("Required");
                    widthsisi.setError("Required");
                    tinggictr.setError("Required");
                    lebarctr.setError("Required");
                }
                if( TextUtils.isEmpty(lebarctr.getText()) && TextUtils.isEmpty(tinggictr.getText())){
                    tinggictr.setError("Required");
                    lebarctr.setError("Required");
                }
                else {
                    CustomActivity rg = (CustomActivity) getActivity();
                    rg.fsize(hs, ws,ht,wt);
                    rg.onBackPressed();
                }
            }
        });
        return v;
    }
}

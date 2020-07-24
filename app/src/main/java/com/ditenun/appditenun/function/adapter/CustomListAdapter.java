package com.ditenun.appditenun.function.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ditenun.appditenun.R;
import com.ditenun.appditenun.function.util.Singlerow;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Singlerow> arrayList;


    public CustomListAdapter(Context c){
        this.context = c;
        arrayList = new ArrayList<>();
        Resources res = c.getResources();
        String [] names = res.getStringArray(R.array.template_name);
        int[] images = {R.drawable.image0, R.drawable.image1,R.drawable.image3,R.drawable.image4,R.drawable.ic_plus};

        for (int i=0;i < names.length;i++){
            arrayList.add(new Singlerow(names[i],images[i]));
        }

    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row_listview,viewGroup,false);
        TextView tv = (TextView)row.findViewById(R.id.textView_template);
        ImageView iv = (ImageView)row.findViewById(R.id.imageView_template);
        Singlerow temp_obj = arrayList.get(i);
        tv.setText(temp_obj.name);
        iv.setImageResource(temp_obj.image);
        return row;
    }
}

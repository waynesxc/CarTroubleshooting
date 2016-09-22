package com.example.user.cartroubleshooting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater myInflater;
    private String [] phenomenon, solve;
    private TextView tv_item, tv_data;
    private ImageButton btn_fix;

    public MyAdapter(Context context, String [] phenomenon, String [] solve){
        this.context = context;
        myInflater = LayoutInflater.from(context);
        this.phenomenon = phenomenon;
        this.solve = solve;
    }

    @Override
    public int getCount() {
        return phenomenon.length;
    }

    @Override
    public Object getItem(int position) {
        return phenomenon[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = myInflater.inflate(R.layout.listview_item, null);
        tv_item = (TextView) convertView.findViewById(R.id.tv_item);
        tv_data = (TextView) convertView.findViewById(R.id.tv_data);
        btn_fix = (ImageButton) convertView.findViewById(R.id.btn_fix);

        tv_item.setText("狀況: "+(position+1)+"　　");
        tv_data.setText(phenomenon[position]);

        btn_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(solve[position]);
                new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("解決方法")
                        .setContentText(solve[position])
                        .setCustomImage(R.drawable.wrench)
                        .show();
            }
        });

        return convertView;
    }
}

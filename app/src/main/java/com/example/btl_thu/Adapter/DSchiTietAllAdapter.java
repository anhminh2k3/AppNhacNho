package com.example.btl_thu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;
import com.example.btl_thu.R;

import java.util.List;

public class DSchiTietAllAdapter extends ArrayAdapter<ListLoiNhac> {
    private Context context;
    private List<ListDS> lsDSanh;
    private List<ListLoiNhac> lsLoiNhacAll;
    private sqlite sql;

//    private DSLoiNhacAdapter dsAdapter;

    public DSchiTietAllAdapter(@NonNull Context context, List<ListLoiNhac> lsLoiNhacAll ,@NonNull List<ListDS> lsDSach) {
        super(context, 0, lsLoiNhacAll);
        this.context = context;
        this.lsDSanh =lsDSach;
        this.lsLoiNhacAll = lsLoiNhacAll;
        sql = new sqlite(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_all, parent, false);
        }
//        if (convertView == null || convertView.getTag() == null || (int) convertView.getTag() != position) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_all, parent, false);
//            convertView.setTag(position);
//        }
        TextView ten = convertView.findViewById(R.id.NameDSAll);
        ListView lvDS = convertView.findViewById(R.id.lvDSAllChiTiet);

//        if (position < lsDSanh.size()) {
            ListDS listDS = lsDSanh.get(position);
            int id = listDS.getID();
            ten.setText(listDS.getNameDS());

            List<ListLoiNhac> loiNhacList = sql.getLoiNhacByDSID(id);
            DSLoiNhacAdapter dsAdapter = new DSLoiNhacAdapter(context, R.layout.item_loinhac, loiNhacList);
            lvDS.setAdapter(dsAdapter);
            notifyDataSetChanged();
//        }


            return convertView;

    }

//    private void setLoiNhacAdapter() {
//        if (dsAdapter == null) {
//            dsAdapter = new DSLoiNhacAdapter(context, R.layout.item_loinhac, lsLoiNhacAll);
//            .setAdapter(dsAdapter);
//        } else {
//            dsAdapter.notifyDataSetChanged();
//            lvDSAllChiTiet.setAdapter(dsAdapter);
//        }
//    }
}

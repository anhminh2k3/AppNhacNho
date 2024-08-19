package com.example.btl_thu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;
import com.example.btl_thu.R;

import java.util.List;

public class DSDuKienAdapter extends ArrayAdapter<ListLoiNhac> {
    private Context context;
    private List<ListDS> lsDS;
    private List<ListLoiNhac> lsLoiNhac;
    private sqlite sql;
    public DSDuKienAdapter(@NonNull Context context, List<ListLoiNhac> lsLoiNhac ,@NonNull List<ListDS> lsDS) {
        super(context, 0, lsLoiNhac);
        this.context = context;
        this.lsLoiNhac = lsLoiNhac;
        this.lsDS =lsDS;
        sql = new sqlite(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_homnay, parent, false);
        }
//        if (convertView == null || convertView.getTag() == null || (int) convertView.getTag() != position) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_all, parent, false);
//            convertView.setTag(position);
//        }
        TextView ten = convertView.findViewById(R.id.NameDSAll);
        ListView lvDS = convertView.findViewById(R.id.lvDSAllChiTiet);

//        if (position < lsDSanh.size()) {
        ListDS listDS = lsDS.get(position);
        int id = listDS.getID();
        ten.setText(listDS.getNameDS());

        List<ListLoiNhac> loiNhacList = sql.getLoiNhacByDSIDDuKien(id);
        DSLoiNhacAdapter dsAdapter = new DSLoiNhacAdapter(context, R.layout.item_loinhac, loiNhacList);
        lvDS.setAdapter(dsAdapter);

//        }


        return convertView;

    }

}

package com.example.btl_thu.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;
import com.example.btl_thu.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DSHomNayAdapter extends ArrayAdapter<ListLoiNhac> {
    private Context context;
    private List<ListDS> lsDS;
    private List<ListLoiNhac> lsLoiNhac;
    private sqlite sql;


    public DSHomNayAdapter(@NonNull Context context, List<ListLoiNhac> lsLoiNhac ,@NonNull List<ListDS> lsDS) {
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

        Collections.sort(lsDS, new Comparator<ListDS>() {
            @Override
            public int compare(ListDS lhs, ListDS rhs) {
                ListLoiNhac loiNhac1 = sql.getLatestLoiNhacByDSID(lhs.getID());
                ListLoiNhac loiNhac2 = sql.getLatestLoiNhacByDSID(rhs.getID());

                // Kiểm tra null để đảm bảo lời nhắc tồn tại
                if (loiNhac1 != null && loiNhac2 != null) {
                    // So sánh theo thời gian mới nhất
                    return loiNhac2.getsTime().compareTo(loiNhac1.getsTime());
                } else if (loiNhac1 == null && loiNhac2 != null) {
                    // Trường hợp lời nhắc của lhs không tồn tại
                    return 1; // Đẩy lhs lên đầu
                } else if (loiNhac1 != null && loiNhac2 == null) {
                    // Trường hợp lời nhắc của rhs không tồn tại
                    return -1; // Đẩy rhs lên đầu
                } else {
                    // Cả hai lời nhắc đều không tồn tại
                    return 0; // Giữ nguyên thứ tự ban đầu
                }
            }
        });

//        if (position < lsDSanh.size()) {
        ListDS listDS = lsDS.get(position);
        int id = listDS.getID();
        ten.setText(listDS.getNameDS());

        List<ListLoiNhac> loiNhacList = sql.getLoiNhacByDSIDToday(id);
        DSLoiNhacAdapter dsAdapter = new DSLoiNhacAdapter(context, R.layout.item_loinhac, loiNhacList);
        lvDS.setAdapter(dsAdapter);

//        }


        return convertView;

    }

}

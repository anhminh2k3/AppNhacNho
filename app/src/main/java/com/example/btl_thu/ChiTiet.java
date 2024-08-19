package com.example.btl_thu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.btl_thu.Adapter.DSDuKienAdapter;
import com.example.btl_thu.Adapter.DSHoanTatAdapter;
import com.example.btl_thu.Adapter.DSHomNayAdapter;
import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;

import java.util.List;

public class ChiTiet extends AppCompatActivity {

    private LinearLayout llDaHoanTatReturnMain;
    private ListView lvHoanTat;
    private sqlite sql;

    private DSHoanTatAdapter dsHoanTatAdapter;
    List<ListLoiNhac> loiNhacList;
    List<ListDS> danhSachList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        sql = new sqlite(this);

        getViews();
        danhSachList = sql.getAllDanhBa();
//        loiNhacList = sql.getLoiNhacByToday();
        loiNhacList = sql.getAllLoiNhac();
        setLoiNhacHoanTatAdapter();

        llDaHoanTatReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTiet.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setLoiNhacHoanTatAdapter() {
//
        if (dsHoanTatAdapter == null) {
            dsHoanTatAdapter = new DSHoanTatAdapter(this, loiNhacList,  danhSachList);
            lvHoanTat.setAdapter(dsHoanTatAdapter);
        } else {
            dsHoanTatAdapter.notifyDataSetChanged();
            lvHoanTat.setAdapter(dsHoanTatAdapter);

        }


    }

    private void getViews(){
        llDaHoanTatReturnMain = findViewById(R.id.llDaHoanTatReturnMain);
        lvHoanTat = findViewById(R.id.lvHoanTat);
    }
}
package com.example.btl_thu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.btl_thu.Adapter.DSDuKienAdapter;
import com.example.btl_thu.Adapter.DSHomNayAdapter;
import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;

import java.util.List;

public class LichDuKien extends AppCompatActivity {

    private LinearLayout llLichDuKienReturnMain;
    private ListView lvLichDuKien;
    private sqlite sql;

    private DSDuKienAdapter dsDuKienAdapter;
    List<ListLoiNhac> loiNhacList;
    List<ListDS> danhSachList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_du_kien);
        sql = new sqlite(this);

        getViews();

//        loiNhacList = sql.getLoiNhacByLichDuKien();
        loiNhacList = sql.getAllLoiNhac();
        danhSachList = sql.getAllDanhBa();
        setLoiNhacLichDKAdapter();

        llLichDuKienReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LichDuKien.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setLoiNhacLichDKAdapter() {

        if (dsDuKienAdapter == null) {
            dsDuKienAdapter = new DSDuKienAdapter(this, loiNhacList,  danhSachList);
            lvLichDuKien.setAdapter(dsDuKienAdapter);
        } else {
            dsDuKienAdapter.notifyDataSetChanged();
            lvLichDuKien.setAdapter(dsDuKienAdapter);

        }

    }

    private void getViews(){
        llLichDuKienReturnMain = findViewById(R.id.llLichDuKienReturnMain);
        lvLichDuKien = findViewById(R.id.lvLichDuKien);
    }
}
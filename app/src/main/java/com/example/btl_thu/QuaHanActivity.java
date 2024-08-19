package com.example.btl_thu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.btl_thu.Adapter.DSHomNayAdapter;
import com.example.btl_thu.Adapter.DSQuaHanAdapter;
import com.example.btl_thu.Adapter.DSchiTietAllAdapter;
import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;

import java.util.List;

public class QuaHanActivity extends AppCompatActivity {
    private LinearLayout llQuaHanReturnMain;
    private sqlite sql;
    private ListView lvQuaHan;
    private DSQuaHanAdapter dsQuaHanAdapter;
    List<ListLoiNhac> loiNhacList;
    List<ListDS> danhSachList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qua_han);
        sql = new sqlite(this);
        getViews();

        danhSachList = sql.getAllDanhBa();
//        loiNhacList = sql.getLoiNhacByToday();
        loiNhacList = sql.getAllLoiNhac();
        setLoiNhacHNAdapter();

        llQuaHanReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuaHanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setLoiNhacHNAdapter() {
//
        if (dsQuaHanAdapter == null) {
            dsQuaHanAdapter = new DSQuaHanAdapter(this, loiNhacList,  danhSachList);
            lvQuaHan.setAdapter(dsQuaHanAdapter);
        } else {
            dsQuaHanAdapter.notifyDataSetChanged();
            lvQuaHan.setAdapter(dsQuaHanAdapter);

        }


    }

    private void getViews(){
        llQuaHanReturnMain = findViewById(R.id.llQuaHanReturMain);
        lvQuaHan = findViewById(R.id.lvQuaHan);
    }
}
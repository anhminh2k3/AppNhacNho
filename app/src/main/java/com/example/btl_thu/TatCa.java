package com.example.btl_thu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.btl_thu.Adapter.DSHomNayAdapter;
import com.example.btl_thu.Adapter.DSchiTietAllAdapter;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;
import com.example.btl_thu.Database.sqlite;

import java.util.List;

public class TatCa extends AppCompatActivity {

    private LinearLayout llTatCaReturnMain;
    private ListView lvAll;

    private DSchiTietAllAdapter dSchiTietAllAdapter;
    List<ListLoiNhac> loiNhacList;
    List<ListDS> danhSachList;
    private sqlite sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_ca);
        sql = new sqlite(this);
        getViews();


        loiNhacList = sql.getAllLoiNhac();
        danhSachList = sql.getAllDanhBa();

        Log.d("Dữ liệu kkk", "Số lượng lời nhắc: " + danhSachList.size());
        if (loiNhacList != null && !loiNhacList.isEmpty()) {
            Log.d("Dữ liệu kkkk", "Số lượng lời nhắc: " + loiNhacList.size());
        } else {
            Log.d("Dữ liệu kkkk", "Không có lời nhắc nào được lấy");
        }
//        dSchiTietAllAdapter= new DSchiTietAllAdapter(this, loiNhacList,  danhSachList);
//        lvAll.setAdapter(dSchiTietAllAdapter);
        setLoiNhacAllAdapter();

        llTatCaReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TatCa.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setLoiNhacAllAdapter() {

        if (dSchiTietAllAdapter == null) {
            dSchiTietAllAdapter= new DSchiTietAllAdapter(this, loiNhacList,  danhSachList);
            lvAll.setAdapter(dSchiTietAllAdapter);
        } else {
//            dSchiTietAllAdapter.clear();
//            dSchiTietAllAdapter.addAll(loiNhacList);
//            dSchiTietAllAdapter.notifyDataSetChanged();
            dSchiTietAllAdapter.notifyDataSetChanged();
            lvAll.setAdapter(dSchiTietAllAdapter);

        }

    }



private void getViews(){
        llTatCaReturnMain = findViewById(R.id.llTatCaReturnMain);
        lvAll = findViewById(R.id.lvAll);
    }
}
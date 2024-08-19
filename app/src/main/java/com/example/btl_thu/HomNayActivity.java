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
import com.example.btl_thu.Model.ListLoiNhac;
import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;



import java.util.List;

public class HomNayActivity extends AppCompatActivity {

    private LinearLayout llHomNayReturnMain;
    private sqlite sql;
    private ListView lvHN;
    private DSHomNayAdapter dsHomNayAdapter;
    private DSchiTietAllAdapter dSchiTietAllAdapter;
    List<ListLoiNhac> loiNhacList;
    List<ListDS> danhSachList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_nay);
        sql = new sqlite(this);
        getViews();

        danhSachList = sql.getAllDanhBa();
//        loiNhacList = sql.getLoiNhacByToday();
        loiNhacList = sql.getAllLoiNhac();



        Log.d("Dữ liệu", "Số lượng lời nhắc: " + danhSachList.size());
        if (loiNhacList != null && !loiNhacList.isEmpty()) {
            Log.d("Dữ liệu kk", "Số lượng lời nhắc: " + loiNhacList.size());
        } else {
            Log.d("Dữ liệu kk", "Không có lời nhắc nào được lấy");
        }

        setLoiNhacHNAdapter();


        llHomNayReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomNayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setLoiNhacHNAdapter() {
//
        if (dsHomNayAdapter == null) {
            dsHomNayAdapter = new DSHomNayAdapter(this, loiNhacList,  danhSachList);
            lvHN.setAdapter(dsHomNayAdapter);
        } else {
            dsHomNayAdapter.notifyDataSetChanged();
            lvHN.setAdapter(dsHomNayAdapter);

        }


    }

    private void getViews(){
        llHomNayReturnMain = findViewById(R.id.llHomNayReturMain);
        lvHN = findViewById(R.id.lvHN);
    }
}
package com.example.btl_thu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_thu.Adapter.DSAdapter;
import com.example.btl_thu.Adapter.DSLoiNhacAdapter;
import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;

import java.util.List;

public class ChiTietDS extends AppCompatActivity {
    private String action = " ";
    private int id;
    private int sl;
    private TextView NameDS;
    private LinearLayout Thoat;
    private ImageView them;
    private ListView lvChiTietDS;
    private DSLoiNhacAdapter dsAdapter;
    private sqlite sql ;

    private List<ListLoiNhac> arrayLN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_ds);
        sql = new sqlite(this);
        getView();

        Intent data = getIntent();
        action = data.getStringExtra("ten");
        id = data.getIntExtra("id", 0);
        Toast.makeText(this, "ID: " + id, Toast.LENGTH_SHORT).show();
        NameDS.setText(action);

        arrayLN = sql.getLoiNhacByDSID(id);
        setLoiNhacAdapter();






        Thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietDS.this, MainActivity.class);
                startActivity(intent);
            }
        });
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietDS.this, ToDoList.class);
                intent.putExtra("ID", id);
                startActivityForResult(intent, 1);
            }
        });
        xoaLoiNhac();


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        updateList();
//    }

    private void updateList() {
        List<ListLoiNhac> loiNhacList = sql.getLoiNhacByDSID(id);
//        arrayLN = sql.getLoiNhacByDSID(id);
        dsAdapter.update(loiNhacList);
//        dsAdapter.notifyDataSetChanged();
        setLoiNhacAdapter();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            int updatedID = data.getIntExtra("dsID", 0);
            Toast.makeText(this, "update ID" + updatedID, Toast.LENGTH_SHORT).show();
            if (updatedID == id) {
                updateList();
            }
        }
    }




    private void setLoiNhacAdapter() {

        if (dsAdapter == null) {
            dsAdapter = new DSLoiNhacAdapter(this, R.layout.item_loinhac, arrayLN);
            lvChiTietDS.setAdapter(dsAdapter);
        }
        else {
            dsAdapter.notifyDataSetChanged();
            lvChiTietDS.setAdapter(dsAdapter);

        }

    }

    private void getView(){
        NameDS =findViewById(R.id.NameDS);
        Thoat = findViewById(R.id.Thoat);
        them = findViewById(R.id.txtThemList);
        lvChiTietDS = findViewById(R.id.lvChiTietDS);
    }

    public void xoaLoiNhac(){
        arrayLN = sql.getLoiNhacByDSID(id);
        dsAdapter = new DSLoiNhacAdapter(this,R.layout.item_loinhac,arrayLN);
        lvChiTietDS.setAdapter(dsAdapter);
    }

    public void XacNhanXoa(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông báo");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Bạn có chắc chắn muốn xóa không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListLoiNhac listLoiNhac = arrayLN.get(position);
                int id = listLoiNhac.getID();
                sql.deleteLoiNhacByDanhSachIdLoiNhac(id);
                arrayLN.remove(position);
                dsAdapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
}
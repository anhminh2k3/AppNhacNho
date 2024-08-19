package com.example.btl_thu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.btl_thu.Adapter.DSAdapter;
import com.example.btl_thu.Adapter.DSLoiNhacAdapter;
import com.example.btl_thu.Database.sqlite;

import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llHomnay, llLichDuKien, llDaHoanTat, llTatCa, llQuaHan;
    private TextView  slDSHN , slDSDK, slAll, slHoanTat, slQuaHan;
    private ListView lvDSMain;
    private DSAdapter dsAdapter;
    private DSLoiNhacAdapter dsLoiNhacAdapter;
    private List<ListDS> arrayList;
    private List<ListLoiNhac> arrLoiNhac;
    private LinearLayout txtThemDS;
    private sqlite sql;
    private AutoCompleteTextView actTimKiemMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();

        sql = new sqlite(MainActivity.this);
        arrayList = sql.getAllDanhBa();


        int sl = sql.countLoiNhacByNgayHienTai();
        slDSHN.setText(String.valueOf(sl));

        int slm = sql.countLoiNhacByNgayMai();
        slDSDK.setText(String.valueOf(slm));

        int slA = sql.countLoiNhacAll();
        slAll.setText(String.valueOf(slA));

        int slHT = sql.countLoiNhacChecked();
        slHoanTat.setText(String.valueOf(slHT));

        int slQH = sql.countLoiNhacByQuaHan();
        slQuaHan.setText(String.valueOf(slQH));

        setAdapter();
        Xoa();
        registerForContextMenu(lvDSMain);
        AutoLoiNhac();
        clickTimKiem();
        actTimKiemMain.setThreshold(1);


        llHomnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomNayActivity.class);
                startActivity(intent);
            }
        });

        llLichDuKien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LichDuKien.class);
                startActivity(intent);
            }
        });

        llDaHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChiTiet.class);
                startActivity(intent);
            }
        });

        llTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TatCa.class);
                startActivity(intent);
            }
        });

        txtThemDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThemDS.class);
                intent.putExtra("action", "add"); // Key "action" với giá trị "add" để chỉ định thêm danh sách
                startActivity(intent);
            }
        });

        llQuaHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuaHanActivity.class);
                startActivity(intent);
            }
        });





    }
    public  void Xoa(){
        arrayList = sql.getAllDanhBa();
        dsAdapter = new DSAdapter(this,R.layout.item_ds,arrayList);
        //dsAdapter.setMainActivity(this);
        lvDSMain.setAdapter(dsAdapter);

//        lvDSMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                XacNhanXoa(position);
//                return false;
//            }
//        });
    }

    public void XacNhanXoa(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông báo");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Bạn có chắc chắn muốn xóa không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListDS listDS = arrayList.get(position);
                int id = listDS.getID();
                sql.deleteDanhBa(id);
                sql.deleteLoiNhacByDanhSachId(id);
                arrayList.remove(position);
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


    private void setAdapter(){
        if(dsAdapter == null){
            dsAdapter = new DSAdapter(this, R.layout.item_ds, arrayList);
            lvDSMain.setAdapter(dsAdapter);
        }
        else{
            dsAdapter.notifyDataSetChanged();
            lvDSMain.setAdapter(dsAdapter);
        }
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        List<ListLoiNhac> loiNhacList = sql.getAllLoiNhac();
//        dsLoiNhacAdapter.update(loiNhacList);
//    }


    public void AutoLoiNhac() {
        arrLoiNhac = sql.getAllLoiNhac();

        List<String> loiNhacTitles = new ArrayList<>();
        for (ListLoiNhac loinhac : arrLoiNhac) {
            loiNhacTitles.add(loinhac.getsTieuDe());
        }

        ArrayAdapter<String> listLoiNhac = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, loiNhacTitles);
        actTimKiemMain.setAdapter(listLoiNhac);
    }

    public void clickTimKiem(){
        actTimKiemMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = actTimKiemMain.getText().toString();
                for(ListLoiNhac loinhac : arrLoiNhac){
                    if(loinhac.getsTieuDe().equals(selectedItem)){
                        Intent intent = new Intent(MainActivity.this, ChiTietDS.class);
                        int id1 = loinhac.getDsID();
                        String tenDanhSach = sql.getTenDanhSachByID(id1);
                        intent.putExtra("ten", tenDanhSach);
                        intent.putExtra("id", loinhac.getDsID());
                        intent.putExtra("tieude", loinhac.getsTieuDe());
                        intent.putExtra("ghichu", loinhac.getsNote());
                        intent.putExtra("ngay", loinhac.getsNgay());
                        intent.putExtra("time", loinhac.getsTime());
                        intent.putExtra("anh", loinhac.getsImage());
                        startActivity(intent);
                        break;
                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == MainActivity.RESULT_OK && data != null) {
            int itemId = data.getIntExtra("itemId", -1);
            String newName = data.getStringExtra("newName");

            if (itemId != -1 && newName != null) {
                // Cập nhật tên danh sách trong danh sách hiển thị
                ListDS listDS = arrayList.get(itemId);
                listDS.setNameDS(newName);
                dsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<ListDS> listDS = sql.getAllDanhBa();
        dsAdapter.updateSua(listDS);
    }


    private void getViews(){
        llHomnay = findViewById(R.id.llHomnay);
        llLichDuKien = findViewById(R.id.llLichDuKien);
        llDaHoanTat = findViewById(R.id.llDaHoanTat);
        llTatCa = findViewById(R.id.llTatCa);
        actTimKiemMain = findViewById(R.id.actTimKiemMain);
        lvDSMain = findViewById(R.id.lvDSMain);
        txtThemDS = findViewById(R.id.txtThemDS);
        slDSHN = findViewById(R.id.slDSHN);
        slDSDK =findViewById(R.id.slDSDK);
        slAll = findViewById(R.id.slAll);
        slHoanTat = findViewById(R.id.slHoanTat);
        llQuaHan = findViewById(R.id.llQuaHan);
        slQuaHan = findViewById(R.id.slQuaHan);
    }


}

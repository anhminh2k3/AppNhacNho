package com.example.btl_thu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.btl_thu.Database.sqlite;

import com.example.btl_thu.Adapter.DSAdapter;
import com.example.btl_thu.Model.ListDS;

import java.util.List;

public class ThemDS extends AppCompatActivity {
    private TextView txtHuy, txtXong;
    private EditText edNameDS;
    private  int itemID;
    private String name;
    List<ListDS> arrList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ds);
        getView();
        sqlite sql =new sqlite(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemID = extras.getInt("id");
            name = extras.getString("ten");

            edNameDS.setText(name);
        }
        txtXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ListDS ds = sendEdNameDS();
//                sql.insDanhBa(ds);
//                Intent data = new Intent(ThemDS.this,MainActivity.class);
//                    startActivity(data);
                ListDS ds = sendEdNameDS();
                Intent intent = getIntent();
                String action = intent.getStringExtra("action");
                if (action.equals("add")) {
                    sql.insDanhBa(ds);
                    Intent data = new Intent(ThemDS.this, MainActivity.class);
                    startActivity(data);
                    finish();
                }
                else if(action.equals("edit")) {
                    String newName = edNameDS.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        sql.updateDanhBa(itemID,newName);
                        // Gửi kết quả về MainActivity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("itemId", itemID);
                        resultIntent.putExtra("newName", newName);
                        setResult(MainActivity.RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(ThemDS.this, "Vui lòng nhập tên danh sách", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        txtHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThemDS.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }
    private ListDS sendEdNameDS(){

        String hoTen = edNameDS.getText().toString();
        ListDS ds = new ListDS(hoTen);
        return ds;
    }
    private void getView(){
        txtHuy = findViewById(R.id.txtHuy);
        txtXong =findViewById(R.id.txtXong);
        edNameDS =findViewById(R.id.edNameDS);
    }
}
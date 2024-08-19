package com.example.btl_thu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.btl_thu.Adapter.DSLoiNhacAdapter;
import com.example.btl_thu.Database.sqlite;

import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;

import java.util.Calendar;

public class ToDoList extends AppCompatActivity {
    private TextView btnHuy, txtImage, txtNgay, txtTime, txtXong;
    private EditText edTieuDe, edNote;
    private DSLoiNhacAdapter dsLoiNhacAdapter;

    final Calendar calendar = Calendar.getInstance();
    private Switch sNgay, sTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        getView();
        sqlite sql = new sqlite(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        Toast.makeText(this, "ID: " + id, Toast.LENGTH_SHORT).show();



        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ToDoList.this, ChiTietDS.class);
//                startActivity(intent);
                onBackPressed();
            }
        });
        txtXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListLoiNhac ds = sendLoiNhac();
                sql.insLoiNhac(ds, id);
                Intent data = new Intent();
                data.putExtra("dsID", id);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        sNgay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    int date, month, year;
                    date = calendar.get(Calendar.DATE);
                    month = calendar.get(Calendar.MONTH);
                    year = calendar.get(Calendar.YEAR);

                    DatePickerDialog dpd = new DatePickerDialog(ToDoList.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int y, int m, int d) {
                                    //txtNgay.setText(d+ "/" +(m+1) + "/" +y);
                                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y);
                                    txtNgay.setText(formattedDate);
                                }
                            },year, month, date);
                    dpd.show();
                }
                else {
                    txtNgay.setText("");
                }
            }
        });

        sTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int hour, minute;
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog tpd = new TimePickerDialog(ToDoList.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            txtTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        }
                    }, hour, minute, true);
                    tpd.show();
                } else {
                    // Xử lý khi trạng thái checked là false
                    txtTime.setText(""); // Đặt lại giá trị của TextView khi không được kiểm tra
                }

                }
        });


    }
    private ListLoiNhac sendLoiNhac(){

        String TieuDe = edTieuDe.getText().toString();
        String GhiChu = edNote.getText().toString();
        String Date = txtNgay.getText().toString();
        String Time = txtTime.getText().toString();
        String Anh = txtImage.getText().toString();

        ListLoiNhac ln = new ListLoiNhac(TieuDe, GhiChu, Date, Time, Anh);
        return ln;
    }

//    private String formatDate(String date) {
//        try {
//            SimpleDateFormat inputFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
//            Date currentDate = inputFormat.parse(date);
//            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//            return outputFormat.format(currentDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//
//    }

/// #E3E2E9

    private void getView(){
        btnHuy = findViewById(R.id.btnHuy);
        txtImage = findViewById(R.id.txtImage);
        edNote = findViewById(R.id.edNote);
        edTieuDe = findViewById(R.id.edTieuDe);
        sNgay =findViewById(R.id.sNgay);
        sTime = findViewById(R.id.sTime);
        txtNgay = findViewById(R.id.txtNgay);
        txtTime = findViewById(R.id.txtTime);
        txtXong = findViewById(R.id.txtXong);
    }
}
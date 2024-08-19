package com.example.btl_thu.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_thu.ChiTietDS;
import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;
import com.example.btl_thu.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DSLoiNhacAdapter extends ArrayAdapter<ListLoiNhac> {
    private Context context;
    private int resourse;

    private List<ListLoiNhac> arrayLN;
    private sqlite sql;
    private boolean[] checkedStates;
    private SharedPreferences sharedPreferences;

    public DSLoiNhacAdapter(@NonNull Context context, int resource, @NonNull List<ListLoiNhac> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourse = resource;
        this.arrayLN = objects;
        sql = new sqlite(context);
        checkedStates = new boolean[objects.size()];
        loadRadioStates();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_loinhac,parent,false);
        TextView tieuDe = convertView.findViewById(R.id.txtTieuDe);
        TextView note = convertView.findViewById(R.id.txtNote);
        TextView time = convertView.findViewById(R.id.txtTime);
        TextView date = convertView.findViewById(R.id.txtDate);
        ImageView anh = convertView.findViewById(R.id.imgAnh);
        RadioButton check = convertView.findViewById(R.id.rbnCheck);
        LinearLayout itemloinhac = convertView.findViewById(R.id.itemloinhac);

        itemloinhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChiTietDS)context).XacNhanXoa(position);
            }
        });
        check.setTag(position);

        ListLoiNhac listLN = arrayLN.get(position);
        tieuDe.setText(listLN.getsTieuDe());
        note.setText(listLN.getsNote());
        time.setText(listLN.getsTime());
        date.setText(listLN.getsNgay());

//        check.setChecked(checkedStates[position]);
        if (position >= 0 && position < checkedStates.length) {
            check.setChecked(checkedStates[position]);
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                boolean currentState = checkedStates[position]; // Lấy trạng thái hiện tại của nút RadioButton

                if (!currentState) {
                    // Chỉ thực hiện khi trạng thái hiện tại là false (chưa được chọn)
                    checkedStates[position] = true; // Đặt trạng thái là true (đã được chọn)
                    check.setChecked(true); // Đặt nút RadioButton là đã chọn

                    int checkValue = 1; // Giá trị check là 1

                    // Gọi phương thức updateCheckValue để cập nhật giá trị check vào database
                    updateCheckValue(position, checkValue);
                } else {
                    // Thực hiện khi trạng thái hiện tại là true (đã được chọn)
                    checkedStates[position] = false; // Đặt trạng thái là false (chưa được chọn)
                    check.setChecked(false); // Đặt nút RadioButton là chưa chọn

                    int checkValue = 0; // Giá trị check là 0

                    // Gọi phương thức updateCheckValue để cập nhật giá trị check vào database
                    updateCheckValue(position, checkValue);
                }

                saveRadioStates();
                notifyDataSetChanged();


            }
        });



        String imgPath = listLN.getsImage();
        if(imgPath != null){
            anh.setImageURI(Uri.parse(imgPath));
        }
        else {
            Toast.makeText(context, "Không hiện ảnh", Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }
    private void saveRadioStates() {
        for (int i = 0; i < checkedStates.length; i++) {
            int isChecked = checkedStates[i] ? 1 : 0;
            ListLoiNhac listLN = arrayLN.get(i);
            sql.updateLoiNhacTick(listLN.getID(), isChecked);
        }
    }
    public void update(List<ListLoiNhac> ds){
        this.arrayLN.clear();
        this.arrayLN.addAll(ds);
        notifyDataSetChanged();
    }

    private void loadRadioStates() {
        for (int i = 0; i < checkedStates.length; i++) {
            ListLoiNhac listLN = arrayLN.get(i);
            int isChecked = sql.getRadioButtonState(listLN.getID());
            checkedStates[i] = (isChecked == 1);
        }
    }

    public void updateCheckValue(int position, int checkValue) {
        ListLoiNhac listLN = arrayLN.get(position);
        listLN.setCheck(checkValue);
        sql.updateLoiNhacCheck(listLN);
    }
    public void addNewReminder(ListLoiNhac newReminder) {
        // Thêm lời nhắc mới vào danh sách arrayLN
        arrayLN.add(0, newReminder);

        // Sắp xếp danh sách arrayLN theo thứ tự ngược (từ mới nhất đến cũ nhất)
        Collections.sort(arrayLN, Collections.reverseOrder());

        // Cập nhật giao diện
        notifyDataSetChanged();
    }


}

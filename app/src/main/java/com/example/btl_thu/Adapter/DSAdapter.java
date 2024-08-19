package com.example.btl_thu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_thu.ChiTietDS;
import com.example.btl_thu.MainActivity;
import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Database.sqlite;
import com.example.btl_thu.R;
import com.example.btl_thu.ThemDS;
import com.example.btl_thu.Model.ListLoiNhac;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DSAdapter extends ArrayAdapter<ListDS> implements PopupMenu.OnMenuItemClickListener {
    private Context context;
    private int resourse;

    private List<ListDS> arrayDS;
    private sqlite databaseHelper;
    private MainActivity mainActivity;
    private int selectedPosition;

    private ListLoiNhac getLatestLoiNhacByDSID(int dsID) {
        List<ListLoiNhac> loiNhacList = databaseHelper.getLoiNhacByDSID(dsID);
        if (loiNhacList.size() > 0) {
            // Sắp xếp danh sách lời nhắc theo thời gian giảm dần
            Collections.sort(loiNhacList, new Comparator<ListLoiNhac>() {
                @Override
                public int compare(ListLoiNhac lhs, ListLoiNhac rhs) {
                    return rhs.getsTime().compareTo(lhs.getsTime());
                }
            });
            // Trả về lời nhắc mới nhất
            return loiNhacList.get(0);
        }
        return null; // Trường hợp không có lời nhắc
    }



    public DSAdapter(@NonNull Context context, int resource, @NonNull List<ListDS> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourse = resource;
        this.arrayDS = objects;
        databaseHelper = new sqlite(context);
        selectedPosition = -1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_ds,parent,false);
        TextView txtTenDS = convertView.findViewById(R.id.txtName);
        ImageView avt = convertView.findViewById(R.id.nen);
        TextView sl = convertView.findViewById(R.id.slDS);
        ImageView imgSelect = convertView.findViewById(R.id.imgSelect);
        LinearLayout item = convertView.findViewById(R.id.itemList);

//        Collections.sort(arrayDS, new Comparator<ListDS>() {
//            @Override
//            public int compare(ListDS lhs, ListDS rhs) {
//                int count1 = databaseHelper.countLoiNhacByDSID(rhs.getID());
//                int count2 = databaseHelper.countLoiNhacByDSID(lhs.getID());
//                return Integer.compare(count1, count2);
//            }
//        });

//        Collections.sort(arrayDS, new Comparator<ListDS>() {
//            @Override
//            public int compare(ListDS lhs, ListDS rhs) {
//                ListLoiNhac loiNhac1 = databaseHelper.getLatestLoiNhacByDSID(lhs.getID());
//                ListLoiNhac loiNhac2 = databaseHelper.getLatestLoiNhacByDSID(rhs.getID());
//
//                // Kiểm tra null để đảm bảo lời nhắc tồn tại
//                if (loiNhac1 != null && loiNhac2 != null) {
//                    // So sánh theo thời gian mới nhất
//                    return loiNhac2.getsTime().compareTo(loiNhac1.getsTime());
//                } else if (loiNhac1 == null && loiNhac2 != null) {
//                    // Trường hợp lời nhắc của lhs không tồn tại
//                    return 1; // Đẩy lhs lên đầu
//                } else if (loiNhac1 != null && loiNhac2 == null) {
//                    // Trường hợp lời nhắc của rhs không tồn tại
//                    return -1; // Đẩy rhs lên đầu
//                } else {
//                    // Cả hai lời nhắc đều không tồn tại
//                    return 0; // Giữ nguyên thứ tự ban đầu
//                }
//            }
//        });
        Collections.sort(arrayDS, new Comparator<ListDS>() {
            @Override
            public int compare(ListDS lhs, ListDS rhs) {
                ListLoiNhac loiNhac1 = getLatestLoiNhacByDSID(lhs.getID());
                ListLoiNhac loiNhac2 = getLatestLoiNhacByDSID(rhs.getID());

                // Kiểm tra null để đảm bảo lời nhắc tồn tại
                if (loiNhac1 != null && loiNhac2 != null) {
                    // So sánh theo thời gian mới nhất
                    return loiNhac2.getsTime().compareTo(loiNhac1.getsTime());
                } else if (loiNhac1 == null && loiNhac2 != null) {
                    // Trường hợp lời nhắc của lhs không tồn tại
                    return 1; // Đẩy lhs lên đầu
                } else if (loiNhac1 != null && loiNhac2 == null) {
                    // Trường hợp lời nhắc của rhs không tồn tại
                    return -1; // Đẩy rhs lên đầu
                } else {
                    // Cả hai lời nhắc đều không tồn tại
                    return 0; // Giữ nguyên thứ tự ban đầu
                }
            }
        });


        ListDS listDS = arrayDS.get(position);

        txtTenDS.setText(listDS.getNameDS());
        int dsID = listDS.getID();
        int countLoiNhac = databaseHelper.countLoiNhacByDSID(dsID);
        sl.setText(String.valueOf(countLoiNhac));



        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, position);
            }
        });
        convertView.setTag(position);





//        String imagePath = listDS.getImage();
//
//        if (imagePath != null) {
//            avt.setImageURI(Uri.parse(imagePath));
//        } else {
//            Toast.makeText(context, "Ko hiện ảnh", Toast.LENGTH_SHORT).show(); // Hoặc làm gì đó khác tùy thuộc vào yêu cầu của bạn
//        }

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = listDS.getNameDS();
                int id = listDS.getID();
                int count = listDS.getCount();
                Intent intent = new Intent(context, ChiTietDS.class);
                intent.putExtra("ten", ten);
                intent.putExtra("id", id);
                context.startActivity(intent);


            }
        });

        return convertView;
    }

    public void showPopup(View v, int position){
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu. setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_suaxoa);
        selectedPosition = position;
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.xoa) {
//            sql = new sqlite(context);
//            arrayDS = sql.getAllDanhBa();
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//            alertDialog.setTitle("Thông báo");
//            alertDialog.setIcon(R.mipmap.ic_launcher);
//            alertDialog.setMessage("Bạn có chắc chắn muốn xóa không ?");
//            alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    ListDS listDS = arrayDS.get(selectedPosition);
//                    int id = listDS.getID();
//                    sql.deleteDanhBa(id);
//                    arrayDS.remove(selectedPosition);
//                    ((MainActivity) context).Xoa(arrayDS);
////                    adapter = new DSAdapter(context, R.layout.item_ds, arrayDS);
////                    notifyDataSetChanged();
//                    dialog.dismiss();
//                }
//            });
//            alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            alertDialog.show();
//            mainActivity = new MainActivity();
//            mainActivity.Xoa();
            ((MainActivity) context).XacNhanXoa(selectedPosition);
            //Toast.makeText(context, "Xoa clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.sua) {
            if (selectedPosition != -1) {
                ListDS listDS = arrayDS.get(selectedPosition);
                int id = listDS.getID();
                String ten = listDS.getNameDS();

                Intent intent = new Intent(context, ThemDS.class);
                intent.putExtra("action", "edit"); // Key "action" với giá trị "exit" để chỉ định sửa danh sách
                intent.putExtra("ten", ten);
                intent.putExtra("id", id);
                context.startActivity(intent);
                //((AppCompatActivity) context).startActivityForResult(intent, 1);
                //Toast.makeText(context, "Sua clicked", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }
    }
    public void updateSua(List<ListDS> ds){
        this.arrayDS.clear();
        this.arrayDS.addAll(ds);
        notifyDataSetChanged();
    }

    public void addNewListItem(ListDS listItem) {
        arrayDS.add(0, listItem);
        notifyDataSetChanged();
    }
}

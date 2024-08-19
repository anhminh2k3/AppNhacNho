package com.example.btl_thu.Database;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.btl_thu.Model.ListDS;
import com.example.btl_thu.Model.ListLoiNhac;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import java.util.ArrayList;
import java.util.List;

public class sqlite extends SQLiteOpenHelper {
    public static final String BD_Name = "listDS.db";
    public static final int DB_Version = 4;
    //Định nghĩa tên bảng
    public static final String TB_Name = "tbl_danhsach";

    public static final String ID = "id";
    public static final String TEN = "TEN";
    public static final String SL = "SL";

    public static final String TB_LoiNhac = "tbl_LoiNhac";
    public static final String TIEUDE = "tieude";
    public static final String NAME_ID = "idName";
    public static final String GHICHU = "ghichu";

    public static final String TICK = "kiemtra";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String IMAGE = "image";


    private Context context;

    public sqlite(@Nullable Context context) {
        super(context, BD_Name, null, DB_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TB_Name + " ( " + ID + " integer primary key AUTOINCREMENT, "
                + TEN + " TEXT, "
                + SL + " TEXT)";

        String loiNhac = "CREATE TABLE " + TB_LoiNhac + " ( "
                + ID + " integer primary key AUTOINCREMENT, "
                + TIEUDE + " TEXT, "
                + GHICHU + " TEXT, "
                + DATE + " TEXT, "
                + TIME + " TEXT, "
                + TICK + " INTEGER DEFAULT 0, "
                + IMAGE + " TEXT, "
                + NAME_ID + " INTEGER, "
                + "Foreign key(" + NAME_ID + ") References "
                + TB_Name + "(" + ID + ")"
                + "ON DELETE CASCADE " + ")";
        //gọi phương thức execSQL
        db.execSQL(sql);
        db.execSQL(loiNhac);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_Name);
        db.execSQL("DROP TABLE IF EXISTS " + TB_LoiNhac);
        //gọi lại hamf onCreate để nâng cấp phien bản và tạo lại bảng
        onCreate(db);

    }

    public void insDanhBa(ListDS ds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEN, ds.getNameDS());
        db.insert(TB_Name, null, values);
        db.close();
        Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show();
    }
    public  void insLoiNhac(ListLoiNhac ln, int id){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIEUDE, ln.getsTieuDe());
        values.put(GHICHU, ln.getsNote());
        values.put(DATE,ln.getsNgay());
        values.put(TIME,ln.getsTime());
        values.put(IMAGE, ln.getsImage());
//        values.put(CHECKED, ln.getCheck());
        values.put(NAME_ID, id);
        bd.insert(TB_LoiNhac, null, values);
        bd.close();
        Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show();

    }
//    public  void insLoiNhacCheck(ListLoiNhac ln){
//        SQLiteDatabase bd = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(CHECKED, ln.getCheck());
//        bd.insert(TB_LoiNhac, null, values);
//        bd.close();
//        Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show();
//
//    }
    public void updateLoiNhacCheck(ListLoiNhac ln  ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TICK, ln.getCheck());

        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(ln.getID())};

        db.update(TB_LoiNhac, values, whereClause, whereArgs);
        db.close();
        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    public void updateLoiNhacTick(int loiNhacID, int isChecked  ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TICK, isChecked);

        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(loiNhacID)};

        db.update(TB_LoiNhac, values, whereClause, whereArgs);
        db.close();
        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("Range")
    public int getRadioButtonState(int loiNhacID) {
        SQLiteDatabase db = this.getReadableDatabase();
        int isChecked = 0;

        String[] columns = {TICK};
        String selection = ID + " = ?";
        String[] selectionArgs = {String.valueOf(loiNhacID)};

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            isChecked = cursor.getInt(cursor.getColumnIndex(TICK));
        }

        cursor.close();
        db.close();

        return isChecked;
    }



    public List<ListDS> getAllDanhBa(){
        List<ListDS> kq = new ArrayList<ListDS>();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TB_Name, null);
        //duyệt qua danh sách bản ghi có trong con tro
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                //thực hiện insert dữ leieuj vào list kq
                do {
                    ListDS ds = new ListDS();
                    ds.setID(cursor.getInt(0));
                    ds.setNameDS(cursor.getString(1));
                    //ds.setCount(Integer.parseInt(cursor.getString(2)));
                    kq.add(ds);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return kq;
    }

    @SuppressLint("Range")
    public List<ListLoiNhac> getAllLoiNhacByDanhSach() {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        List<ListDS> danhSachList = getAllDanhBa();
        for (ListDS danhSach : danhSachList) {
            int danhSachID = danhSach.getID();
            String selectQuery = "SELECT * FROM " + TB_LoiNhac + " WHERE " + NAME_ID + " = " + danhSachID;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ListLoiNhac loiNhac = new ListLoiNhac();
                    loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                    loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                    loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                    loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                    loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                    loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                    loiNhac.setDsID(cursor.getInt(cursor.getColumnIndex(NAME_ID)));


                    loiNhacList.add(loiNhac);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        return loiNhacList;
    }

    @SuppressLint("Range")
    public List<ListLoiNhac> getAllLoiNhacByDanhSachID(int id) {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TB_LoiNhac + " WHERE " + NAME_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ListLoiNhac loiNhac = new ListLoiNhac();
                loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                loiNhac.setDsID(id);

                loiNhacList.add(loiNhac);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return loiNhacList;
    }

//    @SuppressLint("Range")
//    public ListDS getDanhBaByID(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String[] columns = {ID, TEN};
//        String selection = ID + " = ?";
//        String[] selectionArgs = {String.valueOf(id)};
//
//        Cursor cursor = db.query(TB_Name, columns, selection, selectionArgs, null, null, null);
//
//        ListDS danhBa = new ListDS();
//
//        if (cursor.moveToFirst()) {
//            danhBa.setID(cursor.getInt(cursor.getColumnIndex(ID)));
//            danhBa.setNameDS(cursor.getString(cursor.getColumnIndex(TEN)));
//        }
//
//        cursor.close();
//        db.close();
//
//        return danhBa;
//    }

    @SuppressLint("Range")
    public List<ListLoiNhac> getLoiNhacByDSID(int dsID) {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID, TIEUDE, GHICHU, DATE, TIME, IMAGE, NAME_ID};
        String selection = NAME_ID + " = ? ORDER BY " + ID + " DESC";
        String[] selectionArgs = {String.valueOf(dsID)};

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ListLoiNhac loiNhac = new ListLoiNhac();
                loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                loiNhac.setDsID(dsID);


                loiNhacList.add(loiNhac);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return loiNhacList;
    }

    @SuppressLint("Range")
    public List<ListLoiNhac> getLoiNhacByDSIDToday(int dsID) {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID, TIEUDE, GHICHU, DATE, TIME, IMAGE, NAME_ID};
        String selection = NAME_ID + " = ? AND DATE = ?";
        String[] selectionArgs = {String.valueOf(dsID), getCurrentDate()};

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ListLoiNhac loiNhac = new ListLoiNhac();
                loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                loiNhac.setDsID(dsID);

                loiNhacList.add(loiNhac);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return loiNhacList;
    }

    @SuppressLint("Range")
    public List<ListLoiNhac> getLoiNhacByDSIDQuaHan(int dsID) {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID, TIEUDE, GHICHU, DATE, TIME, IMAGE, NAME_ID};
        String selection = NAME_ID + " = ? AND DATE < ?";
        String[] selectionArgs = {String.valueOf(dsID), getCurrentDate()};

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ListLoiNhac loiNhac = new ListLoiNhac();
                loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                loiNhac.setDsID(dsID);

                loiNhacList.add(loiNhac);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return loiNhacList;
    }

    @SuppressLint("Range")
    public List<ListLoiNhac> getLoiNhacByDSIDDuKien(int dsID) {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID, TIEUDE, GHICHU, DATE, TIME, IMAGE, NAME_ID};
        String selection = NAME_ID + " = ? AND DATE > ?";
        String[] selectionArgs = {String.valueOf(dsID), getCurrentDate()};

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ListLoiNhac loiNhac = new ListLoiNhac();
                loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                loiNhac.setDsID(dsID);

                loiNhacList.add(loiNhac);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return loiNhacList;
    }

    public void saveRadioState(int position, boolean isChecked) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TICK, isChecked ? 1 : 0);

        String[] whereArgs = { String.valueOf(position) };

        db.update(TB_LoiNhac, values, "ID=?", whereArgs);

        db.close();
    }
    @SuppressLint("Range")
    public List<ListLoiNhac> getLoiNhacByDSIDCheck(int dsID) {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID, TIEUDE, GHICHU, DATE, TIME, IMAGE, NAME_ID,TICK};
        String selection = NAME_ID + " = ? AND " + TICK + " = ?";
        String[] selectionArgs = {String.valueOf(dsID), "1"};

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ListLoiNhac loiNhac = new ListLoiNhac();
                loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                loiNhac.setDsID(dsID);
                loiNhac.setCheck(1);

                loiNhacList.add(loiNhac);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return loiNhacList;
    }

    @SuppressLint("Range")
    public String getTenDanhSachByID(int id) {
        String tenDanhSach = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {TEN};
        String selection = ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(TB_Name, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            tenDanhSach = cursor.getString(cursor.getColumnIndex(TEN));
        }
        cursor.close();
        Log.d("SQLite", "ten: " + tenDanhSach.toString());

        return tenDanhSach;
    }

    public int countLoiNhacByDSID(int dsID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TB_LoiNhac + " WHERE " + NAME_ID + " = " + dsID;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }
    public int countLoiNhacByNgayHienTai() {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = getCurrentDate(); // Chuỗi đại diện cho ngày hôm nay, ví dụ: "29/06/2023"
        String countQuery = "SELECT COUNT(*) FROM " + TB_LoiNhac + " WHERE " + DATE + " = '" + currentDate + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }
    public void updateLoiNhacTimestamp(int loiNhacID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis()); // Cập nhật thời gian hiện tại

        String selection = NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(loiNhacID) };

        db.update(TB_LoiNhac, values, selection, selectionArgs);
        db.close();
    }

    @SuppressLint("Range")
    public ListLoiNhac getLatestLoiNhacByDSID(int dsID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ListLoiNhac latestLoiNhac = null;

        String[] columns = {ID, TIEUDE, GHICHU, DATE, TIME, IMAGE, NAME_ID};
        String selection = NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(dsID)};
        String orderBy = getCurrentTime() + " DESC"; // Sắp xếp theo thời gian giảm dần

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, orderBy);

        if (cursor.moveToFirst()) {
            latestLoiNhac = new ListLoiNhac();
            latestLoiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
            latestLoiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
            latestLoiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
            latestLoiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
            latestLoiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
            latestLoiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
            latestLoiNhac.setDsID(dsID);
        }

        cursor.close();
        db.close();

        return latestLoiNhac;
    }

    public int countLoiNhacByNgayMai() {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = getCurrentDate(); // Chuỗi đại diện cho ngày hôm nay, ví dụ: "29/06/2023"
        String countQuery = "SELECT COUNT(*) FROM " + TB_LoiNhac + " WHERE " + DATE + " > '" + currentDate + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    public int countLoiNhacByQuaHan() {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = getCurrentDate(); // Chuỗi đại diện cho ngày hôm nay, ví dụ: "29/06/2023"
        String countQuery = "SELECT COUNT(*) FROM " + TB_LoiNhac + " WHERE " + DATE + " < '" + currentDate + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    public int countLoiNhacAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TB_LoiNhac ;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    public int countLoiNhacChecked() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TB_LoiNhac + " WHERE " + TICK + " = 1";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    public List<ListLoiNhac> getAllLoiNhac(){
        List<ListLoiNhac> fn = new ArrayList<ListLoiNhac>();
        Cursor cursor= this.getReadableDatabase().rawQuery("SELECT * FROM "+ TB_LoiNhac, null);
        if(cursor.getCount() > 0){
            if(cursor.moveToFirst()){
                do {
                    ListLoiNhac ds = new ListLoiNhac();
                    ds.setID(cursor.getInt(0));
                    ds.setsTieuDe(cursor.getString(1));
                    ds.setsNote(cursor.getString(2));
                    ds.setsNgay(cursor.getString(3));
                    ds.setsTime(cursor.getString(4));
                    ds.setDsID(cursor.getInt(7));
                    fn.add(ds);

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return fn;
    }

    @SuppressLint("Range")
    public List<ListLoiNhac> getLoiNhacByLichDuKien() {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID,TIEUDE, GHICHU, DATE, TIME, IMAGE, NAME_ID};
        String selection = DATE + " > ?";
        String currentDate = getCurrentDate(); // Chuỗi đại diện cho ngày hôm nay, ví dụ: "29/06/2023"
        String[] selectionArgs = {currentDate};

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ListLoiNhac loiNhac = new ListLoiNhac();
                loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                loiNhac.setDsID(cursor.getInt(cursor.getColumnIndex(NAME_ID)));

                loiNhacList.add(loiNhac);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return loiNhacList;
    }

    @SuppressLint("Range")
    public List<ListLoiNhac> getLoiNhacByToday() {
        List<ListLoiNhac> loiNhacList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID,TIEUDE, GHICHU, DATE, TIME, IMAGE, NAME_ID};
        String selection = DATE + " = ?";
        String currentDate = getCurrentDate(); // Chuỗi đại diện cho ngày hôm nay, ví dụ: "29/06/2023"
        String[] selectionArgs = {currentDate};

        Cursor cursor = db.query(TB_LoiNhac, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ListLoiNhac loiNhac = new ListLoiNhac();
                loiNhac.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                loiNhac.setsTieuDe(cursor.getString(cursor.getColumnIndex(TIEUDE)));
                loiNhac.setsNote(cursor.getString(cursor.getColumnIndex(GHICHU)));
                loiNhac.setsNgay(cursor.getString(cursor.getColumnIndex(DATE)));
                loiNhac.setsTime(cursor.getString(cursor.getColumnIndex(TIME)));
                loiNhac.setsImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                loiNhac.setDsID(cursor.getInt(cursor.getColumnIndex(NAME_ID)));

                loiNhacList.add(loiNhac);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return loiNhacList;
    }



        private String getCurrentDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date currentDate = new Date();
//            return dateFormat.format(currentDate);
            String formattedDate = dateFormat.format(currentDate);
            Log.d("Ngày hôm nay", formattedDate); // In ra giá trị ngày hôm nay
            return formattedDate;
        }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date currentTime = new Date();
        return sdf.format(currentTime);
    }


    public void deleteDanhBa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        db.delete(TB_Name, whereClause, whereArgs);
        db.close();
        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }

    public void deleteLoiNhacByDanhSachId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TB_LoiNhac + " WHERE " + NAME_ID + " = " + id);
//        String whereClause = NAME_ID + " = ?";
//        String[] whereArgs = {String.valueOf(id)};
//        db.delete(TB_LoiNhac, whereClause, whereArgs);
        db.close();
    }

    public void updateDanhBa(int id, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEN, newName);
        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        int rowsUpdated = db.update(TB_Name, values, whereClause, whereArgs);
        db.close();

        if (rowsUpdated > 0) {
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteLoiNhacByDanhSachIdLoiNhac(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TB_LoiNhac + " WHERE " + ID + " = " + id);
//        String whereClause = NAME_ID + " = ?";
//        String[] whereArgs = {String.valueOf(id)};
//        db.delete(TB_LoiNhac, whereClause, whereArgs);
        db.close();
    }
}

package com.example.btl_thu.Model;

public class ListLoiNhac  {
    int ID;
    int check;

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    private String sTieuDe;
    private String sNote;
    private String sNgay;
    private String sTime;
    private String sImage;
    private int dsID;


    public ListLoiNhac() {
    }

    public ListLoiNhac(String sTieuDe, String sNote, String sNgay, String sTime, String sImage) {
        this.sTieuDe = sTieuDe;
        this.sNote = sNote;
        this.sNgay = sNgay;
        this.sTime = sTime;
        this.sImage = sImage;
    }

    public void setDsID(int dsID) {
        this.dsID = dsID;
    }

    public int getDsID() {
        return dsID;
    }

    public ListLoiNhac(String sTieuDe, String sNote, String sNgay, String sTime, String sImage, int dsID) {
        this.sTieuDe = sTieuDe;
        this.sNote = sNote;
        this.sNgay = sNgay;
        this.sTime = sTime;
        this.sImage = sImage;
        this.dsID = dsID;
    }

    public void setsTieuDe(String sTieuDe) {
        this.sTieuDe = sTieuDe;
    }

    public void setsNote(String sNote) {
        this.sNote = sNote;
    }

    public void setsNgay(String sNgay) {
        this.sNgay = sNgay;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public String getsTieuDe() {
        return sTieuDe;
    }

    public String getsNote() {
        return sNote;
    }

    public String getsNgay() {
        return sNgay;
    }

    public String getsTime() {
        return sTime;
    }

    public String getsImage() {
        return sImage;
    }


}

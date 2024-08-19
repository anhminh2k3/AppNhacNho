package com.example.btl_thu.Model;

import java.io.Serializable;

public class ListDS implements Serializable {
    int ID;
    int count;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private String NameDS;
    private String image;

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }


    public void setNameDS(String nameDS) {
        NameDS = nameDS;
    }


    public String getNameDS() {
        return NameDS;
    }

    public ListDS( String nameDS) {
        this.NameDS = nameDS;
//        this.image = " ";

    }

    public ListDS() {
    }
}

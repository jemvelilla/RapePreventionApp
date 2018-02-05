package com.build1.rapepreventionapp.Model;

/**
 * Created by JEMYLA VELILLA on 05/02/2018.
 */

public class UserModel {

    boolean isSelected;
    String name, number;

    public UserModel(boolean isSelected, String name, String number) {

        this.isSelected = isSelected;
        this.name = name;
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

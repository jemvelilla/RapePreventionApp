package com.build1.rapepreventionapp.Model;

/**
 * Created by JEMYLA VELILLA on 05/02/2018.
 */

public class UserModel {

    boolean isSelected;
    String name;

    public UserModel(boolean isSelected, String name) {

        this.isSelected = isSelected;
        this.name = name;
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
}

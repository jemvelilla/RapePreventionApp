package com.build1.rapepreventionapp.Model;

/**
 * Created by JEMYLA VELILLA on 05/02/2018.
 */

public class UserModel {

    boolean isSelected, isAppUser;
    String name, number, id;

    public UserModel(boolean isSelected, boolean isAppUser,
                     String name, String number, String id) {

        this.isSelected = isSelected;
        this.isAppUser = isAppUser;
        this.name = name;
        this.number = number;
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isAppUser() {
        return isAppUser;
    }

    public void setAppUser(boolean appUser) {
        isAppUser = appUser;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

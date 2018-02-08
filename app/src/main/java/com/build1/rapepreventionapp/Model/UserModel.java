package com.build1.rapepreventionapp.Model;

/**
 * Created by JEMYLA VELILLA on 05/02/2018.
 */

public class UserModel extends UserId {

    boolean isSelected, isAppUser;
    String name, number;

<<<<<<< HEAD
    public UserModel(boolean isSelected, boolean isAppUser,
                     String name, String number) {
=======
    public UserModel(boolean isSelected, boolean isAppUser, String name, String number) {
>>>>>>> parent of 73e5702... Get Contact Id

        this.isSelected = isSelected;
        this.isAppUser = isAppUser;
        this.name = name;
        this.number = number;
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
}

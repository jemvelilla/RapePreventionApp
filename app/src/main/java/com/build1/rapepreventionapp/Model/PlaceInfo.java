package com.build1.rapepreventionapp.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Darise on 30/01/2018.
 */

public class PlaceInfo {

    private String name;
    private String address;
    private String id;
    private LatLng latLng;

    public PlaceInfo(String name, String address, String id, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public PlaceInfo() {

    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latLng=" + latLng +
                '}';
    }
}

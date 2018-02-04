package com.build1.rapepreventionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Darise on 30/01/2018.
 */

public class CustomWindowInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomWindowInfoAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.titlePlace);

        if (!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

        if (!snippet.equals("")){
            tvSnippet.setText(snippet);
        }

    }


    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);

        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}